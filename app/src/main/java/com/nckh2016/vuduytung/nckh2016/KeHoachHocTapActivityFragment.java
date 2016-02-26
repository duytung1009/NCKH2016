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

import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class KeHoachHocTapActivityFragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;

    public KeHoachHocTapActivityFragment() {
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
        TextView txtTenNganh = (TextView)view.findViewById(R.id.txtTenNganh);
        txtTenNganh.setText(data.getTenNganh("804"));
        ObjectUser cUser = (ObjectUser)data.getUser(current_user).get(0);
        ListView listViewHocTap = (ListView)view.findViewById(R.id.list_view_hoctap);
        listViewHocTap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return view;
    }
}
