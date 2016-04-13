package com.nckh2016.vuduytung.nckh2016;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements FragmentQuaTrinhHocTap.OnFragmentInteractionListener, FragmentNguoiDung.OnFragmentInteractionListener, FragmentNienGiam.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_TENSINHVIEN = "user_name";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_main_activity";
    public static final String SUB_PREFS_TABLAYOUTSTATE = "tab_position";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ArrayList<Object> mListUser;
    //các asynctask
    MainTask mainTask;
    //các view
    TabLayout tabLayout;
    TabsPagerAdapter mAdapter;
    ViewPager viewPager;
    CircularProgressView progressBar;
    Spinner spinnerNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        appBarLayout.addView(View.inflate(this, R.layout.tabhost_layout, null));
        toolbar.addView(View.inflate(this, R.layout.item_spinner_nguoi_dung, null), -1, ActionBar.LayoutParams.MATCH_PARENT);
        viewPager = (ViewPager) findViewById(R.id.pager);
        progressBar = (CircularProgressView)findViewById(R.id.progressBar);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        spinnerNguoiDung = (Spinner) findViewById(R.id.spinnerNguoiDung);
        spinnerNguoiDung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(SUB_PREFS_MASINHVIEN, ((ObjectUser)mListUser.get(position)).getMasv());
                editor.putString(SUB_PREFS_TENSINHVIEN, ((ObjectUser)mListUser.get(position)).getHoten());
                editor.putString(SUB_PREFS_DATASINHVIEN, ((ObjectUser)mListUser.get(position)).getHocky());
                editor.apply();
                if (tabLayout.getTabCount() > 0) {
                    try{
                        (tabLayout.getTabAt(0)).select();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        tabLayout.setEnabled(false);
        Utils.showProcessBar(this, progressBar, viewPager);
        mainTask = new MainTask(this);
        mainTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(mAdapter == null)){
            mAdapter.notifyDataSetChanged();
        }
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        int position = state.getInt(SUB_PREFS_TABLAYOUTSTATE, 0);
        if(tabLayout.getTabCount() == 3){
            try{
                tabLayout.getTabAt(position).select();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        /*Utils.showProcessBar(this, progressBar, viewPager);
        mainTask = new MainTask(this);
        mainTask.execute();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putInt(SUB_PREFS_TABLAYOUTSTATE, tabLayout.getSelectedTabPosition());
        editor.apply();
        /*if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SQLiteDataController.getInstance(getApplicationContext()).close();
    }

    public void loadTabs(ArrayList<Object> users){
        tabLayout.removeAllTabs();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.whiteTransparent), ContextCompat.getColor(this, R.color.white));
        // Restore preferences
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(!users.isEmpty()){
            if((currentUserData.getString(SUB_PREFS_MASINHVIEN, null) == null) || (currentUserData.getString(SUB_PREFS_MASINHVIEN, null).isEmpty())){
                SharedPreferences.Editor editor = currentUserData.edit();
                editor.putString(SUB_PREFS_MASINHVIEN, ((ObjectUser) users.get(0)).getMasv());
                editor.putString(SUB_PREFS_TENSINHVIEN, ((ObjectUser) users.get(0)).getHoten());
                editor.putString(SUB_PREFS_DATASINHVIEN, ((ObjectUser) users.get(0)).getHocky());
                editor.apply();
                updateNavigationView();
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab1_name), 0);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab2_name), 1);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 2);
            } else{
                current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab1_name), 0);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab2_name), 1);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 2);
            }
        } else {
            SharedPreferences.Editor editor = currentUserData.edit();
            editor.putString(SUB_PREFS_MASINHVIEN, null);
            editor.putString(SUB_PREFS_TENSINHVIEN, null);
            editor.putString(SUB_PREFS_DATASINHVIEN, null);
            editor.apply();
            updateNavigationView();
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 0);
        }
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setAdapter(mAdapter);
    }

    public void loadUser(ArrayList<Object> users){
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNguoiDung.setAdapter(mAdapter);
        if(!users.isEmpty()){
            mListUser = users;
            List<String> mListTenUser = new ArrayList<String>();
            for (Object object : mListUser) {
                ObjectUser value = (ObjectUser) object;
                mListTenUser.add(value != null ? value.getHoten() : null);
            }
            mAdapter.addAll(mListTenUser);
            if (current_user != null) {
                int index = 0;
                for (Object object : mListUser) {
                    ObjectUser value = (ObjectUser) object;
                    if (value.getMasv().equals(current_user)){
                        index = mListUser.indexOf(value);
                    }
                }
                spinnerNguoiDung.setSelection(index);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát ứng dụng")
                .setMessage("Bạn có muốn thoát ứng dụng?")
                .setIcon(R.drawable.error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SQLiteDataController.getInstance(getApplicationContext()).close();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //Hide title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //loadUser();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            tabLayout.setEnabled(false);
            Utils.showProcessBar(this, progressBar, viewPager);
            mainTask = new MainTask(this);
            mainTask.execute();
        }
    }

    public class MainTask extends AsyncTask<Void, Long, ArrayList<Object>> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Object> doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try{
                data.isCreatedDatabase();
            }
            catch (IOException e){
                Log.e("tag", e.getMessage());
            }
            return data.getUser();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Object> objects) {
            super.onPostExecute(objects);
            loadTabs(objects);
            loadUser(objects);
            tabLayout.setEnabled(true);
            Utils.hideProcessBar(mContext, progressBar, viewPager);
        }
    }

    public class TabsPagerAdapter extends FragmentStatePagerAdapter {
        FragmentNguoiDung tab1;
        FragmentQuaTrinhHocTap tab2;
        FragmentNienGiam tab3;
        int mNumOfTabs;

        public TabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (mNumOfTabs){
                case 1:
                    if(tab3 == null){
                        tab3 = new FragmentNienGiam();
                    }
                    return tab3;
                case 3:
                    switch (position) {
                        case 0:
                            if(tab1 == null){
                                tab1 = FragmentNguoiDung.newInstance(null, null);
                            }
                            return tab1;
                        case 1:
                            if(tab2 == null){
                                tab2 = FragmentQuaTrinhHocTap.newInstance(null,null);
                            }
                            return tab2;
                        case 2:
                            if(tab3 == null){
                                tab3 = FragmentNienGiam.newInstance(null, null);
                            }
                            return tab3;
                        default:
                            return null;
                    }
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
