package com.suryavanshi.expenseswallet.DataModal;

import android.content.SharedPreferences;


import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;
    public RealmHelper(Realm realm){
        this.realm = realm;
    }


    public MothlyData retrieveMonth(String month){
        MothlyData monthData = realm.where(MothlyData.class).equalTo("monthName", month).findFirst();
        return monthData;
    }

    public ItemModel retrieve(String date){

        ItemModel models = realm.where(ItemModel.class).equalTo("date",date).findFirst();

       /* AppPreference.monthlyExpence =  String.valueOf(amount);
        SharedPreferences.Editor editor = AppPreference.sharedPreferences.edit();
        editor.putString(AppPreference.MONTHLY_EXPENSE,AppPreference.monthlyExpence);
        editor.commit();*/
        return  models;
    }

    public void save(final ItemModel realmItemModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemModel itemModel = realm.copyToRealm(realmItemModel);

            }
        });

    }
    public void update(String date,TransactionModel model){
        realm.beginTransaction();
        List<TransactionModel> models = realm.where(ItemModel.class).equalTo("date",date).findFirst().transactionModelList;
        models.add(model);
        realm.copyToRealmOrUpdate(models);
        realm.commitTransaction();;
    }

 public void updateItem(String id , TransactionModel model) {
        realm.beginTransaction();
        TransactionModel transactionModel = realm.where(TransactionModel.class).equalTo("id",id).findFirst();
        transactionModel = model;
        realm.copyToRealmOrUpdate(transactionModel);
        realm.commitTransaction();
    }



    public int saveCategory(final ItemCategory realmCategory){
        final int[] saved = {0};
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                ItemCategory isDataExist = realm.where(ItemCategory.class).equalTo("catName", realmCategory.getCatName(),Case.INSENSITIVE).findFirst();
                if(isDataExist == null) {
                    ItemCategory realmCategory1 = realm.copyToRealm(realmCategory);
                    saved[0] = 1;
                }

            }
        });
        return saved[0];
    }

    public ArrayList<ItemCategory> retrieveCategory(){
        ArrayList<ItemCategory> categoryArrayList = new ArrayList<>();
        RealmResults<ItemCategory> realmCategories = realm.where(ItemCategory.class).notEqualTo("catName","Others").findAll();
        for(ItemCategory category : realmCategories){
            categoryArrayList.add(category);
        }
        return categoryArrayList;
    }

    public void UpdateCategory(String name,ItemCategory category){
        deleteCategory(name);
        realm.beginTransaction();
        realm.insert(category);
        realm.commitTransaction();
    }

    public void deleteCategory(String name){
        realm.beginTransaction();
        realm.where(ItemCategory.class).equalTo("catName",name).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }

    public void deleteItem(String date,String id){
        realm.beginTransaction();
        TransactionModel query = realm.where(TransactionModel.class).equalTo("id", id).findFirst();
        ItemModel model = realm.where(ItemModel.class).equalTo("date",date).findFirst();
        model.expense-=Integer.parseInt(query.getAmount());
        query.deleteFromRealm();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();

        SharedPreferences.Editor editor = AppPreference.sharedPreferences.edit();
        editor.putInt(AppPreference.MONTHLY_EXPENSE,model.getExpense());
        editor.commit();
    }


    public ItemCategory getCategory(String name){

        ItemCategory category = realm.where(ItemCategory.class).equalTo("catName",name).findFirst();

        return category;
    }

    public TransactionModel getItem(String id){
        realm.beginTransaction();
        TransactionModel tm = realm.where(TransactionModel.class).equalTo("id",id).
                                                        findFirst();
        realm.commitTransaction();
        return tm;
    }

    public ItemModel getTodaysData(String date) {
        realm.beginTransaction();
        ItemModel itemModel = realm.where(ItemModel.class).equalTo("date",date).findFirst();
        realm.commitTransaction();;
        return itemModel;
    }

    public int updateExpence(String date, int amt) {
        realm.beginTransaction();
        ItemModel model = realm.where(ItemModel.class).equalTo("date",date).findFirst();
        amt+=model.expense;
        model.setExpense(amt);
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        return amt;
    }
    public int getMonthlyExpense(){

        RealmResults<TransactionModel> tModel = realm.where(TransactionModel.class).findAll();

        int expense = 0;
        for (int i=0;i<tModel.size();i++){
            expense+=Integer.parseInt(tModel.get(i).getAmount());
        }

        return expense;
    }

    public void setOtherCategory(String id){
        realm.beginTransaction();
        TransactionModel model = realm.where(TransactionModel.class).equalTo("id",id).findFirst();
        RealmObject categoryObject = realm.createObject(ItemCategory.class);
        ((ItemCategory) categoryObject).setCatName("Others");
        ((ItemCategory) categoryObject).setStyle(R.style.AddExpenceBlueTheme);
        RealmObject themeObject = realm.createObject(ItemTheme.class);
        ((ItemTheme) themeObject).primaryColor = R.color.primaryBlue;
        ((ItemTheme) themeObject).primaryDarkColor = R.color.primaryDarkBlue;

        ((ItemCategory) categoryObject).setTheme((ItemTheme) themeObject);
        model.setCategory((ItemCategory) categoryObject);
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();


    }

    public RealmResults<TransactionModel> getAllExpense(){

        RealmResults<TransactionModel> data = realm.where(TransactionModel.class).findAll();

        return data;
    }

    public void insertNewMonth(String monthName) {
        realm.beginTransaction();
        MothlyData mothlyData = new MothlyData(monthName,0,0,new RealmList<ItemModel>());
        realm.insert(mothlyData);
        realm.commitTransaction();
    }

    public boolean isMonthAvailable(String month) {

        MothlyData data = realm.where(MothlyData.class).equalTo("monthName", month).findFirst();
        if(data == null)
            return false;

        return true;
    }
}
