package com.nckh2016.vuduytung.nckh2016.GoogleDrive;

/**
 * Created by Tung on 31/3/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.nckh2016.vuduytung.nckh2016.BackupFragment2;
import com.nckh2016.vuduytung.nckh2016.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * A DataBufferAdapter to display the results of file listing/querying requests.
 */
public class ResultsAdapter extends DataBufferAdapter<Metadata> {
    private static final String PATTERN = "_backup.txt";
    private Context mContext;
    private BackupFragment2 fragment2;

    public ResultsAdapter(Context context, BackupFragment2 fragment2) {
        super(context, R.layout.item_file);
        this.mContext = context;
        this.fragment2 = fragment2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_file, parent, false);
        final Metadata metadata = getItem(position);
        TextView txtTenFile = (TextView) convertView.findViewById(R.id.txtTenFile);
        TextView txtDuongDan = (TextView) convertView.findViewById(R.id.txtDuongDan);
        txtTenFile.setText(metadata.getTitle());
        String size = "";
        long fileSize = metadata.getFileSize();
        if(fileSize < 1024){
            size = String.valueOf(fileSize) + " bytes - ";
        } else if(fileSize < 1024*1024){
            size = String.valueOf(new DecimalFormat("####0.##").format((double)fileSize/1024)) + " KB - ";
        } else if(fileSize < 1024*1024*1024){
            size = String.valueOf(new DecimalFormat("####0.##").format((double)fileSize/1024/1024)) + " MB - ";
        }
        String description = size + new SimpleDateFormat("dd/MM/yyyy").format(metadata.getModifiedDate());
        txtDuongDan.setText(description);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Khôi phục dữ liệu")
                        .setMessage("Việc khôi phục dữ liệu sẽ ghi đè lên hồ sơ có mã sinh viên là " + metadata.getTitle().replace(PATTERN, "") + ", tiếp tục?")
                        .setIcon(R.drawable.backup_restore)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fragment2.restoreUser(metadata);
                                        dialog.dismiss();
                                    }
                                }
                        ).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        return convertView;
    }
}
