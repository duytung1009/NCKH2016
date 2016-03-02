package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    public PieChart mainChart;
    Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);

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
        SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        loadData();
        return view;
    }
    public void loadData(){
        final SQLiteDataController data = new SQLiteDataController(getContext());
        try {
            data.isCreatedDatabase();
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        final DecimalFormat df = new DecimalFormat("####0.00");
        final Double tongDiem = data.tongDiem(current_user);
        int[] yData = data.soTinChi(current_user);
        final String[] xData = {"F","D","C","B","A"};
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for(int i=0; i<yData.length; i++){
            yVals.add(new Entry(yData[i], i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0; i<xData.length; i++){
            xVals.add(xData[i]);
        }
        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setDrawValues(false);   //hide value
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        dataSet.setColors(new int[] {
                ContextCompat.getColor(getContext(), R.color.diemF),
                ContextCompat.getColor(getContext(), R.color.diemD),
                ContextCompat.getColor(getContext(), R.color.diemC),
                ContextCompat.getColor(getContext(), R.color.diemB),
                ContextCompat.getColor(getContext(), R.color.diemA)});
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //pie data object
        PieData chartData = new PieData(xVals, dataSet);
        chartData.setValueTextSize(12);
        chartData.setValueTextColor(Color.WHITE);
        mainChart.setNoDataTextDescription("no data");
        mainChart.setDrawSliceText(false);  //hide title
        mainChart.setHoleColor(Color.TRANSPARENT);
        mainChart.setHoleRadius(60);
        mainChart.setTransparentCircleRadius(65);
        //mainChart.setCenterTextRadiusPercent(60);
        mainChart.setDescription("Điểm số");
        mainChart.setDescriptionTypeface(light);
        mainChart.setDescriptionTextSize(20);
        mainChart.setDescriptionColor(Color.DKGRAY);
        if(Double.isNaN(tongDiem)){
            mainChart.setCenterText("Chưa có dữ liệu");
        } else{
            mainChart.setCenterText("Tổng điểm\n" + df.format(tongDiem));
        }
        mainChart.setCenterTextTypeface(light);
        mainChart.setCenterTextSize(24);
        mainChart.setCenterTextColor(Color.parseColor("#009688"));
        mainChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                mainChart.setCenterText("Điểm " + xData[e.getXIndex()] + "\n" + e.getVal() + " tín chỉ");
            }

            @Override
            public void onNothingSelected() {
                if(Double.isNaN(tongDiem)){
                    mainChart.setCenterText("Chưa có dữ liệu");
                } else{
                    mainChart.setCenterText("Tổng điểm\n" + df.format(tongDiem));
                }
            }
        });
        //edit legend
        Legend legend = mainChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        //set pie data and refresh
        mainChart.setData(chartData);
        mainChart.animateXY(2000, 2000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
        mainChart.invalidate();
    }
    public void reloadView(Context context){
        SharedPreferences currentUserData = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String new_user = currentUserData.getString("user_mssv", null);
        if(current_user!=null){
            if(new_user.equals(current_user)){
                mainChart.animateXY(2000, 2000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
            } else {
                current_user = new_user;
                loadData();
            }
        } else {
            current_user = new_user;
            loadData();
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
}
