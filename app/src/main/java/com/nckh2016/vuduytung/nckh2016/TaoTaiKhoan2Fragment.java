package com.nckh2016.vuduytung.nckh2016;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaoTaiKhoan2Fragment extends Fragment {
    Spinner mSpinnerNamHoc, mSpinnerHocKy;
    ListView mListHocKy;

    public TaoTaiKhoan2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tao_tai_khoan_2, container, false);
        mSpinnerNamHoc = (Spinner)view.findViewById(R.id.spinnerNamHoc);
        mSpinnerHocKy = (Spinner)view.findViewById(R.id.spinnerHocKy);
        mListHocKy = (ListView)view.findViewById(R.id.list_view_hocky);

        ArrayAdapter<String> mNamHocAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[] {"1","2","3","4","5"});
        mSpinnerNamHoc.setAdapter(mNamHocAdapter);
        ArrayAdapter<String> mHocKyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[] {"10","20","30","40"});
        mSpinnerHocKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDataController data = new SQLiteDataController(parent.getContext());
                try {
                    data.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
                ArrayList<Object> mArrayList = data.getChuongTrinhDaoTao("801", Integer.parseInt(mSpinnerNamHoc.getSelectedItem().toString()), Integer.parseInt(parent.getSelectedItem().toString()), 0);
                AdapterMonHoc monHocAdapter = new AdapterMonHoc(getActivity(), 0, mArrayList);
                mListHocKy.setAdapter(monHocAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerHocKy.setAdapter(mHocKyAdapter);

        return view;
    }

}
