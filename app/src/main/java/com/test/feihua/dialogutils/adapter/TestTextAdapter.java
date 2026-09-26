package com.test.feihua.dialogutils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.feihua.dialogutils.bean.ItemData;
import com.feihua.dialogutils.util.DialogRecord;
import com.feihua.dialogutils.util.DialogUtils;
import com.feihua.dialogutils.view.FDialogBottomSheet;
import com.test.feihua.dialogutils.R;

import java.util.List;

/**
 * Create By feihua  On 2021/8/23
 */
public class TestTextAdapter extends RecyclerView.Adapter<TestTextAdapter.ViewHolder> {

    private List<ItemData> data;
    private Context context;
    private DialogUtils dialogUtils;

    public TestTextAdapter(Context context, List<ItemData> itemData) {
        this.context = context;
        this.data = itemData;
        dialogUtils = DialogUtils.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_text_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemData itemData = data.get(position);
        holder.tv_name.setText(itemData.getName());
        holder.view.setOnClickListener(v -> openByType(itemData.getId()));
    }

    /** 按类型打开对应弹窗（点击入口与 MainActivity 的 auto_open 调试通道共用） */
    public void openByType(int type) {
        switch (type) {
            case DialogRecord.TYPE_DIALOG_TOATS:
                dialogUtils.dialogt("测试", "双按钮提示对话框");
                break;
            case DialogRecord.TYPE_DIALOG_TOATS_1:
                dialogUtils.dialogt1("测试", "单按钮提示对话框").setOnClickListener(v1 -> {
                    dialogUtils.dialogj1("点击标题","内容");
                    dialogUtils.setCanceledOnTouchOutside(true);
                });
                break;
            case DialogRecord.TYPE_DIALOG_LOADING:
                dialogUtils.dialogj("单按钮加载对话框", "测试这是提示的内容");
                break;
            case DialogRecord.TYPE_DIALOG_LOADING_1:
                dialogUtils.dialogj1("无按钮加载对话框", "测试这是提示的内容");
                break;
            case DialogRecord.TYPE_DIALOG_EDIT:
                dialogUtils.dialoge("编辑框对话框", "测试这是提示的内容");
                break;
            case DialogRecord.TYPE_DIALOG_BOTTOM:
//                    dialogUtils.dialogBottomSheet(R.layout.test_bottom_dialog);
                dialogUtils.dialogBottomSheetListIconText("底部列表",new String[]{"1","2","3","4"});
                break;
            case DialogRecord.TYPE_DIALOG_BOTTOM_LAYOUT:
                // 内容根（根布局带圆角白底）自动避让系统栏；根原始 paddingBottom=100dp，验证 max() 不叠加
                dialogUtils.dialogBottomSheet(R.layout.test_bottom_dialog);
                break;
            case DialogRecord.TYPE_DIALOG_BOTTOM_TALL:
                // 内容较高把 sheet 拉到屏幕顶部，验证顶部无黑带
                String[] tall = new String[40];
                for (int i = 0; i < tall.length; i++) {
                    tall[i] = "条目 " + (i + 1);
                }
                dialogUtils.dialogBottomSheetListIconText("底部列表(内容较高)", tall);
                break;
            case DialogRecord.TYPE_DIALOG_BOTTOM_IME:
                // 输入框位于弹窗中下部，弹键盘默认整体不上浮；开启 ime 避让后输入框不被键盘遮挡
                dialogUtils.dialogBottomSheet(R.layout.test_bottom_input);
                if (dialogUtils.getDialog() instanceof FDialogBottomSheet) {
                    ((FDialogBottomSheet) dialogUtils.getDialog()).setHandleImeInsets(true);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
