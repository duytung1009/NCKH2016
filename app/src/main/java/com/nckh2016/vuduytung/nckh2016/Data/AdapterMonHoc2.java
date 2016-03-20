package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.KeHoachHocTap2Fragment;
import com.nckh2016.vuduytung.nckh2016.R;

import java.util.ArrayList;

/**
 * Created by Tung on 27/2/2016.
 */
public class AdapterMonHoc2 extends ArrayAdapter<Object> {
    private Context context;
    private KeHoachHocTap2Fragment mainFragment;
    private ArrayList<Object> objects;
    private ArrayList<String> selectedMonHoc = new ArrayList<String>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public AdapterMonHoc2(KeHoachHocTap2Fragment abc, int resource, ArrayList<Object> objects) {
        super(abc.getContext(), resource, objects);
        this.mainFragment = abc;
        this.context = abc.getContext();
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(objects.size()!=0) {
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
                txSoTinChi.setText(mMonHoc.getTinchi().toString());
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
        return view;
    }

    public void removeAll(){
        selectedMonHoc.clear();
    }

    public ArrayList<String> getSelectedMonHoc(){
        return selectedMonHoc;
    }
}
