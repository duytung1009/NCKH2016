package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link XemNhanhFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link XemNhanhFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class XemNhanhFragment extends Fragment {
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    //các view
    CircularProgressView progressBar;
    RelativeLayout mainLayout;
    LinearLayout listViewDieuKien;
    //các AsyncTask
    MainTask mainTask;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public XemNhanhFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment XemNhanhFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static XemNhanhFragment newInstance(String param1, String param2) {
        XemNhanhFragment fragment = new XemNhanhFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xem_nhanh, container, false);
        final SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        listViewDieuKien = (LinearLayout) view.findViewById(R.id.listViewDieuKien);
        progressBar = (CircularProgressView)view.findViewById(R.id.progressBar);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView) view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.quick_view);
        txtTieuDe.setText(getResources().getString(R.string.title_fragment_xemnhanh));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainTask != null) {
            if (mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MainTask extends AsyncTask<Void, Long, Void> {
        private Context mContext;
        private ObjectUser user;
        private double tongDiem = -1;
        private int tinChiTichLuy = 0;
        private int tongTinChi = -1;
        private ArrayList<Object> userHocPhanTheDuc = new ArrayList<>();

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProcessBar(getContext(), progressBar, mainLayout);
            listViewDieuKien.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            //tổng điểm
            tongDiem = data.tongDiem(current_user);
            //tổng tín chỉ
            int[] yData = data.soTinChi(current_user);
            for (int i = 2; i < yData.length; i++) {
                tongTinChi += yData[i];
            }
            user = data.getUser(current_user);
            if (data.checkNganhHoc4Nam(user.getManganh())) {
                tinChiTichLuy = 120;
            } else {
                tinChiTichLuy = 150;
            }
            //giáo dục thể chất
            userHocPhanTheDuc = data.getHocPhanTheDuc(current_user);
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //tích lũy tín chỉ
            View viewTongTinChi = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTongTinChi = (ImageView) viewTongTinChi.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTongTinChi = (TextView) viewTongTinChi.findViewById(R.id.textViewMonHocDieuKien);
            if (tongTinChi < tinChiTichLuy) {
                imageViewTongTinChi.setImageResource(R.drawable.circle);
            } else {
                imageViewTongTinChi.setImageResource(R.drawable.check);
            }
            textViewTongTinChi.setText("Tín chỉ tích lũy đạt tối thiểu " + tinChiTichLuy + " tín chỉ\nTín chỉ tích lũy hiện tại: " + tongTinChi + " tín chỉ");
            listViewDieuKien.addView(viewTongTinChi);
            //tổng điểm
            View viewTongDiem = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTongDiem = (ImageView) viewTongDiem.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTongDiem = (TextView) viewTongDiem.findViewById(R.id.textViewMonHocDieuKien);
            if (Double.compare(tongDiem, 2) < 0) {
                imageViewTongDiem.setImageResource(R.drawable.circle);
            } else {
                imageViewTongDiem.setImageResource(R.drawable.check);
            }
            textViewTongDiem.setText("Điểm tích lũy đạt từ 2,0 trở lên\nĐiểm tích lũy hiện tại: " + new DecimalFormat("####0.##").format(tongDiem));
            listViewDieuKien.addView(viewTongDiem);
            //học phần giáo dục thể chất
            View viewGiaoDucTheChat = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewGiaoDucTheChat = (ImageView) viewGiaoDucTheChat.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewGiaoDucTheChat = (TextView) viewGiaoDucTheChat.findViewById(R.id.textViewMonHocDieuKien);
            if (userHocPhanTheDuc.size() < 5) {
                imageViewGiaoDucTheChat.setImageResource(R.drawable.circle);
                textViewGiaoDucTheChat.setText("Hoàn thành học phần giáo dục thể chất\nCòn thiếu: " + String.valueOf(5 - userHocPhanTheDuc.size()) + " học phần");
            } else {
                double tongDiemGDTC = 0;
                for (Object value : userHocPhanTheDuc) {
                    if (((ObjectMonHoc) value).getDiem() != -1) {
                        tongDiemGDTC += ((ObjectMonHoc) value).getDiem();
                    }
                }
                tongDiemGDTC = tongDiemGDTC/5;
                if (Double.compare(tongDiemGDTC, 5.5) < 0) {
                    imageViewGiaoDucTheChat.setImageResource(R.drawable.circle);
                    textViewGiaoDucTheChat.setText("Hoàn thành học phần giáo dục thể chất\nTổng điểm hiện tại: " + new DecimalFormat("####0.##").format(tongDiemGDTC));
                } else {
                    imageViewGiaoDucTheChat.setImageResource(R.drawable.check);
                    textViewGiaoDucTheChat.setText("Hoàn thành học phần giáo dục thể chất\nTổng điểm hiện tại: " + new DecimalFormat("####0.##").format(tongDiemGDTC));
                }
            }
            listViewDieuKien.addView(viewGiaoDucTheChat);
            //tự chọn... (cái này khó)
            Utils.hideProcessBar(mContext, progressBar, mainLayout);
        }
    }
}
