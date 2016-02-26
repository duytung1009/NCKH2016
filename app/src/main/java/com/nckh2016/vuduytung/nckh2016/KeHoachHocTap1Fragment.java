package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class KeHoachHocTap1Fragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    ListView listViewHocTap;
    AdapterHocKy hocKyAdapter;

    public KeHoachHocTap1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(currentUserData == null){
            //form dang ky
        } else{
            current_user = currentUserData.getString("user_mssv", null);
        }
        SQLiteDataController data = new SQLiteDataController(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        final ObjectUser cUser = (ObjectUser)data.getUser(current_user).get(0);
        TextView txtTenNganh = (TextView)view.findViewById(R.id.txtTenNganh);
        txtTenNganh.setText(data.getTenNganh(cUser.getManganh()));
        listViewHocTap = (ListView)view.findViewById(R.id.list_view_hoctap);
        hocKyAdapter = new AdapterHocKy(getContext());
        for(int i=0; i<Integer.parseInt(cUser.getNamhoc()); i++){
            hocKyAdapter.addItem(new ObjectHocKy(i+1, 0, cUser.getManganh()));
            if(i+1 != Integer.parseInt(cUser.getNamhoc())){
                for(int j=0; j<4; j++){
                    hocKyAdapter.addItem(new ObjectHocKy(i+1, j+1, cUser.getManganh()));
                }
            } else{
                for(int j=0; j<Integer.parseInt(cUser.getKyhoc()); j++){
                    hocKyAdapter.addItem(new ObjectHocKy(i+1, j+1, cUser.getManganh()));
                }
            }
        }
        listViewHocTap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(hocKyAdapter.getItem(position).getHocKy() != 0){
                    //start new intent...
                    ((KeHoachHocTapActivity)getActivity()).loadFragment2(hocKyAdapter.getItem(position));
                }
            }
        });
        listViewHocTap.setAdapter(hocKyAdapter);
        return view;
    }
}
