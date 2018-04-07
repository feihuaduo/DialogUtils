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
/*对话框相关方法
*
*/
public class DialogUtils
{
	//对话框中提示内容
	private TextView dj_ts;
	
	//对话框对象
	private Dialog builder;
	private Context context;
	private static List<DialogUtils> contexts=new ArrayList<DialogUtils>();

	public void setCanceledOnTouchOutside(boolean cancel){
		builder.setCanceledOnTouchOutside(cancel);
		
		// TODO: Implement this method
	}

	
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
		if (builder==null){
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
		View viewDialog = inflater.inflate(layout, null);	
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
	
	//多选对话框
	public Select dialogCheckbox(String title,final List<String> data,final List<Integer> position){


		final Select se=new Select();
		View viewDialog=initDialog(context,R.layout.dialog_select);
		TextView ds_title=(TextView) viewDialog.findViewById(R.id.ds_title);
		Button ds_qd=(Button) viewDialog.findViewById(R.id.ds_qd);
		Button ds_qx=(Button) viewDialog.findViewById(R.id.ds_qx);
		ListView ds_list=(ListView) viewDialog.findViewById(R.id.ds_list);

		ds_title.setText(title);
		
		final SelectAdapter sa=new SelectAdapter(context,data,position);
		ds_list.setAdapter(sa);
		if(position.size()!=0){
			ds_list.setSelection(position.get(0));
		}
		ds_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
				
						if(conin(position,p3)){
							removein(position,p3);
						}else{
							position.add(new Integer(p3));
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
						se.onc.OnCheckbox(data,position);
						dis();
					}
					// TODO: Implement this method
				}
			});

		    builder.setCanceledOnTouchOutside(true);
		return se;	
	}
	
	private void removein(List<Integer> data,int c){
		for(int i=0;i<data.size();i++ ){
			if(data.get(i)==c){
				data.remove(c);
				return;
			}
		}
	}
	
	private boolean conin(List<Integer> data,int c){
		for(int cc:data){
			if(cc==c){
				return true;
			}
		}
		return false;
	}
	
	//单选对话框
	public Select dialogRadio(String title,final List<String> data,int position){

		
		final Select se=new Select();
		View viewDialog=initDialog(context,R.layout.dialog_select);
		TextView ds_title=(TextView) viewDialog.findViewById(R.id.ds_title);
		Button ds_qd=(Button) viewDialog.findViewById(R.id.ds_qd);
		Button ds_qx=(Button) viewDialog.findViewById(R.id.ds_qx);
		ListView ds_list=(ListView) viewDialog.findViewById(R.id.ds_list);
		
		ds_title.setText(title);
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
						if(se.onr!=null){
					se.onr.onRadio(data,po.get(0));
					}
					dis();
					}
					// TODO: Implement this method
				}
			});
		
	   builder.setCanceledOnTouchOutside(true);
		return se;	
	}
	
	//RecyclerView布局提示对话框
    public View[] dialogRec(boolean b,String title,RecyclerView.Adapter adp,RecyclerView.LayoutManager layout){

        View[] v=new View[2];
        View viewDialog=initDialog(context,R.layout.dialog_rec);
		TextView dr_title=(TextView) viewDialog.findViewById(R.id.dr_title);
        RecyclerView dr_rec= (RecyclerView) viewDialog.findViewById(R.id.dr_rec);
        Button dr_qd=(Button) viewDialog.findViewById(R.id.dr_qd);
        if(b){
            dr_title.setText(title);
        }else{
            dr_title.setVisibility(View.GONE);
        }
		
		dr_rec.setLayoutManager(layout);
		dr_rec.setAdapter(adp);


        v[0]=dr_rec;
		v[1]=dr_qd;
        builder.setCanceledOnTouchOutside(true);
        return v;   
	}
	
//表格布局提示对话框
    public View[] dialoggrid(boolean b,String title,ListAdapter adp,int numColumns){

        View[] v=new View[2];
        View viewDialog=initDialog(context,R.layout.dialog_grid);
		TextView dt_title=(TextView) viewDialog.findViewById(R.id.dt_title);
        GridView dt_grid= (GridView) viewDialog.findViewById(R.id.dt_grid);
        Button dt_qd=(Button) viewDialog.findViewById(R.id.dt_qd);
        if(b){
            dt_title.setText(title);
        }else{
            dt_title.setVisibility(View.GONE);
        }
		dt_grid.setNumColumns(numColumns);
		dt_grid.setAdapter(adp);
		
    
        v[0]=dt_grid;
		v[1]=dt_qd;
        builder.setCanceledOnTouchOutside(true);
        return v;   
	}

	


//listview对话框
	public ListView dialogl1(boolean bb,String title,final BaseAdapter badp){

		View viewDialog=initDialog(context,R.layout.dialog_list);
		TextView dl_title=(TextView) viewDialog.findViewById(R.id.dl_title);
		ListView dl_list=(ListView) viewDialog.findViewById(R.id.dl_list);
		if(bb){
			dl_title.setText(title);
		}else{
			dl_title.setVisibility(View.GONE);
		}
		dl_list.setAdapter(badp);

		builder.setCanceledOnTouchOutside(true);
		return dl_list;
		
		
    }
	
