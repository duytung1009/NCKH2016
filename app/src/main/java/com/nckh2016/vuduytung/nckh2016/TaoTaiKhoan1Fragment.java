package com.nckh2016.vuduytung.nckh2016;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.UserEntry;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectChuyenSau;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectKhoa;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectNganh;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectUserHocKy;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaoTaiKhoan1Fragment extends Fragment {
    public static final String PREFS_NAME = "current_user";
    ArrayList<Object> mListKhoa, mListNganh, mListChuyenSau;
    Spinner mSpinnerKhoa, mSpinnerNganh, mSpinnerChuyenSau, mSpinnerNamHoc;

    public TaoTaiKhoan1Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tao_tai_khoan_1, container, false);
        mSpinnerKhoa = (Spinner)view.findViewById(R.id.spinnerKhoa);
        mSpinnerNganh = (Spinner)view.findViewById(R.id.spinnerNganh);
        mSpinnerChuyenSau = (Spinner)view.findViewById(R.id.spinnerChuyenSau);
        mSpinnerNamHoc = (Spinner)view.findViewById(R.id.spinnerNamHoc);

        ArrayAdapter<String> mNamHocAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[] {"1","2","3","4","5"});
        mSpinnerNamHoc.setAdapter(mNamHocAdapter);

        final SQLiteDataController data = SQLiteDataController.getInstance(getContext());
        try{
            data.isCreatedDatabase();
        }
        catch (IOException e){
            Log.e("tag", e.getMessage());
        }
        mListKhoa = data.getKhoaCoNganh();
        List<String> mListTenKhoa = new ArrayList<String>();
        for (Object object : mListKhoa) {
            ObjectKhoa value = (ObjectKhoa) object;
            mListTenKhoa.add(value != null ? value.getTenkhoa() : null);
        }
        ArrayAdapter<String> mKhoaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListTenKhoa);
        mKhoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListNganh = data.getNganhTheoKhoa(((ObjectKhoa) mListKhoa.get(position)).getMakhoa());
                List<String> mListTenNganh = new ArrayList<String>();
                for (Object object : mListNganh) {
                    ObjectNganh value = (ObjectNganh) object;
                    mListTenNganh.add(value != null ? value.getTennganh() : null);
                }
                ArrayAdapter<String> mNganhAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListTenNganh);
                mNganhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mListChuyenSau = data.getChuyenSauTheoNganh(((ObjectNganh)mListNganh.get(position)).getManganh());
                        List<String> mListTenChuyenSau = new ArrayList<String>();
                        for (Object object : mListChuyenSau) {
                            ObjectChuyenSau value = (ObjectChuyenSau) object;
                            mListTenChuyenSau.add(value != null ? value.getTenchuyensau() : null);
                        }
                        ArrayAdapter<String> mChuyenSauAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListTenChuyenSau);
                        mChuyenSauAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerChuyenSau.setAdapter(mChuyenSauAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                mSpinnerNganh.setAdapter(mNganhAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerKhoa.setAdapter(mKhoaAdapter);
        Button btnDangKy = (Button)view.findViewById(R.id.btnDangKy);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masv = ((EditText)getActivity().findViewById(R.id.editMaSinhVien)).getText().toString();
                String hoten = ((EditText)getActivity().findViewById(R.id.editHoTen)).getText().toString();
                String email = ((EditText)getActivity().findViewById(R.id.editEmail)).getText().toString();
                String makhoa = null, manganh = null, namthu = null, chuyensau = null;
                if(mSpinnerKhoa.getSelectedItem() != null){
                    makhoa = ((ObjectKhoa)mListKhoa.get(mSpinnerKhoa.getSelectedItemPosition())).getMakhoa();
                }
                if(mSpinnerNganh.getSelectedItem() != null){
                    manganh = ((ObjectNganh)mListNganh.get(mSpinnerNganh.getSelectedItemPosition())).getManganh();
                }
                if(mSpinnerChuyenSau.getSelectedItem() != null){
                    chuyensau = ((ObjectChuyenSau)mListChuyenSau.get(mSpinnerChuyenSau.getSelectedItemPosition())).getMachuyensau();
                }
                if(mSpinnerNamHoc.getSelectedItem() != null){
                    namthu = mSpinnerNamHoc.getSelectedItem().toString();
                }

                if(masv.isEmpty() || hoten.isEmpty() || makhoa == null || manganh == null || namthu == null || chuyensau==null){
                    Toast.makeText(getContext(), "bổ sung thêm thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    ObjectUserHocKy newUserHocKy = new ObjectUserHocKy();
                    for(int i=0; i<Integer.parseInt(namthu); i++){
                        newUserHocKy.addHocKy(new ObjectHocKy(i+1, 0, manganh));
                    }
                    ContentValues newUser = new ContentValues();

                    newUser.put(UserEntry.COLUMN_MA_SV, masv);
                    newUser.put(UserEntry.COLUMN_HO_TEN, hoten);
                    newUser.put(UserEntry.COLUMN_EMAIL, email);
                    newUser.put(UserEntry.COLUMN_MA_KHOA, makhoa);
                    newUser.put(UserEntry.COLUMN_MA_NGANH, manganh);
                    newUser.put(UserEntry.COLUMN_NAM_HOC, namthu);
                    newUser.put(UserEntry.COLUMN_HOC_KY, new Gson().toJson(newUserHocKy));
                    newUser.put(UserEntry.COLUMN_MA_CHUYEN_SAU, chuyensau);

                    SQLiteDataController data = SQLiteDataController.getInstance(getContext());
                    try{
                        data.isCreatedDatabase();
                    }
                    catch (IOException e){
                        Log.e("tag", e.getMessage());
                    }
                    if(data.checkUser(masv)){
                        Toast.makeText(getContext(), "mã sinh viên đã có", Toast.LENGTH_SHORT).show();
                    } else{
                        long flag = data.insertNguoiDung(newUser);
                        if(flag == -1){
                            Toast.makeText(getContext(), "insert failed", Toast.LENGTH_SHORT).show();
                        } else {
                            //((TaoTaiKhoanActivity)getActivity()).loadFragment2();
                            SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("user_mssv", masv);
                            editor.putString("user_name", hoten);
                            editor.putString("user_data", new Gson().toJson(newUserHocKy));
                            editor.commit();
                            getActivity().setResult(1);
                            ((TaoTaiKhoanActivity)getActivity()).finish();
                        }
                    }
                }
            }
        });
        return view;
    }
}
