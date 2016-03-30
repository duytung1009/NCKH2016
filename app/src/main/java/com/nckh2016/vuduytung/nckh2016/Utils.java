package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Tung on 30/3/2016.
 */
public class Utils {

    public static void showProcessBar(Context context, View processBar, View content){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        processBar.startAnimation(animFadeIn);
        content.setVisibility(View.GONE);
        processBar.setVisibility(View.VISIBLE);
    }

    public static void hideProcessBar(Context context, View processBar, View content){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_out);
        content.startAnimation(animFadeIn);
        processBar.startAnimation(animFadeOut);
        content.setVisibility(View.VISIBLE);
        processBar.setVisibility(View.GONE);
    }
}
