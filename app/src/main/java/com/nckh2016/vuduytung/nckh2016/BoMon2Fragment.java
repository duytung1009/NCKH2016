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
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoMon2Fragment extends Fragment {

    public BoMon2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bo_mon_2, container, false);
        String maBoMon = getArguments().getString("MaBoMon");
        String tenBoMon = getArguments().getString("TenBoMon");
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        final ArrayList<Object> mListMonHoc = data.getMonHocTheoBoMon(maBoMon);
        AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0);
        mAdapter.addAll(mListMonHoc);
        ListView mMonHoc = (ListView)view.findViewById(R.id.list_view_chonmonhoc);
        mMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectMonHoc) mListMonHoc.get(position)).getMamh());
                intent.putExtra("caller", "BoMonActivity");
                startActivity(intent);
            }
        });
        mMonHoc.setAdapter(mAdapter);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.books);
        String tieuDe = "Bộ môn " + tenBoMon + " - " + mListMonHoc.size() + " môn học";
        txtTieuDe.setText(tieuDe);
        return view;
    }
}
