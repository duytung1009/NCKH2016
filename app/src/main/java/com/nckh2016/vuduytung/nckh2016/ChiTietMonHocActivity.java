package com.nckh2016.vuduytung.nckh2016;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.text.DecimalFormat;

public class ChiTietMonHocActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    TextView txtMaMonHoc, txtTenMonHoc, txtTinChi, txtDieuKien, txtNoiDung, txtTaiLieu, txtDiem, txtDiem2;
    Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*String value = getIntent().getStringExtra("caller");
        switch (value){
            case "BoMonActivity":
                startActivity(new Intent(this, BoMonActivity.class));
                finish();
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                finish();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_hoc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtMaMonHoc = (TextView) findViewById(R.id.txtMaMonHoc);
        txtTenMonHoc = (TextView) findViewById(R.id.txtTenMonHoc);
        txtTinChi = (TextView) findViewById(R.id.txtTinChi);
        txtDieuKien = (TextView) findViewById(R.id.txtDieuKien);
        txtNoiDung = (TextView) findViewById(R.id.txtNoiDung);
        txtTaiLieu = (TextView) findViewById(R.id.txtTaiLieu);
        String maMonHoc = getIntent().getStringExtra("MaMonHoc");
        SQLiteDataController data = new SQLiteDataController(this);
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        if(maMonHoc!=null && maMonHoc.isEmpty() == false){
            ObjectMonHoc mMonHoc = (ObjectMonHoc)data.getMonHoc(maMonHoc).get(0);
            ActionBar ab = getSupportActionBar();
            ab.setTitle("Chi tiết môn học");
            //ab.setSubtitle(mMonHoc.getMamh());
            txtMaMonHoc.setText(mMonHoc.getMamh());
            txtTenMonHoc.setText(mMonHoc.getTenmh());
            txtTinChi.setText(mMonHoc.getTinchi());
            txtDieuKien.setText(mMonHoc.getDieukien());
            txtNoiDung.setText(mMonHoc.getNoidung());
            txtTaiLieu.setText(mMonHoc.getTailieu());
            SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            current_user = currentUserData.getString("user_mssv", null);
            if(current_user != null){
                float diemSo = data.getDiem(current_user, mMonHoc.getMamh());
                if(diemSo != -1){
                    txtDiem = (TextView) findViewById(R.id.txtDiem);
                    txtDiem.setPadding(40,0,60,0);
                    txtDiem.setTypeface(light);
                    txtDiem2 = (TextView) findViewById(R.id.txtDiem2);
                    txtDiem2.setPadding(40,0,60,0);
                    txtDiem2.setText(new DecimalFormat("####0.00").format(diemSo));
                    if(diemSo < 4){
                        txtDiem.setText("F");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemF));
                    } else if(diemSo < 5.5){
                        txtDiem.setText("D");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemD));
                    } else if(diemSo < 7){
                        txtDiem.setText("C");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemC));
                    } else if(diemSo < 8.5){
                        txtDiem.setText("B");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemB));
                    } else {
                        txtDiem.setText("A");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemA));
                    }
                }
            }
        }
    }
}
