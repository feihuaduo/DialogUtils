package com.feihua.dialogutils.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import com.feihua.dialogutils.*;
import com.feihua.dialogutils.view.*;

public class SelectAdapter extends BaseAdapter
{
	private Context context;
	private List<String> data;
	private Zujian zujian;
	private List<Integer> select;
	
	public SelectAdapter(Context context,List<String> data,List<Integer> select){
		this.data=data;
		this.context=context;
		this.select=select;
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
	public View getView(int position, View p2, ViewGroup p3)
	{
		if(p2==null){
			zujian=new Zujian();
			p2=LayoutInflater.from(context).inflate(R.layout.item_select,null);
			zujian.is_text=(TextView) p2.findViewById(R.id.is_text);
			zujian.is_point=(Point) p2.findViewById(R.id.is_point);
			p2.setTag(zujian);
		}else{
			zujian=(Zujian) p2.getTag();
		}
		if(conin(position)){
			zujian.is_point.setVisibility(View.VISIBLE);
		}else{
			zujian.is_point.setVisibility(View.GONE);
		}
		zujian.is_text.setText(data.get(position));
		// TODO: Implement this method
		return p2;
	}
	
	private boolean conin(int c){
		for(int cc:select){
			if(cc==c){
				return true;
			}
		}
		return false;
	}
	
	class Zujian{
		TextView is_text;
		Point is_point;
	}
	
}
