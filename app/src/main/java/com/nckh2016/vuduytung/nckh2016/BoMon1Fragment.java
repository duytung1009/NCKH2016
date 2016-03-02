package com.nckh2016.vuduytung.nckh2016;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectBoMon;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectKhoa;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BoMon1Fragment extends Fragment {
    ArrayList<Object> mListKhoa, mListBoMon;
    Spinner mSpinnerKhoa, mSpinnerBoMon;

    public BoMon1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bo_mon, container, false);
        mSpinnerKhoa = (Spinner)view.findViewById(R.id.spinnerKhoa);
        mSpinnerBoMon = (Spinner)view.findViewById(R.id.spinnerBoMon);
        SQLiteDataController data = new SQLiteDataController(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        mListKhoa = data.getKhoa();
        List<String> mListTenKhoa = new ArrayList<String>();
        for (Object object : mListKhoa) {
            ObjectKhoa value = (ObjectKhoa) object;
            mListTenKhoa.add(value != null ? value.getTenkhoa() : null);
        }
        ArrayAdapter<String> mKhoaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListTenKhoa);
        mKhoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDataController data = new SQLiteDataController(parent.getContext());
                try {
                    data.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
                mListBoMon = data.getBoMonTheoKhoa(((ObjectKhoa) mListKhoa.get(position)).getMakhoa());
                List<String> mListTenBoMon = new ArrayList<String>();
                for (Object object : mListBoMon) {
                    ObjectBoMon value = (ObjectBoMon) object;
                    mListTenBoMon.add(value != null ? value.getTenbomon() : null);
                }
                ArrayAdapter<String> mBoMonAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListTenBoMon);
                mBoMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerBoMon.setAdapter(mBoMonAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerKhoa.setAdapter(mKhoaAdapter);
        Button btnXem = (Button)view.findViewById(R.id.btnXem);
        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BoMonActivity) getActivity()).loadFragment2(((ObjectBoMon)mListBoMon.get(mSpinnerBoMon.getSelectedItemPosition())).getMabomon(), ((ObjectBoMon)mListBoMon.get(mSpinnerBoMon.getSelectedItemPosition())).getTenbomon());
            }
        });
        return view;
    }
}
