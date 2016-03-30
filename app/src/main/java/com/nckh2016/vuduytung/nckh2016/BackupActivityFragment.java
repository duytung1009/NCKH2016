package com.nckh2016.vuduytung.nckh2016;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterListFile;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BackupActivityFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private static final String PATTERN = "_backup.txt";

    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    CircularProgressView progressBar;
    SwipeRefreshLayout swipeContainer;
    MainTask mainTask;
    ListView listViewFile;
    ArrayList<File> listFile = new ArrayList<File>();
    AdapterListFile adapterListFile;

    public BackupActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backup, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        progressBar = (CircularProgressView)view.findViewById(R.id.progressBar);
        final SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        listViewFile = (ListView)view.findViewById(R.id.listViewFile);
        /*listViewFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final File file = (File) adapterListFile.getItem(position);
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
                                    if (data.insertUser(user)) {
                                        Toast.makeText(getContext(), "Đã khôi phục", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
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
        adapterListFile = new AdapterListFile(getContext(), 0);
        listViewFile.setAdapter(adapterListFile);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mainTask != null){
                    if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                        mainTask.cancel(true);
                    }
                }
                Utils.showProcessBar(getContext(), progressBar, listViewFile);
                mainTask = new MainTask(getContext());
                mainTask.execute();
            }
        });
        Button btnBackup = (Button)view.findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ObjectUser user = data.getUser(current_user);
                        user.setUserdata(data.getUserData(current_user));
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        String filename = current_user + PATTERN;
                        File file = new File(Environment.getExternalStorageDirectory(), filename);
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            outputStream.write(json.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), "File backup đã lưu tại " + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Không có quyền truy cập bộ nhớ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ObjectUser user = data.getUser(current_user);
                    user.setUserdata(data.getUserData(current_user));
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    String filename = current_user + PATTERN;
                    File file = new File(Environment.getExternalStorageDirectory(), filename);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(json.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "File backup đã lưu tại " + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.backup_restore);
        txtTieuDe.setText(R.string.txtBackup);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            Utils.showProcessBar(getContext(), progressBar, listViewFile);
            mainTask = new MainTask(getContext());
            mainTask.execute();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    public class MainTask extends AsyncTask<Void, Long, Void> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            listFile.clear();
            scanForUserFile(Environment.getExternalStorageDirectory());
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapterListFile.clear();
            adapterListFile.addAll(listFile);
            Utils.hideProcessBar(getContext(), progressBar, listViewFile);
            swipeContainer.setRefreshing(false);
        }

        public void scanForUserFile(File dir){
            File[] tempListFile = dir.listFiles();
            if (listFile != null) {
                for (int i = 0; i < tempListFile.length; i++) {
                    if (tempListFile[i].isDirectory()) {
                        scanForUserFile(tempListFile[i]);
                    } else {
                        if (tempListFile[i].getName().endsWith(PATTERN)){
                            listFile.add(tempListFile[i]);
                        }
                    }
                }
            }
        }
    }
}
