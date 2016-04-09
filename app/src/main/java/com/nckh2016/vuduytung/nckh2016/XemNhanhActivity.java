package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class XemNhanhActivity extends BaseActivity
        implements XemNhanhFragment1.OnFragmentInteractionListener, XemNhanhFragment2.OnFragmentInteractionListener, XemNhanhFragment3.OnFragmentInteractionListener{
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_xemnhanh_activity";
    public static final String SUB_PREFS_TABLAYOUTSTATE = "tab_position";
    //các view
    TabLayout tabLayout;
    TabsPagerAdapter mAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mon_hoc_chua_qua);
        appBarLayout.addView(View.inflate(this, R.layout.tabhost_layout, null));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        int position = state.getInt(SUB_PREFS_TABLAYOUTSTATE, 0);
        try{
            tabLayout.getTabAt(position).select();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putInt(SUB_PREFS_TABLAYOUTSTATE, tabLayout.getSelectedTabPosition());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //xóa vị trí tab
        SharedPreferences state = getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putInt(SUB_PREFS_TABLAYOUTSTATE, 0);
        editor.apply();
    }

    public void loadTabs(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.whiteTransparent), ContextCompat.getColor(this, R.color.white));
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(R.string.mon_hoc_chua_qua_activity_tab1_name), 0);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.mon_hoc_chua_qua_activity_tab2_name), 1);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.mon_hoc_chua_qua_activity_tab3_name), 2);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
    public void onFragmentInteraction(Uri uri) {

    }

    public class TabsPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public TabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:{
                    return XemNhanhFragment1.newInstance(null, null);
                }
                case 1:{
                    return XemNhanhFragment2.newInstance(null, null);
                }
                case 2:{
                    return XemNhanhFragment3.newInstance(null, null);
                }
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
