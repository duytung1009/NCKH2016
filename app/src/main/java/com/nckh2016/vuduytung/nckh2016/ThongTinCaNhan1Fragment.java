package com.nckh2016.vuduytung.nckh2016;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.animation.AnimatorPath;
import com.nckh2016.vuduytung.nckh2016.animation.PathEvaluator;
import com.nckh2016.vuduytung.nckh2016.animation.PathPoint;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThongTinCaNhan1Fragment extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUser objectUser;
    //các asynctask
    MainTask mainTask;
    //các view
    TextView txtKhoa, txtNganh, txtChuyenSau, txtNamThu;
    ImageButton mFab;
    //test
    private FrameLayout mFabContainer;
    private float mFabSize;
    private boolean mRevealFlag;
    public final static float SCALE_FACTOR      = 20f;
    public final static int ANIMATION_DURATION  = 200;
    public final static int MINIMUN_X_DISTANCE  = 200;

    public ThongTinCaNhan1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_tin_ca_nhan_1, container, false);
        mFabContainer = (FrameLayout) view.findViewById(R.id.fab_container_1);
        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        txtKhoa = (TextView)view.findViewById(R.id.txtKhoa);
        txtNganh = (TextView)view.findViewById(R.id.txtNganh);
        txtChuyenSau = (TextView)view.findViewById(R.id.txtChuyenSau);
        txtNamThu = (TextView)view.findViewById(R.id.txtNamThu);
        mFab = (ImageButton)view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabPressed(v);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    public void onFabPressed(View view) {
        mFab.setImageResource(android.R.color.transparent);
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

    /**
     * We need this setter to translate between the information the animator
     * produces (a new "PathPoint" describing the current animated location)
     * and the information that the button requires (an xy location). The
     * setter will be called by the ObjectAnimator given the 'fabLoc'
     * property string.
     */
    public void setFabLoc(PathPoint newLoc) {
        mFab.setTranslationX(newLoc.mX);

        if (mRevealFlag)
            mFab.setTranslationY(newLoc.mY - (mFabSize / 2));
        else
            mFab.setTranslationY(newLoc.mY);
    }

    private AnimatorListenerAdapter mEndRevealListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            mFab.setVisibility(View.INVISIBLE);
            mFabContainer.removeAllViews();
            mFabContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ((ThongTinCaNhanActivity)getActivity()).loadFragment2();
        }
    };

    private class MainTask extends AsyncTask<Void, Long, ObjectUser> {
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
            txtKhoa.setText(tenKhoa);
            txtNganh.setText(tenNganh);
            txtChuyenSau.setText(tenChuyenSau);
            txtNamThu.setText(objectUser.getNamhoc());
            ViewPropertyAnimator animator = mFab.animate()
                    .scaleX(1).scaleY(1)
                    .setDuration(ANIMATION_DURATION);
            animator.start();
        }
    }
}
