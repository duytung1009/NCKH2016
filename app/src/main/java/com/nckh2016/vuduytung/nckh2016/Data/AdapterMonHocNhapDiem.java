package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.HashMap;

/**
 * Created by Tung on 28/2/2016.
 * optimized
 */
public class AdapterMonHocNhapDiem extends ArrayAdapter<Object> {
    public String current_user = null;
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<Object>();
    private HashMap<String, Float> diemValues = new HashMap<String, Float>();

    public AdapterMonHocNhapDiem(Context context, int resource) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (objects.size() != 0) {
            Object mObject = objects.get(position);
            if(mObject != null){
                final ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc_nhapdiem, parent, false);
                final ViewHolder holder = new ViewHolder();
                holder.position = position;
                holder.itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
                holder.txtMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
                holder.txtTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
                holder.txtSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView);
                holder.edDiem = (EditText) view.findViewById(R.id.edNhapDiem);
                //
                holder.txtMaMonHoc.setText(mMonHoc.getMamh());
                holder.txtTenMonHoc.setText(mMonHoc.getTenmh());
                holder.txtSoTinChi.setText(mMonHoc.getTinchi().toString());
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager lManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        lManager.showSoftInput(holder.edDiem, 0);
                        holder.edDiem.requestFocus();
                    }
                });
                if(mMonHoc.getDiem() != -1){
                    diemValues.put(mMonHoc.getMamh(), (float)mMonHoc.getDiem());
                    holder.edDiem.setText(String.valueOf(mMonHoc.getDiem()));
                } else {
                    if(!diemValues.containsKey(mMonHoc.getMamh())){
                        diemValues.put(mMonHoc.getMamh(), -1f);
                    }
                }
                holder.edDiem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            if(s.length() == 0){
                                diemValues.put(mMonHoc.getMamh(), -1f);
                            } else {
                                float val = Float.parseFloat(s.toString());
                                if(val > 10) {
                                    s.replace(0, s.length(), "10", 0, 2);
                                } else if(val < 0) {
                                    s.replace(0, s.length(), "0", 0, 1);
                                }
                                //The method put will replace the value of an existing key and will create it if doesn't exist.
                                diemValues.put(mMonHoc.getMamh(), Float.parseFloat(s.toString()));
                            }
                        } catch (NumberFormatException ex) {
                            // Do something
                        }
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

    public HashMap<String, Float> getDiem(){
        return diemValues;
    }

    //design patten: ViewHolder
    static class ViewHolder {
        RelativeLayout itemLayout;
        TextView txtMaMonHoc;
        TextView txtTenMonHoc;
        TextView txtSoTinChi;
        ImageView imageView;
        EditText edDiem;
        int position;

        public ViewHolder(){
            //event cho vào đây
        }
    }
}
