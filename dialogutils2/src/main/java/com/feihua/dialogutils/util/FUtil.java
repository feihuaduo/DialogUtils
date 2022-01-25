package com.feihua.dialogutils.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Create By feihua  On 2022/1/25
 */
class FUtil {
    //显示虚拟键盘
    public static void showKeyboard(View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void closeKeyboard(Context context) {
        if (context instanceof Activity)
            closeKeyboard((Activity)context);
    }

    //关闭输入法
    public static void closeKeyboard(Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null)
            return;
        View view = activity.getCurrentFocus();
        if (view == null)
            return;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //关闭输入法
    public static void closeKeyboard(Dialog dialog) {
        if (dialog == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null)
            return;
        View view = dialog.getCurrentFocus();
        if (view == null)
            return;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
