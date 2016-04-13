package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.ChiTietMonHocActivity;
import com.nckh2016.vuduytung.nckh2016.KeHoachHocTap2Fragment;
import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Tung on 27/2/2016.
 */
public class AdapterMonHoc2 extends ArrayAdapter<Object> {
    public String current_user = null;
    private Context context;
    private KeHoachHocTap2Fragment mainFragment;
    private ArrayList<Object> objects;
    private ArrayList<String> selectedMonHoc = new ArrayList<String>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public AdapterMonHoc2(KeHoachHocTap2Fragment abc, int resource, ArrayList<Object> objects, String current_user) {
        super(abc.getContext(), resource, objects);
        this.mainFragment = abc;
        this.context = abc.getContext();
        this.objects = objects;
        this.current_user = current_user;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (objects.size() != 0) {
            Object mObject = objects.get(position);
            if (mObject != null) {
                final ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(context).inflate(R.layout.item_monhoc_2, parent, false);
                RelativeLayout itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
                TextView txMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
                TextView txTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
                TextView txSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);
                final CheckBox ckChon = (CheckBox) view.findViewById(R.id.checkBox);
                txMaMonHoc.setText(mMonHoc.getMamh());
                txTenMonHoc.setText(mMonHoc.getTenmh());
                String soTinChi = "...";
                if (mMonHoc.getTinchi() != null) {
                    soTinChi = String.valueOf(mMonHoc.getTinchi());
                }
                txSoTinChi.setText(soTinChi);
                //xet dieu kien
                String madieukien = mMonHoc.getDieukien();
                boolean chuaQua = false;
                if (madieukien != null) {
                    if (madieukien.length() >= 7) {
                        SQLiteDataController data = SQLiteDataController.getInstance(context);
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
                if (chuaQua) {
                    itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChiTietMonHocActivity.class);
                            intent.putExtra("MaMonHoc", mMonHoc.getMamh());
                            context.startActivity(intent);
                        }
                    });
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        itemLayout.setBackgroundColor(context.getColor(R.color.black_overlay_2));
                    } else {
                        itemLayout.setBackgroundColor(context.getResources().getColor(R.color.black_overlay_2));
                    }
                    ckChon.setEnabled(false);
                } else {
                    //need more logic...
                    itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ckChon.performClick();
                        }
                    });
                    ckChon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                selectedMonHoc.add(mMonHoc.getMamh());
                            } else {
                                selectedMonHoc.remove(selectedMonHoc.indexOf(mMonHoc.getMamh()));
                            }
                        }
                    });
                    ckChon.setChecked(mainFragment.checkAll);
                }
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
}
