package com.feihua.dialogutils.util;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.LinearLayout.*;
import com.feihua.dialogutils.*;
import com.feihua.dialogutils.adapter.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.feihua.dialogutils.bean.*;
import com.feihua.dialogutils.base.OnITItemClickListener;
import android.support.v4.content.ContextCompat;
import com.feihua.dialogutils.view.*;
import android.support.design.widget.*;
/*对话框相关方法
*
*/
public class DialogUtils
{
	//对话框中提示内容
	private TextView tv_toast_message;
	private TextView tv_title;
	//对话框对象
	private Dialog builder;
	private Context context;
	private static List<DialogUtils> contexts=new ArrayList<DialogUtils>();

	private View viewDialog;

	public Context getContext()
	{
		return context;
	}
	
	//获取该类对象
	public static DialogUtils getdx(Context context){
		DialogUtils dut= getDu(context);
		if (dut!=null){		
			return dut;
		}else{	
			DialogUtils du=new DialogUtils(context);
			contexts.add(du);
			return du;
		}
	}
	
	private static DialogUtils getDu(Context con){
		for( DialogUtils du:contexts){
			if(du.getContext().equals(con)){
				return du;
			}
		}
		return null;
	}
	
	private DialogUtils(Context context){
		this.context=context;
	}
	
