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
        holder.view.setOnClickListener(v -> {
            switch (itemData.getId()) {
                case DialogRecord.TYPE_DIALOG_TOATS:
                    dialogUtils.dialogt("测试", itemData.getName());
                    break;
                case DialogRecord.TYPE_DIALOG_TOATS_1:
                    dialogUtils.dialogt1("测试", itemData.getName());
                    break;
                case DialogRecord.TYPE_DIALOG_LOADING:
                    dialogUtils.dialogj(itemData.getName(), "测试这是提示的内容");
                    break;
                case DialogRecord.TYPE_DIALOG_LOADING_1:
                    dialogUtils.dialogj1(itemData.getName(), "测试这是提示的内容");
                    break;
                case DialogRecord.TYPE_DIALOG_EDIT:
                    dialogUtils.dialoge(itemData.getName(), "测试这是提示的内容");
                    break;
                case DialogRecord.TYPE_DIALOG_BOTTOM:
                    dialogUtils.dialogBottomSheetListIconText("底部列表",new String[]{"1","2","3","4"});
                    break;
            }
        });
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
