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

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc2;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeHoachHocTap2Fragment extends Fragment {
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_kehoachhoctap2_fragment";
    public static final String SUB_PREFS_CHECKALL = "checkAll";
    public static final String SUB_PREFS_HOCKY = "hocKy";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectHocKy selectedHocKy;
    //các biến được khởi tạo lại nếu app resume
    public boolean checkAll = false;
    //các adapter
    AdapterMonHoc2 monHocAdapter;
    //các view
    ListView mListHocKy;
    Button btnThemMonHoc;
    CheckBox selectAllCheckBox;
    TextView txtThongBao;

    public KeHoachHocTap2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap_2, container, false);
        // Restore preferences
        SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        txtThongBao = (TextView)view.findViewById(R.id.txtThongBao);
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
        //nếu học kỳ không có môn học nào -> lọc theo hướng chuyên sâu, dùng mã chuyên sâu của người dùng
        if(mArrayList.size() == 0){
            chuyenSau = Integer.parseInt(data.getUser(current_user).getMachuyensau());
            if(chuyenSau == 0){
                txtThongBao.setVisibility(View.VISIBLE);
            } else {
                mArrayList = data.getChuongTrinhDaoTao(selectedHocKy.getNganh(), selectedHocKy.getNamHoc(), maHocKy, chuyenSau);
            }
        }
        ArrayList<Object> mMonHocCaiThien = data.getMonHocCaiThien(current_user, mArrayList);

        if(mMonHocCaiThien.size() > 1){
            Collections.sort(mMonHocCaiThien, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    ObjectMonHoc mh1 = (ObjectMonHoc)o1;
                    ObjectMonHoc mh2 = (ObjectMonHoc)o2;
                    return mh1.getMamh().compareTo(mh2.getMamh());
                }
            });
        }

        monHocAdapter = new AdapterMonHoc2(this, 0);
        monHocAdapter.addAll(mMonHocCaiThien);
        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkAll = true;
                    monHocAdapter.notifyDataSetChanged();
                } else {
                    checkAll = false;
                    monHocAdapter.clearSelected();
                    monHocAdapter.notifyDataSetChanged();
                }
            }
        });
        switch (selectedHocKy.getHocKy()){
            case -3:
            {
                imageView.setImageResource(R.drawable.tuchon_a);
                String tieuDe = "Tự chọn A - " + monHocAdapter.getCount() + " môn học";
                txtTieuDe.setText(tieuDe);
                break;
            }
            case -2:
            {
                imageView.setImageResource(R.drawable.tuchon_b);
                String tieuDe = "Tự chọn B - " + monHocAdapter.getCount() + " môn học";
                txtTieuDe.setText(tieuDe);
                break;
            }
            case -1:
            {
                imageView.setImageResource(R.drawable.tuchon_c);
                String tieuDe = "Tự chọn C - " + monHocAdapter.getCount() + " môn học";
                txtTieuDe.setText(tieuDe);
                break;
            }
            default:
            {
                imageView.setImageResource(R.drawable.books);
                String tieuDe = "Học kỳ " + maHocKy + " - " + monHocAdapter.getCount() + " môn học";
                txtTieuDe.setText(tieuDe);
                break;
            }
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

    @Override
    public void onResume() {
        super.onResume();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        }
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        //checkAll = state.getBoolean(SUB_PREFS_CHECKALL, checkAll);
        if(selectedHocKy == null){
            selectedHocKy = new Gson().fromJson(state.getString(SUB_PREFS_HOCKY, null), ObjectHocKy.class);
        }
        //unchecked all checkbox
        selectAllCheckBox.setChecked(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        //editor.putBoolean(SUB_PREFS_CHECKALL, checkAll);
        editor.putString(SUB_PREFS_HOCKY, new Gson().toJson(selectedHocKy));
        editor.apply();
    }
}
