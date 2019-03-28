package com.suryavanshi.expenseswallet.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.suryavanshi.expenseswallet.Adapters.ExpenseAdapter;
import com.suryavanshi.expenseswallet.DataModal.TransactionModel;
import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;

import io.realm.RealmResults;


public class AllExpenseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_expense_list);

        LinearLayout noTrnsactionLyout = (LinearLayout) findViewById(R.id.ll_no_transaction);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.expense_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RealmResults<TransactionModel> list = AppPreference.realmHelper.getAllExpense();
        if(list.size() == 0){
            noTrnsactionLyout.setVisibility(View.VISIBLE);
        }
        else{
            if(noTrnsactionLyout.getVisibility() == View.VISIBLE){
                noTrnsactionLyout.setVisibility(View.GONE);
            }
            ExpenseAdapter adapter = new ExpenseAdapter(this,list);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_in,R.anim.top_out);
    }
}
