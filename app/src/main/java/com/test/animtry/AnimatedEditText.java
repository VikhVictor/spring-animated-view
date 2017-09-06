package com.test.animtry;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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
    private TextView value;
    private EditText input;

    private boolean isMoving = false;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.input.setWidth(widthMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnClickListener(this);

        label = new TextView(getContext());

        RelativeLayout.LayoutParams lParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        lParams.setMargins(dpToPx(20), 0, 0, 0);
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

        value = new TextView(getContext());

        RelativeLayout.LayoutParams vParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vParams.addRule(CENTER_VERTICAL);
        /*vParams.setMargins(0, 0, dpToPx(20), 0);*/

        value.setBackgroundColor(Color.TRANSPARENT);
        value.setLines(1);
        value.setTextSize(14f);
        value.setEllipsize(TextUtils.TruncateAt.END);
        value.setLayoutParams(vParams);
        value.setText("Some text");
        value.setBackgroundColor(Color.GREEN);

        value.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                value.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                value.setX(getWidth() - value.getWidth() - dpToPx(20));
                animToLeft = SpringAnimationUtils.createSpringAnimation(value, SpringAnimationUtils.TRANSLATION_X,
                        dpToPx(20), 10);
                animToLeft.addEndListener(AnimatedEditText.this);


                animToRight = SpringAnimationUtils.createSpringAnimation(value, SpringAnimationUtils.TRANSLATION_X,
                        value.getX(), 30);
                animToRight.addEndListener(AnimatedEditText.this);
            }
        });
        this.addView(value);

        input = new EditText(getContext());

        RelativeLayout.LayoutParams iParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        iParams.addRule(RIGHT_OF, label.getId());
        //iParams.addRule(ALIGN_PARENT_RIGHT);
        iParams.addRule(CENTER_VERTICAL);
        iParams.setMargins(dpToPx(20), 0, dpToPx(20), 0);

        input.setTextSize(14f);
        input.setOnFocusChangeListener(this);
        input.setLines(1);
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //input.setEllipsize(TextUtils.TruncateAt.END);
        input.setBackgroundColor(Color.TRANSPARENT);
        input.setVisibility(GONE);
        input.setLayoutParams(iParams);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                value.setText(editable.toString());
                //recurseWrapContent(value, true);
            }
        });

        this.addView(input);
    }

    @Override
    public void onClick(View v) {
        if (state == STATE_CLOSED) {
            animToLeft.start();
            animToHide.start();
            RelativeLayout.LayoutParams vParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            vParams.addRule(CENTER_VERTICAL);
            value.setLayoutParams(vParams);
        } else if (state == STATE_OPENED) {
            animToRight = SpringAnimationUtils.createSpringAnimation(value, SpringAnimationUtils.TRANSLATION_X,
                    getWidth() - value.getWidth() - dpToPx(20), 1);
            animToRight.addEndListener(this);
            animToRight.start();
            animToVisible.start();
        }
    }

    private void closeInput() {

    }

    private void openInput() {

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
        return false;
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        return false;
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            input.setVisibility(GONE);
            value.setVisibility(VISIBLE);
            hideKeyboard();
            int iLeft = getWidth() - value.getWidth() - dpToPx(20);
            if (iLeft <= label.getX() + label.getWidth() + dpToPx(20)) {
                animToRight = SpringAnimationUtils.createSpringAnimation(value, SpringAnimationUtils.TRANSLATION_X,
                        label.getX() + label.getWidth() + dpToPx(20), 1);
                value.setWidth((int) (getWidth() - (label.getX() + label.getWidth() + 2 * dpToPx(20))));
            } else {
                animToRight = SpringAnimationUtils.createSpringAnimation(value, SpringAnimationUtils.TRANSLATION_X,
                        getWidth() - value.getWidth() - dpToPx(20), 1);
            }
            animToRight.addEndListener(this);
            animToRight.start();
            animToVisible.start();
        }
    }

    @Override
    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float val, float velocity) {
        if (state == STATE_OPENED) {
            state = STATE_CLOSED;
        } else if (state == STATE_CLOSED) {
            state = STATE_OPENED;
            input.setVisibility(VISIBLE);
            input.requestFocus();
            showKeyboard();
            input.setSelection(input.getText().length());
            value.setVisibility(INVISIBLE);
        }
    }

    public void setLabelText(String text) {
        this.label.setText(text);
    }

    public void setInputText(String text) {
        this.input.setText(text);
    }

    public void setValueText(String text) {
        this.value.setText(text);
    }

    public String getInputText() {
        return this.input.getText().toString();
    }

    private int dpToPx(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getContext()
                .getResources().getDisplayMetrics());
        return px;
    }


    /**
     * Internal method to recurse on view tree. Tag you View nodes in XML layouts to read the logs more easily
     */
    private static void recurseWrapContent( View nodeView, boolean relayoutAllNodes )
    {
        // Does not recurse when visibility GONE
        if ( nodeView.getVisibility() == View.GONE ) {
            // nodeView.layout( nodeView.getLeft(), nodeView.getTop(), 0, 0 );		// No need
            return;
        }

        ViewGroup.LayoutParams layoutParams = nodeView.getLayoutParams();
        boolean isWrapWidth  = ( layoutParams.width  == LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;
        boolean isWrapHeight = ( layoutParams.height == LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;

        if ( isWrapWidth || isWrapHeight ) {

            boolean changed = false;
            int right  = nodeView.getRight();
            int bottom = nodeView.getBottom();

            if ( isWrapWidth  && nodeView.getMeasuredWidth() > 0 ) {
                right = nodeView.getLeft() + nodeView.getMeasuredWidth();
                changed = true;
                Log.v("TAG", "+++ LayoutWrapContentUpdater recurseWrapContent set Width to "+ nodeView.getMeasuredWidth() +" of node Tag="+ nodeView.getTag() +" ["+ nodeView +"]");
            }
            if ( isWrapHeight && nodeView.getMeasuredHeight() > 0 ) {
                bottom = nodeView.getTop() + nodeView.getMeasuredHeight();
                changed = true;
                Log.v("TAG", "+++ LayoutWrapContentUpdater recurseWrapContent set Height to "+ nodeView.getMeasuredHeight() +" of node Tag="+ nodeView.getTag() +" ["+ nodeView +"]");
            }

            if (changed) {
                nodeView.layout( nodeView.getLeft(), nodeView.getTop(), right, bottom );
                // FIXME: Adjust left & top position when gravity = "center" / "bottom" / "right"
            }
        }

        // --- Recurse
        if ( nodeView instanceof ViewGroup ) {
            ViewGroup nodeGroup = (ViewGroup)nodeView;
            for (int i = 0; i < nodeGroup.getChildCount(); i++) {
                recurseWrapContent( nodeGroup.getChildAt(i), relayoutAllNodes );
            }
        }
        return;
    }
}
