package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.main.BaseNavActivity;

//activity thí nghiệm về Meaningful Transitions (
public class ThongTinCaNhanActivity extends BaseNavActivity {
    //các giá trị để dùng load Fragment
    private static final String BLANK_FRAGMENT = "blank";
    private static final String FRAG1 = "FRAG1";
    private static final String FRAG2 = "FRAG2";
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_TENSINHVIEN = "user_name";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    //các view
    TextView txtMaSinhVien, txtTenSinhVien;
    //test
    private FrameLayout mFabContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_thong_tin_ca_nhan);

        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        txtMaSinhVien = (TextView)findViewById(R.id.txtMaSinhVien);
        txtTenSinhVien = (TextView)findViewById(R.id.txtTenSinhVien);
        txtMaSinhVien.setText(currentUserData.getString(SUB_PREFS_MASINHVIEN, null));
        txtTenSinhVien.setText(currentUserData.getString(SUB_PREFS_TENSINHVIEN, null));
        mFabContainer = (FrameLayout) findViewById(R.id.fab_container);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        loadFragment1();
    }

    public void loadFragment1(){
        Fragment frag1 = new ThongTinCaNhan1Fragment();
        replaceFragment(frag1, FRAG1);
    }

    public void loadFragment2(){
        Fragment frag2 = new ThongTinCaNhan2Fragment();
        replaceFragment(frag2, FRAG2);
    }

    private void replaceFragment(Fragment fragment, String frag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fab_container, fragment, frag);
        ft.commit();
        /*boolean fragmentPopped = fm.popBackStackImmediate(frag, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = fm.beginTransaction();
            //ft.addToBackStack(frag);  //tránh một số hành vi không mong muốn
            ft.replace(R.id.fab_container, fragment, frag);
            ft.commit();
        }*/
    }
}