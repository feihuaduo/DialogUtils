package com.feihua.dialogutils.util;


import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feihua.dialogutils.R;
import com.feihua.dialogutils.adapter.IconTextRecyclerViewAdapter;
import com.feihua.dialogutils.adapter.SelectAdapter;
import com.feihua.dialogutils.adapter.TextBaseAdapter;
import com.feihua.dialogutils.base.OnITItemClickListener;
import com.feihua.dialogutils.base.listener.OnCheckboxListener;
import com.feihua.dialogutils.base.listener.OnRadioListener;
import com.feihua.dialogutils.bean.ItemData;
import com.feihua.dialogutils.bean.UpdateLog;
import com.feihua.dialogutils.view.FDialog;
import com.feihua.dialogutils.view.FDialogBottomSheet;

import java.util.ArrayList;
import java.util.List;

/*对话框相关方法
 *
 */
public class DialogUtils {
    private static List<DialogUtils> contexts = new ArrayList<>();
    //对话框中提示内容
    private TextView tv_toast_message;
    private TextView tv_title;
    private CardView cv_card;
    private LinearLayout ll_background;
    //对话框对象
    private Dialog builder;
    private Context context;
    private View viewDialog;
    private Button bt_ok, bt_cancel;

    private DialogUtils(Context context) {
        this.context = context;
    }

    /**
     * 获取DialogUtil对象
     *
     * @param context 上下文对象
     * @return 当前上下文的DialogUtiil对象
     */
    public static DialogUtils getInstance(Context context) {
        DialogUtils dialogUtils = getDu(context);
        if (dialogUtils != null) {
            return dialogUtils;
        } else {
            DialogUtils du = new DialogUtils(context);
            contexts.add(du);
            return du;
        }
    }

    //获取该类对象

    /**
     * @param context 上下文对象
     * @return 当前上下文的DialogUtiil对象
     * @deprecated 使用getInstance方法代替
     */
    @Deprecated
    public static DialogUtils getdx(Context context) {
        DialogUtils dialogUtils = getDu(context);
        if (dialogUtils != null) {
            return dialogUtils;
        } else {
            DialogUtils du = new DialogUtils(context);
            contexts.add(du);
            return du;
        }
    }

    private static DialogUtils getDu(Context con) {
        int i = 0;
        while (i < contexts.size()) {
            DialogUtils dialogUtils = contexts.get(i);
            if (!ContextUtil.isContextExisted(dialogUtils.getContext())) {
                contexts.remove(i);
            } else {
                if (dialogUtils.getContext().equals(con))
                    return dialogUtils;
                i++;
            }
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    //关闭Dialog
    public void dis() {
        if (builder != null && builder.isShowing())
            builder.dismiss();
    }


    private void initDialog(Context context) {
        if (builder == null || builder.getClass() != Dialog.class) {
            builder = new FDialog(context, R.style.dialog);
            //去除原dialog标题
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        Window window = builder.getWindow();
        if (window != null) {
            if (context instanceof Service) {
                window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            } else {
                window.setType(WindowManager.LayoutParams.TYPE_APPLICATION);
            }
            if (ContextUtil.isContextExisted(context))
                builder.show();
        }
    }

    /*
     *初始化对话框布局
     *传入对话框的layout
     *返回该布局
     */
    public View initDialog(Context context, int layout) {
        initDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        viewDialog = inflater.inflate(layout, null);
        int width;
        if (context instanceof Activity) {
            Activity a = (Activity) context;
            Display display = a.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            width = Math.min(point.x, point.y);
        } else {
            DisplayMetrics dm;
            Service se = (Service) context;
            dm = se.getResources().getDisplayMetrics();
            width = Math.min(dm.widthPixels, dm.heightPixels);
        }
        //设置对话框的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width * 73 / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        builder.setContentView(viewDialog, layoutParams);
        return viewDialog;
    }

    public void setDialogWidth(int width) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        builder.setContentView(viewDialog, layoutParams);
    }

    /*多选对话框
     *title 标题
     *data 要选择的数据列表
     *positions 被选中的数据在列表中的角标
     */
    public Select dialogCheckbox(String title, final List<String> data, final List<Integer> positions) {

        final Select se = new Select();
        viewDialog = initDialog(context, R.layout.dialog_select);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        Button bt_ok = viewDialog.findViewById(R.id.bt_ok);
        Button bt_cancel = viewDialog.findViewById(R.id.bt_cancel);
        ListView lv_list = viewDialog.findViewById(R.id.lv_list);
        initTitle(title);
        initDialogBackground(viewDialog);
        final SelectAdapter sa = new SelectAdapter(context, data, positions);
        lv_list.setAdapter(sa);
        if (positions.size() != 0) {
            lv_list.setSelection(positions.get(0));
        }
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

                int position = conin(positions, p3);
                if (position != -1) {
                    removein(positions, position);
                } else {
                    positions.add(p3);
                }
                sa.notifyDataSetChanged();

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dis();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {

                if (se.onc != null) {
                    se.onc.OnCheckbox(data, positions);
                    dis();
                }
            }
        });

        setCanceledOnTouchOutside(true);
        return se;
    }

