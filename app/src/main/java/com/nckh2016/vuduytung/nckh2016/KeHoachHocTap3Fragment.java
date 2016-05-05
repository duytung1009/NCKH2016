package com.nckh2016.vuduytung.nckh2016;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHocNhapDiem;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeHoachHocTap3Fragment extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_kehoachhoctap3_fragment";
    public static final String SUB_PREFS_HOCKY = "hocKy";
    public static final String SUB_PREFS_USERHOCKY = "userHocKy";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectHocKy selectedHocKy, userHocKy;
    //các adapter
    AdapterMonHocNhapDiem monHocAdapter;
    //các view
    ListView mListMonHoc;
    Button btnLuuMonHoc;

    public KeHoachHocTap3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap_3, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        btnLuuMonHoc = (Button)view.findViewById(R.id.btnLuuMonHoc);
        final SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        final ArrayList<Object> mArrayList = data.getMonHoc(null, getArguments().getStringArrayList("mamonhoc"));

        if(mArrayList.size() > 1){
            Collections.sort(mArrayList, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    ObjectMonHoc mh1 = (ObjectMonHoc)o1;
                    ObjectMonHoc mh2 = (ObjectMonHoc)o2;
                    return mh1.getMamh().compareTo(mh2.getMamh());
                }
            });
        }

        selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));
        userHocKy = new ObjectHocKy(getArguments().getInt("user_namhoc"), getArguments().getInt("user_hocky"), getArguments().getString("nganh"));
        monHocAdapter = new AdapterMonHocNhapDiem(getActivity(), 0, mArrayList);
        mListMonHoc = (ListView)view.findViewById(R.id.list_view_monhoc_nhapdiem);
        mListMonHoc.setAdapter(monHocAdapter);
        mListMonHoc.setSelection(0);
        btnLuuMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressInsert = new ProgressDialog(getContext());
                progressInsert.setMessage("Đang xử lý");
                progressInsert.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressInsert.setIndeterminate(true);
                progressInsert.setProgressNumberFormat(null);
                progressInsert.setProgressPercentFormat(null);
                progressInsert.show();
                ArrayList<ObjectUserData> values = new ArrayList<ObjectUserData>();
                HashMap<String, Float> diemValue = monHocAdapter.getDiem();
                for (Map.Entry<String, Float> entry : diemValue.entrySet()) {
                    String key = entry.getKey();
                    Float value = entry.getValue();
                    values.add(new ObjectUserData(
                            current_user,
                            key,
                            String.valueOf(userHocKy.getHocKy()),
                            String.valueOf(userHocKy.getNamHoc()),
                            value.toString()
                    ));
                }
                //loại học kỳ phụ
                if(selectedHocKy.getHocKy() == 3 || selectedHocKy.getHocKy() == 4) {
                    insertDiem(data, values);
                } else {
                    //loại học kỳ cuối - 2 điều kiện xử lý cho ngành học 4 năm và ngành học 5 năm (đáng ra phải đưa phần này vào trong database cơ mà lười...)
                    if(((selectedHocKy.getNamHoc() == 5 && selectedHocKy.getHocKy() == 2)) ||
                            ((selectedHocKy.getNganh().equals("701") || selectedHocKy.getNganh().equals("702")) && selectedHocKy.getNamHoc() == 4 && selectedHocKy.getHocKy() == 2)){
                        insertDiem(data, values);
                    } else {
                        double tongDiem = data.tongDiem(current_user);
                        if(tongDiem < 2){
                            int tinChi = 0;
                            for(Object value : mArrayList){
                                tinChi += ((ObjectMonHoc)value).getTinchi();
                            }
                            tinChi += ((KeHoachHocTapActivity)getActivity()).tinChiHocKy;
                            //tạm tắt chức năng hạn chế đăng ký số tín chỉ theo điểm số hiện tại
                            insertDiem(data, values);
                            /*if(tinChi > 14){
                                Toast.makeText(getContext(), "Đăng ký quá giới hạn 14 tín chỉ\nTổng điểm hiện tại: " + String.valueOf(new DecimalFormat("####0.00").format(tongDiem)), Toast.LENGTH_SHORT).show();
                            } else {
                                insertDiem(data, values);
                            }*/
                        } else {
                            insertDiem(data, values);
                        }
                    }
                }
                progressInsert.dismiss();
            }
        });
        imageView.setImageResource(R.drawable.edit);
        txtTieuDe.setText(R.string.txtNhapDiem);
        return view;
    }

    private void insertDiem(SQLiteDataController mData, ArrayList<ObjectUserData> values){
        boolean flag = mData.insertUserData(values);
        if (flag) {
            getActivity().setResult(1);
            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        if(selectedHocKy == null){
            selectedHocKy = new Gson().fromJson(state.getString(SUB_PREFS_HOCKY, null), ObjectHocKy.class);
        }
        if(userHocKy == null){
            userHocKy = new Gson().fromJson(state.getString(SUB_PREFS_USERHOCKY, null), ObjectHocKy.class);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putString(SUB_PREFS_HOCKY, new Gson().toJson(selectedHocKy));
        editor.putString(SUB_PREFS_USERHOCKY, new Gson().toJson(userHocKy));
        editor.apply();
    }
}
