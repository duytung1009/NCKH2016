package com.nckh2016.vuduytung.nckh2016;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

public class DangKyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void btndangkyOnClick(View view){
        String masv = ((EditText)findViewById(R.id.editMaSinhVien)).getText().toString();
        String hoten = ((EditText)findViewById(R.id.editHoTen)).getText().toString();
        String email = ((EditText)findViewById(R.id.editEmail)).getText().toString();

        ContentValues newUser = new ContentValues();
        newUser.put("masv", masv);
        newUser.put("hoten", hoten);
        newUser.put("email", email);

        SQLiteDataController data = new SQLiteDataController(this);
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        long flag = data.insertNguoiDung(newUser);
        if(flag == -1){
            Toast.makeText(this, "toach", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "thanh cong", Toast.LENGTH_SHORT).show();
        }
    }
}
