package android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.RenderNode;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseIntArray;
import com.android.internal.util.VirtualRefBasePtr;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import com.android.internal.view.animation.HasNativeInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import java.util.ArrayList;
import java.util.Objects;

public class RenderNodeAnimator extends Animator {
    public static final int ALPHA = 11;
    public static final int LAST_VALUE = 11;
    public static final int PAINT_ALPHA = 1;
    public static final int PAINT_STROKE_WIDTH = 0;
    public static final int ROTATION = 5;
    public static final int ROTATION_X = 6;
    public static final int ROTATION_Y = 7;
    public static final int SCALE_X = 3;
    public static final int SCALE_Y = 4;
    private static final int STATE_DELAYED = 1;
    private static final int STATE_FINISHED = 3;
    private static final int STATE_PREPARE = 0;
    private static final int STATE_RUNNING = 2;
    public static final int TRANSLATION_X = 0;
    public static final int TRANSLATION_Y = 1;
    public static final int TRANSLATION_Z = 2;
    public static final int X = 8;
    public static final int Y = 9;
    public static final int Z = 10;
    private static ThreadLocal<DelayedAnimationHelper> sAnimationHelper = new ThreadLocal();
    private static final SparseIntArray sViewPropertyAnimatorMap = new SparseIntArray(15) {
    };
    private float mFinalValue;
    private Handler mHandler;
    private TimeInterpolator mInterpolator;
    private VirtualRefBasePtr mNativePtr;
    private int mRenderProperty = -1;
    private long mStartDelay = 0;
    private long mStartTime;
    private int mState = 0;
    private RenderNode mTarget;
    private final boolean mUiThreadHandlesDelay;
    private long mUnscaledDuration = 300;
    private long mUnscaledStartDelay = 0;
    private View mViewTarget;

    private static class DelayedAnimationHelper implements Runnable {
        private boolean mCallbackScheduled;
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private ArrayList<RenderNodeAnimator> mDelayedAnims = new ArrayList();

        public void addDelayedAnimation(RenderNodeAnimator animator) {
            this.mDelayedAnims.add(animator);
            scheduleCallback();
        }

        public void removeDelayedAnimation(RenderNodeAnimator animator) {
            this.mDelayedAnims.remove(animator);
        }

        private void scheduleCallback() {
            if (!this.mCallbackScheduled) {
                this.mCallbackScheduled = true;
                this.mChoreographer.postCallback(1, this, null);
            }
        }

        public void run() {
            long frameTimeMs = this.mChoreographer.getFrameTime();
            this.mCallbackScheduled = false;
            int end = 0;
            for (int i = 0; i < this.mDelayedAnims.size(); i++) {
                RenderNodeAnimator animator = (RenderNodeAnimator) this.mDelayedAnims.get(i);
                if (!animator.processDelayed(frameTimeMs)) {
                    if (end != i) {
                        this.mDelayedAnims.set(end, animator);
                    }
                    end++;
                }
            }
            while (this.mDelayedAnims.size() > end) {
                ArrayList arrayList = this.mDelayedAnims;
                arrayList.remove(arrayList.size() - 1);
            }
            if (this.mDelayedAnims.size() > 0) {
                scheduleCallback();
            }
        }
    }

    private static native long nCreateAnimator(int i, float f);

    private static native long nCreateCanvasPropertyFloatAnimator(long j, float f);

    private static native long nCreateCanvasPropertyPaintAnimator(long j, int i, float f);

    private static native long nCreateRevealAnimator(int i, int i2, float f, float f2);

    private static native void nEnd(long j);

    private static native long nGetDuration(long j);

    private static native void nSetAllowRunningAsync(long j, boolean z);

    private static native void nSetDuration(long j, long j2);

    private static native void nSetInterpolator(long j, long j2);

    private static native void nSetListener(long j, RenderNodeAnimator renderNodeAnimator);

    private static native void nSetStartDelay(long j, long j2);

    private static native void nSetStartValue(long j, float f);

    private static native void nStart(long j);

    @UnsupportedAppUsage
    public static int mapViewPropertyToRenderProperty(int viewProperty) {
        return sViewPropertyAnimatorMap.get(viewProperty);
    }

    @UnsupportedAppUsage
    public RenderNodeAnimator(int property, float finalValue) {
        this.mRenderProperty = property;
        this.mFinalValue = finalValue;
        this.mUiThreadHandlesDelay = true;
        init(nCreateAnimator(property, finalValue));
    }

    @UnsupportedAppUsage
    public RenderNodeAnimator(CanvasProperty<Float> property, float finalValue) {
        init(nCreateCanvasPropertyFloatAnimator(property.getNativeContainer(), finalValue));
        this.mUiThreadHandlesDelay = false;
    }

