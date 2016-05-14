package com.nckh2016.vuduytung.nckh2016.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Tung on 14/5/2016.
 * http://stackoverflow.com/questions/16641539/animate-change-width-of-android-relativelayout
 */
public class ResizeHeightAnimation extends Animation {
    private int mHeight;
    private int mStartHeight;
    private View mView;

    public ResizeHeightAnimation(View view, int height) {
        mView = view;
        mHeight = height;
        mStartHeight = view.getHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = mStartHeight + (int) ((mHeight - mStartHeight) * interpolatedTime);
        mView.getLayoutParams().height = newHeight;
        mView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
