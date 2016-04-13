package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;

import java.io.IOException;
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
    public void clear() {
        super.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (objects.size() != 0) {
            Object mObject = objects.get(position);
            if (mObject != null) {
                ObjectMonHoc mMonHoc = (ObjectMonHoc) mObject;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc, parent, false);
                RelativeLayout itemLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
                TextView txtMaMonHoc = (TextView) view.findViewById(R.id.txtMaMonHoc);
                TextView txtTenMonHoc = (TextView) view.findViewById(R.id.txtTenMonHoc);
                TextView txtSoTinChi = (TextView) view.findViewById(R.id.txtSoTinChi);
                TextView txtTinChi = (TextView) view.findViewById(R.id.txtTinChi);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                ImageView imageViewBangDiem = (ImageView) view.findViewById(R.id.imageViewBangDiem);
                txtMaMonHoc.setText(mMonHoc.getMamh());
                txtTenMonHoc.setText(mMonHoc.getTenmh());
                SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                try {
                    data.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
                if(data.checkMonHocBoQua(mMonHoc.getMamh())){
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        itemLayout.setBackgroundColor(mContext.getColor(R.color.black_overlay_2));
                    } else {
                        itemLayout.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay_2));
                    }
                }
                String soTinChi = "...";
                if (mMonHoc.getTinchi() != null) {
                    soTinChi = String.valueOf(mMonHoc.getTinchi());
                }
                txtSoTinChi.setText(soTinChi);
                if (mMonHoc.isBangdiem()) {
                    imageViewBangDiem.setVisibility(View.VISIBLE);
                } else {
                    imageViewBangDiem.setVisibility(View.GONE);
                }
                if (mMonHoc.getTuchon() != null) {
                    switch (mMonHoc.getTuchon()) {
                        case "A":
                        {
                            imageView.setImageResource(R.drawable.tuchon_a);
                            txtMaMonHoc.setVisibility(View.GONE);
                            txtTinChi.setVisibility(View.GONE);
                            txtTenMonHoc.setText("Tự chọn A");
                            txtTenMonHoc.setPadding(0, 0, 0, 0);
                            break;
                        }
                        case "B":
                        {
                            imageView.setImageResource(R.drawable.tuchon_b);
                            txtMaMonHoc.setVisibility(View.GONE);
                            txtTinChi.setVisibility(View.GONE);
                            txtTenMonHoc.setText("Tự chọn B");
                            txtTenMonHoc.setPadding(0, 0, 0, 0);
                            break;
                        }
                        case "C":
                        {
                            imageView.setImageResource(R.drawable.tuchon_c);
                            txtMaMonHoc.setVisibility(View.GONE);
                            txtTinChi.setVisibility(View.GONE);
                            txtTenMonHoc.setText("Tự chọn C");
                            txtTenMonHoc.setPadding(0, 0, 0, 0);
                            break;
                        }
                        default:
                            break;
                    }
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
