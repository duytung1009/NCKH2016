package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 20/3/2016.
 */
public class AdapterUser extends ArrayAdapter<Object> {
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<Object>();

    public AdapterUser(Context mContext, int resource) {
        super(mContext, resource);
        this.mContext = mContext;
    }

    @Override
    public void addAll(Collection<?> collection) {
        this.objects = (ArrayList)collection;
        super.addAll(collection);
    }

    @Override
    public void remove(Object object) {
        this.objects.remove(object);
        super.remove(object);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(objects.size()!=0){
            final Object mObject = objects.get(position);
            if(mObject != null) {
                final ObjectUser mUser = (ObjectUser) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
                TextView txtHoTen = (TextView) view.findViewById(R.id.txtHoTen);
                TextView txtMaSinhVien = (TextView) view.findViewById(R.id.txtMaSinhVien);
                Button btnXoa = (Button) view.findViewById(R.id.btnXoa);
                txtHoTen.setText(mUser.getHoten());
                txtMaSinhVien.setText(mUser.getMasv());
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Xóa hồ sơ")
                                .setMessage("Xóa hồ sơ có mã sinh viên " + mUser.getMasv() + "?")
                                .setIcon(R.drawable.error)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new MainTask(mContext).execute(mObject);
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
        return view;
    }

    private class MainTask extends AsyncTask<Object, Long, Integer>{
        private Context mContext;
        private Object mUser;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Object... params) {
            mUser = params[0];
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return data.deleteUser(((ObjectUser) mUser).getMasv());
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer aInt) {
            super.onPostExecute(aInt);
            if(aInt != -1){
                remove(mUser);
            }
        }
    }
}
