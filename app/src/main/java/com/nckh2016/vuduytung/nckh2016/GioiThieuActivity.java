package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUserData;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GioiThieuActivity extends AppCompatActivity {
    private static final String DATABASE_URL = "https://www.dropbox.com/s/s84xumtdab9746m/nckh2016.sqlite?dl=0";
    private DownloadFileFromURL updateTask;
    private Button btnUpdate;
    private ProgressDialog processUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.GONE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateTask != null){
                    if(updateTask.getStatus() == AsyncTask.Status.RUNNING) {
                        updateTask.cancel(true);
                    }
                }
                updateTask = new DownloadFileFromURL(getApplicationContext());
                updateTask.execute(DATABASE_URL);
            }
        });
        processUpdate = new ProgressDialog(this);
        processUpdate.setMessage("Cập nhật CTDT");
        processUpdate.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        processUpdate.setIndeterminate(false);
        processUpdate.setMax(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Transition
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.txtTimKiem));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private Context mContext;
        ArrayList<ObjectUser> allUser = new ArrayList<ObjectUser>();
        ArrayList<ObjectUserData> allUserData = new ArrayList<ObjectUserData>();

        public DownloadFileFromURL(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            processUpdate.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                //sao lưu dữ liệu người dùng
                SQLiteDataController database = SQLiteDataController.getInstance(mContext);
                try {
                    database.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
                allUser = database.getUser();
                allUserData = database.getUserData();
                //database.close();
                //tải database mới về
                URL url = new URL(f_url[0]);
                URLConnection mConnection = url.openConnection();
                mConnection.connect();
                int lengthOfFile = mConnection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(SQLiteDataController.DB_PATH + SQLiteDataController.DB_NAME);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                //phục hồi dữ liệu người dùng
                database = SQLiteDataController.getInstance(mContext);
                try {
                    database.isCreatedDatabase();
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
                database.insertUser(allUser);
                database.insertUserData(allUserData);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            processUpdate.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);
            processUpdate.dismiss();
            Toast.makeText(mContext, "Đã cập nhật CTDT mới nhất", Toast.LENGTH_LONG).show();
        }
    }
}
