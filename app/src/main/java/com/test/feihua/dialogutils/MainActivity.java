package com.test.feihua.dialogutils;


import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feihua.dialogutils.bean.ItemData;
import com.feihua.dialogutils.util.DialogRecord;
import com.test.feihua.dialogutils.adapter.TestTextAdapter;
import com.test.feihua.dialogutils.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private RecyclerView rv_list;
    private TestTextAdapter testTextAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.test.feihua.dialogutils.R.layout.main);

        rv_list=findViewById(com.test.feihua.dialogutils.R.id.rv_list);
        List<ItemData> data=new ArrayList<>();
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_TOATS,-1,"双按钮提示对话框"));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_TOATS_1,-1,"单按钮提示对话框"));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_LOADING,-1,"单按钮加载对话框"));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_LOADING_1,-1,"无按钮加载对话框"));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_EDIT,-1,"编辑框对话框"));

        testTextAdp=new TestTextAdapter(this,data);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setAdapter(testTextAdp);

    }
}
