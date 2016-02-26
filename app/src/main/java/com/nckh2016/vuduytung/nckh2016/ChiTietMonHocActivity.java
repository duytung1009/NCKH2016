package com.nckh2016.vuduytung.nckh2016;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

public class ChiTietMonHocActivity extends AppCompatActivity {
    TextView txtMaMonHoc, txtTenMonHoc, txtTinChi, txtDieuKien, txtNoiDung, txtTaiLieu;

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
        Bundle mBundle = getIntent().getBundleExtra("MaMonHoc");
        SQLiteDataController data = new SQLiteDataController(this);
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        if(mBundle.getString("mamh")!=null && mBundle.getString("mamh").isEmpty() == false){
            ObjectMonHoc mMonHoc = (ObjectMonHoc)data.getMonHoc(mBundle.getString("mamh")).get(0);
            ActionBar ab = getSupportActionBar();
            ab.setTitle("Chi tiết môn học");
            //ab.setSubtitle(mMonHoc.getMamh());
            txtMaMonHoc.setText(mMonHoc.getMamh());
            txtTenMonHoc.setText(mMonHoc.getTenmh());
            txtTinChi.setText(mMonHoc.getTinchi());
            txtDieuKien.setText(mMonHoc.getDieukien());
            txtNoiDung.setText(mMonHoc.getNoidung());
            txtTaiLieu.setText(mMonHoc.getTailieu());
        }
    }
}
