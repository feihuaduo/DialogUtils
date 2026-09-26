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
import androidx.annotation.StyleRes;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    /**
     * 内容根是否自动避让系统栏（默认 true）。
     * false 时库不做任何内容避让，也不保留任何残留 padding，
     * insets 处理完全交由调用方接管（配合构造时传入自定义主题）
     */
    private boolean autoContentInsets = true;

    /**
     * 内容根是否避让 ime（默认 false，默认只避让 systemBars | displayCutout）。
     * 输入框位于弹窗中下部、会被键盘遮挡时开启
     */
    private boolean handleImeInsets = false;

    private View insetContentView;
    private int insetContentOriginalBottom;

    public FDialogBottomSheet(@NonNull Context context) {
        this(context, true);
    }

    public FDialogBottomSheet(@NonNull Context context, boolean isMatch) {
        this(context, isMatch, 0);
    }

    /**
     * @param themeResId 弹窗主题（BottomSheetDialog 的主题只能在构造时传入，事后 setter 无效），
     *                   &lt;= 0 时回退库默认 R.style.bottomSheetDialogStyle
     */
    public FDialogBottomSheet(@NonNull Context context, boolean isMatch, @StyleRes int themeResId) {
        super(context, themeResId > 0 ? themeResId : R.style.bottomSheetDialogStyle);
        this.isMatch = isMatch;
        this.context = context;
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

    /**
     * 内容根避让：给内容根（带圆角白底背景的布局，如 dialog_bottom_sheet_list 的 ll_background）
     * 设置 bottom padding = max(原始padding, systemBars | displayCutout 的 bottom)，
     * 原始 padding 只记录一次，insets 变化时取较大值不叠加。
     * 白色圆角背景延伸到屏幕底、盖住手势小白条，靠的就是该 bottom padding
     *
     * @param content 内容根布局
     */
    public void fitContentInsets(View content) {
        if (content == null) {
            return;
        }
        insetContentView = content;
        insetContentOriginalBottom = content.getPaddingBottom();
        applyContentInsets();
    }

    public void setAutoContentInsets(boolean autoContentInsets) {
        this.autoContentInsets = autoContentInsets;
        if (insetContentView != null) {
            applyContentInsets();
        }
    }

    public void setHandleImeInsets(boolean handleImeInsets) {
        this.handleImeInsets = handleImeInsets;
        if (insetContentView != null) {
            ViewCompat.requestApplyInsets(insetContentView);
        }
    }

    private void applyContentInsets() {
        View content = insetContentView;
        if (content == null) {
            return;
        }
        if (!autoContentInsets) {
            // 关闭时撤销库的避让：移除监听并还原原始 padding，不留任何多余行为
            ViewCompat.setOnApplyWindowInsetsListener(content, null);
            content.setPadding(content.getPaddingLeft(), content.getPaddingTop(),
                    content.getPaddingRight(), insetContentOriginalBottom);
            return;
        }
        final int originalBottom = insetContentOriginalBottom;
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
            int bottom = Math.max(originalBottom, insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout()).bottom);
            if (handleImeInsets) {
                bottom = Math.max(bottom,
                        insets.getInsets(WindowInsetsCompat.Type.ime()).bottom);
            }
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(content);
    }

}