    @UnsupportedAppUsage
    public RenderNodeAnimator(CanvasProperty<Paint> property, int paintField, float finalValue) {
        init(nCreateCanvasPropertyPaintAnimator(property.getNativeContainer(), paintField, finalValue));
        this.mUiThreadHandlesDelay = false;
    }

    public RenderNodeAnimator(int x, int y, float startRadius, float endRadius) {
        init(nCreateRevealAnimator(x, y, startRadius, endRadius));
        this.mUiThreadHandlesDelay = true;
    }

    private void init(long ptr) {
        this.mNativePtr = new VirtualRefBasePtr(ptr);
    }

    private void checkMutable() {
        if (this.mState != 0) {
            throw new IllegalStateException("Animator has already started, cannot change it now!");
        } else if (this.mNativePtr == null) {
            throw new IllegalStateException("Animator's target has been destroyed (trying to modify an animation after activity destroy?)");
        }
    }

    static boolean isNativeInterpolator(TimeInterpolator interpolator) {
        return interpolator.getClass().isAnnotationPresent(HasNativeInterpolator.class);
    }

    private void applyInterpolator() {
        TimeInterpolator timeInterpolator = this.mInterpolator;
        if (timeInterpolator != null && this.mNativePtr != null) {
            long ni;
            if (isNativeInterpolator(timeInterpolator)) {
                ni = ((NativeInterpolatorFactory) this.mInterpolator).createNativeInterpolator();
            } else {
                ni = FallbackLUTInterpolator.createNativeInterpolator(this.mInterpolator, nGetDuration(this.mNativePtr.get()));
            }
            nSetInterpolator(this.mNativePtr.get(), ni);
        }
    }

    public void start() {
        if (this.mTarget == null) {
            throw new IllegalStateException("Missing target!");
        } else if (this.mState == 0) {
            this.mState = 1;
            if (this.mHandler == null) {
                this.mHandler = new Handler(true);
            }
            applyInterpolator();
            if (this.mNativePtr == null) {
                cancel();
            } else if (this.mStartDelay <= 0 || !this.mUiThreadHandlesDelay) {
                nSetStartDelay(this.mNativePtr.get(), this.mStartDelay);
                doStart();
            } else {
                getHelper().addDelayedAnimation(this);
            }
        } else {
            throw new IllegalStateException("Already started!");
        }
    }

    private void doStart() {
        if (this.mRenderProperty == 11) {
            this.mViewTarget.ensureTransformationInfo();
            this.mViewTarget.setAlphaInternal(this.mFinalValue);
        }
        moveToRunningState();
        View view = this.mViewTarget;
        if (view != null) {
            view.invalidateViewProperty(true, false);
        }
    }

    private void moveToRunningState() {
        this.mState = 2;
        VirtualRefBasePtr virtualRefBasePtr = this.mNativePtr;
        if (virtualRefBasePtr != null) {
            nStart(virtualRefBasePtr.get());
        }
        notifyStartListeners();
    }

    private void notifyStartListeners() {
        ArrayList<AnimatorListener> listeners = cloneListeners();
        int numListeners = listeners == null ? 0 : listeners.size();
        for (int i = 0; i < numListeners; i++) {
            ((AnimatorListener) listeners.get(i)).onAnimationStart(this);
        }
    }

    public void cancel() {
        int i = this.mState;
        if (i != 0 && i != 3) {
            if (i == 1) {
                getHelper().removeDelayedAnimation(this);
                moveToRunningState();
            }
            ArrayList<AnimatorListener> listeners = cloneListeners();
            int numListeners = listeners == null ? 0 : listeners.size();
            for (int i2 = 0; i2 < numListeners; i2++) {
                ((AnimatorListener) listeners.get(i2)).onAnimationCancel(this);
            }
            end();
        }
    }

    public void end() {
        int i = this.mState;
        if (i != 3) {
            if (i < 2) {
                getHelper().removeDelayedAnimation(this);
                doStart();
            }
            VirtualRefBasePtr virtualRefBasePtr = this.mNativePtr;
            if (virtualRefBasePtr != null) {
                nEnd(virtualRefBasePtr.get());
                View view = this.mViewTarget;
                if (view != null) {
                    view.invalidateViewProperty(true, false);
                    return;
                }
                return;
            }
            onFinished();
        }
    }

    public void pause() {
        throw new UnsupportedOperationException();
    }

    public void resume() {
        throw new UnsupportedOperationException();
    }

