package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.internal.R;

public class ExtractEditLayout extends LinearLayout {
    Button mExtractActionButton;

    public ExtractEditLayout(Context context) {
        super(context);
    }

    public ExtractEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mExtractActionButton = (Button) findViewById(R.id.inputExtractAction);
    }
}
