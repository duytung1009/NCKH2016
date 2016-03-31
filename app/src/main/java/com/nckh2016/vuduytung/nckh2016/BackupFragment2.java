package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.GoogleDrive.BaseDriveFragment;
import com.nckh2016.vuduytung.nckh2016.GoogleDrive.ResultsAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BackupFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BackupFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackupFragment2 extends BaseDriveFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String PATTERN = "_backup.txt";

    public static final String PREFS_NAME = "current_user";
    public String current_user = null;

    LoadingTask loadingTask;
    CircularProgressView progressBar;
    SwipeRefreshLayout swipeContainer;
    private ListView mResultsListView;
    private ResultsAdapter mResultsAdapter;

    private OnFragmentInteractionListener mListener;

    public BackupFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BackupFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static BackupFragment2 newInstance(String param1, String param2) {
        BackupFragment2 fragment = new BackupFragment2();
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
        View view = inflater.inflate(R.layout.fragment_backup_fragment_2, container, false);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        progressBar = (CircularProgressView) view.findViewById(R.id.progressBar);
        mResultsListView = (ListView) view.findViewById(R.id.listViewResults);
        mResultsAdapter = new ResultsAdapter(getContext());
        mResultsListView.setAdapter(mResultsAdapter);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadingTask != null) {
                    if (loadingTask.getStatus() == AsyncTask.Status.RUNNING) {
                        loadingTask.cancel(true);
                    }
                }
                Utils.showProcessBar(getContext().getApplicationContext(), progressBar, mResultsListView);
                loadingTask = new LoadingTask(getContext().getApplicationContext());
                loadingTask.execute();
            }
        });
        Button btnBackup = (Button)view.findViewById(R.id.btnBackup);
        if(current_user == null){
            btnBackup.setVisibility(View.GONE);
        } else {
            btnBackup.setVisibility(View.VISIBLE);
        }
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drive.DriveApi.newDriveContents(getGoogleApiClient()).setResultCallback(driveContentsCallback);
            }
        });
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.google_drive);
        txtTieuDe.setText(R.string.txtGoogleDrive);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Clears the result buffer to avoid memory leaks as soon
     * as the activity is no longer visible by the user.
     */
    @Override
    public void onStop() {
        super.onStop();
        mResultsAdapter.clear();
        if(loadingTask.getStatus() == AsyncTask.Status.RUNNING) {
            loadingTask.cancel(true);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Utils.showProcessBar(getContext(), progressBar, mResultsListView);
        loadingTask = new LoadingTask(getContext());
        loadingTask.execute();
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

    final private ResultCallback<DriveApi.MetadataBufferResult> metadataCallback = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(DriveApi.MetadataBufferResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Lỗi trong lúc nhận kết quả trả về");
                return;
            }
            mResultsAdapter.clear();
            mResultsAdapter.append(result.getMetadataBuffer());
        }
    };

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Lỗi trong quá trình khởi tạo hồ sơ sao lưu");
                return;
            }
            final DriveContents driveContents = result.getDriveContents();

            // Perform I/O off the UI thread.
            new Thread() {
                @Override
                public void run() {
                    // write content to DriveContents
                    OutputStream outputStream = driveContents.getOutputStream();
                    Writer writer = new OutputStreamWriter(outputStream);
                    try {
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
                        writer.write(json);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(current_user + PATTERN)
                            .setMimeType("text/plain")
                            .setStarred(true).build();

                    // create a file on root folder
                    Drive.DriveApi.getRootFolder(getGoogleApiClient())
                            .createFile(getGoogleApiClient(), changeSet, driveContents)
                            .setResultCallback(fileCallback);
                }
            }.start();
        }
    };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Lỗi trong quá trình khởi tạo hồ sơ sao lưu");
                return;
            }
            showMessage("Đã xong");
        }
    };


    private class LoadingTask extends AsyncTask<Void, Long, Void> {
        private Context mContext;

        public LoadingTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Query query = new Query.Builder()
                    .addFilter(Filters.and(
                            Filters.eq(SearchableField.MIME_TYPE, "text/plain"),
                            Filters.eq(SearchableField.TRASHED, false),
                            Filters.contains(SearchableField.TITLE, "_backup")))
                    .build();
            Drive.DriveApi.query(getGoogleApiClient(), query).setResultCallback(metadataCallback);
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Utils.hideProcessBar(mContext, progressBar, mResultsListView);
            swipeContainer.setRefreshing(false);
        }
    }
}