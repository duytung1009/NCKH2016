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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuanLyKeHoachHocTap2Fragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;

    public QuanLyKeHoachHocTap2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap_2, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        final ObjectHocKy selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));
        SQLiteDataController data = new SQLiteDataController(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        final ArrayList<Object> userData = data.getUserData(current_user, selectedHocKy.getNamHoc(), selectedHocKy.getHocKy());

        ListView lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectMonHoc) userData.get(position)).getMamh());
                intent.putExtra("caller", "QuanLyKeHoachHocTapActivity");
                startActivity(intent);
            }
        });
        TextView txtTenHocKy = (TextView)view.findViewById(R.id.txtTenHocKy);
        txtTenHocKy.setText("Học kỳ " + selectedHocKy.getHocKy() + " năm thứ " + selectedHocKy.getNamHoc());
        Button btnThemMonHoc = (Button)view.findViewById(R.id.btnThemMonHoc);
        btnThemMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KeHoachHocTapActivity.class);
                intent.putExtra("HocKy", selectedHocKy.getHocKy());
                intent.putExtra("NamHoc", selectedHocKy.getNamHoc());
                startActivity(intent);
            }
        });
        if(userData.size() == 0){
            Toast.makeText(getContext(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0, userData);
            lvMonHoc.setAdapter(mAdapter);
        }
        return view;
    }

}
