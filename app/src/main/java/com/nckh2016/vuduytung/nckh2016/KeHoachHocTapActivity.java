package com.nckh2016.vuduytung.nckh2016;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;

import java.util.ArrayList;

public class KeHoachHocTapActivity extends AppCompatActivity {
    ObjectHocKy selectedHocKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_hoach_hoc_tap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadFragment2(ObjectHocKy hocKy){
        String tag = "frag2";
        selectedHocKy = hocKy;
        Bundle bundle = new Bundle();
        bundle.putString("nganh", selectedHocKy.getNganh());
        bundle.putInt("namhoc", selectedHocKy.getNamHoc());
        bundle.putInt("hocky", selectedHocKy.getHocKy());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new KeHoachHocTap2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment_ke_hoach_hoc_tap, frag2, tag);
        ft.commit();
    }

    public void loadFragment3(ArrayList<String> selectedMonHoc){
        String tag = "frag3";
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("mamonhoc", selectedMonHoc);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag3 = new KeHoachHocTap3Fragment();
        frag3.setArguments(bundle);
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment_ke_hoach_hoc_tap, frag3, tag);
        ft.commit();
    }
}
