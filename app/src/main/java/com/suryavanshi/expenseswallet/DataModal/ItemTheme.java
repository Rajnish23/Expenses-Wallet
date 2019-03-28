package com.suryavanshi.expenseswallet.DataModal;



import io.realm.RealmObject;

public class ItemTheme extends RealmObject {

    int primaryColor;
    int primaryDarkColor;

    public ItemTheme(){

    }
    public ItemTheme(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    protected ItemTheme(android.os.Parcel in) {
        primaryColor = in.readInt();
        primaryDarkColor = in.readInt();
    }



    public int getPrimaryColor() {
        return primaryColor;
    }


    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

}
