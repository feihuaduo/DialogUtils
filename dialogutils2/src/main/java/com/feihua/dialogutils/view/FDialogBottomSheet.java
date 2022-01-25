package com.feihua.dialogutils.view;


import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Create By feihua  On 2022/1/25
 */
public class FDialogBottomSheet extends BottomSheetDialog {

    private boolean isMatch;

    public FDialogBottomSheet(@NonNull Context context) {
        this(context, true);
    }

    public FDialogBottomSheet(@NonNull Context context, boolean isMatch) {
        super(context);
        this.isMatch = isMatch;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // for landscape mode
        if (isMatch) {
            BottomSheetBehavior behavior = getBehavior();
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}