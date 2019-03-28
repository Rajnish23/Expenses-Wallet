package com.suryavanshi.expenseswallet.Utilities;

import android.content.SharedPreferences;


import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.DataModal.ItemTheme;
import com.suryavanshi.expenseswallet.DataModal.RealmHelper;


public class AppPreference {


    public static final String PREFERENCENAME = "expencepreference";

    // Keys for Shared Preferences

    public static final String IS_BUDGET_SET = "isbudgetset";
    public static final String BUDGET_AMOUNT = "budgetamount";
    public static final String MONTHLY_EXPENSE = "monthly_expence";

    public static SharedPreferences sharedPreferences;
    public static RealmHelper realmHelper;

    public static String date = "";

    public static ItemTheme itemTheme;

    public static ItemCategory itemCategory ;

    private ItemCategory category = new ItemCategory();

    public static String month = "";





}
