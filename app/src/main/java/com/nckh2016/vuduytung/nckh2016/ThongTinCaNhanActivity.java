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
        TextView txtMaSinhVien = (TextView)findViewById(R.id.txtMaSinhVien);
        TextView txtTenSinhVien = (TextView)findViewById(R.id.txtTenSinhVien);
        TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
        TextView txtKhoa = (TextView)findViewById(R.id.txtKhoa);
        TextView txtNganh = (TextView)findViewById(R.id.txtNganh);
        TextView txtChuyenSau = (TextView)findViewById(R.id.txtChuyenSau);
        TextView txtNamThu = (TextView)findViewById(R.id.txtNamThu);
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
        ObjectUser currentUser = data.getUser(current_user);
        txtMaSinhVien.setText(currentUser.getMasv());
        txtTenSinhVien.setText(currentUser.getHoten());
        txtEmail.setText(currentUser.getEmail());
        txtKhoa.setText(data.getTenKhoa(currentUser.getMakhoa()));
        txtNganh.setText(data.getTenNganh(currentUser.getManganh()));
        txtChuyenSau.setText(data.getTenChuyenSau(currentUser.getManganh(), Integer.parseInt(currentUser.getMachuyensau())));
        txtNamThu.setText(currentUser.getNamhoc());
    }

}
