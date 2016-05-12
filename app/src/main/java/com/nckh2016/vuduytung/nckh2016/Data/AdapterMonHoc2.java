package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.ChiTietMonHocActivity;
import com.nckh2016.vuduytung.nckh2016.KeHoachHocTap2Fragment;
import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 27/2/2016.
 * optimized
 */
public class AdapterMonHoc2 extends ArrayAdapter<Object> {
    public String current_user = null;
    private Context mContext;
    private KeHoachHocTap2Fragment mainFragment;
    private ArrayList<Object> objects = new ArrayList<Object>();
    private ArrayList<String> selectedMonHoc = new ArrayList<String>();

    public AdapterMonHoc2(KeHoachHocTap2Fragment abc, int resource) {
        super(abc.getContext(), resource);
        this.mainFragment = abc;
        this.mContext = abc.getContext();
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
            if (mObject != null) {
                final ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc_2, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.position = position;
                holder.itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
                holder.txtMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
                holder.txtTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
                holder.txtSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView);
                holder.ckChon = (CheckBox) view.findViewById(R.id.checkBox);
                //
                holder.txtMaMonHoc.setText(mMonHoc.getMamh());
                holder.txtTenMonHoc.setText(mMonHoc.getTenmh());
                String soTinChi = "...";
                if (mMonHoc.getTinchi() != null) {
                    soTinChi = String.valueOf(mMonHoc.getTinchi());
                }
                holder.txtSoTinChi.setText(soTinChi);
                //xét điều kiện
                new AsyncTask<ViewHolder, Void, Void>() {
                    private ViewHolder viewHolder;
                    private boolean chuaQua = false;
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
                        String madieukien = mMonHoc.getDieukien();
                        if (madieukien != null) {
                            if (madieukien.length() >= 7) {
                                String[] items = madieukien.split(",");
                                for (String item : items) {
                                    if (data.checkMonHocChuaQua(current_user, item)) {
                                        chuaQua = true;
                                    }
                                }
                            }
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
                        //tạm tắt chức năng xét điều kiện đăng ký môn học, để hủy -> xóa dòng chuaQua = false;
                        //chuaQua = false;
                        if (chuaQua) {
                            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, ChiTietMonHocActivity.class);
                                    intent.putExtra(Utils.MA_MON_HOC, mMonHoc.getMamh());
                                    mContext.startActivity(intent);
                                }
                            });
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                viewHolder.itemLayout.setBackgroundColor(mContext.getColor(R.color.black_overlay_2));
                            } else {
                                viewHolder.itemLayout.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay_2));
                            }
                            viewHolder.ckChon.setEnabled(false);
                        } else {
                            //need more logic...
                            viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewHolder.ckChon.performClick();
                                }
                            });
                            viewHolder.ckChon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        selectedMonHoc.add(mMonHoc.getMamh());
                                    } else {
                                        selectedMonHoc.remove(selectedMonHoc.indexOf(mMonHoc.getMamh()));
                                    }
                                }
                            });
                            viewHolder.ckChon.setChecked(mainFragment.checkAll);
                        }
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
                /*String madieukien = mMonHoc.getDieukien();
                boolean chuaQua = false;
                if (madieukien != null) {
                    if (madieukien.length() >= 7) {
                        SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                        try {
                            data.isCreatedDatabase();
                        } catch (IOException e) {
                            Log.e("tag", e.getMessage());
                        }
                        String[] items = madieukien.split(",");
                        for (String item : items) {
                            if (data.checkMonHocChuaQua(current_user, item)) {
                                chuaQua = true;
                            }
                        }
                    }
                }
                //tạm tắt chức năng xét điều kiện đăng ký môn học, để hủy -> xóa dòng chuaQua = false;
                //chuaQua = false;
                if (chuaQua) {
                    holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ChiTietMonHocActivity.class);
                            intent.putExtra("MaMonHoc", mMonHoc.getMamh());
                            mContext.startActivity(intent);
                        }
                    });
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.itemLayout.setBackgroundColor(mContext.getColor(R.color.black_overlay_2));
                    } else {
                        holder.itemLayout.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay_2));
                    }
                    holder.ckChon.setEnabled(false);
                } else {
                    //need more logic...
                    holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.ckChon.performClick();
                        }
                    });
                    holder.ckChon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                selectedMonHoc.add(mMonHoc.getMamh());
                            } else {
                                selectedMonHoc.remove(selectedMonHoc.indexOf(mMonHoc.getMamh()));
                            }
                        }
                    });
                    holder.ckChon.setChecked(mainFragment.checkAll);
                }*/
            }
        }
        return view;
    }

    public void clearSelected() {
        this.selectedMonHoc.clear();
    }

    public ArrayList<String> getSelectedMonHoc() {
        return selectedMonHoc;
    }

    //design patten: ViewHolder
    static class ViewHolder {
        RelativeLayout itemLayout;
        TextView txtMaMonHoc;
        TextView txtTenMonHoc;
        TextView txtSoTinChi;
        ImageView imageView;
        CheckBox ckChon;
        int position;

        public ViewHolder(){
            //event cho vào đây (đang làm...)
        }
    }
}
