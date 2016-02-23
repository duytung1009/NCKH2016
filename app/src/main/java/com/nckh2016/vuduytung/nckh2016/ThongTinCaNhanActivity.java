package com.nckh2016.vuduytung.nckh2016;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

public class ThongTinCaNhanActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvMaSinhVien = (TextView)findViewById(R.id.tvMaSinhVien);
        TextView tvTenSinhVien = (TextView)findViewById(R.id.tvTenSinhVien);
        TextView tvEmail = (TextView)findViewById(R.id.tvEmail);
        TextView tvKhoa = (TextView)findViewById(R.id.tvKhoa);
        TextView tvNganh = (TextView)findViewById(R.id.tvNganh);
        SQLiteDataController data = new SQLiteDataController(this);
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(currentUserData == null){
            //form dang ky
        } else{
            current_user = currentUserData.getString("user_mssv", null);
        }
        Cursor cUser = data.getAllUserData();
        while(cUser.moveToNext()){
            if(cUser.getString(cUser.getColumnIndexOrThrow("masv")).equals(current_user)){
                tvMaSinhVien.setText(cUser.getString(cUser.getColumnIndexOrThrow("masv")));
                tvTenSinhVien.setText(cUser.getString(cUser.getColumnIndexOrThrow("hoten")));
                tvEmail.setText(cUser.getString(cUser.getColumnIndexOrThrow("email")));
                tvKhoa.setText(data.getTenKhoa(cUser.getString(cUser.getColumnIndexOrThrow("makhoa"))));
                tvNganh.setText(data.getTenNganh(cUser.getString(cUser.getColumnIndexOrThrow("manganh"))));
            }
        }
        cUser.close();
    }

}
