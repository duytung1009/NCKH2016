package com.nckh2016.vuduytung.nckh2016;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc2;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeHoachHocTap2Fragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    public boolean checkAll = false;
    ObjectHocKy selectedHocKy;
    AdapterMonHoc2 monHocAdapter;
    ListView mListHocKy;
    Button btnThemMonHoc;
    CheckBox selectAllCheckBox;

    public KeHoachHocTap2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap_2, container, false);
        // Restore preferences
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        mListHocKy = (ListView)view.findViewById(R.id.list_view_chonmonhoc);
        btnThemMonHoc = (Button)view.findViewById(R.id.btnThemMonHoc);
        selectAllCheckBox = (CheckBox)view.findViewById(R.id.selectAllCheckBox);
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        int maHocKy = 0, chuyenSau = 0;
        switch(selectedHocKy.getNamHoc()){
            case 0:
                maHocKy = selectedHocKy.getHocKy();
                break;
            case 1:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = 1;
                        break;
                    case 2:
                        maHocKy = 2;
                        break;
                    default:
                        maHocKy = 0;
                        break;
                }
                break;
            case 2:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = 3;
                        break;
                    case 2:
                        maHocKy = 4;
                        break;
                    default:
                        maHocKy = 0;
                        break;
                }
                break;
            case 3:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = 5;
                        break;
                    case 2:
                        maHocKy = 6;
                        break;
                    default:
                        maHocKy = 0;
                        break;
                }
                break;
            case 4:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = 7;
                        break;
                    case 2:
                        maHocKy = 8;
                        break;
                    default:
                        maHocKy = 0;
                        break;
                }
                break;
            case 5:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = 9;
                        break;
                    case 2:
                        maHocKy = 10;
                        break;
                    default:
                        maHocKy = 0;
                        break;
                }
                break;
        }
        ArrayList<Object> mArrayList = data.getChuongTrinhDaoTao(selectedHocKy.getNganh(), selectedHocKy.getNamHoc(), maHocKy, chuyenSau);
        if(mArrayList.size() == 0){
            chuyenSau = Integer.parseInt(data.getUser(current_user).getMachuyensau());
            mArrayList = data.getChuongTrinhDaoTao(selectedHocKy.getNganh(), selectedHocKy.getNamHoc(), maHocKy, chuyenSau);
        }
        ArrayList<Object> mMonHocChuaQua = data.getMonHocChuaQua(current_user, mArrayList);
        monHocAdapter = new AdapterMonHoc2(this, 0, mMonHocChuaQua, current_user);
        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkAll = true;
                    monHocAdapter.notifyDataSetChanged();
                } else {
                    checkAll = false;
                    monHocAdapter.removeAll();
                    monHocAdapter.notifyDataSetChanged();
                }
            }
        });
        switch (selectedHocKy.getHocKy()){
            case -3:
                imageView.setImageResource(R.drawable.tuchon_a);
                txtTieuDe.setText("Tự chọn A - " + monHocAdapter.getCount() + " môn học");
                break;
            case -2:
                imageView.setImageResource(R.drawable.tuchon_b);
                txtTieuDe.setText("Tự chọn B - " + monHocAdapter.getCount() + " môn học");
                break;
            case -1:
                imageView.setImageResource(R.drawable.tuchon_c);
                txtTieuDe.setText("Tự chọn C - " + monHocAdapter.getCount() + " môn học");
                break;
            default:
                imageView.setImageResource(R.drawable.books);
                txtTieuDe.setText("Học kỳ " + maHocKy + " - " + monHocAdapter.getCount() + " môn học");
                break;
        }
        mListHocKy.setAdapter(monHocAdapter);
        btnThemMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedMonHoc = monHocAdapter.getSelectedMonHoc();
                if (selectedMonHoc.size() == 0) {
                    Toast.makeText(getContext(), "Chưa chọn môn học", Toast.LENGTH_SHORT).show();
                } else {
                    ((KeHoachHocTapActivity) getActivity()).loadFragment3(selectedHocKy, selectedMonHoc);
                }
            }
        });
        return view;
    }
}
