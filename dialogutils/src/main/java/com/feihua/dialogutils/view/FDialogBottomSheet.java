package com.feihua.dialogutils.view;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.feihua.dialogutils.R;
import com.feihua.dialogutils.util.FUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Create By feihua  On 2022/1/25
 */
public class FDialogBottomSheet extends BottomSheetDialog {

    private boolean isMatch;
    private Context context;

    public FDialogBottomSheet(@NonNull Context context) {
        this(context, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        int width;
        if (context instanceof Activity) {
            Activity a = (Activity) context;
            Display display = a.getWindowManager().getDefaultDisplay();
            android.graphics.Point point = new Point();
            display.getSize(point);
            width = Math.min(point.x, point.y);
        } else {
            DisplayMetrics dm;
            Service se = (Service) context;
            dm = se.getResources().getDisplayMetrics();
            width = Math.min(dm.widthPixels, dm.heightPixels);
        }
        if (window != null) {
            window.setLayout(width, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public FDialogBottomSheet(@NonNull Context context, boolean isMatch) {
        super(context, R.style.bottomSheetDialogStyle);
        this.isMatch = isMatch;
        this.context=context;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // for landscape mode
        if (isMatch) {
            BottomSheetBehavior<FrameLayout> behavior = getBehavior();
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void dismiss() {
        FUtil.closeKeyboard(this);
        super.dismiss();
    }

}