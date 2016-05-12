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

import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChiTietMonHocActivity extends AppCompatActivity {
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_chitietmonhoc_activity";
    public static final String SUB_PREFS_MAMONHOC = "maMonHoc";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private String maMonHoc = null;
    private ArrayList<Boolean> monHocDaQua = new ArrayList<Boolean>();
    //các view
    MainTask mainTask;
    ImageView imageView;
    TextView txtMaMonHoc, txtTenMonHoc, txtTinChi, txtNoiDung, txtTaiLieu, txtDiem, txtDiem2;
    LinearLayout listViewMonHocDieuKien;
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
        imageView = (ImageView) findViewById(R.id.imageView);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        txtMaMonHoc = (TextView) findViewById(R.id.txtMaMonHoc);
        txtTenMonHoc = (TextView) findViewById(R.id.txtTieuDe);
        txtTinChi = (TextView) findViewById(R.id.txtTinChi);
        listViewMonHocDieuKien = (LinearLayout) findViewById(R.id.listViewMonHocDieuKien);
        //txtDieuKien = (TextView) findViewById(R.id.txtDieuKien);
        txtNoiDung = (TextView) findViewById(R.id.txtNoiDung);
        txtTaiLieu = (TextView) findViewById(R.id.txtTaiLieu);
        btnBangDiem = (Button) findViewById(R.id.btnBangDiem);
        ab = getSupportActionBar();
        SharedPreferences currentUserData = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
        current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        maMonHoc = getIntent().getStringExtra(Utils.MA_MON_HOC);
        rightLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (maMonHoc != null && !maMonHoc.isEmpty()) {
            mainTask = new MainTask(this);
            mainTask.execute(maMonHoc);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
        if (current_user == null) {
            current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        }
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        if (maMonHoc == null) {
            maMonHoc = state.getString(SUB_PREFS_MAMONHOC, null);
            if (maMonHoc != null && !maMonHoc.isEmpty()) {
                if (mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                    mainTask.cancel(true);
                }
                mainTask = new MainTask(this);
                mainTask.execute(maMonHoc);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putString(SUB_PREFS_MAMONHOC, maMonHoc);
        editor.apply();
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

    private class MainTask extends AsyncTask<String, Long, ObjectMonHoc> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listViewMonHocDieuKien.removeAllViews();
        }

        @Override
        protected ObjectMonHoc doInBackground(String... params) {
            // Joining:
            StringBuilder buffer = new StringBuilder();
            for (String each : params)
                buffer.append(",").append(each);
            String joined = buffer.deleteCharAt(0).toString();
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            ObjectMonHoc result = new ObjectMonHoc();
            try {
                result = data.getMonHoc(joined);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null) {
                if (result.getMamh() != null && !result.getMamh().isEmpty()) {
                    if (current_user != null) {
                        result.setDiem(data.getDiem(current_user, result.getMamh()));
                        //check loại môn học
                        ObjectUser user = data.getUser(current_user);
                        if (data.checkTuChon(current_user, result.getMamh(), user.getManganh(), -3)) {
                            result.setTuchon("A");
                        } else if (data.checkTuChon(current_user, result.getMamh(), user.getMakhoa() + "0", -2)) {
                            result.setTuchon("B");
                        } else if (data.checkTuChon(current_user, result.getMamh(), "1", -1)) {
                            result.setTuchon("C");
                        } else if (data.checkHocPhanTheDuc(result.getMamh())) {
                            result.setTuchon("TD");
                        } else {
                            result.setTuchon(null);
                        }
                    }
                    //lấy tên môn học điều kiện
                    monHocDaQua.clear();
                    String dieukien = "";
                    String madieukien = result.getDieukien();
                    if (madieukien != null) {
                        if (madieukien.length() >= 7) {
                            String[] items = madieukien.split(",");
                            for (String item : items) {
                                if (data.getTenMonHoc(item) != null) {
                                    dieukien += data.getTenMonHoc(item);
                                    dieukien += "\n";
                                    if (current_user != null) {
                                        monHocDaQua.add(data.checkMonHocChuaQua(current_user, item));
                                    }
                                }
                            }
                        }
                    }
                    result.setDieukien(dieukien);
                    return result;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ObjectMonHoc objectMonHoc) {
            super.onPostExecute(objectMonHoc);
            if (objectMonHoc != null) {
                ab.setSubtitle(objectMonHoc.getMamh());
                if(objectMonHoc.getTuchon() != null){
                    if (objectMonHoc.getTuchon().equals("A")) {
                        imageView.setImageResource(R.drawable.tuchon_a);
                    } else if (objectMonHoc.getTuchon().equals("B")) {
                        imageView.setImageResource(R.drawable.tuchon_b);
                    } else if (objectMonHoc.getTuchon().equals("C")) {
                        imageView.setImageResource(R.drawable.tuchon_c);
                    } else if (objectMonHoc.getTuchon().equals("TD")) {
                        imageView.setImageResource(R.drawable.sport);
                    } else {
                        imageView.setImageResource(R.drawable.literature);
                    }
                } else {
                    imageView.setImageResource(R.drawable.literature);
                }
                txtMaMonHoc.setText(objectMonHoc.getMamh());
                txtTenMonHoc.setSingleLine(false);
                txtTenMonHoc.setText(objectMonHoc.getTenmh());
                String tinChi = "...";
                if (objectMonHoc.getTinchi() != null) {
                    tinChi = objectMonHoc.getTinchi().toString();
                }
                txtTinChi.setText(tinChi);
                String madieukien = objectMonHoc.getDieukien();
                View view;
                ImageView imageViewMonHocDieuKien;
                TextView textViewMonHocDieuKien;
                if (madieukien.isEmpty()) {
                    view = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
                    imageViewMonHocDieuKien = (ImageView) view.findViewById(R.id.imageViewMonHocDieuKien);
                    textViewMonHocDieuKien = (TextView) view.findViewById(R.id.textViewMonHocDieuKien);
                    imageViewMonHocDieuKien.setVisibility(View.GONE);
                    textViewMonHocDieuKien.setText(getResources().getString(R.string.khong));
                    listViewMonHocDieuKien.addView(view);
                } else {
                    int i = 0;
                    String[] items = madieukien.split("\n");
                    for (String item : items) {
                        view = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
                        imageViewMonHocDieuKien = (ImageView) view.findViewById(R.id.imageViewMonHocDieuKien);
                        textViewMonHocDieuKien = (TextView) view.findViewById(R.id.textViewMonHocDieuKien);
                        if (monHocDaQua.get(i)) {
                            imageViewMonHocDieuKien.setImageResource(R.drawable.circle);
                        } else {
                            imageViewMonHocDieuKien.setImageResource(R.drawable.check);
                        }
                        i++;
                        textViewMonHocDieuKien.setText(item);
                        listViewMonHocDieuKien.addView(view);
                    }
                }
                //txtDieuKien.setText(objectMonHoc.getDieukien());
                txtNoiDung.setText(objectMonHoc.getNoidung());
                txtTaiLieu.setText(objectMonHoc.getTailieu());
                //nếu có điểm
                if (objectMonHoc.getDiem() != -1) {
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
                    if (objectMonHoc.getDiem() < 4) {
                        txtDiem.setText("F");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemF));
                    } else if (objectMonHoc.getDiem() < 5.5) {
                        txtDiem.setText("D");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemD));
                    } else if (objectMonHoc.getDiem() < 7) {
                        txtDiem.setText("C");
                        txtDiem.setTextColor(ContextCompat.getColor(mContext, R.color.diemC));
                    } else if (objectMonHoc.getDiem() < 8.5) {
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
