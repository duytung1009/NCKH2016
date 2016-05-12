package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 24/2/2016.
 * optimized
 */
public class AdapterMonHoc extends ArrayAdapter<Object> {
    private String current_user = null;
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<Object>();

    public AdapterMonHoc(Context mContext, int resource) {
        super(mContext, resource);
        this.mContext = mContext;
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
                view = LayoutInflater.from(mContext).inflate(R.layout.item_monhoc, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.position = position;
                holder.itemLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
                holder.txtMaMonHoc = (TextView) view.findViewById(R.id.txtMaMonHoc);
                holder.txtTenMonHoc = (TextView) view.findViewById(R.id.txtTenMonHoc);
                holder.txtSoTinChi = (TextView) view.findViewById(R.id.txtSoTinChi);
                holder.txtTinChi = (TextView) view.findViewById(R.id.txtTinChi);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView);
                //
                holder.imageViewBangDiem = (ImageView) view.findViewById(R.id.imageViewBangDiem);
                holder.txtMaMonHoc.setText(mMonHoc.getMamh());
                holder.txtTenMonHoc.setText(mMonHoc.getTenmh());
                String soTinChi = "...";
                if (mMonHoc.getTinchi() != null) {
                    soTinChi = String.valueOf(mMonHoc.getTinchi());
                }
                holder.txtSoTinChi.setText(soTinChi);
                if (mMonHoc.isBangdiem()) {
                    holder.imageViewBangDiem.setVisibility(View.VISIBLE);
                } else {
                    holder.imageViewBangDiem.setVisibility(View.GONE);
                }
                if (mMonHoc.getTuchon() != null) {
                    switch (mMonHoc.getTuchon()) {
                        case "A":
                        {
                            holder.imageView.setImageResource(R.drawable.tuchon_a);
                            holder.txtMaMonHoc.setVisibility(View.GONE);
                            holder.txtTinChi.setVisibility(View.GONE);
                            holder.txtTenMonHoc.setText(getContext().getResources().getString(R.string.txtTuChonA));
                            holder.txtTenMonHoc.setPadding(0, 0, 0, 0);
                            break;
                        }
                        case "B":
                        {
                            holder.imageView.setImageResource(R.drawable.tuchon_b);
                            holder.txtMaMonHoc.setVisibility(View.GONE);
                            holder.txtTinChi.setVisibility(View.GONE);
                            holder.txtTenMonHoc.setText(getContext().getResources().getString(R.string.txtTuChonB));
                            holder.txtTenMonHoc.setPadding(0, 0, 0, 0);
                            break;
                        }
                        case "C":
                        {
                            holder.imageView.setImageResource(R.drawable.tuchon_c);
                            holder.txtMaMonHoc.setVisibility(View.GONE);
                            holder.txtTinChi.setVisibility(View.GONE);
                            holder.txtTenMonHoc.setText(getContext().getResources().getString(R.string.txtTuChonC));
                            holder.txtTenMonHoc.setPadding(0, 0, 0, 0);
                            break;
                        }
                        default:
                            break;
                    }
                }
                holder.txtDiem = (TextView) view.findViewById(R.id.txtDiem);
                if (mMonHoc.getDiem() == -1){
                    //txtDiem.setText("");
                } else {
                    holder.txtDiem.setText(new DecimalFormat("####0.#").format(mMonHoc.getDiem()));
                }
                //tối ưu code...
                //http://developer.android.com/intl/vi/training/improving-layouts/smooth-scrolling.html
                new AsyncTask<ViewHolder, Void, Void>() {
                    private ViewHolder viewHolder;
                    private boolean monHocBoQua = false;
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
                        monHocBoQua = data.checkMonHocBoQua(mMonHoc.getMamh());
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
                        if(monHocBoQua){
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                viewHolder.itemLayout.setBackgroundColor(mContext.getColor(R.color.black_overlay_2));
                            } else {
                                viewHolder.itemLayout.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay_2));
                            }
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
            }
        }
        return view;
    }

    //design patten: ViewHolder
    static class ViewHolder {
        RelativeLayout itemLayout;
        TextView txtMaMonHoc;
        TextView txtTenMonHoc;
        TextView txtSoTinChi;
        TextView txtTinChi;
        ImageView imageView;
        ImageView imageViewBangDiem;
        TextView txtDiem;
        int position;

        public ViewHolder(){
            //event cho vào đây
        }
    }
}
