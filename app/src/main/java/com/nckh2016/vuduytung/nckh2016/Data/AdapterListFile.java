package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.BackupFragment1;
import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.main.Utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 30/3/2016.
 * optimized
 * (old adapter)
 */
public class AdapterListFile extends ArrayAdapter<Object> {
    private Context mContext;
    private BackupFragment1 mainFragment;
    private ArrayList<Object> objects = new ArrayList<Object>();

    public AdapterListFile(BackupFragment1 fragment1, int resource) {
        super(fragment1.getContext(), resource);
        this.mainFragment = fragment1;
        this.mContext = fragment1.getContext();
    }

    @Override
    public void add(Object object) {
        super.add(object);
        this.objects.add(object);
    }

    @Override
    public void addAll(Collection<?> collection) {
        super.addAll(collection);
        this.objects.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
        this.objects.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (objects.size() != 0) {
            Object mObject = objects.get(position);
            if (mObject != null) {
                File file = (File) mObject;
                final int staticPosition = position;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.itemLayout = (RelativeLayout) view.findViewById(R.id.itemLayout);
                holder.position = position;
                holder.txtTenFile = (TextView) view.findViewById(R.id.txtTenFile);
                holder.txtDuongDan = (TextView) view.findViewById(R.id.txtDuongDan);
                holder.btnXoa = (Button) view.findViewById(R.id.btnXoa);
                holder.txtTenFile.setText(file.getName());
                //holder.txtDuongDan.setText(file.getAbsolutePath());
                String size = "";
                long fileSize = file.length();
                if (fileSize < 1024) {
                    size = String.valueOf(fileSize) + " bytes";
                } else if (fileSize < 1024 * 1024) {
                    size = String.valueOf(new DecimalFormat("####0.##").format((double) fileSize / 1024)) + " KB";
                } else if (fileSize < 1024 * 1024 * 1024) {
                    size = String.valueOf(new DecimalFormat("####0.##").format((double) fileSize / 1024 / 1024)) + " MB";
                }
                String description = size + " - " + file.getPath();
                holder.txtDuongDan.setText(description);
                holder.btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final File file = (File) objects.get(staticPosition);
                        new AlertDialog.Builder(mContext)
                                .setTitle("Xóa")
                                .setMessage("Xóa tệp sao lưu có mã sinh viên " + file.getName().replace(Utils.BACKUP_FILE_PATTERN, "") + "?")
                                .setIcon(R.drawable.error)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mainFragment.deleteFile(file.getAbsolutePath());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final File file = (File) objects.get(staticPosition);
                        new AlertDialog.Builder(mContext)
                                .setTitle("Khôi phục dữ liệu")
                                .setMessage("Việc khôi phục dữ liệu sẽ ghi đè lên hồ sơ có mã sinh viên " + file.getName().replace(Utils.BACKUP_FILE_PATTERN, "") + ", tiếp tục?")
                                .setIcon(R.drawable.backup_restore)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mainFragment.restoreUser(file);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
        }
        return view;
    }

    //design patten: ViewHolder
    static class ViewHolder {
        RelativeLayout itemLayout;
        TextView txtTenFile;
        TextView txtDuongDan;
        Button btnXoa;
        int position;

        public ViewHolder(){
            //event cho vào đây (đang làm...)
        }
    }
}
