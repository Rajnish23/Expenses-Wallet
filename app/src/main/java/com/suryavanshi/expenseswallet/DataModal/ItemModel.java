package com.suryavanshi.expenseswallet.DataModal;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemModel extends RealmObject {

    @PrimaryKey
    String date;
    int expense;
    RealmList<TransactionModel> transactionModelList;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public RealmList<TransactionModel> getTransactionModelList() {
        return transactionModelList;
    }

    public void setTransactionModelList(RealmList<TransactionModel> transactionModelList) {
        this.transactionModelList = transactionModelList;
    }
}
