package com.test.animtry;

import android.support.animation.FloatPropertyCompat;
import android.support.animation.SpringAnimation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mBtnName;
    private TextView mTVLabel;
    private EditText mETName;

    private boolean isOpened = false;

    private SpringAnimation animToHide, animToVisible, animToLeft, animToRight;
    private FloatPropertyCompat<View> propertyCompat, alphaProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnName = (RelativeLayout) findViewById(R.id.btn_name);
        mTVLabel = (TextView) findViewById(R.id.tv_label);
        mETName = (EditText) findViewById(R.id.et_name);

        mBtnName.setOnClickListener(this);


        mTVLabel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTVLabel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mTVLabel.getX(); //height is ready

                animToHide = SpringAnimationUtils.createSpringAnimation(mTVLabel, SpringAnimationUtils.ALPA,
                        0.0f, 0.5f);
                animToVisible = SpringAnimationUtils.createSpringAnimation(mTVLabel, SpringAnimationUtils.ALPA,
                        1.0f, 0.5f);
            }
        });

        mETName.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                float startPos = getLeftCoord(mETName);
                animToLeft = SpringAnimationUtils.createSpringAnimation(mETName, SpringAnimationUtils.TRANSLATION_X,
                        12, 10);
                animToRight = SpringAnimationUtils.createSpringAnimation(mETName, SpringAnimationUtils.TRANSLATION_X,
                        mETName.getX(), 30);
            }
        });

    }

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    public int getLeftCoord(View view) {
        int[] coords = {0, 0};
        view.getLocationOnScreen(coords);
        int absoluteLeft = coords[0];
        return absoluteLeft;
    }

    @Override
    public void onClick(View view) {
        if (isOpened) {
            animToRight.start();
            animToVisible.start();
            isOpened = false;
        } else {
            animToLeft.start();
            animToHide.start();
            isOpened = true;
        }
    }
}
