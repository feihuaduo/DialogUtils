package com.feihua.dialogutils.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.feihua.dialogutils.base.OnITItemClickListener;
import com.feihua.dialogutils.bean.ItemData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.feihua.dialogutils.R;

public class IconTextRecyclerViewAdapter extends RecyclerView.Adapter<IconTextRecyclerViewAdapter.ViewHolder>
{
	private List<ItemData> data;
	private boolean isShowIcon;
	
	public IconTextRecyclerViewAdapter(List<ItemData> data,boolean isShowIcon){
		this.data=data;
		this.isShowIcon=isShowIcon;
	}
	
	@Override
	public IconTextRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup p1, int p2){
		View v=LayoutInflater.from(p1.getContext()).inflate(R.layout.icon_text_recycl_item,p1,false);
		// TODO: Implement this method
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(IconTextRecyclerViewAdapter.ViewHolder vh,final int position){
		
		ItemData itemData=data.get(position);
		vh.tv_name.setText(itemData.getName());
		if(isShowIcon){
			if(itemData.getIcon()!=-1){
			vh.iv_icon.setImageResource(itemData.getIcon());
			}
			vh.iv_icon.setVisibility(View.VISIBLE);
		}else{
			vh.iv_icon.setVisibility(View.GONE);
		}
		
		vh.v.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1){
					if(onITItemClickListener!=null){
					onITItemClickListener.onItemClick(position);
					}
					// TODO: Implement this method
				}
			});
		// TODO: Implement this method
	}

	@Override
	public int getItemCount(){	
		// TODO: Implement this method
		return data.size();
	}
	
	private OnITItemClickListener onITItemClickListener;
	
	
	public void setOnITItemClickListener(OnITItemClickListener onITItemClickListener){
		this.onITItemClickListener=onITItemClickListener;
	}
	
	class ViewHolder extends RecyclerView.ViewHolder{
		androidx.appcompat.widget.AppCompatTextView tv_name;
		ImageView iv_icon;
		View v;
		ViewHolder(View v){
			super(v);
			this.v=v;
			tv_name=(androidx.appcompat.widget.AppCompatTextView) v.findViewById(R.id.tv_name);
			iv_icon=(ImageView) v.findViewById(R.id.iv_icon);
		}
	}
}