	//关闭Dialog
	public void dis(){
		builder.dismiss();		
	}
	
	
	private Dialog initDialog(Context context){
		if (builder==null||builder.getClass()!=Dialog.class){
			builder=new Dialog(context, R.style.dialog);
			//去除原dialog标题
			builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		if (context instanceof Service){
			builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}else{
			builder.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
		}

		builder.show();
		return builder;
	}

	/*
	 *初始化对话框布局
	 *传入对话框的layout
	 *返回该布局
	 */
	public View initDialog(Context context,int layout)
	{
		initDialog(context);

		LayoutInflater inflater = LayoutInflater.from(context);
		viewDialog = inflater.inflate(layout, null);	
		int width;
		if(context instanceof Activity){
			Activity a=(Activity)context;
			Display display = a.getWindowManager().getDefaultDisplay();
			width=display.getWidth();
		}else{
			DisplayMetrics dm = new DisplayMetrics();
			Service se=(Service) context;
			dm = se.getResources().getDisplayMetrics();
			width = dm.widthPixels;
		}
		//设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(width * 73 / 100,LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);
		return viewDialog;
		// TODO: Implement this method
	}
	
	public interface OnRadioListener{
		void onRadio(List<String> data, int position);
	}
	public interface OnCheckboxListener{
		void OnCheckbox(List<String> data, List<Integer> positons);
	}
	
	public class Select{
		private OnRadioListener onr;
		private OnCheckboxListener onc;
		
		public void setOnRadioListener(OnRadioListener onr){
			this.onr=onr;
		}
		public void setOnCheckboxListener(OnCheckboxListener onc){
			this.onc=onc;
		}
	}
	
	/*多选对话框
	*title 标题
	*data 要选择的数据列表
	*positions 被选中的数据在列表中的角标
	*/
	public Select dialogCheckbox(String title,final List<String> data,final List<Integer> positions){


		final Select se=new Select();
		viewDialog=initDialog(context,R.layout.dialog_select);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		Button ds_qd=(Button)viewDialog.findViewById(R.id.ds_qd);
		Button ds_qx=(Button)viewDialog.findViewById(R.id.ds_qx);
		ListView ds_list=(ListView)viewDialog.findViewById(R.id.ds_list);
		initTitle(title);	
		final SelectAdapter sa=new SelectAdapter(context,data,positions);
		ds_list.setAdapter(sa);
		if(positions.size()!=0){
			ds_list.setSelection(positions.get(0));
		}
		ds_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
				
						int position=conin(positions,p3);
						if(position!=-1){
							removein(positions,position);
						}else{
							positions.add(new Integer(p3));
						}
						sa.notifyDataSetChanged();
					
					// TODO: Implement this method
				}
			});
		ds_qx.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					dis();
					// TODO: Implement this method
				}
			});
		ds_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					
					if(se.onc!=null){
						se.onc.OnCheckbox(data,positions);
						dis();
					}
					// TODO: Implement this method
				}
			});

		    setCanceledOnTouchOutside(true);
		return se;	
	}
	
	private void removein(List<Integer> data,int c){
		for(int i=0;i<data.size();i++ ){
			if(data.get(i)==c){
				data.remove(i);
				return;
			}
		}
	}
	
	private int conin(List<Integer> data,int c){
		for(int i=0;i<data.size();i++){
			if(data.get(i)==c){
				return i;
			}
		}
		return -1;
	}
	
	//单选对话框
	public Select dialogRadio(String title,final List<String> data,int position){

		
		final Select se=new Select();
		viewDialog=initDialog(context,R.layout.dialog_select);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		Button ds_qd=(Button)viewDialog.findViewById(R.id.ds_qd);
		Button ds_qx=(Button)viewDialog.findViewById(R.id.ds_qx);
		ListView ds_list=(ListView)viewDialog.findViewById(R.id.ds_list);
		
		initTitle(title);
		final List<Integer> po=new ArrayList<Integer>();
		po.add(new Integer(position));
		final SelectAdapter sa=new SelectAdapter(context,data,po);
		ds_list.setAdapter(sa);
		if(position!=-1){
			ds_list.setSelection(position);
		}
		ds_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if(po.size()!=0){
						if(po.get(0)!=p3){
							po.clear();
							po.add(new Integer(p3));
						}
						sa.notifyDataSetChanged();
					}else{
						po.add(new Integer(p3));
						sa.notifyDataSetChanged();
					}
					// TODO: Implement this method
				}
			});
		ds_qx.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					dis();
					// TODO: Implement this method
				}
			});
		ds_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(po.size()!=0){
						dis();
						if(se.onr!=null){
					se.onr.onRadio(data,po.get(0));
					}
					
					}
					// TODO: Implement this method
				}
			});
		
	   setCanceledOnTouchOutside(true);
		return se;	
	}
	
	//RecyclerView布局提示对话框
    public View[] dialogRec(String title,RecyclerView.Adapter adp,RecyclerView.LayoutManager layout){

        View[] v=new View[2];
        viewDialog=initDialog(context,R.layout.dialog_rec);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
        RecyclerView dr_rec= (RecyclerView)viewDialog.findViewById(R.id.dr_rec);
        Button dr_qd=(Button)viewDialog.findViewById(R.id.dr_qd);
        initTitle(title);
		dr_rec.setLayoutManager(layout);
		dr_rec.setAdapter(adp);
		dr_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					dis();
					// TODO: Implement this method
				}
			});

        v[0]=dr_rec;
		v[1]=dr_qd;
        setCanceledOnTouchOutside(true);
        return v;   
	}
	
	//表格布局提示对话框
    public View[] dialoggrid(String title,ListAdapter adp,int numColumns){

        View[] v=new View[2];
        viewDialog=initDialog(context,R.layout.dialog_grid);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
        GridView dt_grid= (GridView)viewDialog.findViewById(R.id.dt_grid);
        Button dt_qd=(Button)viewDialog.findViewById(R.id.dt_qd);
        initTitle(title);
		dt_grid.setNumColumns(numColumns);
		dt_grid.setAdapter(adp);
		
    
        v[0]=dt_grid;
		v[1]=dt_qd;
        setCanceledOnTouchOutside(true);
        return v;   
	}

	//listview对话框
	public ListView dialogl1(String title,final BaseAdapter badp){

		viewDialog=initDialog(context,R.layout.dialog_list);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		ListView dl_list=(ListView)viewDialog.findViewById(R.id.dl_list);
		initTitle(title);
		dl_list.setAdapter(badp);

		setCanceledOnTouchOutside(true);
		return dl_list;
		
		
    }
	
