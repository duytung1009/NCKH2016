package com.nckh2016.vuduytung.nckh2016.main;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.BackupActivity;
import com.nckh2016.vuduytung.nckh2016.BoMonActivity;
import com.nckh2016.vuduytung.nckh2016.GioiThieuActivity;
import com.nckh2016.vuduytung.nckh2016.HuongDanSuDungActivity;
import com.nckh2016.vuduytung.nckh2016.MainActivity;
import com.nckh2016.vuduytung.nckh2016.NganhActivity;
import com.nckh2016.vuduytung.nckh2016.QuanLyKeHoachHocTapActivity;
import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.SearchResultActivity;
import com.nckh2016.vuduytung.nckh2016.ThongTinCaNhanActivity;
import com.nckh2016.vuduytung.nckh2016.XemNhanhActivity;

public class BaseNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_TENSINHVIEN = "user_name";
    //các biến được khôi phục lại nếu app resume
    private String current_user_masv = null;
    private String current_user_hoten = null;
    //các view
    public Toolbar toolbar;
    public SmoothActionBarDrawerToggle toggle;
    public AppBarLayout appBarLayout;
    public NavigationView navigationView;
    public DrawerLayout fullLayout;
    public FrameLayout frameLayout;
    public CoordinatorLayout coordinatorLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_nav);
    }

    @Override
    public void setContentView(int layoutResID) {
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_nav, null);
        frameLayout = (FrameLayout) fullLayout.findViewById(R.id.drawer_frame);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(fullLayout);
        //http://stackoverflow.com/questions/30289203/android-toolbar-occupies-full-screen
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //disable item icon tint color
        navigationView.setItemIconTintList(null);
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(currentUserData.getString(SUB_PREFS_MASINHVIEN, null) == null || currentUserData.getString(SUB_PREFS_TENSINHVIEN, null) == null){
            //chua co du lieu
        } else{
            current_user_hoten = currentUserData.getString(SUB_PREFS_TENSINHVIEN, null);
            current_user_masv = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
            View headerView = navigationView.getHeaderView(0);
            TextView txtNavTenSinhVien = (TextView) headerView.findViewById(R.id.txtNavTenSinhVien);
            TextView txtNavMaSinhVien = (TextView) headerView.findViewById(R.id.txtNavMaSinhVien);
            txtNavTenSinhVien.setText(current_user_hoten);
            txtNavMaSinhVien.setText(current_user_masv);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationView();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        current_user_hoten = currentUserData.getString(SUB_PREFS_TENSINHVIEN, null);
        current_user_masv = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
    }

    public void updateNavigationView(){
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        View headerView = navigationView.getHeaderView(0);
        TextView txtNavTenSinhVien = (TextView) headerView.findViewById(R.id.txtNavTenSinhVien);
        TextView txtNavMaSinhVien = (TextView) headerView.findViewById(R.id.txtNavMaSinhVien);
        if(currentUserData.getString(SUB_PREFS_MASINHVIEN, null) != null){
            current_user_hoten = currentUserData.getString(SUB_PREFS_TENSINHVIEN, null);
            current_user_masv = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
            txtNavTenSinhVien.setText(current_user_hoten);
            txtNavMaSinhVien.setText(current_user_masv);
        } else {
            String hoten = getResources().getString(R.string.txthoten);
            String masinhvien = getResources().getString(R.string.txtmasinhvien);
            txtNavTenSinhVien.setText(hoten);
            txtNavMaSinhVien.setText(masinhvien);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_0) {
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    if(current_user_masv == null){
                        Toast.makeText(getApplicationContext(), "Bạn cần tạo hồ sơ trước", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseNavActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else if (id == R.id.nav_1) {
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    if(current_user_masv == null){
                        Toast.makeText(getApplicationContext(), "Bạn cần tạo hồ sơ trước", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseNavActivity.this, ThongTinCaNhanActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else if (id == R.id.nav_2) {
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    if(current_user_masv == null){
                        Toast.makeText(getApplicationContext(), "Bạn cần tạo hồ sơ trước", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseNavActivity.this, QuanLyKeHoachHocTapActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else if (id == R.id.nav_3) {
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    if(current_user_masv == null){
                        Toast.makeText(getApplicationContext(), "Bạn cần tạo hồ sơ trước", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseNavActivity.this, XemNhanhActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else if (id == R.id.nav_4) {
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    if(current_user_masv == null){
                        Toast.makeText(getApplicationContext(), "Bạn cần tạo hồ sơ trước", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseNavActivity.this, BackupActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } /*else if (id == R.id.nav_5) {

        } else if (id == R.id.nav_6) {

        }*/ else if (id == R.id.nav_niengiam_1){
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(BaseNavActivity.this, NganhActivity.class);
                    startActivity(intent);
                }
            });
        } else if (id == R.id.nav_niengiam_2){
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(BaseNavActivity.this, BoMonActivity.class);
                    startActivity(intent);
                }
            });
        } else if (id == R.id.nav_niengiam_3){
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(BaseNavActivity.this, HuongDanSuDungActivity.class);
                    startActivity(intent);
                }
            });
        } else if (id == R.id.nav_niengiam_4){
            toggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(BaseNavActivity.this, GioiThieuActivity.class);
                    startActivity(intent);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //http://stackoverflow.com/questions/18343018/optimizing-drawer-and-activity-launching-speed
    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {
        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }
}
