package com.nckh2016.vuduytung.nckh2016;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.Items;
import com.nckh2016.vuduytung.nckh2016.object.ObjectCTDT;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectNganh;

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
        AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0);
        for (Object object : mListCTDT) {
            ObjectCTDT value = (ObjectCTDT) object;
            if(value != null){
                final ObjectMonHoc mListMonHoc = data.getMonHoc(value.getMamh());
                mAdapter.add(mListMonHoc);
            }
        }
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
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        switch (hocKy){
            case -3:
                imageView.setImageResource(R.drawable.tuchon_a);
                txtTieuDe.setText("Tự chọn A");
                break;
            case -2:
                imageView.setImageResource(R.drawable.tuchon_b);
                txtTieuDe.setText("Tự chọn B");
                break;
            case -1:
                imageView.setImageResource(R.drawable.tuchon_c);
                txtTieuDe.setText("Tự chọn C");
                break;
        }
        return view;
    }

}
