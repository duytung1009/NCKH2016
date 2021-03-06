package com.nckh2016.vuduytung.nckh2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link XemNhanhFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link XemNhanhFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class XemNhanhFragment2 extends Fragment {
    //các biến được khôi phục lại nếu app resume
    private String current_user = null;
    private ArrayList<Object> listMonHoc = new ArrayList<>();
    //các view
    CircularProgressView progressBar;
    RelativeLayout mainLayout;
    TextView txtTieuDe;
    ListView lvMonHoc;
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

    public XemNhanhFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment XemNhanhFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static XemNhanhFragment2 newInstance(String param1, String param2) {
        XemNhanhFragment2 fragment = new XemNhanhFragment2();
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
        View view = inflater.inflate(R.layout.fragment_xem_nhanh_2, container, false);
        final SharedPreferences currentUserData = getContext().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        current_user = currentUserData.getString(Utils.SUB_PREFS_MASINHVIEN, null);
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        progressBar = (CircularProgressView)view.findViewById(R.id.progressBar);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.high_priority);
        txtTieuDe = (TextView)view.findViewById(R.id.txtTieuDe);
        lvMonHoc = (ListView)view.findViewById(R.id.lvMonHoc);
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChiTietMonHocActivity.class);
                intent.putExtra(Utils.MA_MON_HOC, ((ObjectMonHoc) listMonHoc.get(position)).getMamh());
                startActivity(intent);
            }
        });
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

    private class MainTask extends AsyncTask<Void, Long, ArrayList<Object>> {
        private Context mContext;

        public MainTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProcessBar(getContext(), progressBar, mainLayout);
        }

        @Override
        protected ArrayList<Object> doInBackground(Void... params) {
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try {
                data.isCreatedDatabase();
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            double diemMin = 0, diemMax = 4;
            return data.getMonHocChuaQua(current_user, data.getMonHocMinMax(current_user, diemMin, diemMax));
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Object> objects) {
            super.onPostExecute(objects);
            listMonHoc = objects;
            AdapterMonHoc mAdapter = new AdapterMonHoc(getContext(), 0);
            mAdapter.addAll(objects);
            txtTieuDe.setText("Môn học chưa qua: " + objects.size() + " môn");
            lvMonHoc.setAdapter(mAdapter);
            Utils.hideProcessBar(mContext, progressBar, mainLayout);
        }
    }
}