    @UnsupportedAppUsage
    public void setTarget(View view) {
        this.mViewTarget = view;
        setTarget(this.mViewTarget.mRenderNode);
    }

    public void setTarget(RecordingCanvas canvas) {
        setTarget(canvas.mNode);
    }

    @UnsupportedAppUsage
    public void setTarget(DisplayListCanvas canvas) {
        setTarget((RecordingCanvas) canvas);
    }

    private void setTarget(RenderNode node) {
        checkMutable();
        if (this.mTarget == null) {
            nSetListener(this.mNativePtr.get(), this);
            this.mTarget = node;
            this.mTarget.addAnimator(this);
            return;
        }
        throw new IllegalStateException("Target already set!");
    }

    @UnsupportedAppUsage
    public void setStartValue(float startValue) {
        checkMutable();
        nSetStartValue(this.mNativePtr.get(), startValue);
    }

    public void setStartDelay(long startDelay) {
        checkMutable();
        if (startDelay >= 0) {
            this.mUnscaledStartDelay = startDelay;
            this.mStartDelay = (long) (ValueAnimator.getDurationScale() * ((float) startDelay));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("startDelay must be positive; ");
        stringBuilder.append(startDelay);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public long getStartDelay() {
        return this.mUnscaledStartDelay;
    }

    @UnsupportedAppUsage
    public RenderNodeAnimator setDuration(long duration) {
        checkMutable();
        if (duration >= 0) {
            this.mUnscaledDuration = duration;
            nSetDuration(this.mNativePtr.get(), (long) (((float) duration) * ValueAnimator.getDurationScale()));
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("duration must be positive; ");
        stringBuilder.append(duration);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public long getDuration() {
        return this.mUnscaledDuration;
    }

    public long getTotalDuration() {
        return this.mUnscaledDuration + this.mUnscaledStartDelay;
    }

    public boolean isRunning() {
        int i = this.mState;
        return i == 1 || i == 2;
    }

    public boolean isStarted() {
        return this.mState != 0;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        checkMutable();
        this.mInterpolator = interpolator;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    /* Access modifiers changed, original: protected */
    public void onFinished() {
        int i = this.mState;
        if (i == 0) {
            releaseNativePtr();
            return;
        }
        if (i == 1) {
            getHelper().removeDelayedAnimation(this);
            notifyStartListeners();
        }
        this.mState = 3;
        ArrayList<AnimatorListener> listeners = cloneListeners();
        int numListeners = listeners == null ? 0 : listeners.size();
        for (int i2 = 0; i2 < numListeners; i2++) {
            ((AnimatorListener) listeners.get(i2)).onAnimationEnd(this);
        }
        releaseNativePtr();
    }

    private void releaseNativePtr() {
        VirtualRefBasePtr virtualRefBasePtr = this.mNativePtr;
        if (virtualRefBasePtr != null) {
            virtualRefBasePtr.release();
            this.mNativePtr = null;
        }
    }

    private ArrayList<AnimatorListener> cloneListeners() {
        ArrayList<AnimatorListener> listeners = getListeners();
        if (listeners != null) {
            return (ArrayList) listeners.clone();
        }
        return listeners;
    }

    public long getNativeAnimator() {
        return this.mNativePtr.get();
    }

    private boolean processDelayed(long frameTimeMs) {
        long j = this.mStartTime;
        if (j == 0) {
            this.mStartTime = frameTimeMs;
        } else if (frameTimeMs - j >= this.mStartDelay) {
            doStart();
            return true;
        }
        return false;
    }

    private static DelayedAnimationHelper getHelper() {
        DelayedAnimationHelper helper = (DelayedAnimationHelper) sAnimationHelper.get();
        if (helper != null) {
            return helper;
        }
        helper = new DelayedAnimationHelper();
        sAnimationHelper.set(helper);
        return helper;
    }

    @UnsupportedAppUsage
    private static void callOnFinished(RenderNodeAnimator animator) {
        Handler handler = animator.mHandler;
        if (handler != null) {
            Objects.requireNonNull(animator);
            handler.post(new -$$Lambda$1kvF4JuyM42-wmyDVPAIYdPz1jE(animator));
            return;
        }
        handler = new Handler(Looper.getMainLooper(), null, true);
        Objects.requireNonNull(animator);
        handler.post(new -$$Lambda$1kvF4JuyM42-wmyDVPAIYdPz1jE(animator));
    }

    public Animator clone() {
        throw new IllegalStateException("Cannot clone this animator");
    }

    public void setAllowRunningAsynchronously(boolean mayRunAsync) {
        checkMutable();
        nSetAllowRunningAsync(this.mNativePtr.get(), mayRunAsync);
    }
}
