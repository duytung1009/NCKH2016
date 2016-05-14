package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nckh2016.vuduytung.nckh2016.adapter.UserAdapter;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.util.ArrayList;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {
    //các asynctask
    MainTask mainTask;
    //view
    CircularProgressView progressBar;
    //RecyclerView và đống rườm rà đi kèm
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (CircularProgressView)findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView)findViewById(R.id.listview_users);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainTask = new MainTask(this);
        mainTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainTask != null) {
            if (mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    private class MainTask extends AsyncTask<Void, Long, ArrayList<ObjectUser>>{
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProcessBar(mContext, progressBar, mRecyclerView);
        }

        @Override
        protected ArrayList<ObjectUser> doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return data.getUser();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<ObjectUser> objectUsers) {
            super.onPostExecute(objectUsers);
            Utils.hideProcessBar(mContext, progressBar, mRecyclerView);
            // specify an adapter (see also next example)
            mAdapter = new UserAdapter(mContext, objectUsers);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
