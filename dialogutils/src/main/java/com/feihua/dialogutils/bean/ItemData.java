package com.feihua.dialogutils.bean;

public class ItemData {
    public static final int ICON_NULL=0;

    private int icon;
    private String name;
    private int id;
    private Object object;

    public static ItemData toItemData(int icon, String name) {
        return ItemData.toItemData(icon, name,null);
    }

    public static ItemData toItemData(int icon, String name,Object object) {
        return ItemData.toItemData(0,icon, name,object);
    }

    public static ItemData toItemData( int id,int icon, String name,Object object) {
        ItemData ul = new ItemData();
        ul.setIcon(icon);
        ul.setName(name);
        ul.setObject(object);
        ul.setId(id);
        return ul;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public static ItemData toItemData(String name) {
        return ItemData.toItemData(ICON_NULL, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
