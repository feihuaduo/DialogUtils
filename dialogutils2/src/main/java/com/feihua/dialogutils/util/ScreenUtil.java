package com.feihua.dialogutils.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Create By feihua  On 2022/1/16
 */
public class ScreenUtil {
    //dp转px
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    //px转dp
    public static int px2dp(Context context, int pxValue) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, context.getResources().getDisplayMetrics()));
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int[] getScreenInfo(Activity context){
        int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        int screenHeight = context.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
        return new int[]{screenHeight,screenWidth};
    }

    /**
     * 返回当前屏幕是否为竖屏。
     * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
         return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}