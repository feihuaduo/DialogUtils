package com.test.feihua.dialogutils;


import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feihua.dialogutils.bean.ItemData;
import com.feihua.dialogutils.util.DialogRecord;
import com.test.feihua.dialogutils.adapter.TestTextAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private RecyclerView rv_list;
    private TestTextAdapter testTextAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.test.feihua.dialogutils.R.layout.main);

        rv_list = findViewById(com.test.feihua.dialogutils.R.id.rv_list);
        List<ItemData> data = new ArrayList<>();
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_TOATS, ItemData.ICON_NULL, "双按钮提示对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_TOATS_1, ItemData.ICON_NULL, "单按钮提示对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_LOADING, ItemData.ICON_NULL, "单按钮加载对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_LOADING_1, ItemData.ICON_NULL, "无按钮加载对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_EDIT, ItemData.ICON_NULL, "编辑框对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_BOTTOM, ItemData.ICON_NULL, "底部列表", ItemData.ICON_NULL));

        testTextAdp = new TestTextAdapter(this, data);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setAdapter(testTextAdp);

    }
}
