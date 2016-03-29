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

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterNamHoc;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUserHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuanLyKeHoachHocTapFragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    MainTask mainTask;
    public ObjectUserHocKy user_data;
    public ObjectUser cUser;
    AdapterNamHoc hocTapAdapter;
    ListView listViewHocTap;
    //ArrayList<ObjectHocKy> listHocKy = new ArrayList<ObjectHocKy>();

    public QuanLyKeHoachHocTapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_ke_hoach_hoc_tap, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        user_data = new Gson().fromJson(currentUserData.getString("user_data", null), ObjectUserHocKy.class);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.report_card);
        txtTieuDe.setText(R.string.txtKeHoachHocTap);
        hocTapAdapter = new AdapterNamHoc(getContext());
        listViewHocTap = (ListView)view.findViewById(R.id.listview_user_data);
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
    public void onStop() {
        super.onStop();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    public void refreshView(){
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        user_data = new Gson().fromJson(currentUserData.getString("user_data", null), ObjectUserHocKy.class);
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
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
            if(cUser != null){
                hocTapAdapter.removeAll();
                //listHocKy.clear();
                for(int i=0; i<Integer.parseInt(cUser.getNamhoc()); i++){
                    hocTapAdapter.addItem(new ObjectHocKy(i+1, 0, cUser.getManganh()));
                    //listHocKy.add(new ObjectHocKy(i + 1, 0, cUser.getManganh()));
                    if(user_data != null){
                        for(ObjectHocKy value : user_data.getUserData()){
                            if(value.getNamHoc() == i+1){
                                if(value.getHocKy() != 0){
                                    //listHocKy.add(new ObjectHocKy(i + 1, value.getHocKy(), cUser.getManganh(), data.getDiemHocKy(cUser.getMasv(), value.getHocKy(), value.getNamHoc())));
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
            /*if(cUser != null){
                hocTapAdapter.removeAll();
                hocTapAdapter.addAll(listHocKy);
                hocTapAdapter.sort();
            }*/
            hocTapAdapter.notifyDataSetChanged();
        }
    }
}
