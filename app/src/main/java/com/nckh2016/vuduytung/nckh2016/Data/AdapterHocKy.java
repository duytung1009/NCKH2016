package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

import java.util.ArrayList;

/**
 * Created by Tung on 26/2/2016.
 */
public class AdapterHocKy extends BaseAdapter {
    private static final int LIST_ITEM_TYPE_1 = 0;
    private static final int LIST_ITEM_TYPE_2 = 1;
    private static final int LIST_ITEM_TYPE_COUNT = 2;

    private ArrayList<ObjectHocKy> mData = new ArrayList<ObjectHocKy>();
    private LayoutInflater mInflater;

    public AdapterHocKy(Context mContext) {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case LIST_ITEM_TYPE_1:
                    convertView = mInflater.inflate(R.layout.item_smalltitle, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.txtNamHoc);
                    break;
                case LIST_ITEM_TYPE_2:
                    convertView = mInflater.inflate(R.layout.item_hocky, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.txtHocKy);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case LIST_ITEM_TYPE_1:
                if(mData.get(position).getNamHoc() == 0){
                    holder.textView.setText("Các môn tự chọn");
                } else {
                    holder.textView.setText("Năm " + mData.get(position).getNamHoc());
                }
                break;
            case LIST_ITEM_TYPE_2:
                switch (mData.get(position).getHocKy()){
                    case -3:
                        holder.textView.setText("Tự chọn A");
                        break;
                    case -2:
                        holder.textView.setText("Tự chọn B");
                        break;
                    case -1:
                        holder.textView.setText("Tự chọn C");
                        break;
                    default:
                        holder.textView.setText("Học kỳ " + mData.get(position).getHocKy());
                        break;
                }
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
