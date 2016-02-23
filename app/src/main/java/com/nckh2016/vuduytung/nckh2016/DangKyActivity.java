package com.nckh2016.vuduytung.nckh2016;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;

public class DangKyActivity extends AppCompatActivity {
    Cursor mKhoaCursor, mNganhCursor;
    SimpleCursorAdapter mKhoaAdapter, mNganhAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SQLiteDataController data = new SQLiteDataController(this);
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        mKhoaCursor = data.getAllKhoa();
        mKhoaAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mKhoaCursor, new String[] {"tenkhoa"}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mKhoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mSpinnerKhoa = (Spinner)findViewById(R.id.spinnerKhoa);
        mSpinnerKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locNganh(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerKhoa.setAdapter(mKhoaAdapter);
    }
    public void locNganh(int position){
        SQLiteDataController data = new SQLiteDataController(getParent());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        Cursor cursor = (Cursor) mKhoaAdapter.getItem(position);
        mNganhCursor = data.getNganh(cursor.getString(cursor.getColumnIndexOrThrow("makhoa")));
        cursor.close();
        if(mNganhCursor != null){
            /*mNganhAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mNganhCursor, new String[]{"tennganh"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mNganhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner mSpinnerNganh = (Spinner)findViewById(R.id.spinnerNganh);
            mSpinnerNganh.setAdapter(mNganhAdapter);
        } else{
            mNganhAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null, new String[]{"tennganh"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mNganhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner mSpinnerNganh = (Spinner)findViewById(R.id.spinnerNganh);
            mSpinnerNganh.setAdapter(mNganhAdapter);*/
        }

    }

    public void btndangkyOnClick(View view){
        String masv = ((EditText)findViewById(R.id.editMaSinhVien)).getText().toString();
        String hoten = ((EditText)findViewById(R.id.editHoTen)).getText().toString();
        String email = ((EditText)findViewById(R.id.editEmail)).getText().toString();
        String makhoa = mKhoaCursor.getString(mKhoaCursor.getColumnIndexOrThrow("makhoa"));
        String manganh = mNganhCursor.getString(mNganhCursor.getColumnIndexOrThrow("manganh"));


        ContentValues newUser = new ContentValues();
        newUser.put("masv", masv);
        newUser.put("hoten", hoten);
        newUser.put("email", email);
        newUser.put("makhoa", makhoa);
        newUser.put("manganh", manganh);

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
