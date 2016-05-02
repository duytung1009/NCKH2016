package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.main.BaseNavActivity;

import java.util.ArrayList;

public class QuanLyKeHoachHocTapActivity extends BaseNavActivity {
    //các giá trị Preferences Global
    /*public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";*/
    //các giá trị để dùng load Fragment
    private static final String BLANK_FRAGMENT = "blank";
    private static final String FRAG1 = "FRAG1";
    private static final String FRAG2 = "FRAG2";
    private static final String FRAG3 = "FRAG3";
    private static final String FRAG4 = "FRAG4";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_quanlykehoachhoctap_activity";
    public static final String SUB_PREFS_HOCKY = "hocKy";
    //
    private static final String HOCKY = "hocky";
    private static final String DANHSACHMONHOC = "danhsachmonhoc";
    //các biến được khôi phục lại nếu app resume
    private ObjectHocKy selectedHocKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quan_ly_ke_hoach_hoc_tap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //lấy dữ liệu được lưu lại khi app Paused
        if(selectedHocKy == null){
            SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
            selectedHocKy = new Gson().fromJson(state.getString(SUB_PREFS_HOCKY, null), ObjectHocKy.class);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putString(SUB_PREFS_HOCKY, new Gson().toJson(selectedHocKy));
        editor.apply();
    }

    public void loadFragment1(){
        /*Bundle bundle = new Bundle();
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user_hocky = currentUserData.getString(SUB_PREFS_DATASINHVIEN, null);
        bundle.putString(USER_HOCKY, user_hocky);*/

        Fragment frag1 = new QuanLyKeHoachHocTap1Fragment();
        replaceFragment(frag1, FRAG1);

        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        Fragment frag1 = new QuanLyKeHoachHocTap1Fragment();
        //frag1.setArguments(bundle);
        ft.addToBackStack(FRAG1);
        ft.replace(R.id.fragment, frag1, FRAG1);
        ft.commit();*/
    }

    public void loadFragment2(ObjectHocKy hocKy){
        Bundle bundle = new Bundle();
        String jsonHocKy = new Gson().toJson(hocKy);
        bundle.putString(HOCKY, jsonHocKy);
        selectedHocKy = hocKy;

        Fragment frag2 = new QuanLyKeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        replaceFragment(frag2, FRAG2);

        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        Fragment frag2 = new QuanLyKeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(FRAG2);
        ft.replace(R.id.fragment, frag2, FRAG2);
        ft.commit();*/
    }

    public void loadFragment3(ObjectHocKy hocKy, ArrayList<String> danhSachMonHoc){
        Bundle bundle = new Bundle();
        String jsonHocKy = new Gson().toJson(hocKy);
        bundle.putStringArrayList(DANHSACHMONHOC, danhSachMonHoc);
        bundle.putString(HOCKY, jsonHocKy);

        Fragment frag3 = new QuanLyKeHoachHocTap3Fragment();
        frag3.setArguments(bundle);
        replaceFragment(frag3, FRAG3);

        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        Fragment frag3 = new QuanLyKeHoachHocTap3Fragment();
        frag3.setArguments(bundle);
        ft.addToBackStack(FRAG3);
        ft.replace(R.id.fragment, frag3, FRAG3);
        ft.commit();*/
    }

    private void replaceFragment(Fragment fragment, String frag){
        FragmentManager fm = getSupportFragmentManager();
        boolean fragmentPopped = fm.popBackStackImmediate(frag, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            ft.addToBackStack(frag);
            ft.replace(R.id.fragment_quanlykehoachhoctap, fragment, frag);
            ft.commit();
        }
    }

    public void refreshFragment1(){
        FragmentManager fm = getSupportFragmentManager();
        //lỗi: không tìm thấy fragment (vì fragment đầu tiên được activity tự động load vào (ko có tag đi kèm) chứ không sử dụng phương thức loadFragment
        //Giải quyết: thêm tag cho fragment gốc (add android:tag=tên_tag vào file xml chứa fragment gốc)
        fm.executePendingTransactions();
        QuanLyKeHoachHocTap1Fragment frag1 = (QuanLyKeHoachHocTap1Fragment)fm.findFragmentByTag(FRAG1);
        if(frag1 == null){
            loadFragment1();
        } else {
            frag1.refreshView();
            fm.popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            FragmentManager fm = getSupportFragmentManager();
            fm.executePendingTransactions();
            QuanLyKeHoachHocTap2Fragment frag2 = (QuanLyKeHoachHocTap2Fragment)fm.findFragmentByTag(FRAG2);
            if(frag2 == null){
                loadFragment2(selectedHocKy);
            } else {
                frag2.refreshView();
            }
        }
    }
}
