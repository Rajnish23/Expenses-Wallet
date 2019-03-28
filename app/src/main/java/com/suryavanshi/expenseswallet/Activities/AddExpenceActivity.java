package com.suryavanshi.expenseswallet.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suryavanshi.expenseswallet.Adapters.CategoryAdapter;
import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.DataModal.ItemModel;
import com.suryavanshi.expenseswallet.DataModal.RealmHelper;
import com.suryavanshi.expenseswallet.DataModal.TransactionModel;
import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;
import com.suryavanshi.expenseswallet.Utilities.ThemeUtils;


import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmList;

public class AddExpenceActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog amountDialog,categoryDialog;
    private TextView dialogAmountTextView,selectCategory,amountEmptyView,emptyCategory,amountLarge;
    private EditText datePicker,expenseAmount,expenceNote;
    private Calendar myCalendar;
    private DatePickerDialog date;
    public LinearLayout ll_top_expence_layout;
    private CategoryAdapter categoryAdapter;
    private ImageView delete;
    private boolean isUpdate = false;
    private String bundelId = "-1";
    private String bundelDate = "";
    private String bundelExpence = "";
    private RealmHelper helper;
    private TransactionModel model = null;
    private NumberFormat formttor;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expence);

        formttor = NumberFormat.getInstance(Locale.US);
        GetId();

        helper = AppPreference.realmHelper;

        Bundle bundle = getIntent().getExtras();


        if(bundle!=null){
            delete.setVisibility(View.VISIBLE);
            isUpdate = true;

            bundelDate = bundle.getString("date");
            bundelId = bundle.getString("id");
            bundelExpence = (bundle.getString("expense"));

            model = helper.getItem(bundelId);
            if(model.getCategory()!=null) {
                AppPreference.itemCategory = model.getCategory();
            }
            else{
               helper.setOtherCategory(bundelId);
               model = helper.getItem(bundelId);

            }

            expenseAmount.setText(formttor.format(Integer.parseInt(model.getAmount())));
            selectCategory.setText(model.getCategory().getCatName());
            if(model.getNote().equals("No Note!")){
                expenceNote.setText("");
                expenceNote.setHint("No Note!");
            }
            else {
                expenceNote.setText(model.getNote());
            }

            int colorPrimary = getResources().getColor(model.getCategory().getTheme().getPrimaryColor());
            int colorPrimaryDark = getResources().getColor(model.getCategory().getTheme().getPrimaryDarkColor());

            int colorFrom = ((ColorDrawable) ll_top_expence_layout.getBackground()).getColor();
            ThemeUtils.setTHEME (model.getCategory().getStyle());
            ThemeUtils.changeBackground(colorFrom,colorPrimary,ll_top_expence_layout,null);
            ThemeUtils.changePrimaryDarkColor(this,colorFrom,colorPrimaryDark);

            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy",Locale.US);
            try {
                Date parseDate = format.parse(bundelDate);
                myCalendar.setTime(parseDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePicker.setText(updateLabel(myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)));
        }
        else{

            showAmountDialog();
        }


    }

    private void GetId(){

        expenceNote = (EditText)findViewById(R.id.note);

        ll_top_expence_layout = (LinearLayout)findViewById(R.id.ll_top_expence_layout) ;

        amountEmptyView = (TextView)findViewById(R.id.amount_empty);
        amountLarge = (TextView)findViewById(R.id.amount_large);
        emptyCategory = (TextView)findViewById(R.id.empty_category);

        ImageView ic_close = (ImageView) findViewById(R.id.ic_close);
        ic_close.setOnClickListener(this);

        selectCategory = findViewById(R.id.selectCategory);
        selectCategory.setOnClickListener(this);

        datePicker = (EditText)findViewById(R.id.select_date);
        datePicker.setOnClickListener(this);

        expenseAmount = (EditText)findViewById(R.id.expence_amount);
        expenseAmount.setOnClickListener(this);

        Button save = (Button)findViewById(R.id.save) ;
        save.setOnClickListener(this);

        myCalendar = Calendar.getInstance();
        datePicker.setText(updateLabel(myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)));

        delete = (ImageView)findViewById(R.id.delete);
        delete.setOnClickListener(this);

    }

    private void setCalendar() {
        int mYear = myCalendar.get(Calendar.YEAR);
        int mMonth = myCalendar.get(Calendar.MONTH);
        int mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        date = new DatePickerDialog(AddExpenceActivity.this,ThemeUtils.getTHEME(),
                (datepicker, selectedyear, selectedmonth, selectedday) -> {

                    myCalendar.set(Calendar.YEAR,selectedyear);
                    myCalendar.set(Calendar.MONTH,selectedmonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH,selectedday);

                    SimpleDateFormat outFormat = new SimpleDateFormat("MMMM", Locale.US);
                    datePicker.setText(updateLabel(myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)));
            }, mYear, mMonth, mDay);

        date.show();

    }

    private String updateLabel(int year, int month, int day) {

        String myFormat = "dd MMM yyyy"; //In which format you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, -1);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_MONTH, 1);

        String today = getString(R.string.today);
        String tomorrow = getString(R.string.tomorrow);
        String yesterday = getString(R.string.yesterday);

        if (!(year == calendar1.get(Calendar.YEAR) &&
                month == calendar1.get(Calendar.MONTH) &&
                day == calendar1.get(Calendar.DAY_OF_MONTH))) {

            if (calendar2.get(Calendar.YEAR) == year &&
                    calendar2.get(Calendar.MONTH) == month &&
                    calendar2.get(Calendar.DAY_OF_MONTH) == day) {
                today = yesterday;
            } else
                if (calendar3.get(Calendar.YEAR) == year &&
                        calendar3.get(Calendar.MONTH) == month &&
                        calendar3.get(Calendar.DAY_OF_MONTH) == day) {
                today = tomorrow;
            } else {
                Calendar calendar4 = Calendar.getInstance();
                calendar4.set(year,month,day);
                today = sdf.format(calendar4.getTime());
            }

        }
        AppPreference.date =  sdf.format(myCalendar.getTime());
        return (today);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_close:
                onBackPressed();
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;

            case R.id.select_date:
                setCalendar();
                break;

            case R.id.expence_amount:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                showAmountDialog();
                break;

            case R.id.clear:
                dialogAmountTextView.setText("");
                dialogAmountTextView.setHint("0");
                break;

            case R.id.cancel:
                amountDialog.dismiss();
                break;

            case R.id.okay:

                if (!TextUtils.isEmpty(dialogAmountTextView.getText())) {

                    expenseAmount.setText((formttor.format(Integer.parseInt(dialogAmountTextView.getText().toString()))));
                    amountEmptyView.setVisibility(View.GONE);
                }
                amountDialog.dismiss();
                break;
            case R.id.save:


                if (TextUtils.isEmpty(expenseAmount.getText())) {
                    amountEmptyView.setVisibility(View.VISIBLE); }
                else
                    if(TextUtils.isEmpty(selectCategory.getText())){
                     emptyCategory.setVisibility(View.VISIBLE);
                    }
                else
                    if(expenseAmount.getText().toString().length() >9){
                        amountLarge.setVisibility(View.VISIBLE);
                    }
                else {
                    NumberFormat format = NumberFormat.getNumberInstance(Locale.US);

                    int amt = 0;
                    try {
                        amt = Integer.parseInt(format.parse(expenseAmount.getText().toString()).toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ItemModel itemModel = helper.getTodaysData(AppPreference.date);
                    RealmList<TransactionModel> transactionModelList = null;

                    if(itemModel == null){
                        transactionModelList = new RealmList<>();
                        itemModel = new ItemModel();
                        itemModel.setExpense(amt);
                        itemModel.setDate(AppPreference.date);
                        TransactionModel model = new TransactionModel();
                        String id = AppPreference.date.replace(" ","_")+(transactionModelList.size()+1);
                        model.setId(id);
                        model.setCategory(AppPreference.itemCategory);
                        model.setAmount(expenseAmount.getText().toString());
                        if (!TextUtils.isEmpty(expenceNote.getText())) {
                            model.setNote(expenceNote.getText().toString());
                        } else {
                            model.setNote("No Note!");
                        }
                        transactionModelList.add(model);
                        itemModel.setTransactionModelList(transactionModelList);
                        helper.save(itemModel);
                    }
                    else {

                        transactionModelList = itemModel.getTransactionModelList();

                        //Todo Updating Transaction With date
                        if (isUpdate) {

                            try {
                                amt = Integer.parseInt(format.parse(bundelExpence).toString()) -
                                        Integer.parseInt(format.parse(model.getAmount()).toString()) +
                                        amt;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            TransactionModel newModel = new TransactionModel();
                            newModel.setId(bundelId);
                            newModel.setCategory(AppPreference.itemCategory);
                            try {
                                newModel.setAmount(format.parse(expenseAmount.getText().toString()).toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!TextUtils.isEmpty(expenceNote.getText())) {
                                newModel.setNote(expenceNote.getText().toString());
                            } else {
                                newModel.setNote("No Note!");
                            }
                            helper.updateItem(bundelId, newModel);
                        } else {
                            amt = helper.updateExpence(AppPreference.date, amt);
                            model = new TransactionModel();
                            String id = AppPreference.date.replace(" ", "_") + (transactionModelList.size() + 1);
                            model.setId(id);
                            model.setCategory(AppPreference.itemCategory);

                            try {
                                model.setAmount(formttor.parse(expenseAmount.getText().toString()).toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!TextUtils.isEmpty(expenceNote.getText())) {
                                model.setNote(expenceNote.getText().toString());
                            } else {
                                model.setNote("No Note!");
                            }
                            helper.update(AppPreference.date, model);
                        }

//                    }
                        SharedPreferences.Editor editor = AppPreference.sharedPreferences.edit();
                        editor.putInt(AppPreference.MONTHLY_EXPENSE, amt);
                        editor.commit();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
                        finish();
                    }
                }
                break;

            case R.id.selectCategory:
                showCategoryDialog();
                break;

            case R.id.add_category:
                Intent categoryAddIntent = new Intent(this,AddCategoryActivity.class);
                categoryDialog.dismiss();
                startActivity(categoryAddIntent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;

            case R.id.delete:

                helper.deleteItem(bundelDate,bundelId);
                finish();
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void addNumber(View view){
        switch (view.getId()){
            case R.id._1:
                dialogAmountTextView.append("1");
                break;
            case R.id._2:
                dialogAmountTextView.append("2");
                break;
            case R.id._3:
                dialogAmountTextView.append("3");
                break;
            case R.id._4:
                dialogAmountTextView.append("4");
                break;
            case R.id._5:
                dialogAmountTextView.append("5");
                break;
            case R.id._6:
                dialogAmountTextView.append("6");
                break;
            case R.id._7:
                dialogAmountTextView.append("7");
                break;
            case R.id._8:
                dialogAmountTextView.append("8");
                break;
            case R.id._9:
                dialogAmountTextView.append("9");
                break;
            case R.id._0:
                dialogAmountTextView.append("0");
                break;
            case R.id._00:
                dialogAmountTextView.append("00");
                break;
            case R.id.backspace:
                if(!TextUtils.isEmpty(dialogAmountTextView.getText())){
                    String amt = dialogAmountTextView.getText().toString();
                    dialogAmountTextView.setText(amt.substring(0,amt.length()-1));
                }else{
                    dialogAmountTextView.setText("");
                    dialogAmountTextView.setHint("0");
                }
                break;
        }
    }

    private void showAmountDialog() {
        amountDialog = new Dialog(AddExpenceActivity.this);
        amountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        amountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        amountDialog.setContentView(R.layout.amount_dialog_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = amountDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        amountDialog.setCancelable(false);

        RelativeLayout dialogTop = (RelativeLayout)amountDialog.findViewById(R.id.dialog_top);
        GradientDrawable drawable = (GradientDrawable)dialogTop.getBackground();
        drawable.setColor( ((ColorDrawable) ll_top_expence_layout.getBackground()).getColor());

        TextView cancel = (TextView)amountDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        dialogAmountTextView = (TextView)amountDialog.findViewById(R.id.dialog_amount_text);
        if(!TextUtils.isEmpty(expenseAmount.getText())) {
            try {
                Number test = NumberFormat.getNumberInstance(Locale.US).parse(expenseAmount.getText().toString());
                dialogAmountTextView.setText(test.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        TextView clear = (TextView)amountDialog.findViewById(R.id.clear);
        clear.setOnClickListener(this);

        TextView ok = (TextView)amountDialog.findViewById(R.id.okay);
        ok.setOnClickListener(this);

        amountDialog.show();
    }

    private void showCategoryDialog() {
        categoryDialog = new Dialog(AddExpenceActivity.this);
        categoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        categoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        categoryDialog.setContentView(R.layout.category_dialog_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = categoryDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        categoryDialog.setCancelable(false);

        RecyclerView categoryRecycler = (RecyclerView)categoryDialog.findViewById(R.id.category_recycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        categoryRecycler.setHasFixedSize(true);
        ArrayList<ItemCategory> categoryList = AppPreference.realmHelper.retrieveCategory();
        CategoryAdapter.RecyclerViewClickListener listener = (view,position) ->{
            int colorFrom = ((ColorDrawable) ll_top_expence_layout.getBackground()).getColor();

            ItemCategory category = categoryList.get(position);
            int colorTo = category.getTheme().getPrimaryColor();
            ThemeUtils.setTHEME(category.getStyle());

            AppPreference.itemCategory = category;
            AppPreference.itemTheme = category.getTheme();
            ThemeUtils.changePrimaryDarkColor(AddExpenceActivity.this,colorFrom,getResources().getColor(category.getTheme().getPrimaryDarkColor()));
            ThemeUtils.changeBackground(colorFrom,getResources().getColor(colorTo),ll_top_expence_layout,categoryDialog);
            selectCategory.setText(category.getCatName());
        };
        categoryAdapter = new CategoryAdapter(categoryList,listener);
        categoryRecycler.setAdapter(categoryAdapter);

        TextView addCategory = (TextView)categoryDialog.findViewById(R.id.add_category);
        addCategory.setOnClickListener(this);

        categoryDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
        categoryDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (categoryDialog!=null){
        categoryDialog.dismiss();
            categoryDialog=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(categoryAdapter!=null){
            categoryAdapter.updateData(AppPreference.realmHelper.retrieveCategory());
        }
    }
}
