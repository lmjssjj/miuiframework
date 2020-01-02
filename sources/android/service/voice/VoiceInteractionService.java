package android.service.voice;

import android.annotation.UnsupportedAppUsage;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.soundtrigger.KeyphraseEnrollmentInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import android.service.voice.AlwaysOnHotwordDetector.Callback;
import android.service.voice.IVoiceInteractionService.Stub;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceActionCheckCallback;
import com.android.internal.app.IVoiceInteractionManagerService;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class VoiceInteractionService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.voice.VoiceInteractionService";
    public static final String SERVICE_META_DATA = "android.voice_interaction";
    private AlwaysOnHotwordDetector mHotwordDetector;
    IVoiceInteractionService mInterface = new Stub() {
        public void ready() {
            Handler.getMain().executeOrSendMessage(PooledLambda.obtainMessage(-$$Lambda$SpnCJ0NiI1Uo14qQ5iHFyV2F2mY.INSTANCE, VoiceInteractionService.this));
        }

        public void shutdown() {
            Handler.getMain().executeOrSendMessage(PooledLambda.obtainMessage(-$$Lambda$VoiceInteractionService$1$ILMD_OnlN3EpU4AqKW9H-tgCoMg.INSTANCE, VoiceInteractionService.this));
        }

        public void soundModelsChanged() {
            Handler.getMain().executeOrSendMessage(PooledLambda.obtainMessage(-$$Lambda$VoiceInteractionService$1$WnZueQJxACwCZWfYsmNtGrcNbEc.INSTANCE, VoiceInteractionService.this));
        }

        public void launchVoiceAssistFromKeyguard() {
            Handler.getMain().executeOrSendMessage(PooledLambda.obtainMessage(-$$Lambda$2vcT7tC5Khx2oNbQI6Zvwrft_YM.INSTANCE, VoiceInteractionService.this));
        }

        public void getActiveServiceSupportedActions(List<String> voiceActions, IVoiceActionCheckCallback callback) {
            Handler.getMain().executeOrSendMessage(PooledLambda.obtainMessage(-$$Lambda$VoiceInteractionService$1$gKwKkiuvnPnBCMXtKcZDpBR3098.INSTANCE, VoiceInteractionService.this, voiceActions, callback));
        }
    };
    private KeyphraseEnrollmentInfo mKeyphraseEnrollmentInfo;
    private final Object mLock = new Object();
    IVoiceInteractionManagerService mSystemService;

    public void onLaunchVoiceAssistFromKeyguard() {
    }

    public static boolean isActiveService(Context context, ComponentName service) {
        String cur = Secure.getString(context.getContentResolver(), Secure.VOICE_INTERACTION_SERVICE);
        if (cur == null || cur.isEmpty()) {
            return false;
        }
        ComponentName curComp = ComponentName.unflattenFromString(cur);
        if (curComp == null) {
            return false;
        }
        return curComp.equals(service);
    }

    public void setDisabledShowContext(int flags) {
        try {
            this.mSystemService.setDisabledShowContext(flags);
        } catch (RemoteException e) {
        }
    }

    public int getDisabledShowContext() {
        try {
            return this.mSystemService.getDisabledShowContext();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void showSession(Bundle args, int flags) {
        IVoiceInteractionManagerService iVoiceInteractionManagerService = this.mSystemService;
        if (iVoiceInteractionManagerService != null) {
            try {
                iVoiceInteractionManagerService.showSession(this.mInterface, args, flags);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        throw new IllegalStateException("Not available until onReady() is called");
    }

    public Set<String> onGetSupportedVoiceActions(Set<String> set) {
        return Collections.emptySet();
    }

    public IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mInterface.asBinder();
        }
        return null;
    }

    public void onReady() {
        this.mSystemService = IVoiceInteractionManagerService.Stub.asInterface(ServiceManager.getService(Context.VOICE_INTERACTION_MANAGER_SERVICE));
        this.mKeyphraseEnrollmentInfo = new KeyphraseEnrollmentInfo(getPackageManager());
    }

    private void onShutdownInternal() {
        onShutdown();
        safelyShutdownHotwordDetector();
    }

    public void onShutdown() {
    }

    private void onSoundModelsChangedInternal() {
        synchronized (this) {
            if (this.mHotwordDetector != null) {
                this.mHotwordDetector.onSoundModelsChanged();
            }
        }
    }

    private void onHandleVoiceActionCheck(List<String> voiceActions, IVoiceActionCheckCallback callback) {
        if (callback != null) {
            try {
                callback.onComplete(new ArrayList(onGetSupportedVoiceActions(new ArraySet((Collection) voiceActions))));
            } catch (RemoteException e) {
            }
        }
    }

    public final AlwaysOnHotwordDetector createAlwaysOnHotwordDetector(String keyphrase, Locale locale, Callback callback) {
        if (this.mSystemService != null) {
            synchronized (this.mLock) {
                safelyShutdownHotwordDetector();
                this.mHotwordDetector = new AlwaysOnHotwordDetector(keyphrase, locale, callback, this.mKeyphraseEnrollmentInfo, this.mInterface, this.mSystemService);
            }
            return this.mHotwordDetector;
        }
        throw new IllegalStateException("Not available until onReady() is called");
    }

    /* Access modifiers changed, original: protected|final */
    @VisibleForTesting
    public final KeyphraseEnrollmentInfo getKeyphraseEnrollmentInfo() {
        return this.mKeyphraseEnrollmentInfo;
    }

    @UnsupportedAppUsage
    public final boolean isKeyphraseAndLocaleSupportedForHotword(String keyphrase, Locale locale) {
        KeyphraseEnrollmentInfo keyphraseEnrollmentInfo = this.mKeyphraseEnrollmentInfo;
        boolean z = false;
        if (keyphraseEnrollmentInfo == null) {
            return false;
        }
        if (keyphraseEnrollmentInfo.getKeyphraseMetadata(keyphrase, locale) != null) {
            z = true;
        }
        return z;
    }

    private void safelyShutdownHotwordDetector() {
        try {
            synchronized (this.mLock) {
                if (this.mHotwordDetector != null) {
                    this.mHotwordDetector.stopRecognition();
                    this.mHotwordDetector.invalidate();
                    this.mHotwordDetector = null;
                }
            }
        } catch (Exception e) {
        }
    }

    public final void setUiHints(Bundle hints) {
        if (hints != null) {
            try {
                this.mSystemService.setUiHints(this.mInterface, hints);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("Hints must be non-null");
    }

    /* Access modifiers changed, original: protected */
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("VOICE INTERACTION");
        synchronized (this.mLock) {
            pw.println("  AlwaysOnHotwordDetector");
            if (this.mHotwordDetector == null) {
                pw.println("    NULL");
            } else {
                this.mHotwordDetector.dump("    ", pw);
            }
        }
    }
}
