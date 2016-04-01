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

import com.nckh2016.vuduytung.nckh2016.Data.ObjectKhoa;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectNganh;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class Nganh1Fragment extends Fragment {
    //các biến được khôi phục lại nếu app resume
    private ArrayList<Object> mListKhoa, mListNganh;
    //các view
    Spinner mSpinnerKhoa, mSpinnerNganh;

    public Nganh1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nganh, container, false);
        mSpinnerKhoa = (Spinner)view.findViewById(R.id.spinnerKhoa);
        mSpinnerNganh = (Spinner)view.findViewById(R.id.spinnerNganh);
        loadKhoa();
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
                loadNganh(position);
                List<String> mListTenNganh = new ArrayList<String>();
                for (Object object : mListNganh) {
                    ObjectNganh value = (ObjectNganh) object;
                    mListTenNganh.add(value != null ? value.getTennganh() : null);
                }
                ArrayAdapter<String> mNganhAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListTenNganh);
                mNganhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerNganh.setAdapter(mNganhAdapter);
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
                ((NganhActivity) getActivity()).loadFragment2(((ObjectNganh) mListNganh.get(mSpinnerNganh.getSelectedItemPosition())).getManganh(), ((ObjectNganh) mListNganh.get(mSpinnerNganh.getSelectedItemPosition())).getTennganh());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mListKhoa.isEmpty()){
            loadKhoa();
        }
    }

    private void loadKhoa(){
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        mListKhoa = data.getKhoaCoNganh();
    }

    private void loadNganh(int khoaPosition){
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        mListNganh = data.getNganhTheoKhoa(((ObjectKhoa) mListKhoa.get(khoaPosition)).getMakhoa());
    }
}
