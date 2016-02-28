package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tung on 28/2/2016.
 */
public class AdapterMonHocNhapDiem extends ArrayAdapter<Object> {
    private Context context;
    private ArrayList<Object> objects;
    TextView txMaMonHoc, txTenMonHoc, txSoTinChi;
    EditText edDiem;
    private HashMap<String, String> diemValues = new HashMap<String, String>();

    public AdapterMonHocNhapDiem(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final Object mObject = objects.get(position);
        if(mObject != null){
            final ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
            view = LayoutInflater.from(context).inflate(R.layout.item_monhoc_nhapdiem, parent, false);
            txMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
            txTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
            txSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);
            edDiem = (EditText) view.findViewById(R.id.edNhapDiem);
            txMaMonHoc.setText(mMonHoc.getMamh());
            txTenMonHoc.setText(mMonHoc.getTenmh());
            txSoTinChi.setText(mMonHoc.getTinchi());
            edDiem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //The method put will replace the value of an existing key and will create it if doesn't exist.
                    diemValues.put(mMonHoc.getMamh(), s.toString());
                }
            });
        }
        return view;
    }

    public HashMap<String, String> getDiem(){
        return diemValues;
    }
}
