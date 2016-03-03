package com.nckh2016.vuduytung.nckh2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectCTDT;

public class NganhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nganh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadFragment2(String maNganh, String tenNganh){
        String tag = "fragment_nganh_2";
        Bundle bundle = new Bundle();
        bundle.putString("MaNganh", maNganh);
        bundle.putString("TenNganh", tenNganh);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new Nganh2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(tag);
        ft.replace(R.id.fragment_nganh, frag2, tag);
        ft.commit();
    }

    public void loadFragment3(ObjectCTDT monHoc){
        if(monHoc.getMamh() == null){
            String tag = "fragment_nganh_3";
            Bundle bundle = new Bundle();
            switch (monHoc.getTuchon()){
                case "A":
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", -3);
                    break;
                case "B":
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", -2);
                    break;
                case "C":
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", -1);
                    break;
                default:
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", 0);
                    break;
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment frag3 = new Nganh3Fragment();
            frag3.setArguments(bundle);
            ft.addToBackStack(tag);
            ft.replace(R.id.fragment_nganh, frag3, tag);
            ft.commit();
        } else {
            Intent intent = new Intent(this, ChiTietMonHocActivity.class);
            intent.putExtra("MaMonHoc", monHoc.getMamh());
            intent.putExtra("caller", "BoMonActivity");
            startActivity(intent);
        }
    }
}
