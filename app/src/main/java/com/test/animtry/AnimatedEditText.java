package com.test.animtry;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by victor on 31.08.17.
 */

public class AnimatedEditText extends RelativeLayout implements View.OnClickListener, View.OnTouchListener, TextView.OnEditorActionListener, View.OnFocusChangeListener, DynamicAnimation.OnAnimationEndListener {

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
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setLines(1);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setOnEditorActionListener(this);
        input.setOnTouchListener(this);
        input.setLayoutParams(iParams);
        input.setText("Some text");
        input.setOnFocusChangeListener(this);

        input.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                input.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                animToLeft = SpringAnimationUtils.createSpringAnimation(input, SpringAnimationUtils.TRANSLATION_X,
                        12, 10);
                animToLeft.addEndListener(AnimatedEditText.this);

                animToRight = SpringAnimationUtils.createSpringAnimation(input, SpringAnimationUtils.TRANSLATION_X,
                        input.getX(), 30);
                animToRight.addEndListener(AnimatedEditText.this);
            }
        });

        this.addView(input);
    }

    @Override
    public void onClick(View v) {
        if (state == STATE_CLOSED) {
            openInput();
            showKeyboard();
        }
    }

    private void closeInput() {
        animToVisible.start();
        animToRight.start();
        hideKeyboard();
    }

    private void openInput() {
        animToHide.start();
        animToLeft.start();
        input.requestFocus();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                && state == STATE_CLOSED) {
            performClick();
        }

        return false;
    }



    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            input.clearFocus();
            closeInput();
        }
        return false;
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            closeInput();
        }
    }

    @Override
    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
        if (state == STATE_CLOSED) {
            state = STATE_OPENED;
        } else if (state == STATE_OPENED) {
            state = STATE_CLOSED;
        }
    }
}
