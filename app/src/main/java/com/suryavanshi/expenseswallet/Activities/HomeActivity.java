package com.suryavanshi.expenseswallet.Activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.suryavanshi.expenseswallet.DataModal.ItemCategory;
import com.suryavanshi.expenseswallet.DataModal.ItemModel;
import com.suryavanshi.expenseswallet.DataModal.ItemTheme;
import com.suryavanshi.expenseswallet.DataModal.MothlyData;
import com.suryavanshi.expenseswallet.DataModal.RealmHelper;
import com.suryavanshi.expenseswallet.DataModal.TransactionModel;
import com.suryavanshi.expenseswallet.Fragments.CategoryFragment;
import com.suryavanshi.expenseswallet.Fragments.HomeFragment;
import com.suryavanshi.expenseswallet.Fragments.NoTransactionFragment;

import com.suryavanshi.expenseswallet.R;
import com.suryavanshi.expenseswallet.Utilities.AppPreference;
import com.suryavanshi.expenseswallet.Utilities.NotificationReciever;
import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "HomeActivity";
    private DrawerLayout mDrawerLayout;
    private TextView titleDate,userName,userGmail;
    private ImageView ic_setting,logout,userPhoto;
    private  LinearLayout googleSignIn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private Realm mRealm;
    private FloatingActionsMenu floatingActionsMenu;


    private static final int NOTIFICATION_ID = 0;
    private RealmHelper helper;
    private Calendar myCalendar;
    private DatePickerDialog date;
    private PendingIntent notifyPendingIntent;
    private boolean alarmUp;
    private Switch switcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //shared preference
        AppPreference.sharedPreferences = getSharedPreferences(AppPreference.PREFERENCENAME,MODE_PRIVATE);
        myCalendar = Calendar.getInstance();


        Realm.init(this);
        RealmConfiguration config=new RealmConfiguration.Builder().build();
        mRealm=Realm.getInstance(config);
        helper = new RealmHelper(mRealm);

        AppPreference.realmHelper = helper;
        AddCategoryToDatabase(helper);


        GetId();
        set();
        GoogleSignIn();
        setNotification();

    }

    private boolean isMonthNotAvailable() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.US);
        String month = sdf.format(myCalendar.getTime());
        boolean flag = helper.isMonthAvailable(month);
        if(!flag){
            return true;
        }
        return false;
    }

    private boolean isFirstDayofMonth() {
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth == 1;
    }

    private void setNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notifyIntent = new Intent(this, NotificationReciever.class);

        //Check if the Alarm is already set, and check the toggle accordingly

        alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        if(alarmUp == false) {
            switcher.setChecked(true);
        }


        //Set up the PendingIntent for the AlarmManager

        notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastMessage;
                if(isChecked){
                    Calendar timeOff9 = Calendar.getInstance();
                    timeOff9.set(Calendar.HOUR_OF_DAY, 22   );
                    timeOff9.set(Calendar.MINUTE, 07);
                    timeOff9.set(Calendar.SECOND, 0);

                    //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            timeOff9.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notifyPendingIntent);
                    toastMessage = "On";
                }
                else {
                    alarmManager.cancel(notifyPendingIntent);
                    mNotificationManager.cancelAll();
                    toastMessage = "Off";
                }
                Toast.makeText(HomeActivity.this, toastMessage, Toast.LENGTH_SHORT)
                        .show();
            }
        });



    }

    private void  set()  {
        SimpleDateFormat inFormat = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
//        Calendar calendar = Calendar.getInstance();
        Date date = myCalendar.getTime();
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE",Locale.US);
        String day = outFormat.format(date);
        outFormat = new SimpleDateFormat("MMMM",Locale.US);
        String month = outFormat.format(date);
        titleDate.setText(day+", "+month+" "+myCalendar.get(Calendar.DAY_OF_MONTH) );
        AppPreference.month = month;

        AppPreference.date = inFormat.format(date);

        Log.i(TAG, "set: "+inFormat.format(date));
    }

    private void setCalendar() {
        int mYear = myCalendar.get(Calendar.YEAR);
        int mMonth = myCalendar.get(Calendar.MONTH);
        int mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        date = new DatePickerDialog(HomeActivity.this,
                (datepicker, selectedyear, selectedmonth, selectedday) -> {

                    myCalendar.set(Calendar.YEAR,selectedyear);
                    myCalendar.set(Calendar.MONTH,selectedmonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH,selectedday);

                    set();
                    setFragment();
                }, mYear, mMonth, mDay);
        date.show();

    }

    private void AddCategoryToDatabase(RealmHelper helper) {
        String categoryName[] = getResources().getStringArray(R.array.categoryName);
        int colorPrimary[] = {R.color.primaryBlue,R.color.primaryRed,R.color.primaryPink,R.color.primaryPurple,
                R.color.primaryJamun,R.color.primarySlateBlue,R.color.primaryLightBlue,R.color.primaryLightCream/*,
                R.color.primaryWallGreen,R.color.primaryParotGreen,R.color.primaryParotLightGreen,R.color.primaryOrange,
                R.color.primaryGrayBlue,R.color.primaryBrown,R.color.primaryPilot,R.color.primaryGray*/};

        int colorPrimaryDark[] = {R.color.primaryDarkBlue,R.color.primaryDarkRed,R.color.primaryDarkPink,R.color.primaryDarkPurple,
                R.color.primaryDarkJamun,R.color.primaryDarkSlateBlue,R.color.primaryDarkLightBlue,R.color.primaryDarkLightCream/*,
                R.color.primaryDarkWallGreen,R.color.primaryDarkParotGreen,R.color.primaryDarkParotLightGreen,R.color.primaryDarkOrange,
                R.color.primaryDarkGrayBlue,R.color.primaryDarkBrown,R.color.primaryDarkPilot,R.color.primaryDarkGray*/};

        int style[] = {R.style.AddExpenceBlueTheme,R.style.AddExpenceRedTheme,R.style.AddExpencePinkTheme,
                R.style.AddExpencePurpleTheme,R.style.AddExpenceJamunTheme,R.style.AddExpenceSlateTheme,R.style.AddExpenceLightBlueTheme,
                R.style.AddExpenceLightCreamTheme/*, R.style.AddExpenceWallGreenTheme,R.style.AddExpenceParrotGreenTheme,R.style.AddExpenceParootLightTheme,
                R.style.AddExpenceOrangeTheme,R.style.AddExpenceGrayBlueTheme,R.style.AddExpenceBrownTheme,R.style.AddExpencePilotTheme,
                R.style.AddExpenceGrayTheme*/};
        if (helper.retrieveCategory().size() == 0){
            int i=0;
            for( i=0;i<categoryName.length;i++){
                ItemCategory itemCategory = new ItemCategory();
                ItemTheme itemTheme = new ItemTheme(colorPrimary[i],colorPrimaryDark[i]);

                itemCategory.setCatName(categoryName[i]);
                itemCategory.setTheme(itemTheme);
                itemCategory.setStyle(style[i]);
                helper.saveCategory(itemCategory);
            }

        }

    }

    public void GetId() {

        //Getting Ids
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        titleDate = (TextView) findViewById(R.id.title_date);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ic_setting = (ImageView) findViewById(R.id.ic_setting);
        View headerLayout = navigationView.getHeaderView(0);
        googleSignIn = (LinearLayout) headerLayout.findViewById(R.id.gsign_in);
        logout = (ImageView)headerLayout.findViewById(R.id.ic_logout);
        userName = (TextView)headerLayout.findViewById(R.id.user_name);
        userGmail = (TextView)headerLayout.findViewById(R.id.user_mail_id);
        userPhoto = (ImageView)headerLayout.findViewById(R.id.user_profile);


        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions_left);
        FloatingActionButton addCashSpend = (FloatingActionButton)findViewById(R.id.cash_spend);
        FloatingActionButton allExpence = (FloatingActionButton)findViewById(R.id.all_expense);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_toggle);
        View actionView = menuItem.getActionView();

        switcher = (Switch) actionView.findViewById(R.id.toogle);

        //setting Click Listener
        ic_setting.setOnClickListener(this);
        googleSignIn.setOnClickListener(this);
        logout.setOnClickListener(this);
        titleDate.setOnClickListener(this);
        addCashSpend.setOnClickListener(this);
        allExpence.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_expense:
                                if(AppPreference.realmHelper.retrieve(AppPreference.date).getTransactionModelList().size()!=0) {
                                    ft.replace(R.id.main_container, new HomeFragment(),"Home Fragment");
                                }
                                else{
                                    ft.replace(R.id.main_container,new NoTransactionFragment(),"No Fragment");
                                }
                                break;
                            case R.id.nav_category:
                               ft.replace(R.id.main_container,new CategoryFragment(),"Category Fragment");

                               break;
                            case R.id.nav_week_summary:
                                Snackbar.make(getWindow().getDecorView(),"Coming Soon",Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_month_summary:
                                Snackbar.make(getWindow().getDecorView(),"Coming Soon",Snackbar.LENGTH_SHORT).show();
                                break;
                        }
                        ft.commit();
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_setting:
                mDrawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.gsign_in:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.ic_logout:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        status -> {
                            // ...
//                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            googleSignIn.setVisibility(View.VISIBLE);
                            logout.setVisibility(View.GONE);
                            userName.setText("");
                            userGmail.setText("");
                            userGmail.setVisibility(View.GONE);
                            userName.setVisibility(View.GONE);
                            mDrawerLayout.closeDrawer(GravityCompat.END);
                        });
                break;
            case R.id.cash_spend:
                Intent addExpenceIntent = new Intent(this,AddExpenceActivity.class);
                startActivity(addExpenceIntent);
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
            case R.id.title_date:
                setCalendar();
                break;
            case R.id.all_expense:
                Intent allExpenceIntent = new Intent(this,AllExpenseList.class);
                startActivity(allExpenceIntent);
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
        }
    }

    private void GoogleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    private void updateUI(boolean b) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            Log.i(TAG,   requestCode + " " + requestCode);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {


        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String token = acct.getIdToken();
            String[] name = acct.getDisplayName().split("\\s+");
            String fname = name[0];
            StringBuilder lname = new StringBuilder();
            for (int i = 1; i < name.length; i++)
                lname.append(name[i]).append(" ");
            String email = acct.getEmail();

            String personPhotoUrl=null;
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
                Glide.with(this).load(personPhotoUrl).into(userPhoto);
            }

            userName.setText(fname+" "+lname);
            userGmail.setText(email);
            userName.setVisibility(View.VISIBLE);
            userGmail.setVisibility(View.VISIBLE);
            googleSignIn.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            mDrawerLayout.closeDrawer(GravityCompat.END);

        } else {

            updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(floatingActionsMenu.isExpanded()){
            floatingActionsMenu.collapse();
        }

        if(isFirstDayofMonth() || isMonthNotAvailable()){
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.US);
            String month = sdf.format(myCalendar.getTime());
            helper.insertNewMonth(month);

        }
        else {
            setFragment();
        }

    }

    private void setFragment() {
        SharedPreferences.Editor editor = AppPreference.sharedPreferences.edit();
        editor.putInt(AppPreference.MONTHLY_EXPENSE,helper.getMonthlyExpense());
        editor.commit();
        set();
        Fragment fragmt = getSupportFragmentManager().findFragmentById(R.id.main_container);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ItemModel itemModel = helper.retrieve(AppPreference.date);
        if(itemModel != null){

            if (fragmt != null) {
                switch (fragmt.getTag()) {
                    case "Category Fragment":
                        ft.replace(R.id.main_container, new CategoryFragment(), "Category Fragment");
                        break;
                    default:
                        if (itemModel.getTransactionModelList().size()!=0)  {
                            ft.replace(R.id.main_container, new HomeFragment(), "Home Fragment");
                        } else {
                            ft.replace(R.id.main_container, new NoTransactionFragment(), "No Fragment");
                        }
                        break;
                }


            } else {

                if(itemModel.getTransactionModelList().size()!=0)  {
                    ft.replace(R.id.main_container, new HomeFragment(), "Home Fragment");
                } else {
                    ft.replace(R.id.main_container, new NoTransactionFragment(), "No Fragment");
                }

            }
        }
        else{
            ft.replace(R.id.main_container, new NoTransactionFragment(), "No Fragment");
        }
        ft.commit();
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveFileToDrive();
    }

    private void saveFileToDrive() {
    }*/
}
