package com.test.animtry;

import android.support.animation.DynamicAnimation;
import android.support.animation.FloatPropertyCompat;
import android.support.animation.SpringAnimation;
import android.view.View;

/**
 * Created by victor on 31.08.17.
 */

public class SpringAnimationUtils {

    public static final int ALPA = 0;
    public static final int TRANSLATION_X = 1;

    public static SpringAnimation createSpringAnimation(View view, int property, float endValue, float velocity) {

        SpringAnimation animation = new SpringAnimation(view, getProperty(property), endValue)
                .setMinimumVisibleChange(getMinVisibleChange(property))
                .setStartVelocity(velocity);
        return animation;
    }

    private static float getMinVisibleChange(int property) {
        switch (property) {
            case ALPA:
                return DynamicAnimation.MIN_VISIBLE_CHANGE_ALPHA;
            default:
                return DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS;
        }
    }

    private static FloatPropertyCompat<View> getProperty(int property) {
        switch (property) {
            case ALPA:
                return getAlphaProperty();
            default:
                return getTranslationX();
        }
    }

    private static FloatPropertyCompat<View> getTranslationX() {
        return new FloatPropertyCompat<View>("TranslationX") {
            @Override
            public float getValue(View view) {
                return view.getX();
            }

            @Override
            public void setValue(View view, float value) {
                view.setX(value);
            }
        };
    }

    public static FloatPropertyCompat<View> getAlphaProperty() {
        return new FloatPropertyCompat<View>("Alpha") {
            @Override
            public float getValue(View view) {
                return view.getAlpha();
            }

            @Override
            public void setValue(View view, float value) {
                view.setAlpha(value);
            }
        };
    }

}
