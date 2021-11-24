package com.feihua.dialogutils.bean;

public class ItemData {
    private int icon;
    private String name;
    private int id;

    public static ItemData toItemData(int icon, String name) {
        return ItemData.toItemData(0,-1, name);
    }

    public static ItemData toItemData( int id,int icon, String name) {
        ItemData ul = new ItemData();
        ul.setIcon(icon);
        ul.setName(name);
        ul.setId(id);
        return ul;
    }

    public static ItemData toItemData(String name) {
        return ItemData.toItemData(-1, name);
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
