package com.nckh2016.vuduytung.nckh2016;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class BoMonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_mon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadFragment2(String maBoMon, String tenBoMon){
        String tag = "fragment_bo_mon_2";
        Bundle bundle = new Bundle();
        bundle.putString("MaBoMon", maBoMon);
        bundle.putString("TenBoMon", tenBoMon);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new BoMon2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment_bo_mon, frag2, tag);
        ft.commit();
    }
}
