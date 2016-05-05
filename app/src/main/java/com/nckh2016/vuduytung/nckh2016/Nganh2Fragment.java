package com.nckh2016.vuduytung.nckh2016;


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

import com.nckh2016.vuduytung.nckh2016.Data.AdapterCTDT;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.Items;
import com.nckh2016.vuduytung.nckh2016.object.ObjectCTDT;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Nganh2Fragment extends Fragment {

    public Nganh2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nganh_2, container, false);
        String maNganh = getArguments().getString("MaNganh");
        String tenNganh = getArguments().getString("TenNganh");
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        final ArrayList<Items> mListCTDT = data.getChuongTrinhDaoTao(maNganh);
        ListView mMonHoc = (ListView)view.findViewById(R.id.list_view_chonmonhoc);
        AdapterCTDT mAdapter = new AdapterCTDT(getContext());

        LinkedHashMap<String ,LinkedHashMap<Integer ,Integer>> mainMap = new LinkedHashMap<>();
        for(Items a : mListCTDT){
            ObjectCTDT value = (ObjectCTDT)a;
            LinkedHashMap<Integer ,Integer> map2 = new LinkedHashMap<>();
            map2.put(value.getHocky(), value.getChuyennganh());
            mainMap.put(String.valueOf(value.getHocky() + "." + value.getChuyennganh()), map2);
        }

        final ArrayList<Items> finalCTDT = new ArrayList<Items>();
        for(Map.Entry<String ,LinkedHashMap<Integer ,Integer>> entry : mainMap.entrySet()){
            String key = entry.getKey();
            LinkedHashMap<Integer ,Integer> value = entry.getValue();
            for(Map.Entry<Integer ,Integer> entry2 : value.entrySet()){
                int hocKy = entry2.getKey();
                int chuyenSau = entry2.getValue();
                if(hocKy > 0){
                    final ArrayList<Items> mListCTDTHocKy = data.getChuongTrinhDaoTao(maNganh, hocKy, chuyenSau);
                    finalCTDT.add(new ObjectHocKy2(maNganh, hocKy, chuyenSau, data.getTenChuyenSau(maNganh, chuyenSau)));
                    for(Items a : mListCTDTHocKy){
                        ObjectCTDT valueHocKy = (ObjectCTDT)a;
                        finalCTDT.add(valueHocKy);
                    }
                }
            }
        }
        mAdapter.addItem(finalCTDT);
        mMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((NganhActivity) getActivity()).loadFragment3((ObjectCTDT) finalCTDT.get(position));
            }
        });
        mMonHoc.setAdapter(mAdapter);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.books);
        String tieuDe = "Chuyên ngành " + tenNganh;
        txtTieuDe.setText(tieuDe);
        return view;
    }

}
