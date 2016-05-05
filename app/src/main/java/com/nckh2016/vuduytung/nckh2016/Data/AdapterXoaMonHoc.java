package com.nckh2016.vuduytung.nckh2016.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 5/5/2016.
 */
public class AdapterXoaMonHoc  extends ArrayAdapter<Object> {
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<Object>();
    private String current_user = null;
    private ProgressDialog progressDelete;

    public AdapterXoaMonHoc(Context context, int resource, String masv) {
        super(context, resource);
        this.mContext = context;
        current_user = masv;
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
            final Object mObject = objects.get(position);
            if (mObject != null) {
                ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc_delete, parent, false);
                TextView txtMaMonHoc = (TextView) view.findViewById(R.id.txtMaMonHoc);
                TextView txtTenMonHoc = (TextView) view.findViewById(R.id.txtTenMonHoc);
                TextView txtSoTinChi = (TextView) view.findViewById(R.id.txtSoTinChi);
                Button btnXoa = (Button)view.findViewById(R.id.btnXoa);
                txtMaMonHoc.setText(mMonHoc.getMamh());
                txtTenMonHoc.setText(mMonHoc.getTenmh());
                String soTinChi = "...";
                if (mMonHoc.getTinchi() != null) {
                    soTinChi = String.valueOf(mMonHoc.getTinchi());
                }
                txtSoTinChi.setText(soTinChi);
                progressDelete = new ProgressDialog(mContext);
                progressDelete.setMessage("Xóa môn học");
                progressDelete.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDelete.setIndeterminate(true);
                progressDelete.setProgressNumberFormat(null);
                progressDelete.setProgressPercentFormat(null);
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MainTask(mContext).execute(mObject);
                    }
                });
            }
        }
        return view;
    }

    private class MainTask extends AsyncTask<Object, Long, Integer> {
        private Context mContext;
        private Object mMonHoc;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDelete.show();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            mMonHoc = params[0];
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return data.deleteMonHoc(current_user, ((ObjectMonHoc)mMonHoc).getMamh());
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
