package com.nckh2016.vuduytung.nckh2016;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TaoTaiKhoanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_tai_khoan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*String tag = "frag1";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag1 = new TaoTaiKhoan1Fragment();
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment, frag1, tag);
        ft.commit();*/
    }

    public void loadFragment2(){
        String tag = "frag2";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new TaoTaiKhoan2Fragment();
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment_tao_tai_khoan, frag2, tag);
        ft.commit();
    }

}
