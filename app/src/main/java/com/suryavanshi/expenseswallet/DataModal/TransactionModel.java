package com.suryavanshi.expenseswallet.DataModal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionModel extends RealmObject {
    @PrimaryKey
    String id;
    String amount;
    String note;
    ItemCategory category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }
}
