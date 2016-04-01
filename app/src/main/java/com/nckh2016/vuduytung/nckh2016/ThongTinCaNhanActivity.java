package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

public class ThongTinCaNhanActivity extends BaseActivity {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    //các asynctask
    MainTask mainTask;
    //các view
    TextView txtMaSinhVien, txtTenSinhVien, txtKhoa, txtNganh, txtChuyenSau, txtNamThu;

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
            ObjectUser objectUser = data.getUser(current_user);
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
            txtMaSinhVien.setText(objectUser.getMasv());
            txtTenSinhVien.setText(objectUser.getHoten());
            txtKhoa.setText(tenKhoa);
            txtNganh.setText(tenNganh);
            txtChuyenSau.setText(tenChuyenSau);
            txtNamThu.setText(objectUser.getNamhoc());
        }
    }
}