package com.nckh2016.vuduytung.nckh2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;

public class QuanLyKeHoachHocTapActivity extends BaseActivity {
    public final static int NAV_INDEX = 1;
    private static final String FRAG1 = "frag1";
    private static final String FRAG2 = "frag2";
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    public String current_user_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_quan_ly_ke_hoach_hoc_tap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(NAV_INDEX).setChecked(true);
    }

    public void loadPreviousFragment(){
        FragmentManager fm = getSupportFragmentManager();
        QuanLyKeHoachHocTapFragment frag1 = (QuanLyKeHoachHocTapFragment)fm.findFragmentByTag("frag1");
        frag1.refreshView();
        fm.popBackStack();
    }

    public void loadFragment2(ObjectHocKy hocKy){
        Bundle bundle = new Bundle();
        bundle.putString("nganh", hocKy.getNganh());
        bundle.putInt("namhoc", hocKy.getNamHoc());
        bundle.putInt("hocky", hocKy.getHocKy());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new QuanLyKeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(FRAG2);
        ft.replace(R.id.fragment, frag2, FRAG2);
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
