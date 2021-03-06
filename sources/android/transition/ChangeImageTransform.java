package android.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.transition.TransitionUtils.MatrixEvaluator;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.Map;

public class ChangeImageTransform extends Transition {
    private static Property<ImageView, Matrix> ANIMATED_TRANSFORM_PROPERTY = new Property<ImageView, Matrix>(Matrix.class, "animatedTransform") {
        public void set(ImageView object, Matrix value) {
            object.animateTransform(value);
        }

        public Matrix get(ImageView object) {
            return null;
        }
    };
    private static TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>() {
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            return null;
        }
    };
    private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";
    private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
    private static final String TAG = "ChangeImageTransform";
    private static final String[] sTransitionProperties = new String[]{PROPNAME_MATRIX, PROPNAME_BOUNDS};

    public ChangeImageTransform(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        TransitionValues transitionValues2 = transitionValues;
        View view = transitionValues2.view;
        if ((view instanceof ImageView) && view.getVisibility() == 0) {
            ImageView imageView = (ImageView) view;
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                Matrix matrix;
                Map<String, Object> values = transitionValues2.values;
                Rect bounds = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                values.put(PROPNAME_BOUNDS, bounds);
                ScaleType scaleType = imageView.getScaleType();
                int drawableWidth = drawable.getIntrinsicWidth();
                int drawableHeight = drawable.getIntrinsicHeight();
                if (scaleType != ScaleType.FIT_XY || drawableWidth <= 0 || drawableHeight <= 0) {
                    matrix = new Matrix(imageView.getImageMatrix());
                } else {
                    float scaleX = ((float) bounds.width()) / ((float) drawableWidth);
                    float scaleY = ((float) bounds.height()) / ((float) drawableHeight);
                    matrix = new Matrix();
                    matrix.setScale(scaleX, scaleY);
                }
                values.put(PROPNAME_MATRIX, matrix);
            }
        }
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Map map = startValues.values;
        String str = PROPNAME_BOUNDS;
        Rect startBounds = (Rect) map.get(str);
        Rect endBounds = (Rect) endValues.values.get(str);
        Map map2 = startValues.values;
        String str2 = PROPNAME_MATRIX;
        Matrix startMatrix = (Matrix) map2.get(str2);
        Matrix endMatrix = (Matrix) endValues.values.get(str2);
        if (startBounds == null || endBounds == null || startMatrix == null || endMatrix == null) {
            return null;
        }
        if (startBounds.equals(endBounds) && startMatrix.equals(endMatrix)) {
            return null;
        }
        ObjectAnimator animator;
        ImageView imageView = endValues.view;
        Drawable drawable = imageView.getDrawable();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            animator = createNullAnimator(imageView);
        } else {
            ANIMATED_TRANSFORM_PROPERTY.set(imageView, startMatrix);
            animator = createMatrixAnimator(imageView, startMatrix, endMatrix);
        }
        return animator;
    }

    private ObjectAnimator createNullAnimator(ImageView imageView) {
        return ObjectAnimator.ofObject((Object) imageView, ANIMATED_TRANSFORM_PROPERTY, NULL_MATRIX_EVALUATOR, Matrix.IDENTITY_MATRIX, Matrix.IDENTITY_MATRIX);
    }

    private ObjectAnimator createMatrixAnimator(ImageView imageView, Matrix startMatrix, Matrix endMatrix) {
        return ObjectAnimator.ofObject((Object) imageView, ANIMATED_TRANSFORM_PROPERTY, new MatrixEvaluator(), startMatrix, endMatrix);
    }
}
