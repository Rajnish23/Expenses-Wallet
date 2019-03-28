package com.suryavanshi.expenseswallet.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.DataModal.ItemTheme;
import com.suryavanshi.expenseswallet.DataModal.RealmHelper;
import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;
import com.suryavanshi.expenseswallet.Utilities.ThemeUtils;


import de.hdodenhof.circleimageview.CircleImageView;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_color,ll_top_category;
    private EditText newCategoryName;
    private TextView emptyCategoryWarning,categoryExist;
    private ImageView colorChooser,delete;
    private int ID = -1;
    private String categoryName;
    private boolean isUpdate = false;
    private RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getId();

        Bundle bundle = getIntent().getExtras();
        helper = AppPreference.realmHelper;

        if(bundle!=null){
            categoryName = bundle.getString("categoryName");
            ItemCategory category = helper.getCategory(categoryName);
            int colorFrom = ((ColorDrawable) ll_top_category.getBackground()).getColor();
            int colorPrimaryTo = getResources().getColor(category.getTheme().getPrimaryColor());
            int colorPrimaryDarkTo = getResources().getColor(category.getTheme().getPrimaryDarkColor());
            ThemeUtils.changeBackground(colorFrom,colorPrimaryTo,ll_top_category,null);
            ThemeUtils.changePrimaryDarkColor(this,colorFrom,colorPrimaryDarkTo);
            colorChooser.setVisibility(View.GONE);
            ll_color.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            isUpdate = true;
            newCategoryName.setText(categoryName);
        }

        CircleImageView color1 = (CircleImageView) findViewById(R.id.color1);
        color1.setOnClickListener(this);
        CircleImageView color2 = (CircleImageView) findViewById(R.id.color2);
        color2.setOnClickListener(this);
        CircleImageView color3 = (CircleImageView) findViewById(R.id.color3);
        color3.setOnClickListener(this);
        CircleImageView color4 = (CircleImageView) findViewById(R.id.color4);
        color4.setOnClickListener(this);
        CircleImageView color5 = (CircleImageView) findViewById(R.id.color5);
        color5.setOnClickListener(this);
        CircleImageView color6 = (CircleImageView) findViewById(R.id.color6);
        color6.setOnClickListener(this);
        CircleImageView color7 = (CircleImageView) findViewById(R.id.color7);
        color7.setOnClickListener(this);
        CircleImageView color8 = (CircleImageView) findViewById(R.id.color8);
        color8.setOnClickListener(this);
        CircleImageView color9 = (CircleImageView) findViewById(R.id.color9);
        color9.setOnClickListener(this);
        CircleImageView color10 = (CircleImageView) findViewById(R.id.color10);
        color10.setOnClickListener(this);
        CircleImageView color11 = (CircleImageView) findViewById(R.id.color11);
        color11.setOnClickListener(this);
        CircleImageView color12 = (CircleImageView) findViewById(R.id.color12);
        color12.setOnClickListener(this);
        color12.setOnClickListener(this);
        CircleImageView color13 = (CircleImageView) findViewById(R.id.color13);
        color13.setOnClickListener(this);
        CircleImageView color14 = (CircleImageView) findViewById(R.id.color14);
        color14.setOnClickListener(this);
        CircleImageView color15 = (CircleImageView) findViewById(R.id.color15);
        color15.setOnClickListener(this);
        CircleImageView color16 = (CircleImageView) findViewById(R.id.color16);
        color16.setOnClickListener(this);

    }

    private void getId() {
        ImageView close = (ImageView)findViewById(R.id.ic_close);
        close.setOnClickListener(this);

        ll_color = (LinearLayout)findViewById(R.id.color_layout);
        ll_top_category = (LinearLayout)findViewById(R.id.ll_top_category_layout);

        colorChooser = (ImageView)findViewById(R.id.color_chooser);
        colorChooser.setOnClickListener(this);

        newCategoryName = (EditText)findViewById(R.id.newCatName);

        emptyCategoryWarning = (TextView)findViewById(R.id.empty_category);

        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(this);

        categoryExist = (TextView)findViewById(R.id.category_exist);
        delete = (ImageView) findViewById(R.id.delete);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int colorFrom = ((ColorDrawable) ll_top_category.getBackground()).getColor();

        switch (v.getId()){
            case R.id.ic_close:
                onBackPressed();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.color_chooser:
                if(ll_color.getVisibility() == View.VISIBLE){
                    ll_color.setVisibility(View.GONE);
                }
                else{
                    ll_color.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.save:
                if(TextUtils.isEmpty(newCategoryName.getText())){
                    emptyCategoryWarning.setVisibility(View.VISIBLE);
                }
                else{

                    ItemCategory category = new ItemCategory();
                    category.setCatName(newCategoryName.getText().toString());
                    category.setTheme(AppPreference.itemTheme);
                    category.setStyle(ThemeUtils.getTHEME());
                    int isSaved = 0;
                    if(isUpdate){
                        helper.UpdateCategory(categoryName,category);
//                        CategoryActivity.categoryList = helper.retrieveCategory();
                        finish();
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }
                    else{
                        isSaved = helper.saveCategory(category);
                        if(isSaved == 0){
                            categoryExist.setVisibility(View.VISIBLE);
                        }
                        else{
                            //helper.retrieveCategory();
                            finish();
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new CategoryFragment());
                        }
                    }

                }
                break;
            case R.id.delete:
                Dialog deleteCategoryDialog = new Dialog(this);
                deleteCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                deleteCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteCategoryDialog.setContentView(R.layout.delete_category_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = deleteCategoryDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                deleteCategoryDialog.setCancelable(false);

                TextView cancel = (TextView)deleteCategoryDialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCategoryDialog.dismiss();
                    }
                });

                TextView delete = (TextView)deleteCategoryDialog.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.deleteCategory(categoryName);
                        deleteCategoryDialog.dismiss();
                        finish();
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }
                });
                deleteCategoryDialog.show();

                break;

            case R.id.color1:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkPilot));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryPilot),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpencePilotTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryPilot,R.color.primaryDarkPilot);
                break;
            case R.id.color2:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkBrown));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryBrown),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceBrownTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryBrown,R.color.primaryDarkBrown);
                break;
            case R.id.color3:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkGrayBlue));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryGrayBlue),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceGrayBlueTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryGrayBlue,R.color.primaryDarkGrayBlue);
                break;
            case R.id.color4:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkOrange));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryOrange),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceOrangeTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryOrange,R.color.primaryDarkOrange);
                break;
            case R.id.color5:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkParotLightGreen));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryParotLightGreen),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceParootLightTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryParotLightGreen,R.color.primaryDarkParotLightGreen);
                break;
            case R.id.color6:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkParotGreen));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryParotGreen),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceParrotGreenTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryParotGreen,R.color.primaryDarkParotGreen);
                break;
            case R.id.color7:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkWallGreen));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryWallGreen),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceWallGreenTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryWallGreen,R.color.primaryDarkWallGreen);
                break;
            case R.id.color8:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkLightCream));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryLightCream),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceLightCreamTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryLightCream,R.color.primaryDarkLightCream);
                break;
            case R.id.color9:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkLightBlue));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryLightBlue),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceLightBlueTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryLightBlue,R.color.primaryDarkLightBlue);
                break;
            case R.id.color10:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkSlateBlue));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primarySlateBlue),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceSlateTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primarySlateBlue,R.color.primaryDarkSlateBlue);break;
            case R.id.color11:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkJamun));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryJamun),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceJamunTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryJamun,R.color.primaryDarkJamun);
                break;
            case R.id.color12:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkRed));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryRed),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceRedTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryRed,R.color.primaryDarkRed);
                break;
            case R.id.color13:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkBlue));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryBlue),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceBlueTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryBlue,R.color.primaryDarkBlue);
                break;
            case R.id.color14:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkPink));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryPink),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpencePinkTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryPink,R.color.primaryDarkPink);
                break;
            case R.id.color15:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkPurple));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryPurple),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpencePurpleTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryPurple,R.color.primaryDarkPurple);
                break;
            case R.id.color16:
                ThemeUtils.changePrimaryDarkColor(AddCategoryActivity.this,colorFrom,getResources().getColor(R.color.primaryDarkGray));
                ThemeUtils.changeBackground(colorFrom,getResources().getColor(R.color.primaryGray),ll_top_category,null);
                ThemeUtils.setTHEME( R.style.AddExpenceGrayTheme);
                AppPreference.itemTheme = new ItemTheme(R.color.primaryGray,R.color.primaryDarkGray);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
