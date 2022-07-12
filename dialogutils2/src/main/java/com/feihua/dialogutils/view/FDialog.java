package com.feihua.dialogutils.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feihua.dialogutils.util.FUtil;

/**
 * Create By feihua  On 2022/5/27
 */
public class FDialog extends Dialog {
    public FDialog(@NonNull Context context) {
        super(context);
    }

    public FDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        FUtil.closeKeyboard(this);
        super.dismiss();
    }
}
