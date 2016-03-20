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
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements FragmentQuaTrinhHocTap.OnFragmentInteractionListener, FragmentNguoiDung.OnFragmentInteractionListener, FragmentNienGiam.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    MainTask mainTask;
    ArrayList<Object> mListUser;
    TabLayout tabLayout;
    TabsPagerAdapter mAdapter;
    ViewPager viewPager;
    CircularProgressView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        appBarLayout.addView(View.inflate(this, R.layout.tabhost_layout, null));
        toolbar.addView(View.inflate(this, R.layout.item_spinner_nguoi_dung, null), -1, ActionBar.LayoutParams.MATCH_PARENT);
        viewPager = (ViewPager) findViewById(R.id.pager);
        progressBar = (CircularProgressView)findViewById(R.id.progressBar);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mainTask = new MainTask(this);
        mainTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i=0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        if(!(mAdapter == null)){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    public void loadTabs(ArrayList<Object> users){
        tabLayout.removeAllTabs();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.whiteTransparent), ContextCompat.getColor(this, R.color.white));
        tabLayout.removeAllTabs();
        // Restore preferences
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(!users.isEmpty()){
            if((currentUserData.getString("user_mssv", null) == null) || (currentUserData.getString("user_mssv", null).isEmpty())){
                SharedPreferences.Editor editor = currentUserData.edit();
                editor.putString("user_mssv", ((ObjectUser) users.get(0)).getMasv());
                editor.putString("user_name", ((ObjectUser) users.get(0)).getHoten());
                editor.putString("user_data", ((ObjectUser) users.get(0)).getHocky());
                editor.commit();
                updateNavigationView();
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab1_name), 0);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab2_name), 1);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 2);
            } else{
                current_user = currentUserData.getString("user_mssv", null);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab1_name), 0);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab2_name), 1);
                tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 2);
            }
        } else {
            SharedPreferences.Editor editor = currentUserData.edit();
            editor.putString("user_mssv", null);
            editor.putString("user_name", null);
            editor.putString("user_data", null);
            editor.commit();
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
        Spinner spinnerNguoiDung = (Spinner) findViewById(R.id.spinnerNguoiDung);
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
            spinnerNguoiDung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("user_mssv", ((ObjectUser) mListUser.get(position)).getMasv());
                    editor.putString("user_name", ((ObjectUser) mListUser.get(position)).getHoten());
                    editor.putString("user_data", ((ObjectUser) mListUser.get(position)).getHocky());
                    editor.commit();
                    if (tabLayout.getTabCount() > 0) {
                        (tabLayout.getTabAt(0)).select();
                    }
                    updateNavigationView();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
            //recreate();
            //super.onResume();
            viewPager.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            mainTask = new MainTask(this);
            mainTask.execute();

            /*tabLayout.removeAllTabs();
            // Restore preferences
            SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            if((currentUserData.getString("user_mssv", null) == null) || (currentUserData.getString("user_mssv", null).isEmpty())){
                //form dang ky
                tabLayout.addTab(tabLayout.newTab().setText("niên giám"), 0);
            } else{
                current_user = currentUserData.getString("user_mssv", null);
                tabLayout.addTab(tabLayout.newTab().setText("chức năng"), 0);
                tabLayout.addTab(tabLayout.newTab().setText("hồ sơ"), 1);
                tabLayout.addTab(tabLayout.newTab().setText("niên giám"), 2);
            }
            mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(mAdapter);*/
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
            viewPager.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
