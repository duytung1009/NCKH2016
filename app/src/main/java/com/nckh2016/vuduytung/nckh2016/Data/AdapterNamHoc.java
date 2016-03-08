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
import java.util.ArrayList;

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

    public void addItem(ObjectHocKy item) {
        mData.add(item);
        notifyDataSetChanged();
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
                    for(ObjectHocKy value : mData){
                        if(value.getNamHoc() == i.getNamHoc()){
                            if(value.getHocKy() > max){
                                max = value.getHocKy();
                            }
                        }
                    }
                    if(max >= 4){
                        maxHocKy = true;
                    }
                    final int hocKyMoi = max + 1;
                    final int viTri = position + max + 1;
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

                                SQLiteDataController data = new SQLiteDataController(mContext);
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
                    txtHocKy.setText("Học kỳ " + i.getHocKy());
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
