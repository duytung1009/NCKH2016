package com.nckh2016.vuduytung.nckh2016;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.MyContract;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.animation.AnimatorPath;
import com.nckh2016.vuduytung.nckh2016.animation.PathEvaluator;
import com.nckh2016.vuduytung.nckh2016.main.BaseActivity;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;

public class ThongTinCaNhanActivity extends BaseActivity {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUser objectUser;
    //các asynctask
    MainTask mainTask;
    //các view
    TextView txtMaSinhVien, txtTenSinhVien, txtKhoa, txtNganh, txtChuyenSau, txtNamThu;
    Button btnAddYear;
    FloatingActionButton mFab;
    //test
    private FrameLayout mFabContainer;
    private LinearLayout mControlsContainer;
    private boolean mRevealFlag;
    public final static float SCALE_FACTOR      = 20f;
    public final static int ANIMATION_DURATION  = 300;
    public final static int MINIMUN_X_DISTANCE  = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_thong_tin_ca_nhan);
        //test
        mFabContainer = (FrameLayout) findViewById(R.id.fab_container);
        mControlsContainer = (LinearLayout)findViewById(R.id.layout1);

        txtMaSinhVien = (TextView)findViewById(R.id.txtMaSinhVien);
        txtTenSinhVien = (TextView)findViewById(R.id.txtTenSinhVien);
        txtKhoa = (TextView)findViewById(R.id.txtKhoa);
        txtNganh = (TextView)findViewById(R.id.txtNganh);
        txtChuyenSau = (TextView)findViewById(R.id.txtChuyenSau);
        txtNamThu = (TextView)findViewById(R.id.txtNamThu);
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabPressed(v);
            }
        });
        btnAddYear = (Button)findViewById(R.id.btnAddYear);
        btnAddYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_user != null && objectUser != null){
                    SQLiteDataController data = SQLiteDataController.getInstance(getApplicationContext());
                    try{
                        data.isCreatedDatabase();
                    }
                    catch (IOException e){
                        Log.e("tag", e.getMessage());
                    }
                    ContentValues newData = new ContentValues();
                    newData.put(MyContract.UserEntry.COLUMN_NAM_HOC, (Integer.parseInt(objectUser.getNamhoc()) + 1));
                    data.updateNguoiDung(current_user, newData);
                    if(mainTask != null){
                        if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                            mainTask.cancel(true);
                        }
                    }
                    mainTask = new MainTask(getApplicationContext());
                    mainTask.execute();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences currentUserData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        mainTask = new MainTask(this);
        mainTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    public void onFabPressed(View view) {
        final float startX = mFab.getX();

        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.curveTo(-200, 200, -400, 100, -600, 50);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(ANIMATION_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Math.abs(startX - mFab.getX()) > MINIMUN_X_DISTANCE) {
                    if (!mRevealFlag) {
                        mFab.animate()
                                .scaleXBy(SCALE_FACTOR)
                                .scaleYBy(SCALE_FACTOR)
                                .setListener(mEndRevealListener)
                                .setDuration(ANIMATION_DURATION);

                        mRevealFlag = true;
                    }
                }
            }
        });
    }

    private AnimatorListenerAdapter mEndRevealListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            mFab.setVisibility(View.INVISIBLE);

            for (int i = 0; i < mControlsContainer.getChildCount(); i++) {
                View v = mControlsContainer.getChildAt(i);
                ViewPropertyAnimator animator = v.animate()
                        .scaleX(1).scaleY(1)
                        .setDuration(ANIMATION_DURATION);

                animator.setStartDelay(i * 50);
                animator.start();
            }
        }
    };

    private class MainTask extends AsyncTask<Void, Long, ObjectUser>{
        private Context mContext;
        private String tenKhoa, tenNganh, tenChuyenSau;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ObjectUser doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try{
                data.isCreatedDatabase();
            }
            catch (IOException e){
                Log.e("tag", e.getMessage());
            }
            objectUser = data.getUser(current_user);
            tenKhoa = data.getTenKhoa(objectUser.getMakhoa());
            tenNganh = data.getTenNganh(objectUser.getManganh());
            tenChuyenSau = data.getTenChuyenSau(objectUser.getManganh(), Integer.parseInt(objectUser.getMachuyensau()));
            return objectUser;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ObjectUser objectUser) {
            super.onPostExecute(objectUser);
            if(objectUser.getNamhoc().equals("5") || (objectUser.getNamhoc().equals("4") && objectUser.getMakhoa().equals("7"))){
                btnAddYear.setVisibility(View.GONE);
            }
            txtMaSinhVien.setText(objectUser.getMasv());
            txtTenSinhVien.setText(objectUser.getHoten());
            txtKhoa.setText(tenKhoa);
            txtNganh.setText(tenNganh);
            txtChuyenSau.setText(tenChuyenSau);
            txtNamThu.setText(objectUser.getNamhoc());
        }
    }
}