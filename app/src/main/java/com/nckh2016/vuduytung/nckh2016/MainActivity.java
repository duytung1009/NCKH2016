package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectUser;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements FragmentQuaTrinhHocTap.OnFragmentInteractionListener, FragmentNguoiDung.OnFragmentInteractionListener, FragmentNienGiam.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    ArrayList<Object> mListUser;
    TabLayout tabLayout;
    TabsPagerAdapter mAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(mAdapter == null)){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void loadTabs(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.removeAllTabs();

        // Restore preferences
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if((currentUserData.getString("user_mssv", null) == null) || (currentUserData.getString("user_mssv", null).isEmpty())){
            //form dang ky
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 0);
        } else{
            current_user = currentUserData.getString("user_mssv", null);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab1_name), 0);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab2_name), 1);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.main_activity_tab3_name), 2);
        }

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        //Hide title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Inflate the menu; this adds items to the action bar if it is present.
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
        SQLiteDataController data = SQLiteDataController.getInstance(getApplicationContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        if(!data.getUser().isEmpty()){
            mListUser = data.getUser();
            List<String> mListTenUser = new ArrayList<String>();
            for (Object object : mListUser) {
                ObjectUser value = (ObjectUser) object;
                mListTenUser.add(value != null ? value.getHoten() : null);
            }
            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mListTenUser);
            Spinner spinnerNguoiDung = (Spinner) findViewById(R.id.spinnerNguoiDung);
            spinnerNguoiDung.setAdapter(mAdapter);
            spinnerNguoiDung.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("user_mssv", ((ObjectUser)mListUser.get(position)).getMasv());
                    editor.putString("user_name", ((ObjectUser)mListUser.get(position)).getHoten());
                    editor.putString("user_data", ((ObjectUser)mListUser.get(position)).getHocky());
                    editor.commit();
                    if(tabLayout.getTabCount() > 0){
                        (tabLayout.getTabAt(0)).select();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (current_user != null) {
                int index = 0;
                for (Object object : mListUser) {
                    ObjectUser value = (ObjectUser) object;
                    if (value.getMasv().equals(current_user)){
                        index = mListUser.indexOf(value);
                    }
                }
                spinnerNguoiDung.setSelection(index);
            }
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            super.onResume();
            recreate();
            tabLayout.removeAllTabs();
            // Restore preferences
            SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            if((currentUserData.getString("user_mssv", null) == null) || (currentUserData.getString("user_mssv", null).isEmpty())){
                //form dang ky
                tabLayout.addTab(tabLayout.newTab().setText("niên giám"), 0);
            } else{
                current_user = currentUserData.getString("user_mssv", null);
                tabLayout.addTab(tabLayout.newTab().setText("chức năng"), 0);
                tabLayout.addTab(tabLayout.newTab().setText("hồ sơ"), 1);
                tabLayout.addTab(tabLayout.newTab().setText("niên giám"), 2);
            }
            mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(mAdapter);
        }
    }
}
