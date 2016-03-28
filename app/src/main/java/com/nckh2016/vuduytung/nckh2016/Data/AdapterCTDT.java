package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

import java.util.ArrayList;

/**
 * Created by Tung on 3/3/2016.
 */
public class AdapterCTDT extends BaseAdapter {
    private static final int LIST_ITEM_TYPE_1 = 0;
    private static final int LIST_ITEM_TYPE_2 = 1;
    private static final int LIST_ITEM_TYPE_COUNT = 2;

    private Context mContext;
    private ArrayList<Items> mData = new ArrayList<Items>();
    private LayoutInflater mInflater;

    public AdapterCTDT(Context mContext) {
        this.mContext = mContext;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Items item) {
        this.mData.add(item);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<Items> items){
        this.mData = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if((mData.get(position)).isHocKy())
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
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Items i = mData.get(position);
        if (i != null) {
            int type = getItemViewType(position);
            switch(type) {
                case LIST_ITEM_TYPE_1:
                    view = mInflater.inflate(R.layout.item_smalltitle, null);
                    ObjectHocKy2 hocKy = (ObjectHocKy2)mData.get(position);
                    view.setOnClickListener(null);
                    TextView txtTenHocKy = (TextView)view.findViewById(R.id.txtNamHoc);
                    if(hocKy.getChuyensau() == 0){
                        txtTenHocKy.setText("Học kỳ " + hocKy.getHocky() );
                    } else {
                        txtTenHocKy.setText("Học kỳ " + hocKy.getHocky() + " - hướng chuyên sâu " + hocKy.getChuyensau() + "\n" + hocKy.getTenchuyensau());
                    }
                    break;
                case LIST_ITEM_TYPE_2:
                    final ObjectCTDT monHoc = (ObjectCTDT)mData.get(position);
                    if(monHoc.getHocky()>0){
                        view = mInflater.inflate(R.layout.item_monhoc, null);
                        TextView txtMaMonHoc = (TextView)view.findViewById(R.id.txtMaMonHoc);
                        TextView txtTenMonHoc = (TextView)view.findViewById(R.id.txtTenMonHoc);
                        TextView txtSoTinChi = (TextView)view.findViewById(R.id.txtSoTinChi);
                        TextView txtTinChi = (TextView)view.findViewById(R.id.txtTinChi);
                        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
                        ImageView imageViewBangDiem = (ImageView)view.findViewById(R.id.imageViewBangDiem);
                        imageViewBangDiem.setVisibility(View.GONE);
                        txtMaMonHoc.setText(monHoc.getMamh());
                        txtTenMonHoc.setText(monHoc.getTenmh());
                        txtSoTinChi.setText(monHoc.getTinchi());
                        if(monHoc.getTuchon()!=null){
                            switch (monHoc.getTuchon()){
                                case "A":
                                    imageView.setImageResource(R.drawable.tuchon_a);
                                    txtMaMonHoc.setVisibility(View.GONE);
                                    txtTinChi.setVisibility(View.GONE);
                                    txtTenMonHoc.setText("Tự chọn A");
                                    txtTenMonHoc.setPadding(0, 0, 0, 0);
                                    /*view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ((NganhActivity) mContext).loadFragment3(monHoc.getMabm(), -3);
                                        }
                                    });*/
                                    break;
                                case "B":
                                    imageView.setImageResource(R.drawable.tuchon_b);
                                    txtMaMonHoc.setVisibility(View.GONE);
                                    txtTinChi.setVisibility(View.GONE);
                                    txtTenMonHoc.setText("Tự chọn B");
                                    txtTenMonHoc.setPadding(0, 0, 0, 0);
                                    /*view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ((NganhActivity) mContext).loadFragment3(monHoc.getMabm(), -2);
                                        }
                                    });*/
                                    break;
                                case "C":
                                    imageView.setImageResource(R.drawable.tuchon_c);
                                    txtMaMonHoc.setVisibility(View.GONE);
                                    txtTinChi.setVisibility(View.GONE);
                                    txtTenMonHoc.setText("Tự chọn C");
                                    txtTenMonHoc.setPadding(0, 0, 0, 0);
                                    /*view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ((NganhActivity) mContext).loadFragment3(monHoc.getMabm(), -1);
                                        }
                                    });*/
                                    break;
                                default:
                                    /*view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mContext, ChiTietMonHocActivity.class);
                                            intent.putExtra("MaMonHoc", monHoc.getMamh());
                                            intent.putExtra("caller", "BoMonActivity");
                                            mContext.startActivity(intent);
                                        }
                                    });*/
                                    break;
                            }
                        } else {
                            /*view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, ChiTietMonHocActivity.class);
                                    intent.putExtra("MaMonHoc", monHoc.getMamh());
                                    intent.putExtra("caller", "BoMonActivity");
                                    mContext.startActivity(intent);
                                }
                            });*/
                        }
                    }
                    break;
            }
        }
        return view;
    }
}
