package com.nckh2016.vuduytung.nckh2016;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;

public class QuanLyKeHoachHocTapActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    public String current_user_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_ke_hoach_hoc_tap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        current_user_name = currentUserData.getString("user_name", null);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(current_user_name);
        ab.setSubtitle(current_user);
    }

    public void loadPreviousFragment(){
        FragmentManager fm = getSupportFragmentManager();
        QuanLyKeHoachHocTapFragment frag1 = (QuanLyKeHoachHocTapFragment)fm.findFragmentByTag("frag1");
        frag1.refreshView();
        fm.popBackStack();
    }

    public void loadFragment2(ObjectHocKy hocKy){
        String tag = "frag2";
        Bundle bundle = new Bundle();
        bundle.putString("nganh", hocKy.getNganh());
        bundle.putInt("namhoc", hocKy.getNamHoc());
        bundle.putInt("hocky", hocKy.getHocKy());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new QuanLyKeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment, frag2, tag);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            FragmentManager fm = getSupportFragmentManager();
            QuanLyKeHoachHocTap2Fragment frag2 = (QuanLyKeHoachHocTap2Fragment)fm.findFragmentByTag("frag2");
            frag2.refreshView();
        }
    }
}
