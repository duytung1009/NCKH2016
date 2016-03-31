package com.nckh2016.vuduytung.nckh2016.GoogleDrive;

/**
 * Created by Tung on 31/3/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.nckh2016.vuduytung.nckh2016.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * A DataBufferAdapter to display the results of file listing/querying requests.
 */
public class ResultsAdapter extends DataBufferAdapter<Metadata> {
    private static final String PATTERN = "_backup.txt";

    public ResultsAdapter(Context context) {
        super(context, R.layout.item_file);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_file, parent, false);
        Metadata metadata = getItem(position);
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
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                try {
                    data.isCreatedDatabase();
                } catch (IOException e) {
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
                                        Toast.makeText(getContext(), "Không thể đọc dữ liệu từ tệp đã chọn", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (data.insertUser(user)) {
                                            Toast.makeText(getContext(), "Đã khôi phục", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
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
        });*/
        return convertView;
    }
}
