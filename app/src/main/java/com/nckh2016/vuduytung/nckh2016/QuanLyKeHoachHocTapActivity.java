package com.nckh2016.vuduytung.nckh2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.main.BaseActivity;

public class QuanLyKeHoachHocTapActivity extends BaseActivity {
    //các giá trị Preferences Global
    /*public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";*/
    //các giá trị để dùng load Fragment
    private static final String BLANK_FRAGMENT = "blank";
    private static final String FRAG1 = "frag1";
    private static final String FRAG2 = "frag2";
    //private static final String USER_HOCKY = "user_hocky";
    private static final String HOCKY = "hocky";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quan_ly_ke_hoach_hoc_tap);
    }

    public void loadFragment1(){
        /*Bundle bundle = new Bundle();
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_hocky = currentUserData.getString(SUB_PREFS_DATASINHVIEN, null);
        bundle.putString(USER_HOCKY, user_hocky);*/
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        Fragment frag1 = new QuanLyKeHoachHocTap1Fragment();
        //frag1.setArguments(bundle);
        ft.addToBackStack(FRAG1);
        ft.replace(R.id.fragment, frag1, FRAG1);
        ft.commit();
    }

    public void loadFragment2(ObjectHocKy hocKy){
        Bundle bundle = new Bundle();
        String jsonHocKy = new Gson().toJson(hocKy);
        bundle.putString(HOCKY, jsonHocKy);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        Fragment frag2 = new QuanLyKeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(FRAG2);
        ft.replace(R.id.fragment, frag2, FRAG2);
        ft.commit();
    }

    /*public void loadPreviousFragment(){
        FragmentManager fm = getSupportFragmentManager();
        QuanLyKeHoachHocTap1Fragment frag1 = (QuanLyKeHoachHocTap1Fragment)fm.findFragmentByTag(FRAG1);
        frag1.refreshView();
        fm.popBackStack();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            FragmentManager fm = getSupportFragmentManager();
            QuanLyKeHoachHocTap2Fragment frag2 = (QuanLyKeHoachHocTap2Fragment)fm.findFragmentByTag(FRAG2);
            frag2.refreshView();
        }
    }
}
