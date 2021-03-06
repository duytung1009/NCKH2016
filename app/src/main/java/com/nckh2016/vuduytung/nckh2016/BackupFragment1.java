package com.nckh2016.vuduytung.nckh2016;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterListFile;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackupFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackupFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //các giá trị Global trong Activity
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    //các asynctask + biến liên quan
    MainTask mainTask;
    private ArrayList<File> listFile = new ArrayList<File>();
    //các adapter
    AdapterListFile adapterListFile;
    //các view
    ProgressDialog progressBackup, progressRestore, progressDelete; //xử lý quá nhanh -> không cần thiết lắm...
    CircularProgressView progressBar;
    SwipeRefreshLayout swipeContainer;
    ListView listViewFile;
    RelativeLayout mainLayout;

    //RecyclerView và đống rườm rà đi kèm
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    public BackupFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BackupFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static BackupFragment1 newInstance(String param1, String param2) {
        BackupFragment1 fragment = new BackupFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_backup_fragment_1, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        /*mRecyclerView = (RecyclerView)view.findViewById(R.id.listViewFile);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);*/

        mainLayout = (RelativeLayout)view.findViewById(R.id.mainLayout);
        progressBar = (CircularProgressView)view.findViewById(R.id.progressBar);
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
                mainTask = new MainTask(getContext());
                mainTask.execute();
            }
        });
        listViewFile = (ListView)view.findViewById(R.id.listViewFile);
        adapterListFile = new AdapterListFile(BackupFragment1.this, 0);
        listViewFile.setAdapter(adapterListFile);
        //button
        Button btnBackup = (Button)view.findViewById(R.id.btnBackup);
        if(current_user == null){
            btnBackup.setVisibility(View.GONE);
        } else {
            btnBackup.setVisibility(View.VISIBLE);
        }
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Sao lưu hồ sơ")
                        .setMessage("Sao lưu hồ sơ có mã sinh viên " + current_user + "?")
                        .setIcon(R.drawable.backup_restore)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                backupUser();
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
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.folder);
        txtTieuDe.setText(R.string.txtMemory);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new AsyncTask<Void, Long, Void>(){
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBackup = new ProgressDialog(getContext());
                            progressBackup.setMessage("Sao lưu hồ sơ");
                            progressBackup.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressBackup.setIndeterminate(true);
                            progressBackup.setProgressNumberFormat(null);
                            progressBackup.setProgressPercentFormat(null);
                            progressBackup.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                            try{
                                data.isCreatedDatabase();
                            }
                            catch (IOException e){
                                Log.e("tag", e.getMessage());
                            }
                            ObjectUser user = data.getUser(current_user);
                            user.setUserdata(data.getUserData(current_user));
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            String filename = current_user + Utils.BACKUP_FILE_PATTERN;
                            File file = new File(Environment.getExternalStorageDirectory(), filename);
                            try {
                                FileOutputStream outputStream = new FileOutputStream(file);
                                outputStream.write(json.getBytes());
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                                mainTask.cancel(true);
                            }
                            progressBackup.dismiss();
                            mainTask = new MainTask(getContext());
                            mainTask.execute();
                            Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionWriteSuccess) + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
                        }
                    }.execute();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionDenided), Toast.LENGTH_SHORT).show();
                }
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainTask = new MainTask(getContext());
                    mainTask.execute();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionDenided), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionDenided), Toast.LENGTH_SHORT).show();
            }
        } else {
            mainTask = new MainTask(getContext());
            mainTask.execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void backupUser(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionDenided), Toast.LENGTH_SHORT).show();
            }
        } else {
            new AsyncTask<Void, Long, Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressBackup = new ProgressDialog(getContext());
                    progressBackup.setMessage("Sao lưu hồ sơ");
                    progressBackup.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBackup.setIndeterminate(true);
                    progressBackup.setProgressNumberFormat(null);
                    progressBackup.setProgressPercentFormat(null);
                    progressBackup.show();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                    try{
                        data.isCreatedDatabase();
                    }
                    catch (IOException e){
                        Log.e("tag", e.getMessage());
                    }
                    ObjectUser user = data.getUser(current_user);
                    user.setUserdata(data.getUserData(current_user));
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    String filename = current_user + Utils.BACKUP_FILE_PATTERN;
                    File file = new File(Environment.getExternalStorageDirectory(), filename);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(json.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                        mainTask.cancel(true);
                    }
                    progressBackup.dismiss();
                    mainTask = new MainTask(getContext());
                    mainTask.execute();
                    Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionWriteSuccess) + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
    }

    public void restoreUser(File file){
        new AsyncTask<File, Long, Void>(){
            private int mCase = -1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressRestore = new ProgressDialog(getContext());
                progressRestore.setMessage("Khôi phục hồ sơ");
                progressRestore.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressRestore.setIndeterminate(true);
                progressRestore.setProgressNumberFormat(null);
                progressRestore.setProgressPercentFormat(null);
                progressRestore.show();
            }

            @Override
            protected Void doInBackground(File... params) {
                File file = params[0];
                SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                try {
                    data.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
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
                        mCase = 0;
                    } else {
                        if (data.insertUser(user)) {
                            mCase = 1;
                        } else {
                            mCase = 2;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressRestore.dismiss();
                switch(mCase){
                    case 0:{
                        Toast.makeText(getContext(), "Không thể đọc dữ liệu từ tệp đã chọn", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1:{
                        ((BackupActivity) getActivity()).updateNavigationView();
                        Toast.makeText(getContext(), "Đã khôi phục", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2:{
                        Toast.makeText(getContext(), "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:{
                        Toast.makeText(getContext(), "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }.execute(file);
    }

    public void deleteFile(String path){
        new AsyncTask<String, Long, Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDelete = new ProgressDialog(getContext());
                progressDelete.setMessage("Xóa hồ sơ");
                progressDelete.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDelete.setIndeterminate(true);
                progressDelete.setProgressNumberFormat(null);
                progressDelete.setProgressPercentFormat(null);
                progressDelete.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                String path = params[0];
                File file = new File(path);
                return file.delete();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                progressDelete.dismiss();
                if(aBoolean){
                    if(mainTask != null){
                        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                            mainTask.cancel(true);
                        }
                    }
                    mainTask = new MainTask(getContext());
                    mainTask.execute();
                } else {
                    Toast.makeText(getContext(), "Xóa hồ sơ thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(path);
    }

    public class MainTask extends AsyncTask<Void, Long, Void> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProcessBar(getContext(), progressBar, mainLayout);
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
            /*mAdapter = new FileAdapter(getContext(), listFile);
            mRecyclerView.setAdapter(mAdapter);*/
            Utils.hideProcessBar(mContext, progressBar, mainLayout);
            swipeContainer.setRefreshing(false);
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getResources().getString(R.string.txtPermissionDenided), Toast.LENGTH_SHORT).show();
            }
        }

        public void scanForUserFile(File dir){
            File[] tempListFile = dir.listFiles();
            if (listFile != null) {
                if(tempListFile != null){
                    for (int i = 0; i < tempListFile.length; i++) {
                        if (tempListFile[i].isDirectory()) {
                            scanForUserFile(tempListFile[i]);
                        } else {
                            if (tempListFile[i].getName().equals(current_user + Utils.BACKUP_FILE_PATTERN)){
                                listFile.add(tempListFile[i]);
                            }
                        }
                    }
                }
            }
        }
    }
}
