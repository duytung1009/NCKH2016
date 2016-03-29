package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.QuanLyKeHoachHocTapActivity;
import com.nckh2016.vuduytung.nckh2016.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tung on 8/3/2016.
 */
public class AdapterNamHoc extends BaseAdapter {
    private static final int LIST_ITEM_TYPE_1 = 0;
    private static final int LIST_ITEM_TYPE_2 = 1;
    private static final int LIST_ITEM_TYPE_COUNT = 2;
    public static final String PREFS_NAME = "current_user";

    private ArrayList<ObjectHocKy> mData = new ArrayList<ObjectHocKy>();
    private LayoutInflater mInflater;
    private Context mContext;

    public AdapterNamHoc(Context mContext) {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
    }

    // sắp xếp lại thứ tự các học kỳ
    public void sort(){
        for(int i = 0; i<mData.size()-1; i++){
            for(int j = i+1; j<mData.size(); j++){
                if(mData.get(i).getNamHoc() == mData.get(j).getNamHoc()){
                    if(mData.get(i).getHocKy() > mData.get(j).getHocKy()){
                        doiCho(i,j);
                    }
                }
            }
        }
        //notifyDataSetChanged();
    }
    public void doiCho(int obj1, int obj2){
        Collections.swap(mData, obj1, obj2);
    }

    public void addItem(ObjectHocKy item) {
        mData.add(item);
        //notifyDataSetChanged();
    }

    public void addAll(ArrayList<ObjectHocKy> items){
        mData.addAll(items);
        //notifyDataSetChanged();
    }

    public void removeAll(){
        mData.clear();
        //notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getHocKy() == 0)
            return LIST_ITEM_TYPE_1;
        else
            return LIST_ITEM_TYPE_2;
    }

    @Override
    public int getViewTypeCount() {
        return LIST_ITEM_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ObjectHocKy getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ObjectHocKy i = mData.get(position);
        if (i != null) {
            int type = getItemViewType(position);
            switch(type) {
                case LIST_ITEM_TYPE_1:
                    view = mInflater.inflate(R.layout.item_namhoc, null);
                    //view.setOnClickListener(null);
                    int max = 0;
                    boolean maxHocKy = false;
                    boolean hk1 = false, hk2 = false, hk3 = false, hk4 = false;
                    //lọc ra học kỳ nào còn trống từ 1->4
                    //đoạn này hại não vãi...
                    for(ObjectHocKy value : mData){
                        if(value.getNamHoc() == i.getNamHoc()){
                            if(value.getHocKy() == 1){
                                hk1 = true;
                            } else if(value.getHocKy() == 2){
                                hk2 = true;
                            } else if(value.getHocKy() == 3){
                                hk3 = true;
                            } else if(value.getHocKy() == 4){
                                hk4 = true;
                            }
                        }
                    }
                    if(!hk1 || !hk2 || !hk3 || !hk4){
                        if(!hk1){
                            max = 1;
                        } else if(!hk2){
                            max = 2;
                        } else if(!hk3){
                            max = 3;
                        } else if(!hk4){
                            max = 4;
                        }
                    } else {
                        maxHocKy = true;
                    }
                    final int hocKyMoi = max;
                    final int viTri = position + max;
                    TextView txtTenNamHoc = (TextView)view.findViewById(R.id.txtNamHoc);
                    txtTenNamHoc.setText("Năm thứ " + i.getNamHoc());
                    Button btnThemHocKy = (Button)view.findViewById(R.id.btnThemHocKy);
                    if(!maxHocKy){
                        btnThemHocKy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ObjectHocKy newHocKy = new ObjectHocKy(i.getNamHoc(), hocKyMoi, i.getNganh());
                                mData.add(viTri, newHocKy);
                                notifyDataSetChanged();
                                SharedPreferences currentUserData = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
                                SharedPreferences.Editor editor = currentUserData.edit();
                                Gson gson = new Gson();
                                ObjectUserHocKy userData = gson.fromJson(currentUserData.getString("user_data", null), ObjectUserHocKy.class);
                                userData.addHocKy(newHocKy);
                                String json = gson.toJson(userData);
                                editor.putString("user_data", json);
                                editor.commit();

                                SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                                try{
                                    data.isCreatedDatabase();
                                }
                                catch (IOException e){
                                    Log.e("tag", e.getMessage());
                                }
                                ContentValues updateValues = new ContentValues();
                                updateValues.put(MyContract.UserEntry.COLUMN_HOC_KY, json);
                                data.updateNguoiDung(currentUserData.getString("user_mssv",null), updateValues);
                            }
                        });
                    }
                    break;
                case LIST_ITEM_TYPE_2:
                    view = mInflater.inflate(R.layout.item_hocky, null);
                    TextView txtHocKy = (TextView)view.findViewById(R.id.txtHocKy);
                    TextView txtDiemHocKy = (TextView)view.findViewById(R.id.txtDiemHocKy);
                    txtHocKy.setText("Học kỳ " + i.getHocKy());
                    if(i.getDiem() != -1){
                        txtDiemHocKy.setText(new DecimalFormat("####0.##").format(i.getDiem()));
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((QuanLyKeHoachHocTapActivity)mContext).loadFragment2(i);
                        }
                    });
                    break;
            }
        }
        return view;
    }
}
