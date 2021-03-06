package com.android.internal.os;

import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public class KernelSingleUidTimeReader {
    private static final boolean DBG = false;
    private static final String PROC_FILE_DIR = "/proc/uid/";
    private static final String PROC_FILE_NAME = "/time_in_state";
    private static final String TAG = KernelSingleUidTimeReader.class.getName();
    @VisibleForTesting
    public static final int TOTAL_READ_ERROR_COUNT = 5;
    private static final String UID_TIMES_PROC_FILE = "/proc/uid_time_in_state";
    @GuardedBy({"this"})
    private final int mCpuFreqsCount;
    @GuardedBy({"this"})
    private boolean mCpuFreqsCountVerified;
    private final Injector mInjector;
    @GuardedBy({"this"})
    private SparseArray<long[]> mLastUidCpuTimeMs;
    @GuardedBy({"this"})
    private int mReadErrorCounter;
    @GuardedBy({"this"})
    private boolean mSingleUidCpuTimesAvailable;

    @VisibleForTesting
    public static class Injector {
        public byte[] readData(String procFile) throws IOException {
            return Files.readAllBytes(Paths.get(procFile, new String[0]));
        }
    }

    KernelSingleUidTimeReader(int cpuFreqsCount) {
        this(cpuFreqsCount, new Injector());
    }

    public KernelSingleUidTimeReader(int cpuFreqsCount, Injector injector) {
        this.mLastUidCpuTimeMs = new SparseArray();
        this.mSingleUidCpuTimesAvailable = true;
        this.mInjector = injector;
        this.mCpuFreqsCount = cpuFreqsCount;
        if (this.mCpuFreqsCount == 0) {
            this.mSingleUidCpuTimesAvailable = false;
        }
    }

    public boolean singleUidCpuTimesAvailable() {
        return this.mSingleUidCpuTimesAvailable;
    }

    public long[] readDeltaMs(int uid) {
        synchronized (this) {
            if (this.mSingleUidCpuTimesAvailable) {
                String procFile = new StringBuilder(PROC_FILE_DIR);
                procFile.append(uid);
                procFile.append(PROC_FILE_NAME);
                procFile = procFile.toString();
                try {
                    byte[] data = this.mInjector.readData(procFile);
                    if (!this.mCpuFreqsCountVerified) {
                        verifyCpuFreqsCount(data.length, procFile);
                    }
                    ByteBuffer buffer = ByteBuffer.wrap(data);
                    buffer.order(ByteOrder.nativeOrder());
                    long[] computeDelta = computeDelta(uid, readCpuTimesFromByteBuffer(buffer));
                    return computeDelta;
                } catch (Exception e) {
                    int i = this.mReadErrorCounter + 1;
                    this.mReadErrorCounter = i;
                    if (i >= 5) {
                        this.mSingleUidCpuTimesAvailable = false;
                    }
                    return null;
                }
            }
            return null;
        }
    }

    private void verifyCpuFreqsCount(int numBytes, String procFile) {
        int actualCount = numBytes / 8;
        if (this.mCpuFreqsCount == actualCount) {
            this.mCpuFreqsCountVerified = true;
            return;
        }
        this.mSingleUidCpuTimesAvailable = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Freq count didn't match,count from /proc/uid_time_in_state=");
        stringBuilder.append(this.mCpuFreqsCount);
        stringBuilder.append(", butcount from ");
        stringBuilder.append(procFile);
        stringBuilder.append("=");
        stringBuilder.append(actualCount);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private long[] readCpuTimesFromByteBuffer(ByteBuffer buffer) {
        long[] cpuTimesMs = new long[this.mCpuFreqsCount];
        for (int i = 0; i < this.mCpuFreqsCount; i++) {
            cpuTimesMs[i] = buffer.getLong() * 10;
        }
        return cpuTimesMs;
    }

    public long[] computeDelta(int uid, long[] latestCpuTimesMs) {
        synchronized (this) {
            if (this.mSingleUidCpuTimesAvailable) {
                long[] deltaTimesMs = getDeltaLocked((long[]) this.mLastUidCpuTimeMs.get(uid), latestCpuTimesMs);
                if (deltaTimesMs == null) {
                    return null;
                }
                boolean hasNonZero = false;
                for (int i = deltaTimesMs.length - 1; i >= 0; i--) {
                    if (deltaTimesMs[i] > 0) {
                        hasNonZero = true;
                        break;
                    }
                }
                if (hasNonZero) {
                    this.mLastUidCpuTimeMs.put(uid, latestCpuTimesMs);
                    return deltaTimesMs;
                }
                return null;
            }
            return null;
        }
    }

    @GuardedBy({"this"})
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    public long[] getDeltaLocked(long[] lastCpuTimesMs, long[] latestCpuTimesMs) {
        int i = latestCpuTimesMs.length;
        while (true) {
            i--;
            if (i >= 0) {
                if (latestCpuTimesMs[i] < 0) {
                    return null;
                }
            } else if (lastCpuTimesMs == null) {
                return latestCpuTimesMs;
            } else {
                long[] deltaTimesMs = new long[latestCpuTimesMs.length];
                for (int i2 = latestCpuTimesMs.length - 1; i2 >= 0; i2--) {
                    deltaTimesMs[i2] = latestCpuTimesMs[i2] - lastCpuTimesMs[i2];
                    if (deltaTimesMs[i2] < 0) {
                        return null;
                    }
                }
                return deltaTimesMs;
            }
        }
    }

    public void setAllUidsCpuTimesMs(SparseArray<long[]> allUidsCpuTimesMs) {
        synchronized (this) {
            this.mLastUidCpuTimeMs.clear();
            for (int i = allUidsCpuTimesMs.size() - 1; i >= 0; i--) {
                long[] cpuTimesMs = (long[]) allUidsCpuTimesMs.valueAt(i);
                if (cpuTimesMs != null) {
                    this.mLastUidCpuTimeMs.put(allUidsCpuTimesMs.keyAt(i), (long[]) cpuTimesMs.clone());
                }
            }
        }
    }

    public void removeUid(int uid) {
        synchronized (this) {
            this.mLastUidCpuTimeMs.delete(uid);
        }
    }

    public void removeUidsInRange(int startUid, int endUid) {
        if (endUid >= startUid) {
            synchronized (this) {
                this.mLastUidCpuTimeMs.put(startUid, null);
                this.mLastUidCpuTimeMs.put(endUid, null);
                int startIdx = this.mLastUidCpuTimeMs.indexOfKey(startUid);
                this.mLastUidCpuTimeMs.removeAtRange(startIdx, (this.mLastUidCpuTimeMs.indexOfKey(endUid) - startIdx) + 1);
            }
        }
    }

    @VisibleForTesting
    public SparseArray<long[]> getLastUidCpuTimeMs() {
        return this.mLastUidCpuTimeMs;
    }

    @VisibleForTesting
    public void setSingleUidCpuTimesAvailable(boolean singleUidCpuTimesAvailable) {
        this.mSingleUidCpuTimesAvailable = singleUidCpuTimesAvailable;
    }
}
