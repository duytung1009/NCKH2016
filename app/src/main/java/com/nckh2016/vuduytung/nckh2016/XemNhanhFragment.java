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
    LinearLayout listViewDieuKien, layoutTuChonA, layoutTuChonB, layoutTuChonC;
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
        layoutTuChonA = (LinearLayout) view.findViewById(R.id.layoutTuChonA);
        layoutTuChonB = (LinearLayout) view.findViewById(R.id.layoutTuChonB);
        layoutTuChonC = (LinearLayout) view.findViewById(R.id.layoutTuChonC);
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
        private ArrayList<Object> tuChonA = new ArrayList<>();
        private ArrayList<Object> tuChonB = new ArrayList<>();
        private ArrayList<Object> tuChonC = new ArrayList<>();

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
            //tự chọn
            tuChonA = data.getTuChon(current_user, 1);
            tuChonB = data.getTuChon(current_user, 2);
            tuChonC = data.getTuChon(current_user, 3);
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //ngán cái phần này vãi...
            //tích lũy tín chỉ
            View viewTongTinChi = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTongTinChi = (ImageView) viewTongTinChi.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTongTinChi = (TextView) viewTongTinChi.findViewById(R.id.textViewMonHocDieuKien);
            String tongTinChiText = "Tín chỉ tích lũy đạt tối thiểu " + tinChiTichLuy + " tín chỉ\nTín chỉ tích lũy hiện tại: " + tongTinChi + " tín chỉ\n";
            if (tongTinChi < tinChiTichLuy) {
                imageViewTongTinChi.setImageResource(R.drawable.close_circle_outline);
                tongTinChiText += "Còn thiếu: " + (tinChiTichLuy - tongTinChi) + " tín chỉ\n";
            } else {
                imageViewTongTinChi.setImageResource(R.drawable.check);
            }
            textViewTongTinChi.setText(tongTinChiText);
            listViewDieuKien.addView(viewTongTinChi);
            //tổng điểm
            double[] mocDiem = {2,2.5,3.2,3.6};
            String[] xepLoai = {"Trung bình","Khá","Giỏi","Xuất sắc"};
            View viewTongDiem = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTongDiem = (ImageView) viewTongDiem.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTongDiem = (TextView) viewTongDiem.findViewById(R.id.textViewMonHocDieuKien);
            if (Double.compare(tongDiem, 2) < 0) {
                imageViewTongDiem.setImageResource(R.drawable.close_circle_outline);
            } else {
                imageViewTongDiem.setImageResource(R.drawable.check);
            }
            String tongDiemText = "Điểm tích lũy đạt từ 2,0 trở lên\nĐiểm tích lũy hiện tại: " + new DecimalFormat("####0.##").format(tongDiem) + "\n";
            if(Double.compare(tongDiem, mocDiem[0]) < 0){
                tongDiemText += "Chưa đủ điều kiện xét tốt nghiệp\nCòn thiếu: " + new DecimalFormat("####0.##").format(mocDiem[0] - tongDiem) + " điểm để đạt loại " + xepLoai[0] + "\n";
            } else if (Double.compare(tongDiem, mocDiem[1]) < 0) {
                tongDiemText += "Xếp loại học tập: " + xepLoai[0] + "\nCòn thiếu: " + new DecimalFormat("####0.##").format(mocDiem[1] - tongDiem) + " điểm để đạt loại " + xepLoai[1] + "\n";
            } else if (Double.compare(tongDiem, mocDiem[2]) < 0) {
                tongDiemText += "Xếp loại học tập: " + xepLoai[1] + "\nCòn thiếu: " + new DecimalFormat("####0.##").format(mocDiem[2] - tongDiem) + " điểm để đạt loại " + xepLoai[2] + "\n";
            } else if (Double.compare(tongDiem, mocDiem[3]) < 0) {
                tongDiemText += "Xếp loại học tập: " + xepLoai[2] + "\nCòn thiếu: " + new DecimalFormat("####0.##").format(mocDiem[3] - tongDiem) + " điểm để đạt loại " + xepLoai[3] + "\n";
            } else {
                tongDiemText += "Xếp loại học tập: " + xepLoai[3] + "\n";
            }
            textViewTongDiem.setText(tongDiemText);
            listViewDieuKien.addView(viewTongDiem);
            //học phần giáo dục thể chất
            View viewGiaoDucTheChat = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewGiaoDucTheChat = (ImageView) viewGiaoDucTheChat.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewGiaoDucTheChat = (TextView) viewGiaoDucTheChat.findViewById(R.id.textViewMonHocDieuKien);
            String giaoDucTheChatText = "";
            if (userHocPhanTheDuc.size() < 5) {
                imageViewGiaoDucTheChat.setImageResource(R.drawable.close_circle_outline);
                giaoDucTheChatText = "Hoàn thành học phần giáo dục thể chất\nCòn thiếu: " + String.valueOf(5 - userHocPhanTheDuc.size()) + " học phần\n";
            } else {
                double tongDiemGDTC = 0;
                for (Object value : userHocPhanTheDuc) {
                    if (((ObjectMonHoc) value).getDiem() != -1) {
                        tongDiemGDTC += ((ObjectMonHoc) value).getDiem();
                    }
                }
                tongDiemGDTC = tongDiemGDTC/5;
                if (Double.compare(tongDiemGDTC, 5.5) < 0) {
                    imageViewGiaoDucTheChat.setImageResource(R.drawable.close_circle_outline);
                    giaoDucTheChatText = "Hoàn thành học phần giáo dục thể chất\nTổng điểm hiện tại: " + new DecimalFormat("####0.##").format(tongDiemGDTC) + "\nCòn thiếu: " + new DecimalFormat("####0.##").format(5.5 - tongDiemGDTC) + " điểm\n";
                } else {
                    imageViewGiaoDucTheChat.setImageResource(R.drawable.check);
                    giaoDucTheChatText = "Hoàn thành học phần giáo dục thể chất\nTổng điểm hiện tại: " + new DecimalFormat("####0.##").format(tongDiemGDTC) + "\n";
                }
            }
            textViewGiaoDucTheChat.setText(giaoDucTheChatText);
            listViewDieuKien.addView(viewGiaoDucTheChat);
            //tự chọn A
            int tinChiTuChonA = 0, monHocDaQuaTuChonA = 0;
            for(Object obj : tuChonA){
                ObjectMonHoc objMonHoc = (ObjectMonHoc)obj;
                if(Double.compare(objMonHoc.getDiem(), 4) > 0){
                    monHocDaQuaTuChonA++;
                    tinChiTuChonA += objMonHoc.getTinchi();
                }
            }
            View viewTuChonA = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTuChonA = (ImageView) viewTuChonA.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTuChonA = (TextView) viewTuChonA.findViewById(R.id.textViewMonHocDieuKien);
            String tuChonAText = "Đã học " + tuChonA.size() + " môn\nĐã qua " + monHocDaQuaTuChonA + " môn";
            if (tinChiTuChonA < 6) {
                imageViewTuChonA.setImageResource(R.drawable.close_circle_outline);
                tuChonAText += "\nCòn thiếu: " + (6 - tinChiTuChonA) + " tín chỉ";
            } else {
                imageViewTuChonA.setImageResource(R.drawable.check);
                tuChonAText += "\nĐạt " + tinChiTuChonA + " tín chỉ";
            }
            textViewTuChonA.setText(tuChonAText);
            layoutTuChonA.addView(viewTuChonA);
            //tự chọn B
            int tinChiTuChonB = 0, monHocDaQuaTuChonB = 0;
            for(Object obj : tuChonB){
                ObjectMonHoc objMonHoc = (ObjectMonHoc)obj;
                if(Double.compare(objMonHoc.getDiem(), 4) > 0){
                    monHocDaQuaTuChonB++;
                    tinChiTuChonB += objMonHoc.getTinchi();
                }
            }
            View viewTuChonB = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTuChonB = (ImageView) viewTuChonB.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTuChonB = (TextView) viewTuChonB.findViewById(R.id.textViewMonHocDieuKien);
            String tuChonBText = "Đã học " + tuChonB.size() + " môn\nĐã qua " + monHocDaQuaTuChonB + " môn";
            if (tinChiTuChonB < 8) {
                imageViewTuChonB.setImageResource(R.drawable.close_circle_outline);
                tuChonBText += "\nCòn thiếu: " + (8 - tinChiTuChonB) + " tín chỉ";
            } else {
                imageViewTuChonB.setImageResource(R.drawable.check);
                tuChonBText += "\nĐạt " + tinChiTuChonB + " tín chỉ";
            }
            textViewTuChonB.setText(tuChonBText);
            layoutTuChonB.addView(viewTuChonB);
            //tự chọn C
            int tinChiTuChonC = 0, monHocDaQuaTuChonC = 0;
            for(Object obj : tuChonC){
                ObjectMonHoc objMonHoc = (ObjectMonHoc)obj;
                if(Double.compare(objMonHoc.getDiem(), 4) > 0){
                    monHocDaQuaTuChonC++;
                    tinChiTuChonC += objMonHoc.getTinchi();
                }
            }
            View viewTuChonC = View.inflate(mContext, R.layout.item_monhoc_dieukien, null);
            ImageView imageViewTuChonC = (ImageView) viewTuChonC.findViewById(R.id.imageViewMonHocDieuKien);
            TextView textViewTuChonC = (TextView) viewTuChonC.findViewById(R.id.textViewMonHocDieuKien);
            String tuChonCText = "Đã học " + tuChonC.size() + " môn\nĐã qua " + monHocDaQuaTuChonC + " môn";
            if (tinChiTuChonC < 8) {
                imageViewTuChonC.setImageResource(R.drawable.close_circle_outline);
                tuChonCText += "\nCòn thiếu: " + (8 - tinChiTuChonC) + " tín chỉ";
            } else {
                imageViewTuChonC.setImageResource(R.drawable.check);
                tuChonCText += "\nĐạt " + tinChiTuChonC + " tín chỉ";
            }
            textViewTuChonC.setText(tuChonCText);
            layoutTuChonC.addView(viewTuChonC);
            Utils.hideProcessBar(mContext, progressBar, mainLayout);
        }
    }
}
