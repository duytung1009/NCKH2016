package com.nckh2016.vuduytung.nckh2016;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.Items;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectCTDT;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectNganh;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Nganh3Fragment extends Fragment {


    public Nganh3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nganh_3, container, false);
        String maNganh = getArguments().getString("MaNganh");
        Integer hocKy = getArguments().getInt("HocKy");
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        switch (hocKy){
            case -3:
                //vẫn dùng mã ngành
                break;
            case -2:
                //dùng mã khoa + số 0
                maNganh = ((ObjectNganh)data.getNganh(maNganh).get(0)).getMakhoa() + "0";
                break;
            case -1:
                //mã toàn trường - 1
                maNganh = "1";
                break;
        }
        final ArrayList<Items> mListCTDT = data.getChuongTrinhDaoTao(maNganh, hocKy, 0);
        ArrayList<String> mListMaMonHoc = new ArrayList<String>();
        for (Object object : mListCTDT) {
            ObjectCTDT value = (ObjectCTDT) object;
            mListMaMonHoc.add(value != null ? value.getMamh() : null);
        }
        final ArrayList<Object> mListMonHoc = data.getMonHoc(mListMaMonHoc);
        AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0);
        mAdapter.addAll(mListMonHoc);
        ListView mMonHoc = (ListView)view.findViewById(R.id.list_view_chonmonhoc_2);
        mMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectCTDT) mListCTDT.get(position)).getMamh());
                intent.putExtra("caller", "BoMonActivity");
                startActivity(intent);
            }
        });
        mMonHoc.setAdapter(mAdapter);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        switch (hocKy){
            case -3:
                txtTieuDe.setText("Tự chọn A");
                break;
            case -2:
                txtTieuDe.setText("Tự chọn B");
                break;
            case -1:
                txtTieuDe.setText("Tự chọn C");
                break;
        }
        return view;
    }

}