//listview对话框
	public ListView dialogl(String title,final String[] ss){
		
		viewDialog=initDialog(context,R.layout.dialog_list);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		ListView dl_list=(ListView)viewDialog.findViewById(R.id.dl_list);
		initTitle(title);
		BaseAdapter b=new BaseAdapter(){

			@Override
			public int getCount()
			{
				return ss.length;//设置此数据适配起有几个item
			}

			@Override
			public Object getItem(int p1)//未知，好像无用
			{
				// TODO: Implement this method
				return ss[p1];
			}

			@Override
			public long getItemId(int p1)//未知，好像无用
			{
				// TODO: Implement this method
				return p1;
			}

			@Override
			public View getView(int p1, View p2, ViewGroup p3)
			{
				TextView t=new TextView(context);
				t.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
				t.setText(ss[p1]);//p1为对应的数组下标
				t.setTextSize(20);
				t.setGravity(Gravity.CENTER);
				t.setPadding(6,6,6,6);
				t.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
				return t;//返回设置好的view组件，也可以是布局，也可以是控件
			}
		};
		dl_list.setAdapter(b);

      setCanceledOnTouchOutside(true);
		return dl_list;

	}

//EditText对话框
	public View[] dialoge(String title,String hint){

		View[] v=new View[2];
		viewDialog=initDialog(context,R.layout.dialog_edit);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		EditText de_ed=(EditText)viewDialog.findViewById(R.id.de_ed);
		Button de_qd=(Button)viewDialog.findViewById(R.id.de_qd);
		initTitle(title);
		de_ed.setHint(hint);
		de_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1){
					dis();
					// TODO: Implement this method
				}
			});
		
		v[0]=de_ed;
		v[1]=de_qd;
	   setCanceledOnTouchOutside(true);
		return v;

	}


