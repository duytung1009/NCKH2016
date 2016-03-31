package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Tung on 30/3/2016.
 */
public class AdapterListFile extends ArrayAdapter<Object> {
    private static final String PATTERN = "_backup.txt";
    private Context mContext;
    private ArrayList<Object> objects = new ArrayList<Object>();

    public AdapterListFile(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
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
        if(objects.size()!=0) {
            Object mObject = objects.get(position);
            if (mObject != null) {
                File file = (File)mObject;
                final int staticPosition = position;
                view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
                TextView txtTenFile = (TextView)view.findViewById(R.id.txtTenFile);
                TextView txtDuongDan = (TextView)view.findViewById(R.id.txtDuongDan);
                txtTenFile.setText(file.getName());
                //txtDuongDan.setText(file.getAbsolutePath());
                String size = "";
                long fileSize = file.length();
                if(fileSize < 1024){
                    size = String.valueOf(fileSize) + " bytes";
                } else if(fileSize < 1024*1024){
                    size = String.valueOf(new DecimalFormat("####0.##").format((double)fileSize/1024)) + " KB";
                } else if(fileSize < 1024*1024*1024){
                    size = String.valueOf(new DecimalFormat("####0.##").format((double)fileSize/1024/1024)) + " MB";
                }
                String description = size + " - " + file.getAbsolutePath();
                txtDuongDan.setText(description);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                        try{
                            data.isCreatedDatabase();
                        }
                        catch (IOException e){
                            Log.e("tag", e.getMessage());
                        }
                        final File file = (File) objects.get(staticPosition);
                        new AlertDialog.Builder(getContext())
                                .setTitle("Khôi phục dữ liệu")
                                .setMessage("Việc khôi phục dữ liệu sẽ ghi đè lên hồ sơ có mã sinh viên là " + file.getName().replace(PATTERN, "") + ", tiếp tục?")
                                .setIcon(R.drawable.backup_restore)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            FileInputStream fis = new FileInputStream(file);
                                            InputStreamReader isr = new InputStreamReader(fis);
                                            BufferedReader bufferedReader = new BufferedReader(isr);
                                            StringBuilder sb = new StringBuilder();
                                            String line;
                                            while ((line = bufferedReader.readLine()) != null) {
                                                sb.append(line);
                                            }
                                            String json = sb.toString();
                                            Gson gson = new Gson();
                                            ObjectUser user = gson.fromJson(json, ObjectUser.class);
                                            if (user.getMasv() == null) {
                                                Toast.makeText(mContext, "Không thể đọc dữ liệu từ tệp đã chọn", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (data.insertUser(user)) {
                                                    Toast.makeText(mContext, "Đã khôi phục", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(mContext, "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
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
}
