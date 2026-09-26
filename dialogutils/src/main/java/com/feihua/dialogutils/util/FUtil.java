package com.feihua.dialogutils.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Create By feihua  On 2022/1/25
 */
public class FUtil {
    //显示虚拟键盘
    public static void showKeyboard(View v) {
        if (v == null) {
            return;
        }
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        // 不能用 toggleSoftInput：它是切换键盘状态，键盘已弹出时（如从其它输入框打开弹窗）反而会收起。
        // 这里只负责立即弹出，等待时机（如弹窗显示后再弹）由调用方自行延时，与宿主 OYUtils.showKeyboard 一致
        imm.showSoftInput(v, 0);
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
