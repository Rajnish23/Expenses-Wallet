package com.suryavanshi.expenseswallet.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.suryavanshi.expenseswallet.Activities.AddExpenceActivity;
import com.suryavanshi.expenseswallet.Adapters.ItemAdapter;
import com.suryavanshi.expenseswallet.DataModal.ItemModel;
import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;
import com.suryavanshi.expenseswallet.Utilities.ProgressBarAnimation;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ItemAdapter.RecyclerViewClickListener {



    private TextView budgetAmount,monthlyExpence;
    private ProgressBar progressBar;
    private Dialog budgetDialog;
    private ImageView inrImage;
    private String budgetAmt;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ItemModel itemModel;
    private NumberFormat numberFormat = null;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView setBudget = (TextView)view.findViewById(R.id.setBudget);
        budgetAmount = (TextView)view.findViewById(R.id.budget_amount);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        inrImage = (ImageView) view.findViewById(R.id.INR);
        monthlyExpence = (TextView) view.findViewById(R.id.monthly_expence);
        recyclerView = (RecyclerView) view.findViewById(R.id.item_recycler);
        numberFormat = NumberFormat.getInstance(Locale.US);


        setBudget.setOnClickListener(this);

        itemModel = AppPreference.realmHelper.retrieve(AppPreference.date);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new ItemAdapter(getActivity(),itemModel.getTransactionModelList(),this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setBudget:
                showBudgetDialog();
                break;

        }
    }

    public void setProgressAnimation(String maxValue){
        int max = Integer.parseInt(maxValue);

        progressBar.setMax(max);
        int progressValue = (AppPreference.sharedPreferences.getInt(AppPreference.MONTHLY_EXPENSE,0));
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar,0,progressValue);
        anim.setDuration(5000);
        progressBar.startAnimation(anim);
    }

    private void showBudgetDialog(){
        budgetDialog = new Dialog(getActivity());
        budgetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        budgetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        budgetDialog.setContentView(R.layout.set_budget_dialog_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = budgetDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        budgetDialog.setCancelable(false);


        final TextView warningAmount = (TextView)budgetDialog.findViewById(R.id.warning_amount);

        TextView cancel = (TextView)budgetDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(budgetDialog.isShowing()) {
                    if(warningAmount.getVisibility() == View.VISIBLE){
                        warningAmount.setVisibility(View.GONE);
                    }
                    if(budgetAmt!=null) {
                        setProgressAnimation(budgetAmt);
                    }
                    budgetDialog.dismiss();
                }
            }
        });

        final EditText editTextbudgetAmount = (EditText) budgetDialog.findViewById(R.id.edittext_budget_amount);
        final SeekBar seekBar = (SeekBar) budgetDialog.findViewById(R.id.seek_bar);
        seekBar.setMax(200000);

        if(!TextUtils.isEmpty(budgetAmount.getText())){


            try {
                Number value = numberFormat.parse(budgetAmount.getText().toString());
                editTextbudgetAmount.setText(value.toString());
                seekBar.setProgress(Integer.parseInt(value.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        editTextbudgetAmount.setSelection(editTextbudgetAmount.getText().length());

        final int[] progressValue = {0};
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue[0] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editTextbudgetAmount.setText(String.valueOf(progressValue[0]));
            }
        });

        editTextbudgetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s) ){
                    seekBar.setProgress(Integer.parseInt(s.toString()));
                }
            }
        });

        TextView set_budget = (TextView)budgetDialog.findViewById(R.id.set_budget);
        set_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editTextbudgetAmount.getText()) &&
                        editTextbudgetAmount.getText().toString().trim().length()>0){


                    SharedPreferences.Editor editor = AppPreference.sharedPreferences.edit();

                    editor.putBoolean(AppPreference.IS_BUDGET_SET,true);
                    editor.putString(AppPreference.BUDGET_AMOUNT,editTextbudgetAmount.getText().toString());
                    editor.commit();

                    budgetAmt = (editTextbudgetAmount.getText().toString());
                    String budgetFormat = numberFormat.format(Integer.parseInt(budgetAmt));
                    budgetAmount.setText(budgetFormat);

                    inrImage.setVisibility(View.VISIBLE);
                    budgetDialog.dismiss();
                    setProgressAnimation(budgetAmt);
                }
                else{
                    warningAmount.setVisibility(View.VISIBLE);
                }
            }
        });


        budgetDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppPreference.sharedPreferences.getBoolean(AppPreference.IS_BUDGET_SET,false)){
            budgetAmt = AppPreference.sharedPreferences.getString(AppPreference.BUDGET_AMOUNT,"0");
            budgetAmount.setText(numberFormat.format(Integer.parseInt(budgetAmt)));
            inrImage.setVisibility(View.VISIBLE);

            setProgressAnimation(budgetAmt);
        }
        monthlyExpence.setText(numberFormat.format(AppPreference.sharedPreferences.getInt(AppPreference.MONTHLY_EXPENSE,0)));


        itemModel = AppPreference.realmHelper.retrieve(AppPreference.date);
        adapter.updateData(itemModel.getTransactionModelList());

       /* if(itemModel.getTransactionModelList().size() == 0){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_container,new NoTransactionFragment(),"No Fragment");
            ft.commit();
        }*/



    }

    @Override
    public void onClick(int position) {
        Intent cashIntent = new Intent(getActivity(),AddExpenceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("date",itemModel.getDate());
        bundle.putString("id",itemModel.getTransactionModelList().get(position).getId());
        bundle.putString("expense",""+AppPreference.sharedPreferences.getInt(AppPreference.MONTHLY_EXPENSE,0));
        cashIntent.putExtras(bundle);
        startActivity(cashIntent);
    }

    @Override
    public void onClickImg(int position) {

    }
}
