package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 24/2/2016.
 */
public class AdapterMonHoc extends ArrayAdapter<Object> {
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<Object>();

    public AdapterMonHoc(Context mContext, int resource) {
        super(mContext, resource);
        this.mContext = mContext;
    }

    @Override
    public void addAll(Collection<?> collection) {
        this.objects = (ArrayList)collection;
        super.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(objects.size()!=0){
            Object mObject = objects.get(position);
            if(mObject != null) {
                ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc, parent, false);
                TextView txtMaMonHoc = (TextView) view.findViewById(R.id.txtMaMonHoc);
                TextView txtTenMonHoc = (TextView) view.findViewById(R.id.txtTenMonHoc);
                TextView txtSoTinChi = (TextView) view.findViewById(R.id.txtSoTinChi);
                TextView txtTinChi = (TextView) view.findViewById(R.id.txtTinChi);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                //TextView txtIcon = (TextView)view.findViewById(R.id.txtIcon);
                txtMaMonHoc.setText(mMonHoc.getMamh());
                txtTenMonHoc.setText(mMonHoc.getTenmh());
                txtSoTinChi.setText(mMonHoc.getTinchi());
                if (mMonHoc.getTuchon() != null) {
                    switch (mMonHoc.getTuchon()) {
                        case "A":
                            imageView.setImageResource(R.drawable.tuchon_a);
                            txtMaMonHoc.setVisibility(View.GONE);
                            txtTinChi.setVisibility(View.GONE);
                            txtTenMonHoc.setText("Tự chọn A");
                            txtTenMonHoc.setPadding(0, 0, 0, 0);
                        /*imageView.setVisibility(View.GONE);
                        txtMaMonHoc.setVisibility(View.GONE);
                        txtTinChi.setVisibility(View.GONE);
                        txtTenMonHoc.setText("Tự chọn A");
                        txtTenMonHoc.setPadding(0,0,0,0);
                        txtIcon.setText("A");
                        txtIcon.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tuchonA));*/
                            break;
                        case "B":
                            imageView.setImageResource(R.drawable.tuchon_b);
                            txtMaMonHoc.setVisibility(View.GONE);
                            txtTinChi.setVisibility(View.GONE);
                            txtTenMonHoc.setText("Tự chọn B");
                            txtTenMonHoc.setPadding(0, 0, 0, 0);
                        /*imageView.setVisibility(View.GONE);
                        txtMaMonHoc.setVisibility(View.GONE);
                        txtTinChi.setVisibility(View.GONE);
                        txtTenMonHoc.setText("Tự chọn B");
                        txtTenMonHoc.setPadding(0, 0, 0, 0);
                        txtIcon.setText("B");
                        txtIcon.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tuchonB));*/
                            break;
                        case "C":
                            imageView.setImageResource(R.drawable.tuchon_c);
                            txtMaMonHoc.setVisibility(View.GONE);
                            txtTinChi.setVisibility(View.GONE);
                            txtTenMonHoc.setText("Tự chọn C");
                            txtTenMonHoc.setPadding(0, 0, 0, 0);
                        /*imageView.setVisibility(View.GONE);
                        txtMaMonHoc.setVisibility(View.GONE);
                        txtTinChi.setVisibility(View.GONE);
                        txtTenMonHoc.setText("Tự chọn C");
                        txtTenMonHoc.setPadding(0, 0, 0, 0);
                        txtIcon.setText("C");
                        txtIcon.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tuchonC));*/
                            break;
                        default:
                            //txtIcon.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    //txtIcon.setVisibility(View.GONE);
                }
                if (mMonHoc.getDiem() != -1) {
                    TextView txtDiem = (TextView) view.findViewById(R.id.txtDiem);
                    txtDiem.setText(new DecimalFormat("####0.#").format(mMonHoc.getDiem()));
                }
            }
        }
        return view;
    }
}
