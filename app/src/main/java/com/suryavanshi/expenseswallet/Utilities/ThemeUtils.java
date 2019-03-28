package com.suryavanshi.expenseswallet.Utilities;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.widget.LinearLayout;

import com.suryavanshi.expenseswallet.R;

public class ThemeUtils {

    public static int THEME = R.style.AddExpenceBlueTheme;//B for blue R for Red


    public static int getTHEME() {
        return THEME;
    }

    public static void setTHEME(int THEME) {
        ThemeUtils.THEME = THEME;
    }

    public static void changeBackground(int from, int to, LinearLayout ll_top_expence_layout, Dialog categoryDialog){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (int) animator.getAnimatedValue();
                ll_top_expence_layout.setBackgroundColor(color);
            }

        });
        colorAnimation.start();
        if(categoryDialog!=null && categoryDialog.isShowing()){
            categoryDialog.dismiss();
        }
    }

    public static void changePrimaryDarkColor(Activity context, int from, int to){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int color = (int) animator.getAnimatedValue();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    context.getWindow().setStatusBarColor(color);

                }
            }
        });
        colorAnimation.start();
    }
}