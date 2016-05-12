package com.nckh2016.vuduytung.nckh2016.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 5/5/2016.
 * optimized
 */
public class AdapterXoaMonHoc  extends ArrayAdapter<Object> {
    private String current_user = null;
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<>();
    private ProgressDialog progressDelete;

    public AdapterXoaMonHoc(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        final SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        this.objects.add(object);
    }

    @Override
    public void addAll(Collection<?> collection) {
        super.addAll(collection);
        this.objects = (ArrayList) collection;
    }

    @Override
    public void remove(Object object) {
        this.objects.remove(object);
        super.remove(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (objects.size() != 0) {
            Object mObject = objects.get(position);
            if (mObject != null) {
                final ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc_delete, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.position = position;
                holder.itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
                holder.txtMaMonHoc = (TextView) view.findViewById(R.id.txtMaMonHoc);
                holder.txtTenMonHoc = (TextView) view.findViewById(R.id.txtTenMonHoc);
                holder.txtSoTinChi = (TextView) view.findViewById(R.id.txtSoTinChi);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView);
                holder.btnXoa = (Button)view.findViewById(R.id.btnXoa);
                //
                holder.txtMaMonHoc.setText(mMonHoc.getMamh());
                holder.txtTenMonHoc.setText(mMonHoc.getTenmh());
                String soTinChi = "...";
                if (mMonHoc.getTinchi() != null) {
                    soTinChi = String.valueOf(mMonHoc.getTinchi());
                }
                holder.txtSoTinChi.setText(soTinChi);
                progressDelete = new ProgressDialog(mContext);
                progressDelete.setMessage("Xóa môn học");
                progressDelete.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDelete.setIndeterminate(true);
                progressDelete.setProgressNumberFormat(null);
                progressDelete.setProgressPercentFormat(null);
                holder.btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MainTask(mContext).execute(mMonHoc);
                    }
                });
                new AsyncTask<ViewHolder, Void, Void>() {
                    private ViewHolder viewHolder;
                    private int checkTuChon = 0;

                    @Override
                    protected Void doInBackground(ViewHolder... params) {
                        viewHolder = params[0];
                        SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                        try {
                            data.isCreatedDatabase();
                        } catch (IOException e) {
                            Log.e("tag", e.getMessage());
                        }
                        if(current_user != null){
                            ObjectUser user = data.getUser(current_user);
                            if(data.checkTuChon(current_user, mMonHoc.getMamh(), user.getManganh(),-3)){
                                checkTuChon = 1;
                            } else if (data.checkTuChon(current_user, mMonHoc.getMamh(), user.getMakhoa() + "0", -2)){
                                checkTuChon = 2;
                            } else if (data.checkTuChon(current_user, mMonHoc.getMamh(), "1", -1)){
                                checkTuChon = 3;
                            } else if (data.checkHocPhanTheDuc(mMonHoc.getMamh())){
                                checkTuChon = 4;
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        switch (checkTuChon){
                            case 0:{
                                viewHolder.imageView.setImageResource(R.drawable.literature);
                                break;
                            }
                            case 1:{
                                viewHolder.imageView.setImageResource(R.drawable.tuchon_a);
                                break;
                            }
                            case 2:{
                                viewHolder.imageView.setImageResource(R.drawable.tuchon_b);
                                break;
                            }
                            case 3:{
                                viewHolder.imageView.setImageResource(R.drawable.tuchon_c);
                                break;
                            }
                            case 4:{
                                viewHolder.imageView.setImageResource(R.drawable.sport);
                                break;
                            }
                        }
                    }
                }.execute(holder);
            }
        }
        return view;
    }

    //design patten: ViewHolder
    static class ViewHolder {
        RelativeLayout itemLayout;
        TextView txtMaMonHoc;
        TextView txtTenMonHoc;
        TextView txtSoTinChi;
        ImageView imageView;
        Button btnXoa;
        int position;

        public ViewHolder(){
            //event cho vào đây
        }
    }

    private class MainTask extends AsyncTask<ObjectMonHoc, Long, Integer> {
        private Context mContext;
        private ObjectMonHoc mMonHoc;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDelete.show();
        }

        @Override
        protected Integer doInBackground(ObjectMonHoc... params) {
            mMonHoc = params[0];
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return data.deleteMonHoc(current_user, mMonHoc.getMamh());
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDelete.dismiss();
            if(integer != -1){
                remove(mMonHoc);
            }
        }
    }
}
