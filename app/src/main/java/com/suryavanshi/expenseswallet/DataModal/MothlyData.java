package com.suryavanshi.expenseswallet.DataModal;


import io.realm.RealmList;
import io.realm.RealmObject;

public class MothlyData extends RealmObject {

    private String monthName;
    private int monthBudget;
    private int monthlyExpense;
    private RealmList<ItemModel> itemModels;


    public MothlyData(){

    }
    public MothlyData(String monthName, int monthBudget, int monthlyExpense, RealmList<ItemModel> itemModels) {
        this.monthName = monthName;
        this.monthBudget = monthBudget;
        this.monthlyExpense = monthlyExpense;
        this.itemModels = itemModels;
    }

    public String getMonthName() {
        return monthName;
    }

    public int getMonthBudget() {
        return monthBudget;
    }

    public int getMonthlyExpense() {
        return monthlyExpense;
    }

    public RealmList<ItemModel> getItemModels() {
        return itemModels;
    }
}
