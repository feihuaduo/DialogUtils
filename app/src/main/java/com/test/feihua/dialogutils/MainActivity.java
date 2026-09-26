package com.test.feihua.dialogutils;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feihua.dialogutils.bean.ItemData;
import com.feihua.dialogutils.util.DialogRecord;
import com.test.feihua.dialogutils.adapter.TestTextAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_list;
    private TestTextAdapter testTextAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 与宿主 OURYGO-OY BaseActivity.onCreate 相同的启用方式：
        // 状态栏 auto 跟随深色模式；导航栏必须显式 light(透明,透明)——默认 auto 会把
        // isNavigationBarContrastEnforced 置为 true，手势小白条区域出现半透明遮罩
        EdgeToEdge.enable(this,
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT));
        setContentView(com.test.feihua.dialogutils.R.layout.main);

        // 与宿主 BaseActivity.setupWindowInsets + updatePadding 相同：内容根避让
        // 状态栏（顶部），底部让列表末项不被手势条遮挡
        View root = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsetsCompat) -> {
            Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, insets.top, 0, insets.bottom);
            return windowInsetsCompat;
        });

        rv_list = findViewById(com.test.feihua.dialogutils.R.id.rv_list);
        List<ItemData> data = new ArrayList<>();
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_TOATS, ItemData.ICON_NULL, "双按钮提示对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_TOATS_1, ItemData.ICON_NULL, "单按钮提示对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_LOADING, ItemData.ICON_NULL, "单按钮加载对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_LOADING_1, ItemData.ICON_NULL, "无按钮加载对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_EDIT, ItemData.ICON_NULL, "编辑框对话框", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_BOTTOM, ItemData.ICON_NULL, "底部列表", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_BOTTOM_LAYOUT, ItemData.ICON_NULL, "底部自定义布局", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_BOTTOM_TALL, ItemData.ICON_NULL, "底部列表(内容较高)", ItemData.ICON_NULL));
        data.add(ItemData.toItemData(DialogRecord.TYPE_DIALOG_BOTTOM_IME, ItemData.ICON_NULL, "底部输入框(ime)", ItemData.ICON_NULL));

        testTextAdp = new TestTextAdapter(this, data);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setAdapter(testTextAdp);

        // 临时调试通道：am start 带 --ei auto_open <type> 即可自动弹出对应弹窗
        // （MIUI 限制 adb 注入触摸事件，无法远程点击列表项）
        int autoOpen = getIntent().getIntExtra("auto_open", -1);
        if (autoOpen >= 0) {
            rv_list.postDelayed(() -> testTextAdp.openByType(autoOpen), 800);
        }

    }
}
