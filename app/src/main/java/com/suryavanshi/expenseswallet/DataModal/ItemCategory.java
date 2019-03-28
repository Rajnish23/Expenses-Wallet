package com.suryavanshi.expenseswallet.DataModal;


import io.realm.RealmObject;


public class ItemCategory extends RealmObject  {


    String catName;
    int style;
    ItemTheme theme;

    public ItemCategory(){

    }



    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public ItemTheme getTheme() {
        return theme;
    }

    public void setTheme(ItemTheme theme) {
        this.theme = theme;
    }


}
