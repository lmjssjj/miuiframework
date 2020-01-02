package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.view.InputQueue;
import android.view.InputQueue.Callback;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback2;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import dalvik.system.BaseDexClassLoader;
import java.io.File;

public class NativeActivity extends Activity implements Callback2, Callback, OnGlobalLayoutListener {
    private static final String KEY_NATIVE_SAVED_STATE = "android:native_state";
    public static final String META_DATA_FUNC_NAME = "android.app.func_name";
    public static final String META_DATA_LIB_NAME = "android.app.lib_name";
    private InputQueue mCurInputQueue;
    private SurfaceHolder mCurSurfaceHolder;
    private boolean mDestroyed;
    private boolean mDispatchingUnhandledKey;
    private InputMethodManager mIMM;
    int mLastContentHeight;
    int mLastContentWidth;
    int mLastContentX;
    int mLastContentY;
    final int[] mLocation = new int[2];
    private NativeContentView mNativeContentView;
    @UnsupportedAppUsage
    private long mNativeHandle;

    static class NativeContentView extends View {
        NativeActivity mActivity;

        public NativeContentView(Context context) {
            super(context);
        }

        public NativeContentView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    private native String getDlError();

    @UnsupportedAppUsage
    private native long loadNativeCode(String str, String str2, MessageQueue messageQueue, String str3, String str4, String str5, int i, AssetManager assetManager, byte[] bArr, ClassLoader classLoader, String str6);

    private native void onConfigurationChangedNative(long j);

    private native void onContentRectChangedNative(long j, int i, int i2, int i3, int i4);

    private native void onInputQueueCreatedNative(long j, long j2);

    private native void onInputQueueDestroyedNative(long j, long j2);

    private native void onLowMemoryNative(long j);

    private native void onPauseNative(long j);

    private native void onResumeNative(long j);

    private native byte[] onSaveInstanceStateNative(long j);

    private native void onStartNative(long j);

    private native void onStopNative(long j);

    private native void onSurfaceChangedNative(long j, Surface surface, int i, int i2, int i3);

    private native void onSurfaceCreatedNative(long j, Surface surface);

    private native void onSurfaceDestroyedNative(long j);

    private native void onSurfaceRedrawNeededNative(long j, Surface surface);

    private native void onWindowFocusChangedNative(long j, boolean z);

    private native void unloadNativeCode(long j);

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = savedInstanceState;
        String libname = "main";
        String funcname = "ANativeActivity_onCreate";
        this.mIMM = (InputMethodManager) getSystemService(InputMethodManager.class);
        getWindow().takeSurface(this);
        getWindow().takeInputQueue(this);
        getWindow().setFormat(4);
        getWindow().setSoftInputMode(16);
        this.mNativeContentView = new NativeContentView(this);
        NativeContentView nativeContentView = this.mNativeContentView;
        nativeContentView.mActivity = this;
        setContentView((View) nativeContentView);
        this.mNativeContentView.requestFocus();
        this.mNativeContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        try {
            String libname2;
            String funcname2;
            ActivityInfo ai = getPackageManager().getActivityInfo(getIntent().getComponent(), 128);
            if (ai.metaData != null) {
                String ln = ai.metaData.getString(META_DATA_LIB_NAME);
                if (ln != null) {
                    libname = ln;
                }
                ln = ai.metaData.getString(META_DATA_FUNC_NAME);
                if (ln != null) {
                    funcname = ln;
                }
                libname2 = libname;
                funcname2 = funcname;
            } else {
                libname2 = libname;
                funcname2 = funcname;
            }
            ClassLoader classLoader = (BaseDexClassLoader) getClassLoader();
            String path = classLoader.findLibrary(libname2);
            StringBuilder stringBuilder;
            if (path != null) {
                String ai2 = path;
                this.mNativeHandle = loadNativeCode(path, funcname2, Looper.myQueue(), getAbsolutePath(getFilesDir()), getAbsolutePath(getObbDir()), getAbsolutePath(getExternalFilesDir(null)), VERSION.SDK_INT, getAssets(), bundle != null ? bundle.getByteArray(KEY_NATIVE_SAVED_STATE) : null, classLoader, classLoader.getLdLibraryPath());
                if (this.mNativeHandle != 0) {
                    super.onCreate(savedInstanceState);
                    return;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to load native library \"");
                stringBuilder.append(ai2);
                stringBuilder.append("\": ");
                stringBuilder.append(getDlError());
                throw new UnsatisfiedLinkError(stringBuilder.toString());
            }
            ClassLoader classLoader2 = classLoader;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to find native library ");
            stringBuilder.append(libname2);
            stringBuilder.append(" using classloader: ");
            stringBuilder.append(classLoader2.toString());
            throw new IllegalArgumentException(stringBuilder.toString());
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Error getting activity info", e);
        }
    }

