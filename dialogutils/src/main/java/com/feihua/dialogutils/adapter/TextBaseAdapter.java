package com.feihua.dialogutils.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.feihua.dialogutils.R;


public class TextBaseAdapter extends BaseAdapter {

    private Context context;
    private String[] data;
    private int leftPadding;
    private int rightPadding;
    private int topPadding;
    private int bottomPadding;

    public TextBaseAdapter(Context context, String[] data, int leftPadding, int topPadding,  int rightPadding,int bottomPadding) {
        this.context = context;
        this.data = data;
        this.leftPadding = leftPadding;
        this.rightPadding = rightPadding;
        this.topPadding = topPadding;
        this.bottomPadding = bottomPadding;
    }

    @Override
    public int getCount() {
        return data.length;//设置此数据适配起有几个item
    }

    @Override
    public Object getItem(int p1)//未知，好像无用
    {
        return data[p1];
    }

    @Override
    public long getItemId(int p1)//未知，好像无用
    {
        return p1;
    }

    @Override
    public View getView(int p1, View p2, ViewGroup p3) {
        AppCompatTextView t = new AppCompatTextView(context);
        t.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        t.setText(data[p1]);//p1为对应的数组下标
        t.setTextSize(20);
        t.setGravity(Gravity.CENTER);
        t.setPadding(leftPadding,topPadding,rightPadding,bottomPadding);
        t.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        return t;//返回设置好的view组件，也可以是布局，也可以是控件
    }

}
