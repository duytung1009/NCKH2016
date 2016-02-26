package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

import java.util.ArrayList;

/**
 * Created by Tung on 27/2/2016.
 */
public class AdapterMonHoc2 extends ArrayAdapter<Object> {
    private Context context;
    private ArrayList<Object> objects;

    public AdapterMonHoc2(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final Object mObject = objects.get(position);
        if(mObject != null){
            ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
            view = LayoutInflater.from(context).inflate(R.layout.list_item_monhoc_2, parent, false);
            TextView txMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
            TextView txTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
            TextView txSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);
            txMaMonHoc.setText(mMonHoc.getMamh());
            txTenMonHoc.setText(mMonHoc.getTenmh());
            txSoTinChi.setText(mMonHoc.getTinchi());
        }

        return view;
    }
}
