package com.nckh2016.vuduytung.nckh2016;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class BackupActivity extends BaseActivity
        implements BackupFragment1.OnFragmentInteractionListener, BackupFragment2.OnFragmentInteractionListener{
    //các view
    TabLayout tabLayout;
    TabsPagerAdapter mAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_backup);
        appBarLayout.addView(View.inflate(this, R.layout.tabhost_layout, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTabs();
    }

    public void loadTabs(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.whiteTransparent), ContextCompat.getColor(this, R.color.white));
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(R.string.txtMemory), 0);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.txtGoogleDrive), 1);
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
                case 0:
                    return BackupFragment1.newInstance(null, null);
                case 1:
                    return BackupFragment2.newInstance(null, null);
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
