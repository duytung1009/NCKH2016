package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MonHocChuaQuaActivityFragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;

    public MonHocChuaQuaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mon_hoc_chua_qua, container, false);
        final SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        double diem = 5.5;
        final ArrayList<Object> userMonHocChuaQua = data.getMonHocChuaQua(current_user, diem);
        AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0);
        mAdapter.addAll(userMonHocChuaQua);
        TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        txtTitle.setText("Môn học chưa qua: " + userMonHocChuaQua.size() + " môn");
        ListView lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectMonHoc) userMonHocChuaQua.get(position)).getMamh());
                intent.putExtra("caller", "QuanLyKeHoachHocTapActivity");
                startActivity(intent);
            }
        });
        lvMonHoc.setAdapter(mAdapter);
        return view;
    }
}
