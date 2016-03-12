package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMonHocChuaQua1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMonHocChuaQua1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMonHocChuaQua1 extends Fragment {
    public static final String PREFS_NAME = "current_user";
    public String current_user = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentMonHocChuaQua1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMonHocChuaQua1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMonHocChuaQua1 newInstance(String param1, String param2) {
        FragmentMonHocChuaQua1 fragment = new FragmentMonHocChuaQua1();
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
        View view = inflater.inflate(R.layout.fragment_fragment_mon_hoc_chua_qua_1, container, false);
        final SharedPreferences currentUserData = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString("user_mssv", null);
        SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        double diemMin = 0, diemMax = 4;
        final ArrayList<Object> userMonHocChuaQua = data.getMonHocChuaQua(current_user, diemMin, diemMax);
        AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0);
        mAdapter.addAll(userMonHocChuaQua);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        imageView.setImageResource(R.drawable.high_priority);
        txtTieuDe.setText("Môn học chưa qua: " + userMonHocChuaQua.size() + " môn");
        ListView lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectMonHoc) userMonHocChuaQua.get(position)).getMamh());
                intent.putExtra("caller", "MonHocChuaQuaActivity");
                startActivity(intent);
            }
        });
        lvMonHoc.setAdapter(mAdapter);
        return view;
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
