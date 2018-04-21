package com.feihua.dialogutils.bean;

public class ItemData{
	private int icon;
	private String name;

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return name;
	}


	public void setIcon(int icon){
		this.icon=icon;
	}

	public int getIcon(){
		return icon;
	}

	public static ItemData toItemData(int icon,String name){
		ItemData ul=new ItemData();
		ul.setIcon(icon);
		ul.setName(name);
		return ul;
	}

	public static ItemData toItemData(String name){
		ItemData ul=new ItemData();
		ul.setIcon(-1);
		ul.setName(name);
		return ul;
	}

}
