package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private HashMap<String, Float> diemValues = new HashMap<String, Float>();

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
            RelativeLayout itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
            TextView txMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
            TextView txTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
            TextView txSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);
            final EditText edDiem = (EditText) view.findViewById(R.id.edNhapDiem);
            txMaMonHoc.setText(mMonHoc.getMamh());
            txTenMonHoc.setText(mMonHoc.getTenmh());
            txSoTinChi.setText(mMonHoc.getTinchi().toString());
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager lManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(edDiem, 0);
                    /*edDiem.setFocusableInTouchMode(true);
                    edDiem.requestFocus();*/
                    edDiem.requestFocus();
                }
            });
            if(diemValues.containsKey(mMonHoc.getMamh())){
                edDiem.setText(diemValues.get(mMonHoc.getMamh()).toString());
            }
            edDiem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if(s.length() != 0){
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
        }
        return view;
    }

    public HashMap<String, Float> getDiem(){
        return diemValues;
    }
}
