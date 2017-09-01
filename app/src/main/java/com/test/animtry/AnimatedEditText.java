package com.test.animtry;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by victor on 31.08.17.
 */

public class AnimatedEditText extends RelativeLayout {

    private TextView label;
    private EditText input;

    public AnimatedEditText(Context context) {
        super(context);
    }

    public AnimatedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
