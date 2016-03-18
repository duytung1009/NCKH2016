package com.nckh2016.vuduytung.nckh2016;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements FragmentQuaTrinhHocTap.OnFragmentInteractionListener, FragmentNguoiDung.OnFragmentInteractionListener, FragmentNienGiam.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    ArrayList<Object> mListUser;
    TabLayout tabLayout;
    TabsPagerAdapter mAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        appBarLayout.addView(View.inflate(this, R.layout.tabhost_layout, null));
        toolbar.addView(View.inflate(this, R.layout.item_spinner_nguoi_dung, null), -1, ActionBar.LayoutParams.MATCH_PARENT);
        loadTabs();
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

    public void loadTabs(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.whiteTransparent), ContextCompat.getColor(this, R.color.white));
        tabLayout.removeAllTabs();

        // Restore preferences
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if((currentUserData.getString("user_mssv", null) == null) || (currentUserData.getString("user_mssv", null).isEmpty())){
            //form dang ky
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 0);
        } else{
            current_user = currentUserData.getString("user_mssv", null);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab1_name), 0);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab2_name), 1);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 2);
        }

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = (ViewPager) findViewById(R.id.pager);
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
        SQLiteDataController data = SQLiteDataController.getInstance(getApplicationContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        if(!data.getUser().isEmpty()){
            mListUser = data.getUser();
            List<String> mListTenUser = new ArrayList<String>();
            for (Object object : mListUser) {
                ObjectUser value = (ObjectUser) object;
                mListTenUser.add(value != null ? value.getHoten() : null);
            }
            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mListTenUser);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerNguoiDung = (Spinner) findViewById(R.id.spinnerNguoiDung);
            spinnerNguoiDung.setAdapter(mAdapter);
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
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            super.onResume();
            recreate();
            tabLayout.removeAllTabs();
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
            viewPager.setAdapter(mAdapter);
        }
    }
}