    private static String getAbsolutePath(File file) {
        return file != null ? file.getAbsolutePath() : null;
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        this.mDestroyed = true;
        if (this.mCurSurfaceHolder != null) {
            onSurfaceDestroyedNative(this.mNativeHandle);
            this.mCurSurfaceHolder = null;
        }
        InputQueue inputQueue = this.mCurInputQueue;
        if (inputQueue != null) {
            onInputQueueDestroyedNative(this.mNativeHandle, inputQueue.getNativePtr());
            this.mCurInputQueue = null;
        }
        unloadNativeCode(this.mNativeHandle);
        super.onDestroy();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        onPauseNative(this.mNativeHandle);
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        onResumeNative(this.mNativeHandle);
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        byte[] state = onSaveInstanceStateNative(this.mNativeHandle);
        if (state != null) {
            outState.putByteArray(KEY_NATIVE_SAVED_STATE, state);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        super.onStart();
        onStartNative(this.mNativeHandle);
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        onStopNative(this.mNativeHandle);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!this.mDestroyed) {
            onConfigurationChangedNative(this.mNativeHandle);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        if (!this.mDestroyed) {
            onLowMemoryNative(this.mNativeHandle);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!this.mDestroyed) {
            onWindowFocusChangedNative(this.mNativeHandle, hasFocus);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!this.mDestroyed) {
            this.mCurSurfaceHolder = holder;
            onSurfaceCreatedNative(this.mNativeHandle, holder.getSurface());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (!this.mDestroyed) {
            this.mCurSurfaceHolder = holder;
            onSurfaceChangedNative(this.mNativeHandle, holder.getSurface(), format, width, height);
        }
    }

    public void surfaceRedrawNeeded(SurfaceHolder holder) {
        if (!this.mDestroyed) {
            this.mCurSurfaceHolder = holder;
            onSurfaceRedrawNeededNative(this.mNativeHandle, holder.getSurface());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mCurSurfaceHolder = null;
        if (!this.mDestroyed) {
            onSurfaceDestroyedNative(this.mNativeHandle);
        }
    }

    public void onInputQueueCreated(InputQueue queue) {
        if (!this.mDestroyed) {
            this.mCurInputQueue = queue;
            onInputQueueCreatedNative(this.mNativeHandle, queue.getNativePtr());
        }
    }

    public void onInputQueueDestroyed(InputQueue queue) {
        if (!this.mDestroyed) {
            onInputQueueDestroyedNative(this.mNativeHandle, queue.getNativePtr());
            this.mCurInputQueue = null;
        }
    }

    public void onGlobalLayout() {
        this.mNativeContentView.getLocationInWindow(this.mLocation);
        int w = this.mNativeContentView.getWidth();
        int h = this.mNativeContentView.getHeight();
        int[] iArr = this.mLocation;
        if (iArr[0] != this.mLastContentX || iArr[1] != this.mLastContentY || w != this.mLastContentWidth || h != this.mLastContentHeight) {
            iArr = this.mLocation;
            this.mLastContentX = iArr[0];
            this.mLastContentY = iArr[1];
            this.mLastContentWidth = w;
            this.mLastContentHeight = h;
            if (!this.mDestroyed) {
                onContentRectChangedNative(this.mNativeHandle, this.mLastContentX, this.mLastContentY, this.mLastContentWidth, this.mLastContentHeight);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setWindowFlags(int flags, int mask) {
        getWindow().setFlags(flags, mask);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setWindowFormat(int format) {
        getWindow().setFormat(format);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void showIme(int mode) {
        this.mIMM.showSoftInput(this.mNativeContentView, mode);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void hideIme(int mode) {
        this.mIMM.hideSoftInputFromWindow(this.mNativeContentView.getWindowToken(), mode);
    }
}
