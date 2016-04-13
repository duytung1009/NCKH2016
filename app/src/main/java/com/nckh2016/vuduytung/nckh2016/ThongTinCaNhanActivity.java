package com.nckh2016.vuduytung.nckh2016;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.MyContract;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.BaseActivity;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;

public class ThongTinCaNhanActivity extends BaseActivity {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUser objectUser;
    //các asynctask
    MainTask mainTask;
    //các view
    TextView txtMaSinhVien, txtTenSinhVien, txtKhoa, txtNganh, txtChuyenSau, txtNamThu;
    Button btnAddYear;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_thong_tin_ca_nhan);
        txtMaSinhVien = (TextView)findViewById(R.id.txtMaSinhVien);
        txtTenSinhVien = (TextView)findViewById(R.id.txtTenSinhVien);
        txtKhoa = (TextView)findViewById(R.id.txtKhoa);
        txtNganh = (TextView)findViewById(R.id.txtNganh);
        txtChuyenSau = (TextView)findViewById(R.id.txtChuyenSau);
        txtNamThu = (TextView)findViewById(R.id.txtNamThu);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        btnAddYear = (Button)findViewById(R.id.btnAddYear);
        btnAddYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_user != null && objectUser != null){
                    SQLiteDataController data = SQLiteDataController.getInstance(getApplicationContext());
                    try{
                        data.isCreatedDatabase();
                    }
                    catch (IOException e){
                        Log.e("tag", e.getMessage());
                    }
                    ContentValues newData = new ContentValues();
                    newData.put(MyContract.UserEntry.COLUMN_NAM_HOC, (Integer.parseInt(objectUser.getNamhoc()) + 1));
                    data.updateNguoiDung(current_user, newData);
                    if(mainTask != null){
                        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                            mainTask.cancel(true);
                        }
                    }
                    mainTask = new MainTask(getApplicationContext());
                    mainTask.execute();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        mainTask = new MainTask(this);
        mainTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    private class MainTask extends AsyncTask<Void, Long, ObjectUser>{
        private Context mContext;
        private String tenKhoa, tenNganh, tenChuyenSau;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ObjectUser doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try{
                data.isCreatedDatabase();
            }
            catch (IOException e){
                Log.e("tag", e.getMessage());
            }
            objectUser = data.getUser(current_user);
            tenKhoa = data.getTenKhoa(objectUser.getMakhoa());
            tenNganh = data.getTenNganh(objectUser.getManganh());
            tenChuyenSau = data.getTenChuyenSau(objectUser.getManganh(), Integer.parseInt(objectUser.getMachuyensau()));
            return objectUser;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ObjectUser objectUser) {
            super.onPostExecute(objectUser);
            if(objectUser.getNamhoc().equals("5") || (objectUser.getNamhoc().equals("4") && objectUser.getMakhoa().equals("7"))){
                btnAddYear.setVisibility(View.GONE);
            }
            txtMaSinhVien.setText(objectUser.getMasv());
            txtTenSinhVien.setText(objectUser.getHoten());
            txtKhoa.setText(tenKhoa);
            txtNganh.setText(tenNganh);
            txtChuyenSau.setText(tenChuyenSau);
            txtNamThu.setText(objectUser.getNamhoc());
        }
    }
}