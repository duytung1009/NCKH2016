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
import android.widget.ListView;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHocNhapDiem;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUserData;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeHoachHocTap3Fragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    ObjectHocKy selectedHocKy, userHocKy;
    AdapterMonHocNhapDiem monHocAdapter;
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
        current_user = currentUserData.getString("user_mssv", null);
        btnLuuMonHoc = (Button)view.findViewById(R.id.btnLuuMonHoc);
        final SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        ArrayList<Object> mArrayList = data.getMonHoc(getArguments().getStringArrayList("mamonhoc"));
        selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));
        userHocKy = new ObjectHocKy(getArguments().getInt("user_namhoc"), getArguments().getInt("user_hocky"), getArguments().getString("nganh"));
        monHocAdapter = new AdapterMonHocNhapDiem(getActivity(), 0, mArrayList);
        mListMonHoc = (ListView)view.findViewById(R.id.list_view_monhoc_nhapdiem);
        mListMonHoc.setAdapter(monHocAdapter);
        mListMonHoc.setSelection(0);
        btnLuuMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                boolean flag = data.insertUserData(values);
                if (flag) {
                    getActivity().setResult(1);
                    ((KeHoachHocTapActivity)getActivity()).finish();
                } else {
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}
