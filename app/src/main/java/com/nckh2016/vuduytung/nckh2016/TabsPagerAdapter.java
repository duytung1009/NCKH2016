package com.nckh2016.vuduytung.nckh2016;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Tung on 19/2/2016.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    FragmentNguoiDung tab1;
    FragmentQuaTrinhHocTap tab2;
    FragmentNienGiam tab3;
    int mNumOfTabs;

    public TabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (mNumOfTabs){
            case 1:
                if(tab3 == null){
                    tab3 = new FragmentNienGiam();
                }
                return tab3;
            case 3:
                switch (position) {
                    case 0:
                        if(tab1 == null){
                            tab1 = FragmentNguoiDung.newInstance(null, null);
                        }
                        return tab1;
                    case 1:
                        if(tab2 == null){
                            tab2 = FragmentQuaTrinhHocTap.newInstance(null,null);
                        }
                        return tab2;
                    case 2:
                        if(tab3 == null){
                            tab3 = FragmentNienGiam.newInstance(null, null);
                        }
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
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
