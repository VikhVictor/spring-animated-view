package com.test.animtry;

import android.content.Context;
import android.graphics.Color;
import android.support.animation.SpringAnimation;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by victor on 31.08.17.
 */

public class AnimatedEditText extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private static final int STATE_CLOSED = 0;
    private static final int STATE_OPENED = 1;

    private TextView label;
    private EditText input;

    private int state = STATE_CLOSED;

    private SpringAnimation animToHide, animToVisible, animToLeft, animToRight;


    public AnimatedEditText(Context context) {
        super(context);
        init();
    }

    public AnimatedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnClickListener(this);

        label = new TextView(getContext());

        RelativeLayout.LayoutParams lParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        lParams.addRule(ALIGN_PARENT_LEFT);
        lParams.addRule(CENTER_VERTICAL);
        label.setLayoutParams(lParams);
        label.setText("Label");
        label.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                label.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                animToHide = SpringAnimationUtils.createSpringAnimation(label, SpringAnimationUtils.ALPA,
                        0.0f, 0.5f);
                animToVisible = SpringAnimationUtils.createSpringAnimation(label, SpringAnimationUtils.ALPA,
                        1.0f, 0.5f);
            }
        });

        this.addView(label);

        input = new EditText(getContext());

        RelativeLayout.LayoutParams iParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        iParams.addRule(ALIGN_PARENT_RIGHT);
        iParams.addRule(CENTER_VERTICAL);

        input.setBackgroundColor(Color.TRANSPARENT);
        //input.setFocusable(false);
        //input.setClickable(false);
        //input.setEnabled(false);
        input.setOnTouchListener(this);
        input.setLayoutParams(iParams);
        input.setText("Some text");

        input.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                input.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                animToLeft = SpringAnimationUtils.createSpringAnimation(input, SpringAnimationUtils.TRANSLATION_X,
                        12, 10);
                animToRight = SpringAnimationUtils.createSpringAnimation(input, SpringAnimationUtils.TRANSLATION_X,
                        input.getX(), 30);
            }
        });

        this.addView(input);
    }

    @Override
    public void onClick(View v) {
        if (state == STATE_CLOSED) {
            animToHide.start();
            animToLeft.start();
            input.requestFocus();
            state = STATE_OPENED;
        } else if (state == STATE_OPENED) {
            animToVisible.start();
            animToRight.start();
            state = STATE_CLOSED;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                && state == STATE_CLOSED) {
            performClick();
        }

        return false;
    }
}