//加载对话框
	public Button dialogj(String title,final String message){
		
		viewDialog=initDialog(context,R.layout.dialog_jiazai);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		 tv_toast_message=(TextView)viewDialog.findViewById(R.id.tv_toast_message);
		Button dj_qx=(Button)viewDialog.findViewById(R.id.dj_qx);
		initTitle(title);
		dj_qx.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1){
					dis();
					// TODO: Implement this method
				}
			});
		
		tv_toast_message.setText(message);
		setCanceledOnTouchOutside(false);
		return dj_qx;

	}

	//无按钮加载对话框
    public void dialogj1(String title,final String message){
		
		viewDialog=initDialog(context,R.layout.dialog_jiazai1);
        tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
        tv_toast_message=(TextView)viewDialog.findViewById(R.id.tv_toast_message);
        initTitle(title);
        tv_toast_message.setText(message);
        setCanceledOnTouchOutside(false);
	}
	

	//单按钮单图片提示对话框
    public View[] dialogi(String title,final String message,int drawableId){

        View[] v=new View[1];
        viewDialog=initDialog(context,R.layout.dialog_image);
        tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
        tv_toast_message=(TextView)viewDialog.findViewById(R.id.di_ts);
		ImageView di_image=(ImageView)viewDialog.findViewById(R.id.di_image);
		di_image.setImageResource(drawableId);
        Button di_qd=(Button)viewDialog.findViewById(R.id.di_qd);
        initTitle(title);
        tv_toast_message.setText(message);
		di_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1){
					dis();
					// TODO: Implement this method
				}
			});
		
        v[0]=di_qd;
       	setCanceledOnTouchOutside(true);
        return v;   
	}
	
	//单按钮提示对话框
    public Button dialogt1(String title,final String message){

        viewDialog=initDialog(context,R.layout.dialog_toast1);
        tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
        tv_toast_message=(TextView)viewDialog.findViewById(R.id.dt_ts);
        Button dt_qd=(Button)viewDialog.findViewById(R.id.dt_qd);
        initTitle(title);
        tv_toast_message.setText(message);
		dt_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1){
					dis();
					// TODO: Implement this method
				}
			});
   	   setCanceledOnTouchOutside(true);
        return dt_qd;   
	}


	//双按钮提示对话框
	public View[] dialogt(String title,final String message){

		View[] v=new View[2];
		viewDialog=initDialog(context,R.layout.dialog_toast);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		tv_toast_message=(TextView)viewDialog.findViewById(R.id.dt_ts);
		Button dt_qd=(Button)viewDialog.findViewById(R.id.dt_qd);
		Button dt_qx=(Button)viewDialog.findViewById(R.id.dt_qx);
		initTitle(title);
		tv_toast_message.setText(message);
		v[0]=dt_qx;
		v[1]=dt_qd;
        setCanceledOnTouchOutside(true);
		return v;	
	}
	
	/*更新日志对话框
	*title 标题
	*data 更新日志列表
	*/
	public View[] dialogUpdateLog(String title,final List<UpdateLog> data){

	    View[] vv=new View[2];
		viewDialog=initDialog(context,R.layout.dialog_update_log);
		tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
		ListView du_list=(ListView)viewDialog.findViewById(R.id.duu_list);
		Button du_qd=(Button)viewDialog.findViewById(R.id.duu_qd);
		initTitle(title);
		du_list.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
					cmb.setText(data.get(p3).getVersion()+"\n"+data.get(p3).getMessage());//复制命令
					Toast.makeText(context,"已复制到剪贴板",Toast.LENGTH_SHORT).show();
					// TODO: Implement this method
					return true;
				}
			});
		du_list.setAdapter(new BaseAdapter(){

				Zujian zujian;
				class Zujian{
					TextView upda_vosin,upda_message;
				}
				@Override
				public int getCount()
				{
					// TODO: Implement this method
					return data.size();
				}

				@Override
				public Object getItem(int p1)
				{
					// TODO: Implement this method
					return data.get(p1);
				}

				@Override
				public long getItemId(int p1)
				{
					// TODO: Implement this method
					return p1;
				}

				@Override
				public View getView(int p1, View p2, ViewGroup p3)
				{
					if(p2==null){
						zujian=new Zujian();
						p2=LayoutInflater.from(context).inflate(R.layout.item_update_log,null);
						zujian.upda_vosin=(TextView)p2.findViewById(R.id.upda_vosin);
						zujian.upda_message=(TextView)p2.findViewById(R.id.upda_message);

						p2.setTag(zujian);

					}else{
						zujian=(Zujian) p2.getTag();
					}
					zujian.upda_vosin.setText(data.get(p1).getVersion());
					zujian.upda_message.setText(data.get(p1).getMessage());

					// TODO: Implement this method
					return p2;
				}
			});

		setCanceledOnTouchOutside(true);
		vv[0]=du_list;
		vv[1]=du_qd;
		return vv;

	}
	
	//提示更新对话框
	public View[] dialogAppUpdate(String version,String message){

		View[] v=new View[2];
		viewDialog=initDialog(context,R.layout.dialog_app_update);
		Button du_qd=(Button)viewDialog.findViewById(R.id.du_qd);
		Button du_qx=(Button)viewDialog.findViewById(R.id.du_qx);
		TextView du_version=(TextView)viewDialog.findViewById(R.id.du_version_name);
		TextView du_update_message=(TextView)viewDialog.findViewById(R.id.du_update_message);

		du_version.setText(version);
		du_update_message.setText(message);


		v[0]=du_qx;
		v[1]=du_qd;
        setCanceledOnTouchOutside(false);
		return v;	
	}
	
	/*
	*底部滑出空白Dialog
	*layoutId layout的id
	*/
	public View dialogBottomSheet(int layoutId){
		builder = new BottomSheetDialog((Activity)context);
		View view = LayoutInflater.from(context).inflate(layoutId, null);
		builder.setContentView(view);
		builder.show();
		return view;
	}
	
	public class IconTextItem{
		private OnITItemClickListener onITItemClickListener;
		
		public void setOnITItemClickListener(OnITItemClickListener onITItemClickListener){
			this.onITItemClickListener=onITItemClickListener;
		}
	}
	
	
	/*
	 *底部划出的Dialog列表
	 *title 标题
	 *list  列表
	 */
	public IconTextItem dialogBottomSheetListIconText(String title,String[] list){

		final IconTextItem it= new IconTextItem();

		builder = new BottomSheetDialog((Activity)context);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_list, null);
		builder.setContentView(view);
		builder.show();

		List<ItemData> data=new ArrayList<ItemData>();
		for(String s:list){
			data.add(ItemData.toItemData(s));
		}
		RecyclerView rv_new_file_list=(RecyclerView)view.findViewById(R.id.rv_list);
		tv_title=(TextView)view.findViewById(R.id.tv_title);	
		initTitle(title);
		rv_new_file_list.setLayoutManager(new LinearLayoutManager(context));
		IconTextRecyclerViewAdapter nFAdp=new IconTextRecyclerViewAdapter(data,false);
		rv_new_file_list.setAdapter(nFAdp);
		nFAdp.setOnITItemClickListener(new OnITItemClickListener(){

				@Override
				public void onItemClick(int position){
					if(it.onITItemClickListener!=null){
						it.onITItemClickListener.onItemClick(position);
					}
					// TODO: Implement this method
				}
			});


		return it; 
	}
	
	/*
	*底部划出的Dialog列表
	*title 标题
	*data item列表
	*isShowIcon 是否显示图标
	*/
	public IconTextItem dialogBottomSheetListIconText(String title,List<ItemData> data,boolean isShowIcon){
		
		final IconTextItem it= new IconTextItem();
		
		builder = new BottomSheetDialog((Activity)context);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_list, null);
		builder.setContentView(view);
		builder.show();
		
		RecyclerView rv_new_file_list=(RecyclerView)view.findViewById(R.id.rv_list);
		tv_title=(TextView)view.findViewById(R.id.tv_title);	
		initTitle(title);
		rv_new_file_list.setLayoutManager(new LinearLayoutManager(context));
		IconTextRecyclerViewAdapter nFAdp=new IconTextRecyclerViewAdapter(data,isShowIcon);
		rv_new_file_list.setAdapter(nFAdp);
		nFAdp.setOnITItemClickListener(new OnITItemClickListener(){

				@Override
				public void onItemClick(int position){
					if(it.onITItemClickListener!=null){
					it.onITItemClickListener.onItemClick(position);
					}
					// TODO: Implement this method
				}
			});
		
		
		return it; 
	}

	//SeekBar对话框
    public View[] dialogSeekBar(String title,int max,int progress){

		View[] v=new View[2];
        viewDialog=initDialog(context,R.layout.dialog_seekbar);
        tv_title=(TextView)viewDialog.findViewById(R.id.tv_title);
        SeekBar ds_sb=(SeekBar)viewDialog.findViewById(R.id.ds_sb);
        Button ds_qd=(Button)viewDialog.findViewById(R.id.ds_qd);
        initTitle(title);
        ds_sb.setMax(max);
		ds_sb.setProgress(progress);
		ds_qd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1){
					dis();
					// TODO: Implement this method
				}
			});
		setCanceledOnTouchOutside(true);
		v[0]=ds_sb;
		v[1]=ds_qd;
		
        return v;   
	}

	public View getViewDialog(){
		return viewDialog;
	}
	
	public TextView getTitle(){
		return tv_title;
	}
	
	public void setTitle(String title){
		if (tv_title!=null){
			initTitle(title);
		}
	}

	private void initTitle(String title){
		if(title!=null&&!title.equals("")){
			tv_title.setText(title);
			tv_title.setVisibility(View.VISIBLE);
		}else{
			tv_title.setVisibility(View.GONE);
		}
	}
	
	//设置对话框是否能被返回键或者触控屏幕关闭
	public void setCanceledOnTouchOutside(boolean cancel){
		builder.setCanceledOnTouchOutside(cancel);	
		// TODO: Implement this method
	}
	
	public void setToast(String s){
		if (tv_toast_message!=null){
			tv_toast_message.setText(s);
		}
	}
	
	public TextView getToastTextView(){
		return tv_toast_message;
	}
	
}
