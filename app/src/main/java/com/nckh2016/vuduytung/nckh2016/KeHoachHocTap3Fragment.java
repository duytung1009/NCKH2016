package com.nckh2016.vuduytung.nckh2016;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHocNhapDiem;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeHoachHocTap3Fragment extends Fragment {
    AdapterMonHocNhapDiem monHocAdapter;
    ListView mListMonHoc;

    public KeHoachHocTap3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap_3, container, false);
        SQLiteDataController data = new SQLiteDataController(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        ArrayList<Object> mArrayList = data.getMonHoc(getArguments().getStringArrayList("mamonhoc"));
        monHocAdapter = new AdapterMonHocNhapDiem(getActivity(), 0, mArrayList);
        mListMonHoc = (ListView)view.findViewById(R.id.list_view_monhoc_nhapdiem);
        mListMonHoc.setAdapter(monHocAdapter);
        return view;
    }

}
