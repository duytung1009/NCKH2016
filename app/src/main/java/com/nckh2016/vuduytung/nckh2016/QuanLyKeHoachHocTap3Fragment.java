package com.nckh2016.vuduytung.nckh2016;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHocNhapDiem;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuanLyKeHoachHocTap3Fragment extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_quanlykehoachhoctap3_fragment";
    //các giá trị global
    private static final String HOCKY = "hocky";
    private static final String DANHSACHMONHOC = "danhsachmonhoc";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectHocKy selectedHocKy;
    //các adapter
    AdapterMonHocNhapDiem monHocAdapter;
    //các asynctask
    MainTask mainTask;
    //các view
    ListView lvMonHoc;
    TextView txtThongBao, txtTieuDe;
    Button btnCapNhatMonHoc;
    ImageView imageView;



    public QuanLyKeHoachHocTap3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap_3, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        selectedHocKy = new Gson().fromJson(getArguments().getString(HOCKY), ObjectHocKy.class);

        txtThongBao = (TextView)view.findViewById(R.id.txtThongBao);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.report_card);
        txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        String tieuDe = "Học kỳ " + selectedHocKy.getHocKy() + " năm thứ " + selectedHocKy.getNamHoc();
        txtTieuDe.setText(tieuDe);
        btnCapNhatMonHoc = (Button)view.findViewById(R.id.btnCapNhatMonHoc);
        btnCapNhatMonHoc.setVisibility(View.GONE);
        lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        return view;
    }

    private void updateDiem(ArrayList<ObjectUserData> values){
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        boolean flag = data.updateUserData(values);
        if (flag) {
            ((QuanLyKeHoachHocTapActivity)getActivity()).loadFragment2(selectedHocKy);
        } else {
            Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
        }
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
        protected void onPostExecute(ArrayList<Object> objects) {
            super.onPostExecute(objects);
            if(objects.size() == 0){
                Utils.switchView(mContext, lvMonHoc, txtThongBao);
                btnCapNhatMonHoc.setVisibility(View.GONE);
            } else {
                Utils.switchView(mContext, txtThongBao, lvMonHoc);
                btnCapNhatMonHoc.setVisibility(View.VISIBLE);
                if(objects.size() > 1){
                    Collections.sort(objects, new Comparator<Object>() {
                        public int compare(Object o1, Object o2) {
                            ObjectMonHoc mh1 = (ObjectMonHoc)o1;
                            ObjectMonHoc mh2 = (ObjectMonHoc)o2;
                            return mh1.getMamh().compareTo(mh2.getMamh());
                        }
                    });
                }
                monHocAdapter = new AdapterMonHocNhapDiem(getActivity(), 0, objects);
                lvMonHoc.setAdapter(monHocAdapter);
                lvMonHoc.setSelection(0);
                btnCapNhatMonHoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog progressInsert = new ProgressDialog(getContext());
                        progressInsert.setMessage("Đang xử lý");
                        progressInsert.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressInsert.setIndeterminate(true);
                        progressInsert.setProgressNumberFormat(null);
                        progressInsert.setProgressPercentFormat(null);
                        progressInsert.show();
                        ArrayList<ObjectUserData> values = new ArrayList<ObjectUserData>();
                        HashMap<String, Float> diemValue = monHocAdapter.getDiem();
                        for (Map.Entry<String, Float> entry : diemValue.entrySet()) {
                            String key = entry.getKey();
                            Float value = entry.getValue();
                            values.add(new ObjectUserData(
                                    current_user,
                                    key,
                                    String.valueOf(selectedHocKy.getHocKy()),
                                    String.valueOf(selectedHocKy.getNamHoc()),
                                    value.toString()
                            ));
                        }
                        updateDiem(values);
                        progressInsert.dismiss();
                    }
                });
            }
        }
    }
}
