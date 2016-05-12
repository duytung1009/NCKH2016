package com.nckh2016.vuduytung.nckh2016.main;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Tung on 30/3/2016.
 * chỗ để nhét tất cả những cái tạp nham (global)
 */
public class Utils {
    //các giá trị Preferences Global (lưu thông tin sinh viên hiện tại)
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    public static final String SUB_PREFS_TENSINHVIEN = "user_name";
    public static final String SUB_PREFS_DATASINHVIEN = "user_data";
    //các giá trị global - đang update...
    public static final String MA_MON_HOC = "MaMonHoc";
    public static final String BACKUP_FILE_PATTERN = "_backup.txt";
    //danh sách lọc
    public static final String[] DANH_SACH_NGANH_HOC_4_NAM = new String[]{"701", "702"};
    public static final String[] DANH_SACH_HOC_PHAN_THE_DUC = new String[] {"4010701","4010702","4010703","4010704","4010705"};
    public static final String[] DANH_SACH_BO_QUA = new String[] {"4010701","4010702","4010703","4010704","4010705","4080508","4080509"};

    public static void showProcessBar(Context context, View processBar, View content){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        processBar.startAnimation(animFadeIn);
        content.setVisibility(View.GONE);
        processBar.setVisibility(View.VISIBLE);
    }

    public static void hideProcessBar(Context context, View processBar, View content){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_out);
        content.startAnimation(animFadeIn);
        processBar.startAnimation(animFadeOut);
        content.setVisibility(View.VISIBLE);
        processBar.setVisibility(View.GONE);
    }

    public static void switchView(Context context, View viewOut, View viewIn){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_out);
        viewIn.startAnimation(animFadeIn);
        viewOut.startAnimation(animFadeOut);
        viewIn.setVisibility(View.VISIBLE);
        viewOut.setVisibility(View.GONE);
    }
}
