package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentQuaTrinhHocTap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentQuaTrinhHocTap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentQuaTrinhHocTap extends Fragment {
    //các giá trị Preferences Global
    public static final String PREFS_NAME = "current_user";
    public static final String SUB_PREFS_MASINHVIEN = "user_mssv";
    //các giá trị Preferences của Activity
    public static final String PREFS_STATE = "saved_state_quatrinhhoctap_fragment";
    public static final String SUB_PREFS_TONGTINCHI = "tongTinChi";
    public static final String SUB_PREFS_TONGDIEM = "tongDiem";
    public static final String SUB_PREFS_TONGTINCHINGANH = "tongTinChiNganh";
    public static final String SUB_PREFS_YDATA = "yData";
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private int tongTinChi;
    private Double tongDiem;
    private int[] yData;
    private int tongTinChiNganh;//
    //các asynctask
    MainTask mainTask;
    //các view
    PieDataSet dataSet;
    PieData chartData;
    PieChart mainChart;
    CircularProgressView progressBar;
    Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);
    DecimalFormat df;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentQuaTrinhHocTap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentQuaTrinhHocTap.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentQuaTrinhHocTap newInstance(String param1, String param2) {
        FragmentQuaTrinhHocTap fragment = new FragmentQuaTrinhHocTap();
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
        View view = inflater.inflate(R.layout.fragment_fragment_qua_trinh_hoc_tap, container, false);
        //get chart
        mainChart = (PieChart)view.findViewById(R.id.mainChart);
        progressBar = (CircularProgressView)view.findViewById(R.id.progressBar);
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        df = new DecimalFormat("####0.00");
        mainChart.setNoDataTextDescription("no data");
        mainChart.setDrawSliceText(false);  //hide title
        mainChart.setDescription("");
        mainChart.setDescriptionTypeface(light);
        mainChart.setDescriptionTextSize(20);
        mainChart.setDescriptionColor(Color.DKGRAY);
        mainChart.setCenterTextTypeface(light);
        mainChart.setCenterTextSize(24);
        mainChart.setCenterTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mainChart.setDrawHoleEnabled(true);
        mainChart.setHoleColor(Color.TRANSPARENT);
        mainChart.setHoleRadius(60);
        mainChart.setTransparentCircleRadius(65);
        mainChart.setDrawCenterText(true);
        mainChart.setRotationEnabled(false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpChart();
        mainTask = new MainTask(getContext());
        mainTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        //lấy dữ liệu Global
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(current_user == null){
            current_user = currentUserData.getString(SUB_PREFS_MASINHVIEN, null);
        }
        //lấy dữ liệu được lưu lại khi app Paused
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        if(tongTinChi == 0){
            tongTinChi = state.getInt(SUB_PREFS_TONGTINCHI, 0);
        }
        if(tongDiem == null || tongDiem.isNaN()){
            tongDiem = Double.parseDouble(state.getString(SUB_PREFS_TONGDIEM, tongDiem.toString()));
        }
        if(tongTinChiNganh == 0){
            tongTinChiNganh = state.getInt(SUB_PREFS_TONGTINCHINGANH, 0);
        }
        if(yData == null || yData.length == 0){
            yData = new Gson().fromJson(state.getString(SUB_PREFS_YDATA, null), int[].class);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //lưu dữ liệu ra Preferences
        SharedPreferences state = getContext().getSharedPreferences(PREFS_STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = state.edit();
        editor.putInt(SUB_PREFS_TONGTINCHI, tongTinChi);
        editor.putString(SUB_PREFS_TONGDIEM, tongDiem.toString());
        editor.putInt(SUB_PREFS_TONGTINCHINGANH, tongTinChiNganh);
        editor.putString(SUB_PREFS_YDATA, new Gson().toJson(yData));
        editor.apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mainTask != null){
            if(mainTask.getStatus() == AsyncTask.Status.RUNNING) {
                mainTask.cancel(true);
            }
        }
    }

    public void setUpChart(){
        tongDiem = Double.NaN;
        yData = new int[6];
        final String[] xData = {"Đang học","F","D","C","B","A"};
        tongTinChi = 0;
        /*for(int value : yData){
            tongTinChi += value;
        }*/
        //không tính tín chỉ bị F
        //bỏ qua i=1 là số tín chỉ đang học
        for(int i=2; i<yData.length; i++){
            tongTinChi += yData[i];
        }
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for(int i=0; i<yData.length; i++){
            yVals.add(new Entry(yData[i], i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0; i<xData.length; i++){
            xVals.add(xData[i]);
        }
        dataSet = new PieDataSet(yVals, "");
        dataSet.setDrawValues(false);   //hide value
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        dataSet.setColors(new int[] {
                ContextCompat.getColor(getContext(), R.color.danghoc),
                ContextCompat.getColor(getContext(), R.color.diemF),
                ContextCompat.getColor(getContext(), R.color.diemD),
                ContextCompat.getColor(getContext(), R.color.diemC),
                ContextCompat.getColor(getContext(), R.color.diemB),
                ContextCompat.getColor(getContext(), R.color.diemA)});
        //pie data object
        chartData = new PieData(xVals, dataSet);
        chartData.setValueTextSize(12);
        chartData.setValueTextColor(Color.WHITE);

        if(Double.isNaN(tongDiem)){
            mainChart.setCenterText(getResources().getString(R.string.khong));
        } else{
            mainChart.setCenterText("Tổng điểm\n" + df.format(tongDiem) + "\nTín chỉ tích lũy\n" + tongTinChi);
        }

        mainChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                String centerText = "";
                if(e.getXIndex() == 0){
                    centerText = "Đang học" + "\n" + new DecimalFormat("####0").format(e.getVal()) + " tín chỉ";
                } else {
                    centerText = "Điểm " + xData[e.getXIndex()] + "\n" + new DecimalFormat("####0").format(e.getVal()) + " tín chỉ";
                }
                mainChart.setCenterText(centerText);
            }

            @Override
            public void onNothingSelected() {
                if(Double.isNaN(tongDiem)){
                    mainChart.setCenterText(getResources().getString(R.string.khong));
                } else{
                    mainChart.setCenterText("Tổng điểm\n" + df.format(tongDiem) + "\nTín chỉ tích lũy\n" + tongTinChi);
                }
            }
        });
        //edit legend
        Legend legend = mainChart.getLegend();
        legend.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        legend.setWordWrapEnabled(true);

        //set pie data and refresh
        //mainChart.setData(chartData);
        //mainChart.animateXY(2000, 2000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
        //mainChart.invalidate();
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

    public class MainTask extends AsyncTask<Void, Long, Void> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProcessBar(getContext(), progressBar, mainChart);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(getActivity().getApplicationContext());
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            tongDiem = data.tongDiem(current_user);
            yData = data.soTinChi(current_user);
            final String[] xData = {"Đang học", "F","D","C","B","A"};
            tongTinChi = 0;
            //bỏ qua i=0 là số tín chỉ đang học, i=1 là số tín chỉ điểm F
            for(int i=2; i<yData.length; i++){
                tongTinChi += yData[i];
            }
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for(int i=0; i<yData.length; i++){
                yVals.add(new Entry(yData[i], i));
            }
            ArrayList<String> xVals = new ArrayList<String>();
            for(int i=0; i<xData.length; i++){
                xVals.add(xData[i]);
            }
            dataSet = new PieDataSet(yVals, "");
            dataSet.setDrawValues(false);   //hide value
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);
            dataSet.setColors(new int[] {
                    ContextCompat.getColor(getContext(), R.color.danghoc),
                    ContextCompat.getColor(getContext(), R.color.diemF),
                    ContextCompat.getColor(getContext(), R.color.diemD),
                    ContextCompat.getColor(getContext(), R.color.diemC),
                    ContextCompat.getColor(getContext(), R.color.diemB),
                    ContextCompat.getColor(getContext(), R.color.diemA)});
            //pie data object
            chartData = new PieData(xVals, dataSet);
            chartData.setValueTextSize(12);
            chartData.setValueTextColor(Color.WHITE);
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainChart.setData(chartData);
            if(Double.isNaN(tongDiem)){
                mainChart.setCenterText(getResources().getString(R.string.khong));
            } else{
                mainChart.setCenterText("Tổng điểm\n" + df.format(tongDiem) + "\nTín chỉ tích lũy\n" + tongTinChi);
            }
            Utils.hideProcessBar(getContext(), progressBar, mainChart);
            mainChart.animateXY(2000, 2000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
            //mainChart.invalidate();
        }
    }
}
