package com.nckh2016.vuduytung.nckh2016;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {
    //các adapter
    AdapterUser userAdapter;
    //các view
    ListView mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListUser = (ListView)findViewById(R.id.listview_users);
        SQLiteDataController data = SQLiteDataController.getInstance(this);
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        userAdapter = new AdapterUser(this, 0);
        userAdapter.addAll(data.getUser());
        mListUser.setAdapter(userAdapter);
    }
}
