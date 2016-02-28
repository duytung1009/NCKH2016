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
        switch (mNumOfTabs){
            case 1:
                FragmentNienGiam tab = new FragmentNienGiam();
                return tab;
            case 3:
                switch (position) {
                    case 0:
                        FragmentNguoiDung tab1 = new FragmentNguoiDung();
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
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        switch (object.getClass().getSimpleName()){
            case "FragmentNguoiDung":
                return 0;
            case "FragmentQuaTrinhHocTap":
                return 1;
            case "FragmentNienGiam":
                return 2;
            default:
                return super.getItemPosition(object);
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
