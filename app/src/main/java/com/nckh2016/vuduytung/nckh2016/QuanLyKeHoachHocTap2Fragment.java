package com.nckh2016.vuduytung.nckh2016;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUserHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuanLyKeHoachHocTap2Fragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    ObjectHocKy selectedHocKy;
    ArrayList<Object> userMonHoc;
    AdapterMonHoc mAdapter;
    ListView lvMonHoc;
    TextView txtThongBao;

    public QuanLyKeHoachHocTap2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap_2, container, false);
        final SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));
        final SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        final ObjectUser user = data.getUser(current_user);
        userMonHoc = data.getUserData(current_user, selectedHocKy.getNamHoc(), selectedHocKy.getHocKy());
        final Gson gson = new Gson();
        final ObjectUserHocKy userData = gson.fromJson(currentUserData.getString("user_data", null), ObjectUserHocKy.class);

        txtThongBao = (TextView)view.findViewById(R.id.txtThongBao);
        lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectMonHoc) userMonHoc.get(position)).getMamh());
                intent.putExtra("caller", "QuanLyKeHoachHocTapActivity");
                startActivity(intent);
            }
        });
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.report_card);
        txtTieuDe.setText("Học kỳ " + selectedHocKy.getHocKy() + " năm thứ " + selectedHocKy.getNamHoc());
        Button btnThemMonHoc = (Button)view.findViewById(R.id.btnThemMonHoc);
        btnThemMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KeHoachHocTapActivity.class);
                intent.putExtra("Nganh", data.getTenNganh(user.getManganh()));
                intent.putExtra("ChuyenSau", data.getTenChuyenSau(user.getManganh(), Integer.parseInt(user.getMachuyensau())));
                intent.putExtra("HocKy", selectedHocKy.getHocKy());
                intent.putExtra("NamHoc", selectedHocKy.getNamHoc());
                startActivityForResult(intent, 1);
            }
        });
        Button btnXoaHocKy = (Button)view.findViewById(R.id.btnXoaHocKy);
        btnXoaHocKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                .setTitle("Xóa học kỳ")
                .setMessage("Xóa toàn bộ thông tin của học kỳ này?")
                .setIcon(R.drawable.error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //xóa
                        boolean flag = userData.removeHocKy(selectedHocKy);
                        if (flag) {
                            SharedPreferences.Editor editor = currentUserData.edit();
                            editor.putString("user_data", gson.toJson(userData));
                            editor.commit();
                            SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                            try {
                                data.isCreatedDatabase();
                            } catch (IOException e) {
                                Log.e("tag", e.getMessage());
                            }
                            ContentValues updateValues = new ContentValues();
                            updateValues.put(MyContract.UserEntry.COLUMN_HOC_KY, gson.toJson(userData));
                            data.updateNguoiDung(current_user, updateValues);
                            data.deleteUserData(current_user, selectedHocKy.getHocKy(), selectedHocKy.getNamHoc());
                            ((QuanLyKeHoachHocTapActivity) getContext()).loadPreviousFragment();
                        } else {
                            Toast.makeText(getContext(), "Lỗi! xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        if(userMonHoc.size() == 0){
            lvMonHoc.setVisibility(View.GONE);
        } else {
            txtThongBao.setVisibility(View.GONE);
            mAdapter = new AdapterMonHoc(getContext(), 0);
            mAdapter.addAll(userMonHoc);
            lvMonHoc.setAdapter(mAdapter);
        }
        return view;
    }

    public void refreshView(){
        lvMonHoc.setVisibility(View.VISIBLE);
        txtThongBao.setVisibility(View.GONE);
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        userMonHoc = data.getUserData(current_user, selectedHocKy.getNamHoc(), selectedHocKy.getHocKy());
        mAdapter = new AdapterMonHoc(getContext(), 0);
        mAdapter.addAll(userMonHoc);
        lvMonHoc.setAdapter(mAdapter);
    }
}
