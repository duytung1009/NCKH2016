package com.nckh2016.vuduytung.nckh2016;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUserHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuanLyKeHoachHocTap2Fragment extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_quanlykehoachhoctap2_fragment";
    //các giá trị global
    private static final String HOCKY = "hocky";
    private static final String MONHOC = "MaMonHoc";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUserHocKy userData;
    private ObjectHocKy selectedHocKy;
    private ArrayList<Object> userMonHoc;
    //các adapter
    AdapterMonHoc mAdapter;
    //các asynctask
    MainTask mainTask;
    //các view
    ListView lvMonHoc;
    TextView txtThongBao, txtTieuDe, txtTongTinChi;
    Button btnThemMonHoc, btnSuaHocKy, btnXoaHocKy;
    ImageView imageView;

    public QuanLyKeHoachHocTap2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap_2, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        userData = new Gson().fromJson(currentUserData.getString(SUB_PREFS_DATASINHVIEN, null), ObjectUserHocKy.class);
        selectedHocKy = new Gson().fromJson(getArguments().getString(HOCKY), ObjectHocKy.class);
        //selectedHocKy = new ObjectHocKy(getArguments().getInt("namhoc"), getArguments().getInt("hocky"), getArguments().getString("nganh"));

        txtThongBao = (TextView)view.findViewById(R.id.txtThongBao);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.report_card);
        txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        txtTongTinChi = (TextView)view.findViewById(R.id.txtTongTinChi);
        String tieuDe = "Học kỳ " + selectedHocKy.getHocKy() + " năm thứ " + selectedHocKy.getNamHoc();
        txtTieuDe.setText(tieuDe);
        btnThemMonHoc = (Button)view.findViewById(R.id.btnThemMonHoc);
        btnSuaHocKy = (Button)view.findViewById(R.id.btnSuaHocKy);
        btnXoaHocKy = (Button)view.findViewById(R.id.btnXoaHocKy);
        lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra(MONHOC, ((ObjectMonHoc) userMonHoc.get(position)).getMamh());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    public void refreshView(){
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    private class MainTask extends AsyncTask<Void, Long, Void>{
        private Context mContext;
        private String tenNganh, tenChuyenSau;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try{
                data.isCreatedDatabase();
            }
            catch (IOException e){
                Log.e("tag", e.getMessage());
            }
            userMonHoc = data.getUserData(current_user, selectedHocKy.getNamHoc(), selectedHocKy.getHocKy());
            ObjectUser user = data.getUser(current_user);
            tenNganh = data.getTenNganh(user.getManganh());
            tenChuyenSau = data.getTenChuyenSau(user.getManganh(), Integer.parseInt(user.getMachuyensau()));
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(userMonHoc.size() == 0){
                Utils.switchView(mContext, lvMonHoc, txtThongBao);
            } else {
                Utils.switchView(mContext, txtThongBao, lvMonHoc);

                if(userMonHoc.size() > 1){
                    Collections.sort(userMonHoc, new Comparator<Object>() {
                        public int compare(Object o1, Object o2) {
                            ObjectMonHoc mh1 = (ObjectMonHoc)o1;
                            ObjectMonHoc mh2 = (ObjectMonHoc)o2;
                            return mh1.getMamh().compareTo(mh2.getMamh());
                        }
                    });
                }

                mAdapter = new AdapterMonHoc(getContext(), 0);
                mAdapter.addAll(userMonHoc);
                lvMonHoc.setAdapter(mAdapter);
            }
            int tongTC = 0;
            for(Object value : userMonHoc){
                String[] danhSachBoQua = new String[] {"4010701","4010702","4010703","4010704","4010705","4080508","4080509"};
                List<String> listBoQua = Arrays.asList(danhSachBoQua);
                if(!listBoQua.contains(((ObjectMonHoc)value).getMamh()) && ((ObjectMonHoc)value).getDiem() != -1f) {
                    tongTC += ((ObjectMonHoc)value).getTinchi();
                }
            }
            String tongTinChi = String.valueOf(tongTC);
            final int finalTongTC = tongTC;
            txtTongTinChi.setText(tongTinChi);
            btnThemMonHoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), KeHoachHocTapActivity.class);
                    intent.putExtra("Nganh", tenNganh);
                    intent.putExtra("ChuyenSau", tenChuyenSau);
                    intent.putExtra("HocKy", selectedHocKy.getHocKy());
                    intent.putExtra("NamHoc", selectedHocKy.getNamHoc());
                    intent.putExtra("TinChi", finalTongTC);
                    startActivityForResult(intent, 1);
                }
            });
            btnSuaHocKy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> danhSachMonHoc = new ArrayList<String>();
                    for(Object value : userMonHoc){
                        danhSachMonHoc.add(((ObjectMonHoc)value).getMamh());
                    }
                    ((QuanLyKeHoachHocTapActivity)mContext).loadFragment3(selectedHocKy, danhSachMonHoc);
                }
            });
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
                                        SharedPreferences currentUserData = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = currentUserData.edit();
                                        editor.putString(SUB_PREFS_DATASINHVIEN, new Gson().toJson(userData));
                                        editor.apply();
                                        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                                        try {
                                            data.isCreatedDatabase();
                                        } catch (IOException e) {
                                            Log.e("tag", e.getMessage());
                                        }
                                        ContentValues updateValues = new ContentValues();
                                        updateValues.put(MyContract.UserEntry.COLUMN_HOC_KY, new Gson().toJson(userData));
                                        data.updateNguoiDung(current_user, updateValues);
                                        data.deleteUserData(current_user, selectedHocKy.getHocKy(), selectedHocKy.getNamHoc());
                                        ((QuanLyKeHoachHocTapActivity) getContext()).loadFragment1();
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
        }
    }
}
