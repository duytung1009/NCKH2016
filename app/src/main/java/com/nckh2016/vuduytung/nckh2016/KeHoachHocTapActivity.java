package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.main.BaseActivity;

import java.util.ArrayList;

public class KeHoachHocTapActivity extends BaseActivity {
    //các giá trị để dùng load Fragment
    private static final String FRAG1 = "frag1";
    private static final String FRAG2 = "frag2";
    private static final String FRAG3 = "frag3";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_kehoachhoctap_activity";
    public static final String SUB_PREFS_HOCKY = "hocKy";
    //các biến được khôi phục lại nếu app resume
    private ObjectHocKy selectedHocKy;
    public int tinChiHocKy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ke_hoach_hoc_tap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tenNganh = getIntent().getStringExtra("Nganh");
        String tenChuyenSau = getIntent().getStringExtra("ChuyenSau");
        tinChiHocKy = getIntent().getIntExtra("TinChi", 0);
        ActionBar ab = getSupportActionBar();
        try{
            ab.setTitle(tenNganh);
            ab.setSubtitle(tenChuyenSau);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
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

    public void loadFragment2(ObjectHocKy hocKy){
        selectedHocKy = hocKy;
        Bundle bundle = new Bundle();
        bundle.putString("nganh", selectedHocKy.getNganh());
        bundle.putInt("namhoc", selectedHocKy.getNamHoc());
        bundle.putInt("hocky", selectedHocKy.getHocKy());

        Fragment frag2 = new KeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        replaceFragment(frag2, FRAG2);
    }

    public void loadFragment3(ObjectHocKy hocKy, ArrayList<String> selectedMonHoc){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("mamonhoc", selectedMonHoc);
        bundle.putString("nganh", hocKy.getNganh());
        bundle.putInt("namhoc", hocKy.getNamHoc());
        bundle.putInt("hocky", hocKy.getHocKy());
        bundle.putInt("user_namhoc", getIntent().getIntExtra("NamHoc", -1));
        bundle.putInt("user_hocky", getIntent().getIntExtra("HocKy", -1));

        Fragment frag3 = new KeHoachHocTap3Fragment();
        frag3.setArguments(bundle);
        replaceFragment(frag3, FRAG3);
    }

    private void replaceFragment(Fragment fragment, String frag){
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(frag, 0);
        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            ft.replace(R.id.fragment_ke_hoach_hoc_tap, fragment, frag);
            ft.addToBackStack(frag);
            ft.commit();
        }
    }
}
