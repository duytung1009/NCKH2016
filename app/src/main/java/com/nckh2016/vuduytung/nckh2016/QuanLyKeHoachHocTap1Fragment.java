package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterNamHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUserHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuanLyKeHoachHocTap1Fragment extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_quanlykehoachhoctap1_fragment";
    public static final String SUB_PREFS_USER = "user";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUserHocKy user_hocky;
    private ObjectUser cUser;
    //các asynctask
    MainTask mainTask;
    //các adapter
    AdapterNamHoc hocTapAdapter;
    //các view
    ListView listViewHocTap;
    CircularProgressView progressBar;

    public QuanLyKeHoachHocTap1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        //user_hocky = new Gson().fromJson(getArguments().getString(USER_HOCKY), ObjectUserHocKy.class);
        //user_hocky = new Gson().fromJson(currentUserData.getString(SUB_PREFS_DATASINHVIEN, null), ObjectUserHocKy.class);
        progressBar = (CircularProgressView)view.findViewById(R.id.progressBar);
        listViewHocTap = (ListView)view.findViewById(R.id.listview_user_data);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.report_card);
        txtTieuDe.setText(R.string.txtKeHoachHocTap);
        hocTapAdapter = new AdapterNamHoc(getContext());
        listViewHocTap.setAdapter(hocTapAdapter);
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
        if(user_hocky == null){
            //user_hocky = new Gson().fromJson(getArguments().getString(USER_HOCKY), ObjectUserHocKy.class);
            user_hocky = new Gson().fromJson(currentUserData.getString(SUB_PREFS_DATASINHVIEN, null), ObjectUserHocKy.class);
        }
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        if(cUser == null){
            cUser = new Gson().fromJson(state.getString(SUB_PREFS_USER, null), ObjectUser.class);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putString(SUB_PREFS_USER, new Gson().toJson(cUser));
        editor.apply();
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

    public void refreshView(){
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }

        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    public class MainTask extends AsyncTask<Void, Long, Void> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProcessBar(getContext(), progressBar, listViewHocTap);
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
            cUser = data.getUser(current_user);
            user_hocky = new Gson().fromJson(cUser.getHocky(), ObjectUserHocKy.class);
            if(cUser != null){
                hocTapAdapter.removeAll();
                for(int i=0; i<Integer.parseInt(cUser.getNamhoc()); i++){
                    hocTapAdapter.addItem(new ObjectHocKy(i+1, 0, cUser.getManganh()));
                    if(user_hocky != null){
                        for(ObjectHocKy value : user_hocky.getUserData()){
                            if(value.getNamHoc() == i+1){
                                if(value.getHocKy() != 0){
                                    hocTapAdapter.addItem(new ObjectHocKy(i + 1, value.getHocKy(), cUser.getManganh(), data.getDiemHocKy(cUser.getMasv(), value.getHocKy(), value.getNamHoc())));
                                }
                            }
                        }
                    }
                }
                hocTapAdapter.sort();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hocTapAdapter.notifyDataSetChanged();
            Utils.hideProcessBar(getContext(), progressBar, listViewHocTap);
        }
    }
}