    private void removein(List<Integer> data, int c) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == c) {
                data.remove(i);
                return;
            }
        }
    }

    private int conin(List<Integer> data, int c) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == c) {
                return i;
            }
        }
        return -1;
    }

    //单选对话框
    public Select dialogRadio(String title, final List<String> data, int position) {


        final Select se = new Select();
        viewDialog = initDialog(context, R.layout.dialog_select);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        Button bt_ok = viewDialog.findViewById(R.id.bt_ok);
        Button bt_cancel = viewDialog.findViewById(R.id.bt_cancel);
        ListView lv_list = viewDialog.findViewById(R.id.lv_list);

        initTitle(title);
        initDialogBackground(viewDialog);
        final List<Integer> po = new ArrayList<>();
        po.add(position);
        final SelectAdapter sa = new SelectAdapter(context, data, po);
        lv_list.setAdapter(sa);
        if (position != -1) {
            lv_list.setSelection(position);
        }
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                if (po.size() != 0) {
                    if (po.get(0) != p3) {
                        po.clear();
                        po.add(p3);
                    }
                    sa.notifyDataSetChanged();
                } else {
                    po.add(p3);
                    sa.notifyDataSetChanged();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dis();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                if (po.size() != 0) {
                    dis();
                    if (se.onr != null) {
                        se.onr.onRadio(data, po.get(0));
                    }

                }
            }
        });

        setCanceledOnTouchOutside(true);
        return se;
    }

    //RecyclerView布局提示对话框
    public View[] dialogRec(String title, RecyclerView.Adapter adp, RecyclerView.LayoutManager layout) {

        View[] v = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_rec);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        RecyclerView dr_rec = viewDialog.findViewById(R.id.dr_rec);
        Button bt_ok = viewDialog.findViewById(R.id.bt_ok);
        initTitle(title);
        initDialogBackground(viewDialog);
        dr_rec.setLayoutManager(layout);
        dr_rec.setAdapter(adp);
        bt_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dis();
            }
        });

        v[0] = dr_rec;
        v[1] = bt_ok;
        setCanceledOnTouchOutside(true);
        return v;
    }

    //表格布局提示对话框
    public View[] dialoggrid(String title, ListAdapter adp, int numColumns) {

        View[] v = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_grid);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        GridView dt_grid = viewDialog.findViewById(R.id.dt_grid);
        Button bt_ok = viewDialog.findViewById(R.id.bt_ok);
        initTitle(title);
        initDialogBackground(viewDialog);
        dt_grid.setNumColumns(numColumns);
        dt_grid.setAdapter(adp);


        v[0] = dt_grid;
        v[1] = bt_ok;
        setCanceledOnTouchOutside(true);
        return v;
    }

    //listview对话框
    public ListView dialogl1(String title, final BaseAdapter badp) {

        viewDialog = initDialog(context, R.layout.dialog_list);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        ListView lv_list = viewDialog.findViewById(R.id.lv_list);
        initTitle(title);
        initDialogBackground(viewDialog);
        lv_list.setAdapter(badp);

        setCanceledOnTouchOutside(true);
        return lv_list;
    }

    //listview对话框
    public ListView dialogl(String title, final String[] data) {
        return dialogl(title, data, 16, 16, 16, 16);
    }

    //listview对话框
    public ListView dialogl(String title, final String[] data, int leftPadding, int rightPadding, int topPadding, int bottomPadding) {

        viewDialog = initDialog(context, R.layout.dialog_list);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        ListView lv_list = viewDialog.findViewById(R.id.lv_list);
        initTitle(title);
        initDialogBackground(viewDialog);
        BaseAdapter b = new TextBaseAdapter(context, data, leftPadding, topPadding, rightPadding, bottomPadding);
        lv_list.setAdapter(b);

        setCanceledOnTouchOutside(true);
        return lv_list;

    }

    public View[] dialoge(String title, String hint) {
        return dialoge(title, hint, "");
    }

    //EditText对话框
    public View[] dialoge(String title, String hint, String message) {

        View[] v = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_edit);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        final EditText et_edit = viewDialog.findViewById(R.id.et_edit);
        bt_ok = viewDialog.findViewById(R.id.bt_ok);
        initTitle(title);
        initDialogBackground(viewDialog);
        et_edit.setHint(hint);
        et_edit.setHint(message);
        bt_ok.setOnClickListener(p1 -> dis());

        v[0] = et_edit;
        v[1] = bt_ok;
        setCanceledOnTouchOutside(true);
        new Handler().postDelayed(() -> {
            FUtil.showKeyboard(et_edit);
            et_edit.setSelection(et_edit.getText().length());
        }, 100);
        return v;
    }

    //加载对话框
    public Button dialogj(String title, final String message) {

        viewDialog = initDialog(context, R.layout.dialog_jiazai);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        tv_toast_message = viewDialog.findViewById(R.id.tv_toast_message);
        Button bt_cancel = viewDialog.findViewById(R.id.bt_cancel);
        initTitle(title);
        initDialogBackground(viewDialog);
        bt_cancel.setOnClickListener(p1 -> dis());

        tv_toast_message.setText(message);
        setCanceledOnTouchOutside(false);
        return bt_cancel;

    }

    //无按钮加载对话框
    public void dialogj1(String title, final String message) {

        viewDialog = initDialog(context, R.layout.dialog_jiazai1);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        tv_toast_message = viewDialog.findViewById(R.id.tv_toast_message);
        initTitle(title);
        initDialogBackground(viewDialog);
        tv_toast_message.setText(message);
        setCanceledOnTouchOutside(false);
    }

    //单按钮单图片提示对话框
    public View[] dialogi(String title, final String message, int drawableId) {

        View[] v = new View[1];
        viewDialog = initDialog(context, R.layout.dialog_image);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        tv_toast_message = viewDialog.findViewById(R.id.di_ts);
        ImageView di_image = viewDialog.findViewById(R.id.di_image);
        di_image.setImageResource(drawableId);
        Button di_qd = viewDialog.findViewById(R.id.di_qd);
        initTitle(title);
        initDialogBackground(viewDialog);
        tv_toast_message.setText(message);
        di_qd.setOnClickListener(p1 -> dis());

        v[0] = di_qd;
        setCanceledOnTouchOutside(true);
        return v;
    }

    //单按钮提示对话框
    public Button dialogt1(String title, final String message) {

        viewDialog = initDialog(context, R.layout.dialog_toast1);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        tv_toast_message = viewDialog.findViewById(R.id.dt_ts);
        bt_ok = viewDialog.findViewById(R.id.bt_ok);
        initTitle(title);
        initDialogBackground(viewDialog);
        tv_toast_message.setText(message);
        bt_ok.setOnClickListener(p1 -> dis());
        setCanceledOnTouchOutside(true);
        return bt_ok;
    }

    //双按钮提示对话框
    public View[] dialogt(String title, final String message) {

        View[] v = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_toast);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        tv_toast_message = viewDialog.findViewById(R.id.dt_ts);
        bt_ok = viewDialog.findViewById(R.id.bt_ok);
        bt_cancel = viewDialog.findViewById(R.id.dt_qx);
        initTitle(title);
        initDialogBackground(viewDialog);
        tv_toast_message.setText(message);
        v[0] = bt_cancel;
        v[1] = bt_ok;
        setCanceledOnTouchOutside(true);
        return v;
    }

    /*更新日志对话框
     *title 标题
     *data 更新日志列表
     */
    public View[] dialogUpdateLog(String title, final List<UpdateLog> data) {
        View[] vv = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_update_log);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        ListView lv_list = viewDialog.findViewById(R.id.duu_list);
        Button bt_ok = viewDialog.findViewById(R.id.duu_qd);
        initTitle(title);
        initDialogBackground(viewDialog);
        lv_list.setOnItemLongClickListener((p1, p2, p3, p4) -> {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, data.get(p3).getVersion() + "\n" + data.get(p3).getMessage()));
            Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            return true;
        });
        lv_list.setAdapter(new BaseAdapter() {

            ViewHolder zujian;

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int p1) {
                return data.get(p1);
            }

            @Override
            public long getItemId(int p1) {
                return p1;
            }

            @Override
            public View getView(int p1, View p2, ViewGroup p3) {
                if (p2 == null) {
                    zujian = new ViewHolder();
                    p2 = LayoutInflater.from(context).inflate(R.layout.item_update_log, null);
                    zujian.upda_vosin = p2.findViewById(R.id.upda_vosin);
                    zujian.upda_message = p2.findViewById(R.id.upda_message);

                    p2.setTag(zujian);

                } else {
                    zujian = (ViewHolder) p2.getTag();
                }
                zujian.upda_vosin.setText(data.get(p1).getVersion());
                zujian.upda_message.setText(data.get(p1).getMessage());

                return p2;
            }

            class ViewHolder {
                TextView upda_vosin, upda_message;
            }
        });

        setCanceledOnTouchOutside(true);
        vv[0] = lv_list;
        vv[1] = bt_ok;
        return vv;

    }

    //提示更新对话框
    public View[] dialogAppUpdate(String version, String message) {

        View[] v = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_app_update);
        Button bt_ok = viewDialog.findViewById(R.id.bt_ok);
        Button bt_cancel = viewDialog.findViewById(R.id.bt_cancel);
        TextView tv_version = viewDialog.findViewById(R.id.tv_version_name);
        TextView du_update_message = viewDialog.findViewById(R.id.du_update_message);

        initDialogBackground(viewDialog);

        tv_version.setText(version);
        du_update_message.setText(message);


        v[0] = bt_cancel;
        v[1] = bt_ok;
        setCanceledOnTouchOutside(false);
        return v;
    }

    public View dialogBottomSheet(int layoutId) {
        return dialogBottomSheet(layoutId, true);
    }

    /*
     *底部滑出空白Dialog
     *layoutId layout的id
     */
    public View dialogBottomSheet(int layoutId, boolean isMatch) {
        builder = new FDialogBottomSheet(context, isMatch);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        try {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } catch (Exception e) {

        }
        builder.setContentView(view);
        Window window = builder.getWindow();
        if (window != null)
            window.findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
        builder.show();
        return view;
    }

    public IconTextItem dialogBottomSheetListIconText(String title, String[] list) {
        return dialogBottomSheetListIconText(title, true, list);
    }

    /*
     *底部划出的Dialog列表
     *title 标题
     *list  列表
     */
    public IconTextItem dialogBottomSheetListIconText(String title, boolean isMatch, String[] list) {

        final IconTextItem it = new IconTextItem();

        builder = new FDialogBottomSheet(context, isMatch);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_list, null);
        builder.setContentView(view);
        Window window = builder.getWindow();
        if (window != null)
            window.findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
        builder.show();

        List<ItemData> data = new ArrayList<>();
        for (String s : list) {
            data.add(ItemData.toItemData(s));
        }
        RecyclerView rv_new_file_list = view.findViewById(R.id.rv_list);
        tv_title = view.findViewById(R.id.tv_title);
        initTitle(title);
        initDialogBackground(view);
        rv_new_file_list.setLayoutManager(new LinearLayoutManager(context));
        IconTextRecyclerViewAdapter nFAdp = new IconTextRecyclerViewAdapter(data, false);
        rv_new_file_list.setAdapter(nFAdp);
        nFAdp.setOnITItemClickListener(new OnITItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (it.onITItemClickListener != null) {
                    it.onITItemClickListener.onItemClick(position);
                }
            }
        });
        return it;
    }

    public IconTextItem dialogBottomSheetListIconText(String title, List<ItemData> data, boolean isShowIcon) {
        return dialogBottomSheetListIconText(title, data, isShowIcon, true);
    }

    /*
     *底部划出的Dialog列表
     *title 标题
     *data item列表
     *isShowIcon 是否显示图标
     */
    public IconTextItem dialogBottomSheetListIconText(String title, List<ItemData> data, boolean isShowIcon, boolean isMatch) {

        final IconTextItem it = new IconTextItem();

        builder = new FDialogBottomSheet(context, isMatch);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_list, null);
        builder.setContentView(view);
        Window window = builder.getWindow();
        if (window != null)
            window.findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
        builder.show();

        RecyclerView rv_new_file_list = view.findViewById(R.id.rv_list);
        tv_title = view.findViewById(R.id.tv_title);
        initTitle(title);
        initDialogBackground(view);
        rv_new_file_list.setLayoutManager(new LinearLayoutManager(context));
        IconTextRecyclerViewAdapter nFAdp = new IconTextRecyclerViewAdapter(data, isShowIcon);
        rv_new_file_list.setAdapter(nFAdp);
        nFAdp.setOnITItemClickListener(new OnITItemClickListener() {

            @Override
            public void onItemClick(int position) {
                if (it.onITItemClickListener != null) {
                    it.onITItemClickListener.onItemClick(position);
                }
            }
        });


        return it;
    }

    //SeekBar对话框
    public View[] dialogSeekBar(String title, int max, int progress) {

        View[] v = new View[2];
        viewDialog = initDialog(context, R.layout.dialog_seekbar);
        tv_title = viewDialog.findViewById(R.id.tv_title);
        SeekBar ds_sb = viewDialog.findViewById(R.id.ds_sb);
        Button bt_ok = viewDialog.findViewById(R.id.bt_ok);
        initTitle(title);
        initDialogBackground(viewDialog);
        ds_sb.setMax(max);
        ds_sb.setProgress(progress);
        bt_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dis();
            }
        });
        setCanceledOnTouchOutside(true);
        v[0] = ds_sb;
        v[1] = bt_ok;

        return v;
    }

    public View getViewDialog() {
        return viewDialog;
    }

    public TextView getTitle() {
        return tv_title;
    }

    public void setTitle(String title) {
        if (tv_title != null) {
            initTitle(title);
        }
    }

    private void initTitle(String title) {
        if (tv_title == null)
            return;
        if (title != null && !title.equals("")) {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.GONE);
        }
    }


    private void initDialogBackground(View viewDialog) {
        cv_card = viewDialog.findViewById(R.id.cv_card);
        ll_background = viewDialog.findViewById(R.id.ll_background);
    }

    public void setDialogBackground(Drawable drawable) {
        if (ll_background != null) {
            ll_background.setBackgroundDrawable(drawable);
            if (cv_card != null)
                cv_card.setCardBackgroundColor(c(R.color.transparent));
            if (bt_ok != null)
                bt_ok.setBackgroundResource(R.drawable.click_transparent_background);
            if (bt_cancel != null)
                bt_cancel.setBackgroundResource(R.drawable.click_transparent_background);
        }
    }

    public void setDialogBackgroundResource(int resource) {
        if (ll_background != null) {
            ll_background.setBackgroundResource(resource);
            if (cv_card != null)
                cv_card.setCardBackgroundColor(c(R.color.transparent));
            if (bt_ok != null)
                bt_ok.setBackgroundResource(R.drawable.click_transparent_background);
            if (bt_cancel != null)
                bt_cancel.setBackgroundResource(R.drawable.click_transparent_background);
        }
    }

    public void setDialogBackgroundColor(int color) {
        if (cv_card != null) {
            cv_card.setCardBackgroundColor(color);
            if (ll_background != null)
                ll_background.setBackgroundColor(c(R.color.transparent));
            if (bt_ok != null)
                bt_ok.setBackgroundResource(R.drawable.click_transparent_background);
            if (bt_cancel != null)
                bt_cancel.setBackgroundResource(R.drawable.click_transparent_background);
        }
    }

    public void setDialogBackgroundDefault() {
        if (ll_background != null)
            ll_background.setBackgroundColor(c(R.color.transparent));
        if (cv_card != null)
            cv_card.setCardBackgroundColor(c(R.color.colorDialogBackground));
        if (bt_ok != null)
            bt_ok.setBackgroundResource(R.drawable.click_background);
        if (bt_cancel != null)
            bt_cancel.setBackgroundResource(R.drawable.click_background);
    }


    private int c(int color) {
        return ContextCompat.getColor(context, color);
    }

    //设置对话框是否能被返回键或者触控屏幕关闭
    public void setCanceledOnTouchOutside(final boolean cancel) {
        builder.setCanceledOnTouchOutside(cancel);
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && !cancel)
                return true;
            return false;
        });
    }

    public void setMessage(String message) {
        if (tv_toast_message != null) {
            tv_toast_message.setText(message);
        }
    }

    public void setTitleColor(int color) {
        if (tv_title != null)
            tv_title.setTextColor(color);
    }

    public void setMessageColor(int color) {
        if (tv_toast_message != null) {
            tv_toast_message.setTextColor(color);
        }
    }

    public TextView getMessageTextView() {
        return tv_toast_message;
    }

    public Dialog getDialog() {
        return builder;
    }

    public class Select {
        private OnRadioListener onr;
        private OnCheckboxListener onc;

        public void setOnRadioListener(OnRadioListener onr) {
            this.onr = onr;
        }

        public void setOnCheckboxListener(OnCheckboxListener onc) {
            this.onc = onc;
        }
    }

    public class IconTextItem {
        private OnITItemClickListener onITItemClickListener;

        public void setOnITItemClickListener(OnITItemClickListener onITItemClickListener) {
            this.onITItemClickListener = onITItemClickListener;
        }
    }

}
