package com.nckh2016.vuduytung.nckh2016;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;

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
        TextView tvNamThu = (TextView)findViewById(R.id.tvNamThu);
        SQLiteDataController data = SQLiteDataController.getInstance(this);
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
        ArrayList<Object> mListUser = data.getUser(current_user);
        ObjectUser currentUser = (ObjectUser)mListUser.get(0);
        tvMaSinhVien.setText(currentUser.getMasv());
        tvTenSinhVien.setText(currentUser.getHoten());
        tvEmail.setText(currentUser.getEmail());
        tvKhoa.setText(data.getTenKhoa(currentUser.getMakhoa()));
        tvNganh.setText(data.getTenNganh(currentUser.getManganh()));
        tvNamThu.setText(currentUser.getNamhoc());

        /*Cursor cUser = data.getAllUserData();
        while(cUser.moveToNext()){
            if(cUser.getString(cUser.getColumnIndexOrThrow("masv")).equals(current_user)){
                tvMaSinhVien.setText(cUser.getString(cUser.getColumnIndexOrThrow("masv")));
                tvTenSinhVien.setText(cUser.getString(cUser.getColumnIndexOrThrow("hoten")));
                tvEmail.setText(cUser.getString(cUser.getColumnIndexOrThrow("email")));
                tvKhoa.setText(data.getTenKhoa(cUser.getString(cUser.getColumnIndexOrThrow("makhoa"))));
                tvNganh.setText(data.getTenNganh(cUser.getString(cUser.getColumnIndexOrThrow("manganh"))));
            }
        }
        cUser.close();*/
    }

}
