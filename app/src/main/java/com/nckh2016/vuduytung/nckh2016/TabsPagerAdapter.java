package com.nckh2016.vuduytung.nckh2016;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Tung on 19/2/2016.
 */
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
                FragmentDangKy tab1 = new FragmentDangKy();
                return tab1;
            case 1:
                FragmentQuaTrinhHocTap tab2 = new FragmentQuaTrinhHocTap();
                return tab2;
            case 2:
                FragmentNienGiam tab3 = new FragmentNienGiam();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
