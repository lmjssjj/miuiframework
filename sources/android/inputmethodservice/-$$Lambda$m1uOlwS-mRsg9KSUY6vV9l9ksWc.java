package android.inputmethodservice;

import android.os.ResultReceiver;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$m1uOlwS-mRsg9KSUY6vV9l9ksWc implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$m1uOlwS-mRsg9KSUY6vV9l9ksWc INSTANCE = new -$$Lambda$m1uOlwS-mRsg9KSUY6vV9l9ksWc();

    private /* synthetic */ -$$Lambda$m1uOlwS-mRsg9KSUY6vV9l9ksWc() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((CallbackImpl) obj).showSoftInput(((Integer) obj2).intValue(), (ResultReceiver) obj3);
    }
}
