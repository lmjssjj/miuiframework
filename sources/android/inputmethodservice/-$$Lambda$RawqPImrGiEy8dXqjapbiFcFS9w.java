package android.inputmethodservice;

import android.view.inputmethod.CompletionInfo;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RawqPImrGiEy8dXqjapbiFcFS9w implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$RawqPImrGiEy8dXqjapbiFcFS9w INSTANCE = new -$$Lambda$RawqPImrGiEy8dXqjapbiFcFS9w();

    private /* synthetic */ -$$Lambda$RawqPImrGiEy8dXqjapbiFcFS9w() {
    }

    public final void accept(Object obj, Object obj2) {
        ((CallbackImpl) obj).displayCompletions((CompletionInfo[]) obj2);
    }
}
