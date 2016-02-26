package com.nckh2016.vuduytung.nckh2016;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc2;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeHoachHocTap2Fragment extends Fragment {
    ObjectHocKy selectedHocKy;
    ListView mListHocKy;

    public KeHoachHocTap2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap_2, container, false);
        selectedHocKy = new ObjectHocKy(getArguments().getInt("hocky"), getArguments().getInt("namhoc"), getArguments().getString("nganh"));
        mListHocKy = (ListView)view.findViewById(R.id.list_view_chonmonhoc);
        SQLiteDataController data = new SQLiteDataController(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        String maHocKy = "0";
        switch(selectedHocKy.getNamHoc()){
            case 1:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = "10";
                        break;
                    case 2:
                        maHocKy = "20";
                        break;
                    default:
                        maHocKy = "0";
                        break;
                }
                break;
            case 2:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = "30";
                        break;
                    case 2:
                        maHocKy = "40";
                        break;
                    default:
                        maHocKy = "0";
                        break;
                }
                break;
            case 3:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = "50";
                        break;
                    case 2:
                        maHocKy = "60";
                        break;
                    default:
                        maHocKy = "0";
                        break;
                }
                break;
            case 4:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = "70";
                        break;
                    case 2:
                        maHocKy = "80";
                        break;
                    default:
                        maHocKy = "0";
                        break;
                }
                break;
            case 5:
                switch (selectedHocKy.getHocKy()){
                    case 1:
                        maHocKy = "90";
                        break;
                    case 2:
                        maHocKy = "100";
                        break;
                    default:
                        maHocKy = "0";
                        break;
                }
                break;
        }
        ArrayList<Object> mArrayList = data.getCTDT(selectedHocKy.getNganh(), maHocKy);
        AdapterMonHoc2 monHocAdapter = new AdapterMonHoc2(getActivity(), 0, mArrayList);
        mListHocKy.setAdapter(monHocAdapter);
        return view;
    }
}