//listview对话框
	public ListView dialogl(boolean bb,String title,final String[] ss){
		
		View viewDialog=initDialog(context,R.layout.dialog_list);
		TextView dl_title=(TextView) viewDialog.findViewById(R.id.dl_title);
		ListView dl_list=(ListView) viewDialog.findViewById(R.id.dl_list);
		if(bb){
			dl_title.setText(title);
		}else{
			dl_title.setVisibility(View.GONE);
		}
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
				//返回的view将是对应的item的布局视图,p1为i数组数据对应的下标
				//注意将ff改成你显示dialog的那个Activity
				TextView t=new TextView(context);
				t.setTextColor(Color.parseColor("#008cf9"));
				t.setText(ss[p1]);//p1为对应的数组下标
				t.setTextSize(20);
				t.setGravity(Gravity.CENTER);
				t.setPadding(6,6,6,6);
				t.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
				return t;//返回设置好的view组件，也可以是布局，也可以是控件
			}
		};
		dl_list.setAdapter(b);

      builder.setCanceledOnTouchOutside(true);
		return dl_list;

	}

//EditText对话框
	public View[] dialoge(boolean bb,String title,String hint){

		View[] v=new View[2];
		View viewDialog=initDialog(context,R.layout.dialog_edit);
		TextView de_title=(TextView) viewDialog.findViewById(R.id.de_title);
		EditText de_ed=(EditText) viewDialog.findViewById(R.id.de_ed);
		Button de_qd=(Button) viewDialog.findViewById(R.id.de_qd);
		if(bb){
			de_title.setText(title);
		}else{
			de_title.setVisibility(View.GONE);
		}
		de_ed.setHint(hint);
		v[0]=de_ed;
		v[1]=de_qd;
	   builder.setCanceledOnTouchOutside(true);
		return v;

	}


//加载对话框
	public View[] dialogj(boolean b,String title,final String neirong){

		View[] v=new View[2];
		View viewDialog=initDialog(context,R.layout.dialog_jiazai);
		TextView dj_title=(TextView) viewDialog.findViewById(R.id.dj_title);
		 dj_ts=(TextView) viewDialog.findViewById(R.id.dj_ts);
		Button dj_qx=(Button) viewDialog.findViewById(R.id.dj_qx);
		if(b){
			dj_title.setText(title);
		}else{
			dj_title.setVisibility(View.GONE);
		}
		dj_ts.setText(neirong);
		v[0]=dj_qx;
        v[1]=dj_ts;
        builder.setCanceledOnTouchOutside(false);
		return v;

	}

//无按钮加载对话框
    public View[] dialogj1(boolean b,String title,final String neirong){

        View[] v=new View[1];
		View viewDialog=initDialog(context,R.layout.dialog_jiazai1);
        TextView dj_title=(TextView) viewDialog.findViewById(R.id.dj_title);
        dj_ts=(TextView) viewDialog.findViewById(R.id.dj_ts);
        if(b){
            dj_title.setText(title);
        }else{
            dj_title.setVisibility(View.GONE);
        }
        dj_ts.setText(neirong);
        v[0]=dj_ts;
        builder.setCanceledOnTouchOutside(false);
        return v;

	}
	

//单按钮单图片提示对话框
    public View[] dialogi(boolean b,String title,final String neirong,int im){

        View[] v=new View[1];
        View viewDialog=initDialog(context,R.layout.dialog_image);
        TextView di_title=(TextView) viewDialog.findViewById(R.id.di_title);
        dj_ts=(TextView) viewDialog.findViewById(R.id.di_ts);
		ImageView di_image=(ImageView) viewDialog.findViewById(R.id.di_image);
		di_image.setImageResource(im);
        Button di_qd=(Button) viewDialog.findViewById(R.id.di_qd);
        if(b){
            di_title.setText(title);
        }else{
            di_title.setVisibility(View.GONE);
        }
        dj_ts.setText(neirong);
        v[0]=di_qd;
       builder.setCanceledOnTouchOutside(true);
        return v;   
	}
	
	
	
//单按钮提示对话框
    public View[] dialogt1(boolean b,String title,final String neirong){

        View[] v=new View[1];
        View viewDialog=initDialog(context,R.layout.dialog_toast1);
        TextView dt_title=(TextView) viewDialog.findViewById(R.id.dt_title);
        dj_ts=(TextView) viewDialog.findViewById(R.id.dt_ts);
        Button dt_qd=(Button) viewDialog.findViewById(R.id.dt_qd);
        if(b){
            dt_title.setText(title);
        }else{
            dt_title.setVisibility(View.GONE);
        }
        dj_ts.setText(neirong);
        v[0]=dt_qd;
   	   builder.setCanceledOnTouchOutside(true);
        return v;   
	}


	//提示对话框
	public View[] dialogt(boolean b,String title,final String neirong){

		View[] v=new View[2];
		View viewDialog=initDialog(context,R.layout.dialog_toast);
		TextView dt_title=(TextView) viewDialog.findViewById(R.id.dt_title);
		dj_ts=(TextView) viewDialog.findViewById(R.id.dt_ts);
		Button dt_qd=(Button) viewDialog.findViewById(R.id.dt_qd);
		Button dt_qx=(Button) viewDialog.findViewById(R.id.dt_qx);
		if(b){
			dt_title.setText(title);
		}else{
			dt_title.setVisibility(View.GONE);
		}
		dj_ts.setText(neirong);
		v[0]=dt_qx;
		v[1]=dt_qd;
        builder.setCanceledOnTouchOutside(true);
		return v;	
	}
	
	public void setToast(String s){
		if(dj_ts!=null){
		dj_ts.setText(s);
		}
	}

}
