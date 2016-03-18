package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.text.DecimalFormat;

public class ChiTietMonHocActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    MainTask mainTask;
    String maMonHoc;
    TextView txtMaMonHoc, txtTenMonHoc, txtTinChi, txtDieuKien, txtNoiDung, txtTaiLieu, txtDiem, txtDiem2;
    Button btnBangDiem;
    LinearLayout rightLayout;
    ActionBar ab;
    Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_hoc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.literature);
        rightLayout = (LinearLayout)findViewById(R.id.rightLayout);
        txtMaMonHoc = (TextView) findViewById(R.id.txtMaMonHoc);
        txtTenMonHoc = (TextView) findViewById(R.id.txtTieuDe);
        txtTinChi = (TextView) findViewById(R.id.txtTinChi);
        txtDieuKien = (TextView) findViewById(R.id.txtDieuKien);
        txtNoiDung = (TextView) findViewById(R.id.txtNoiDung);
        txtTaiLieu = (TextView) findViewById(R.id.txtTaiLieu);
        ab = getSupportActionBar();
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        maMonHoc = getIntent().getStringExtra("MaMonHoc");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(maMonHoc!=null && maMonHoc.isEmpty() == false){
            mainTask = new MainTask(this);
            mainTask.execute(maMonHoc);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
            mainTask.cancel(true);
        }
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

    private class MainTask extends AsyncTask<String, Long, ObjectMonHoc>{
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ObjectMonHoc doInBackground(String... params) {
            // Joining:
            StringBuilder buffer = new StringBuilder();
            for (String each : params)
                buffer.append(",").append(each);
            String joined = buffer.deleteCharAt(0).toString();
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try{
                data.isCreatedDatabase();
            }
            catch (IOException e){
                Log.e("tag", e.getMessage());
            }
            ObjectMonHoc mMonHoc = (ObjectMonHoc)data.getMonHoc(joined).get(0);
            if(current_user != null){
                mMonHoc.setDiem(data.getDiem(current_user, mMonHoc.getMamh()));
            }
            return mMonHoc;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ObjectMonHoc objectMonHoc) {
            super.onPostExecute(objectMonHoc);
            if(objectMonHoc != null){
                ab.setSubtitle(objectMonHoc.getMamh());
                txtMaMonHoc.setText(objectMonHoc.getMamh());
                txtTenMonHoc.setSingleLine(false);
                txtTenMonHoc.setText(objectMonHoc.getTenmh());
                txtTinChi.setText(objectMonHoc.getTinchi().toString());
                txtDieuKien.setText(objectMonHoc.getDieukien());
                txtNoiDung.setText(objectMonHoc.getNoidung());
                txtTaiLieu.setText(objectMonHoc.getTailieu());
                if(objectMonHoc.getDiem() != -1){
                    btnBangDiem = (Button)findViewById(R.id.btnBangDiem);
                    btnBangDiem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getBaseContext(), BangDiemActivity.class);
                            intent.putExtra("MaMonHoc", maMonHoc);
                            startActivity(intent);
                        }
                    });
                    txtDiem = (TextView) findViewById(R.id.txtDiem);
                    txtDiem.setTypeface(light);
                    txtDiem2 = (TextView) findViewById(R.id.txtDiem2);
                    txtDiem2.setText(new DecimalFormat("####0.00").format(objectMonHoc.getDiem()));
                    if(objectMonHoc.getDiem() < 4){
                        txtDiem.setText("F");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemF));
                    } else if(objectMonHoc.getDiem() < 5.5){
                        txtDiem.setText("D");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemD));
                    } else if(objectMonHoc.getDiem() < 7){
                        txtDiem.setText("C");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemC));
                    } else if(objectMonHoc.getDiem() < 8.5){
                        txtDiem.setText("B");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemB));
                    } else {
                        txtDiem.setText("A");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemA));
                    }
                    rightLayout.setVisibility(View.VISIBLE);
                } else {
                    rightLayout.setVisibility(View.GONE);
                }
            }
        }
    }
}
