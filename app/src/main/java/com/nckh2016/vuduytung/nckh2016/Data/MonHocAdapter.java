package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.R;

/**
 * Created by Tung on 20/2/2016.
 */
public class MonHocAdapter extends CursorAdapter {
    public MonHocAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_monhoc, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txMaMonHoc = (TextView) view.findViewById(R.id.txMaMonHoc);
        TextView txTenMonHoc = (TextView) view.findViewById(R.id.txTenMonHoc);
        TextView txSoTinChi = (TextView) view.findViewById(R.id.txSoTinChi);

        txMaMonHoc.setText(cursor.getString(cursor.getColumnIndexOrThrow("mamh")));
        txTenMonHoc.setText(cursor.getString(cursor.getColumnIndexOrThrow("tenmh")));
        txSoTinChi.setText(cursor.getString(cursor.getColumnIndexOrThrow("tinchi")));
    }
}
