package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
    TextView txtMaMonHoc, txtTenMonHoc, txtTinChi, txtDieuKien, txtNoiDung, txtTaiLieu, txtDiem, txtDiem2;
    Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*String value = getIntent().getStringExtra("caller");
        switch (value){
            case "BoMonActivity":
                startActivity(new Intent(this, BoMonActivity.class));
                finish();
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                finish();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_hoc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        txtMaMonHoc = (TextView) findViewById(R.id.txtMaMonHoc);
        txtTenMonHoc = (TextView) findViewById(R.id.txtTieuDe);
        txtTinChi = (TextView) findViewById(R.id.txtTinChi);
        txtDieuKien = (TextView) findViewById(R.id.txtDieuKien);
        txtNoiDung = (TextView) findViewById(R.id.txtNoiDung);
        txtTaiLieu = (TextView) findViewById(R.id.txtTaiLieu);
        String maMonHoc = getIntent().getStringExtra("MaMonHoc");
        SQLiteDataController data = SQLiteDataController.getInstance(this);
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        if(maMonHoc!=null && maMonHoc.isEmpty() == false){
            ObjectMonHoc mMonHoc = (ObjectMonHoc)data.getMonHoc(maMonHoc).get(0);
            ActionBar ab = getSupportActionBar();
            ab.setSubtitle(mMonHoc.getMamh());
            imageView.setImageResource(R.drawable.literature);
            txtMaMonHoc.setText(mMonHoc.getMamh());
            txtTenMonHoc.setSingleLine(false);
            txtTenMonHoc.setText(mMonHoc.getTenmh());
            txtTinChi.setText(mMonHoc.getTinchi());
            txtDieuKien.setText(mMonHoc.getDieukien());
            txtNoiDung.setText(mMonHoc.getNoidung());
            txtTaiLieu.setText(mMonHoc.getTailieu());
            SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            current_user = currentUserData.getString("user_mssv", null);
            if(current_user != null){
                float diemSo = data.getDiem(current_user, mMonHoc.getMamh());
                if(diemSo != -1){
                    txtDiem = (TextView) findViewById(R.id.txtDiem);
                    txtDiem.setPadding(40,0,60,0);
                    txtDiem.setTypeface(light);
                    txtDiem2 = (TextView) findViewById(R.id.txtDiem2);
                    txtDiem2.setPadding(40,0,60,0);
                    txtDiem2.setText(new DecimalFormat("####0.00").format(diemSo));
                    if(diemSo < 4){
                        txtDiem.setText("F");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemF));
                    } else if(diemSo < 5.5){
                        txtDiem.setText("D");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemD));
                    } else if(diemSo < 7){
                        txtDiem.setText("C");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemC));
                    } else if(diemSo < 8.5){
                        txtDiem.setText("B");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemB));
                    } else {
                        txtDiem.setText("A");
                        txtDiem.setTextColor(ContextCompat.getColor(this, R.color.diemA));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
}
