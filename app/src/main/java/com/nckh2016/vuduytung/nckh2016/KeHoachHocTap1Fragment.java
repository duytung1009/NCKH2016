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
import android.widget.ImageView;
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

    public KeHoachHocTap1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ke_hoach_hoc_tap, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        final ObjectUser cUser = data.getUser(current_user);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        TextView txtTenNganh = (TextView)view.findViewById(R.id.txtTieuDe2);
        imageView.setImageResource(R.drawable.books);
        txtTieuDe.setText(getResources().getString(R.string.txtChuyenNganh) + " ");
        txtTenNganh.setText(data.getTenNganh(cUser.getManganh()));
        final ListView listViewHocTap = (ListView)view.findViewById(R.id.list_view_hoctap);
        final AdapterHocKy hocKyAdapter = new AdapterHocKy(getContext());
        int tongNamHoc = 5;
        if(cUser.getManganh() == "701" || cUser.getManganh() == "702"){
            tongNamHoc = 4;
        }
        for(int i=0; i<tongNamHoc; i++){
            //add nam hoc
            hocKyAdapter.addItem(new ObjectHocKy(i+1, 0, cUser.getManganh()));
            //add hoc ky 1
            hocKyAdapter.addItem(new ObjectHocKy(i+1, 1, cUser.getManganh()));
            //add hoc ky 2
            hocKyAdapter.addItem(new ObjectHocKy(i+1, 2, cUser.getManganh()));
        }
        hocKyAdapter.addItem((new ObjectHocKy(0, 0, cUser.getManganh())));
        //tự chọn A - mã chuyên ngành
        hocKyAdapter.addItem((new ObjectHocKy(0, -3, cUser.getManganh())));
        //tự chọn B - mã khoa thêm số 0 đằng sau
        hocKyAdapter.addItem((new ObjectHocKy(0, -2, cUser.getMakhoa() + "0")));
        //tự chọn C - mã 1 (toàn trường)
        hocKyAdapter.addItem((new ObjectHocKy(0, -1, "1")));
        listViewHocTap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (hocKyAdapter.getItem(position).getHocKy() != 0) {
                    ((KeHoachHocTapActivity) getActivity()).loadFragment2(hocKyAdapter.getItem(position));
                }
            }
        });
        listViewHocTap.setAdapter(hocKyAdapter);
        return view;
    }
}
