package com.nckh2016.vuduytung.nckh2016;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterXoaMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUserHocKy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuanLyKeHoachHocTap4Fragment extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_quanlykehoachhoctap4_fragment";
    //các giá trị global
    private static final String HOCKY = "hocky";
    private static final String DANHSACHMONHOC = "danhsachmonhoc";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUserHocKy userData;
    private ObjectHocKy selectedHocKy;
    //các adapter
    AdapterXoaMonHoc monHocAdapter;
    //các asynctask
    MainTask mainTask;
    //các view
    ListView lvMonHoc;
    TextView txtThongBao, txtTieuDe;
    Button btnXoaHocKy;
    ImageView imageView;

    public QuanLyKeHoachHocTap4Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap_4, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        userData = new Gson().fromJson(currentUserData.getString(SUB_PREFS_DATASINHVIEN, null), ObjectUserHocKy.class);
        selectedHocKy = new Gson().fromJson(getArguments().getString(HOCKY), ObjectHocKy.class);
        txtThongBao = (TextView)view.findViewById(R.id.txtThongBao);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.report_card);
        txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        String tieuDe = "Học kỳ " + selectedHocKy.getHocKy() + " năm thứ " + selectedHocKy.getNamHoc();
        txtTieuDe.setText(tieuDe);
        btnXoaHocKy = (Button)view.findViewById(R.id.btnXoaHocKy);
        lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    private class MainTask extends AsyncTask<Void, Long, ArrayList<Object>> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Object> doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return data.getMonHoc(current_user, getArguments().getStringArrayList(DANHSACHMONHOC));
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Object> objects) {
            super.onPostExecute(objects);
            if(objects.size() == 0){
                Utils.switchView(mContext, lvMonHoc, txtThongBao);
            } else {
                Utils.switchView(mContext, txtThongBao, lvMonHoc);
                if(objects.size() > 1){
                    Collections.sort(objects, new Comparator<Object>() {
                        public int compare(Object o1, Object o2) {
                            ObjectMonHoc mh1 = (ObjectMonHoc)o1;
                            ObjectMonHoc mh2 = (ObjectMonHoc)o2;
                            return mh1.getMamh().compareTo(mh2.getMamh());
                        }
                    });
                }
                monHocAdapter = new AdapterXoaMonHoc(getActivity(), 0, current_user);
                monHocAdapter.addAll(objects);
                lvMonHoc.setAdapter(monHocAdapter);
                lvMonHoc.setSelection(0);
            }
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
                                        //((QuanLyKeHoachHocTapActivity) getContext()).loadFragment1();
                                        ((QuanLyKeHoachHocTapActivity) getContext()).refreshFragment1();
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
