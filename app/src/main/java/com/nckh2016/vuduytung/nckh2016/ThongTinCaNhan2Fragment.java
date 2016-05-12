package com.nckh2016.vuduytung.nckh2016;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.nckh2016.vuduytung.nckh2016.Data.MyContract;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectChuyenSau;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThongTinCaNhan2Fragment extends Fragment {
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ObjectUser objectUser;
    private ArrayList<Object> mListChuyenSau = new ArrayList<Object>();
    //các asynctask
    MainTask mainTask;
    //các view
    EditText editHoTen;
    Spinner spinnerChuyenSau;
    NumberPicker numberPicker;
    Button btnCancel, btnUpdate;
    //test
    private FrameLayout mControlsContainer;
    private float mFabSize;
    private boolean mRevealFlag;
    public final static float SCALE_FACTOR = 20f;
    public final static int ANIMATION_DURATION = 200;
    public final static int MINIMUN_X_DISTANCE = 200;

    public ThongTinCaNhan2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_tin_ca_nhan_2, container, false);
        mControlsContainer = (FrameLayout) view.findViewById(R.id.fab_container_2);
        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        editHoTen = (EditText) view.findViewById(R.id.editHoTen);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationSlideDown();
            }
        });
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hoTen = editHoTen.getText().toString();
                final int namThu = numberPicker.getValue();
                if (hoTen.isEmpty()) {
                    Toast.makeText(getContext(), "Chưa nhập họ tên", Toast.LENGTH_SHORT).show();
                } else {
                    if (namThu < Integer.parseInt(objectUser.getNamhoc())) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Cảnh báo")
                                .setMessage("Năm học bạn chọn nhỏ hơn năm học hiện tại (dữ liệu trong năm học sẽ bị loại bỏ nếu số năm học bị giảm đi)\nTiếp tục?")
                                .setIcon(R.drawable.error)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ContentValues userValue = new ContentValues();
                                        userValue.put(MyContract.UserEntry.COLUMN_HO_TEN, hoTen);
                                        userValue.put(MyContract.UserEntry.COLUMN_NAM_HOC, String.valueOf(namThu));
                                        if (spinnerChuyenSau.getVisibility() == View.VISIBLE && spinnerChuyenSau.getSelectedItemPosition() != 0) {
                                            String maChuyenSau = ((ObjectChuyenSau) mListChuyenSau.get((spinnerChuyenSau.getSelectedItemPosition() - 1))).getMachuyensau();
                                            userValue.put(MyContract.UserEntry.COLUMN_MA_CHUYEN_SAU, maChuyenSau);
                                        }
                                        dialog.dismiss();
                                        updateUser(userValue);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {
                        ContentValues userValue = new ContentValues();
                        userValue.put(MyContract.UserEntry.COLUMN_HO_TEN, hoTen);
                        userValue.put(MyContract.UserEntry.COLUMN_NAM_HOC, String.valueOf(namThu));
                        if (spinnerChuyenSau.getVisibility() == View.VISIBLE && spinnerChuyenSau.getSelectedItemPosition() != 0) {
                            String maChuyenSau = ((ObjectChuyenSau) mListChuyenSau.get((spinnerChuyenSau.getSelectedItemPosition() - 1))).getMachuyensau();
                            userValue.put(MyContract.UserEntry.COLUMN_MA_CHUYEN_SAU, maChuyenSau);
                        }
                        updateUser(userValue);
                    }
                }
            }
        });
        spinnerChuyenSau = (Spinner) view.findViewById(R.id.spinnerChuyenSau);
        spinnerChuyenSau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    Toast.makeText(getContext(), "Hướng chuyên sâu chỉ có thể chọn một lần\nĐã chọn " + spinnerChuyenSau.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        return view;
    }

    private void updateUser(ContentValues userValue) {
        ProgressDialog progressUpdate = new ProgressDialog(getContext());
        progressUpdate.setMessage("Cập nhật thông tin");
        progressUpdate.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressUpdate.setIndeterminate(true);
        progressUpdate.setProgressNumberFormat(null);
        progressUpdate.setProgressPercentFormat(null);
        progressUpdate.show();
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        if (current_user != null) {
            data.updateNguoiDung(current_user, userValue);
        }
        progressUpdate.dismiss();
        animationSlideDown();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        if (current_user == null) {
            current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        }
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mainTask != null) {
            if (mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    public void animationSlideDown() {
        Animation slideOutDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
        slideOutDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ThongTinCaNhanActivity) getActivity()).loadFragment1();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mControlsContainer.startAnimation(slideOutDown);
    }

    private class MainTask extends AsyncTask<Void, Long, ObjectUser> {
        private Context mContext;

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
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            objectUser = data.getUser(current_user);
            if (objectUser.getMachuyensau().equals("0")) {
                mListChuyenSau = data.getChuyenSauTheoNganh(objectUser.getManganh());
            }
            return objectUser;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ObjectUser objectUser) {
            super.onPostExecute(objectUser);
            editHoTen.setText(objectUser.getHoten());
            if (objectUser.getMachuyensau().equals("0")) {
                List<String> mListTenChuyenSau = new ArrayList<String>();
                mListTenChuyenSau.add(0, getResources().getString(R.string.txtChuaChonChuyenNganh));
                int i = 1;
                for (Object object : mListChuyenSau) {
                    ObjectChuyenSau value = (ObjectChuyenSau) object;
                    mListTenChuyenSau.add(i, value != null ? value.getTenchuyensau() : null);
                    i++;
                }
                ArrayAdapter<String> mChuyenSauAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, mListTenChuyenSau);
                mChuyenSauAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerChuyenSau.setAdapter(mChuyenSauAdapter);
                spinnerChuyenSau.setVisibility(View.VISIBLE);
            } else {
                spinnerChuyenSau.setVisibility(View.GONE);
            }
            int namhoc = -1;
            try {
                namhoc = Integer.parseInt(objectUser.getNamhoc());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (namhoc != -1) {
                numberPicker.setValue(namhoc);
            }
        }
    }
}
