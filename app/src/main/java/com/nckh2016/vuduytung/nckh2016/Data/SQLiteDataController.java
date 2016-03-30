package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nckh2016.vuduytung.nckh2016.Data.MyContract.BoMonEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.ChuongTrinhDaoTaoEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.ChuyenSauEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.KhoaEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.MonHocEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.NganhEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.UserDataEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.UserEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tung on 20/2/2016.
 */
public class SQLiteDataController extends SQLiteOpenHelper {
    public static SQLiteDataController mInstance = null;
    // database
    public static String DB_PATH = "/data/data/com.nckh2016.vuduytung.nckh2016/databases/";
    private static String DB_NAME = "nckh2016db.sqlite";
    private static final int DATABASE_VERSION = 1;
    public SQLiteDatabase database;
    private final Context mContext;

    private SQLiteDataController(Context cont) {
        super(cont, DB_NAME, null, DATABASE_VERSION);
        this.mContext = cont;
        // TODO Auto-generated constructor stub
    }

    //singleton
    public static SQLiteDataController getInstance(Context cont){
        if (mInstance == null) {
            mInstance = new SQLiteDataController(cont.getApplicationContext());
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public synchronized void close() {
        super.close();
        if(database != null){
            if(database.isOpen()){
                database.close();
            }
        }
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = mContext.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void CopyAssets() {
        AssetManager assetManager = mContext.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                out = new FileOutputStream(DB_PATH + filename);

                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public boolean isCreatedDatabase() throws IOException {
        //Default là đã có DB
        boolean result = true;
        //Nếu chưa tồn tại DB thì copy từ Asses vào Data
        if (!checkExistDataBase()) {
            this.getReadableDatabase();
            try {
                //copyDataBase();
                CopyAssets();
                result = false;
            } catch (Exception e) {
                throw new Error("Error copying database");
            }

        }
        return result;
    }

    private boolean checkExistDataBase() {

        try {
            String myPath = DB_PATH + DB_NAME;
            File fileDB = new File(myPath);

            if (fileDB.exists()) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        return file.delete();
    }

    public void openDataBase() throws SQLException {
        if(database == null){
            database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        } else {
            if(!database.isOpen()){
                database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            }
        }
    }

    //phương thức truy vấn CSDL

    /**
     * thêm người dùng
     * @param values thông tin cần thêm (ContentValues)
     * @return
     */
    public long insertNguoiDung(ContentValues values){
        long flag = -1;
        try{
            openDataBase();
            flag = database.insert(UserEntry.TABLE_NAME, null, values);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //close();
            return flag;
        }
    }

    /**
     * thêm người dùng + dữ liệu người dùng (điểm số...)
     * @param user thông tin người dùng cần thêm (ObjectUser)
     * @return
     */
    public boolean insertUser(ObjectUser user){
        boolean result = true;
        try{
            openDataBase();
            if(checkUser(user.getMasv())){
                //update bảng user
                ContentValues newUser = new ContentValues();
                newUser.put(UserEntry.COLUMN_HO_TEN, user.getHoten());
                newUser.put(UserEntry.COLUMN_EMAIL, user.getEmail());
                newUser.put(UserEntry.COLUMN_MA_KHOA, user.getMakhoa());
                newUser.put(UserEntry.COLUMN_MA_NGANH, user.getManganh());
                newUser.put(UserEntry.COLUMN_NAM_HOC, user.getNamhoc());
                newUser.put(UserEntry.COLUMN_HOC_KY, user.getHocky());
                newUser.put(UserEntry.COLUMN_MA_CHUYEN_SAU, user.getMachuyensau());
                long flag = database.update(UserEntry.TABLE_NAME, newUser, UserEntry.COLUMN_MA_SV + "=?" , new String[]{user.getMasv()});
                //xóa dữ liệu cũ ở bảng userdata, thêm dữ liệu mới
                deleteUserData(user.getMasv());
                boolean flag2 = insertUserData(user.getUserdata());
                //kiểm tra kết quả
                if(flag == -1 || !flag2){
                    result = false;
                }
            } else {
                //insert bảng user
                ContentValues newUser = new ContentValues();
                newUser.put(UserEntry.COLUMN_MA_SV, user.getMasv());
                newUser.put(UserEntry.COLUMN_HO_TEN, user.getHoten());
                newUser.put(UserEntry.COLUMN_EMAIL, user.getEmail());
                newUser.put(UserEntry.COLUMN_MA_KHOA, user.getMakhoa());
                newUser.put(UserEntry.COLUMN_MA_NGANH, user.getManganh());
                newUser.put(UserEntry.COLUMN_NAM_HOC, user.getNamhoc());
                newUser.put(UserEntry.COLUMN_HOC_KY, user.getHocky());
                newUser.put(UserEntry.COLUMN_MA_CHUYEN_SAU, user.getMachuyensau());
                long flag = database.insert(UserEntry.TABLE_NAME, null, newUser);
                //thêm dữ liệu mới ở bảng userdata
                boolean flag2 = insertUserData(user.getUserdata());
                //kiểm tra kết quả
                if(flag == -1 || !flag2){
                    result = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //close();
            return result;
        }
    }

    /**
     * cập nhật người dùng đã có
     * @param masv mã sinh viên (String)
     * @param values thông tin cần cập nhật (ContentValues)
     * @return
     */
    public long updateNguoiDung(String masv, ContentValues values){
        long flag = -1;
        try{
            openDataBase();
            String[] args = new String[]{masv};
            flag = database.update(UserEntry.TABLE_NAME, values, UserEntry.COLUMN_MA_SV + "=?", args);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //close();
            return flag;
        }
    }

    /**
     * cập nhật thông tin môn học của người dùng
     * @param masv mã sinh viên (String)
     * @param mamh mã môn học (String)
     * @param values
     * @return
     */
    public long updateUserData(String masv, String mamh, ContentValues values){
        long flag = -1;
        try{
            openDataBase();
            String[] args = new String[]{masv};
            flag = database.update(UserDataEntry.TABLE_NAME, values,
                    UserDataEntry.COLUMN_MA_SV + "=? AND " + UserDataEntry.COLUMN_MA_MON_HOC + " =?", new String[]{masv, mamh});
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //close();
            return flag;
        }
    }

    /**
     * thêm điểm số của người dùng
     * @param values mảng dữ liệu (ObjectUserData)
     * @return
     */
    public boolean insertUserData(ArrayList<ObjectUserData> values){
        boolean flag = true;
        Cursor mCursor = null;
        try{
            openDataBase();
            for(ObjectUserData value : values){
                ContentValues userData = new ContentValues();
                userData.put(UserDataEntry.COLUMN_MA_SV, value.getMasv());
                userData.put(UserDataEntry.COLUMN_MA_MON_HOC, value.getMamonhoc());
                userData.put(UserDataEntry.COLUMN_HOC_KY, value.getHocky());
                userData.put(UserDataEntry.COLUMN_NAM_THU, value.getNamthu());
                userData.put(UserDataEntry.COLUMN_DIEM_SO, value.getDiemso());
                long num = -1;
                mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                        + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = " + value.getMasv()
                        + " AND " + UserDataEntry.COLUMN_MA_MON_HOC + " = " + value.getMamonhoc()
                        , null);
                //insert (thay vì update) những môn chưa qua - cần cập nhật lại hàm tính điểm + tính tổng số tín chỉ
                //+ " AND " + UserDataEntry.COLUMN_HOC_KY + " = " + value.getHocky()
                //+ " AND " + UserDataEntry.COLUMN_NAM_THU + " = " + value.getNamthu()
                if(mCursor.moveToFirst()) {
                    num = database.update(UserDataEntry.TABLE_NAME, userData,
                            UserDataEntry.COLUMN_MA_SV + " = ? AND " + UserDataEntry.COLUMN_MA_MON_HOC + " = ?",
                            new String[]{value.getMasv(), value.getMamonhoc()});
                } else{
                    num = database.insert(UserDataEntry.TABLE_NAME, null, userData);
                }
                if (num == -1) {
                    flag = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
            return flag;
        }
    }

    /**
     * tính tổng điểm của người dùng
     * @param masv mã sinh viên (String)
     * @return
     */
    public Double tongDiem(String masv){
        //thêm cái phần tính tự chọn hại não vãi (ai giỏi viết lại cho nó bớt hại não với...)
        double tongDiem = 0;
        double tongTinChi = 0;
        ArrayList<Object> tuChonA = new ArrayList<Object>();
        ArrayList<Object> tuChonB = new ArrayList<Object>();
        ArrayList<Object> tuChonC = new ArrayList<Object>();
        double diemSo = 0;
        int tinChi = 0;
        Cursor mCursor = null;
        ObjectUser user = getUser(masv);
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = " + masv, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    String mamh = mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC));
                    diemSo = mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO));
                    tinChi = ((ObjectMonHoc) (getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC))).get(0))).getTinchi();
                    if(checkTuChon(mamh, "1", -1)){
                        tuChonC.add(new ObjectMonHoc(mamh, diemSo, tinChi));
                    } else if(checkTuChon(mamh, user.getMakhoa() + "0", -2)){
                        tuChonB.add(new ObjectMonHoc(mamh, diemSo, tinChi));
                    } else if(checkTuChon(mamh, user.getManganh(), -3)){
                        tuChonA.add(new ObjectMonHoc(mamh, diemSo, tinChi));
                    } else {
                        //không tính những môn bị điểm F
                        if(diemSo >= 4){
                            tongDiem += (diemSo * tinChi);
                            tongTinChi += tinChi;
                        }
                        //tính toàn bộ các môn
                        /*tongDiem += (diemSo * tinChi);
                        tongTinChi += tinChi;*/
                    }
                }
                if(tuChonA.size() != 0){
                    tuChonA = sort(tuChonA);
                    double diemA = 0;
                    double diemTuChonA = 0;
                    int tinChiTuChonA = 0;
                    int maxTinChi = 6;
                    int i = 0;
                    while((tinChiTuChonA < maxTinChi) && (i<tuChonA.size())){
                        tinChiTuChonA += ((ObjectMonHoc)tuChonA.get(i)).getTinchi();
                        diemTuChonA += (((ObjectMonHoc)tuChonA.get(i)).getDiem() * ((ObjectMonHoc)tuChonA.get(i)).getTinchi());
                        i++;
                    }
                    tongDiem += diemTuChonA;
                    tongTinChi += tinChiTuChonA;
                    /*diemA = diemTuChonA/tinChiTuChonA;
                    if(tinChiTuChonA > maxTinChi){
                        tongDiem += (diemA * maxTinChi);
                        tongTinChi += maxTinChi;
                    } else {
                        tongDiem += (diemA * tinChiTuChonA);
                        tongTinChi += tinChiTuChonA;
                    }*/
                }
                if(tuChonB.size() != 0){
                    tuChonB = sort(tuChonB);
                    double diemB = 0;
                    double diemTuChonB = 0;
                    int tinChiTuChonB = 0;
                    int maxTinChi = 8;
                    int i = 0;
                    while((tinChiTuChonB < maxTinChi) && (i<tuChonB.size())){
                        tinChiTuChonB += ((ObjectMonHoc)tuChonB.get(i)).getTinchi();
                        diemTuChonB += (((ObjectMonHoc)tuChonB.get(i)).getDiem() * ((ObjectMonHoc)tuChonB.get(i)).getTinchi());
                        i++;
                    }
                    tongDiem += diemTuChonB;
                    tongTinChi += tinChiTuChonB;
                    /*diemB = diemTuChonB/tinChiTuChonB;
                    if(tinChiTuChonB > maxTinChi){
                        tongDiem += (diemB * maxTinChi);
                        tongTinChi += maxTinChi;
                    } else {
                        tongDiem += (diemB * tinChiTuChonB);
                        tongTinChi += tinChiTuChonB;
                    }*/
                }
                if(tuChonC.size() != 0){
                    tuChonC = sort(tuChonC);
                    double diemC = 0;
                    double diemTuChonC = 0;
                    int tinChiTuChonC = 0;
                    int maxTinChi = 8;
                    int i = 0;
                    while((tinChiTuChonC < maxTinChi) && (i<tuChonC.size())){
                        tinChiTuChonC += ((ObjectMonHoc)tuChonC.get(i)).getTinchi();
                        diemTuChonC += (((ObjectMonHoc)tuChonC.get(i)).getDiem() * ((ObjectMonHoc)tuChonC.get(i)).getTinchi());
                        i++;
                    }
                    tongDiem += diemTuChonC;
                    tongTinChi += tinChiTuChonC;
                    /*diemC = diemTuChonC/tinChiTuChonC;
                    if(tinChiTuChonC > maxTinChi){
                        tongDiem += (diemC * maxTinChi);
                        tongTinChi += maxTinChi;
                    } else {
                        tongDiem += (diemC * tinChiTuChonC);
                        tongTinChi += tinChiTuChonC;
                    }*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return (tongDiem / tongTinChi)/10*4;
    }

    /**
     * tính tổng số tín chỉ của người dùng, phân loại A,B,C,D,F
     * @param masv mã sinh viên (String)
     * @return
     */
    public int[] soTinChi(String masv){
        int[] soTinChi = {0,0,0,0,0};
        ArrayList<Object> tuChonA = new ArrayList<Object>();
        ArrayList<Object> tuChonB = new ArrayList<Object>();
        ArrayList<Object> tuChonC = new ArrayList<Object>();
        Cursor mCursor = null;
        ObjectUser user = getUser(masv);
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = " + masv, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    String mamh = mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC));
                    double diem = mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO));
                    int tinChi = ((ObjectMonHoc) (getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC))).get(0))).getTinchi();
                    if(checkTuChon(mamh, "1", -1)){
                        tuChonC.add(new ObjectMonHoc(mamh, diem, tinChi));
                    } else if(checkTuChon(mamh, user.getMakhoa() + "0", -2)){
                        tuChonB.add(new ObjectMonHoc(mamh, diem, tinChi));
                    } else if(checkTuChon(mamh, user.getManganh(), -3)){
                        tuChonA.add(new ObjectMonHoc(mamh, diem, tinChi));
                    } else {
                        if(diem < 4){
                            soTinChi[0] += tinChi;
                        } else if(diem < 5.5){
                            soTinChi[1] += tinChi;
                        } else if(diem < 7){
                            soTinChi[2] += tinChi;
                        } else if(diem < 8.5){
                            soTinChi[3] += tinChi;
                        } else {
                            soTinChi[4] += tinChi;
                        }
                    }
                }
                if(tuChonA.size() != 0){
                    tuChonA = sort(tuChonA);
                    int tinChiTuChonA = 0;
                    int maxTinChi = 6;
                    int i = 0;
                    while((tinChiTuChonA < maxTinChi) && (i<tuChonA.size())){
                        int tinChi = ((ObjectMonHoc)tuChonA.get(i)).getTinchi();
                        double diem = ((ObjectMonHoc)tuChonA.get(i)).getDiem();
                        if(diem < 4){
                            soTinChi[0] += tinChi;
                        } else if(diem < 5.5){
                            soTinChi[1] += tinChi;
                        } else if(diem < 7){
                            soTinChi[2] += tinChi;
                        } else if(diem < 8.5){
                            soTinChi[3] += tinChi;
                        } else {
                            soTinChi[4] += tinChi;
                        }
                        tinChiTuChonA += tinChi;
                        i++;
                    }
                }
                if(tuChonB.size() != 0){
                    tuChonB = sort(tuChonB);
                    int tinChiTuChonB = 0;
                    int maxTinChi = 8;
                    int i = 0;
                    while((tinChiTuChonB < maxTinChi) && (i<tuChonB.size())){
                        int tinChi = ((ObjectMonHoc)tuChonB.get(i)).getTinchi();
                        double diem = ((ObjectMonHoc)tuChonB.get(i)).getDiem();
                        if(diem < 4){
                            soTinChi[0] += tinChi;
                        } else if(diem < 5.5){
                            soTinChi[1] += tinChi;
                        } else if(diem < 7){
                            soTinChi[2] += tinChi;
                        } else if(diem < 8.5){
                            soTinChi[3] += tinChi;
                        } else {
                            soTinChi[4] += tinChi;
                        }
                        tinChiTuChonB += tinChi;
                        i++;
                    }
                }
                if(tuChonC.size() != 0){
                    tuChonC = sort(tuChonC);
                    int tinChiTuChonC = 0;
                    int maxTinChi = 8;
                    int i = 0;
                    while((tinChiTuChonC < maxTinChi) && (i<tuChonC.size())){
                        int tinChi = ((ObjectMonHoc)tuChonC.get(i)).getTinchi();
                        double diem = ((ObjectMonHoc)tuChonC.get(i)).getDiem();
                        if(diem < 4){
                            soTinChi[0] += tinChi;
                        } else if(diem < 5.5){
                            soTinChi[1] += tinChi;
                        } else if(diem < 7){
                            soTinChi[2] += tinChi;
                        } else if(diem < 8.5){
                            soTinChi[3] += tinChi;
                        } else {
                            soTinChi[4] += tinChi;
                        }
                        tinChiTuChonC += tinChi;
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return soTinChi;
    }

    /**
     * Module tính tự chọn
     * sắp xếp danh sách (môn điểm cao lên đầu)
     * @param values danh sách cần sắp xếp (ArrayList<Object>)
     * @return
     */
    public ArrayList<Object> sort(ArrayList<Object> values){
        if(values.size()>=2){
            for(int i=0; i<values.size()-1; i++){
                for(int j=i+1; j<values.size(); j++){
                    ObjectMonHoc obj1 = (ObjectMonHoc)values.get(i);
                    ObjectMonHoc obj2 = (ObjectMonHoc)values.get(j);
                    if(obj1.getDiem() < obj2.getDiem()){
                        Collections.swap(values, i, j);
                    }
                }
            }
        }
        return values;
    }
    /**
     * Module tính tự chọn
     * hàm kiểm tra môn học có phải môn tự chọn không
     * hocKy: -1 tự chọn C, -2 tự chọn B, -3 tự chọn A
     * mabm: tự chọn C: 1, tự chọn B: mã khoa + số 0, tự chọn A: mã chuyên ngành
     * @param mamh mã môn học (String)
     * @param mabm mã tùy biến (String)
     * @param hocKy học kỳ (int)
     * @return
     */
    public boolean checkTuChon(String mamh, String mabm, int hocKy) {
        boolean result = false;
        Cursor mCursor = null;
        try {
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + mabm
                    + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = " + hocKy
                    + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + mamh, null);
            if(mCursor.moveToFirst()){
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy danh sách tất cả các môn học
     *
     * @return
     */
    public ArrayList<Object> getMonHoc(){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + MonHocEntry.TABLE_NAME, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin 1 môn học
     * @param maMonHoc mã môn học (String)
     * @return
     */
    public ArrayList<Object> getMonHoc(String maMonHoc){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " = " + maMonHoc, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin nhiều môn học
     * @param maMonHoc mã môn học (ArrayList<String>)
     * @return
     */
    public ArrayList<Object> getMonHoc(ArrayList<String> maMonHoc){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            StringBuilder ids = new StringBuilder();
            ids.append("(");
            for(int i = 0; i < maMonHoc.size(); i++) {
                ids.append(String.valueOf(maMonHoc.get(i)));
                if (i < maMonHoc.size() - 1) {
                    ids.append(",");
                }
            }
            ids.append(")");
            mCursor = database.rawQuery("SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " IN " + ids.toString(), null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin các môn học theo bộ môn
     * @param maBoMon mã bộ môn (String)
     * @return
     */
    public ArrayList<Object> getMonHocTheoBoMon(String maBoMon){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_BO_MON + " = " + maBoMon, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy các môn tự chọn A (lọc theo ngành)
     * @param manganh mã ngành (String)
     * @return
     */
    public ArrayList<Object> getTuChonA(String manganh){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + manganh
                    + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = -3", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC))).get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }
    //tự chọn B - theo khoa
    //tự chọn C - theo toàn trường

    /**
     * lấy chương trình đào tạo theo ngành
     * @param manganh mã ngành (String)
     * @return
     */
    public ArrayList<Items> getChuongTrinhDaoTao(String manganh){
        ArrayList<Items> result = new ArrayList<Items>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + manganh, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectCTDT(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_TU_CHON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy chương trình đào tạo theo ngành, theo từng học kỳ
     * @param manganh mã ngành (String)
     * @param hocKy mã học kỳ (int)
     * @param chuyenSau mã chuyên sâu (int)
     * @return
     */
    public ArrayList<Items> getChuongTrinhDaoTao(String manganh, int hocKy, int chuyenSau){
        ArrayList<Items> result = new ArrayList<Items>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + manganh
                    + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = " + hocKy
                    + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH + " = " + chuyenSau, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectCTDT(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_TU_CHON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy chương trình đào tạo theo ngành (có kế hoạch thay thế)
     * @param mabm
     * @param namHoc
     * @param hocKy
     * @param chuyenSau
     * @return
     */
    public ArrayList<Object> getChuongTrinhDaoTao(String mabm, int namHoc, int hocKy, int chuyenSau){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            if(hocKy == 0){
                mCursor = database.rawQuery("SELECT * FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                        + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + mabm
                        + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " IN (" + ((namHoc*2)-1) + " , " + (namHoc*2)
                        + ") AND " + ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH + " = " + chuyenSau, null);
                if(mCursor != null) {
                    while(mCursor.moveToNext()){
                        result.add(getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC))).get(0));
                    }
                }
            } else{
                mCursor = database.rawQuery("SELECT * FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                        + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + mabm
                        + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = " + hocKy
                        + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH + " = " + chuyenSau, null);
                if(mCursor != null) {
                    while(mCursor.moveToNext()){
                        result.add(getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC))).get(0));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy danh sách các môn học chưa qua
     * @param maSinhVien mã sinh viên (String)
     * @param danhSachMonHoc danh sách môn đã học (ArrayList<Object>)
     * @return
     */
    public ArrayList<Object> getMonHocChuaQua(String maSinhVien, ArrayList<Object> danhSachMonHoc){
        ArrayList<Object> monHocChuaQua = new ArrayList<Object>();
        for (Object value:danhSachMonHoc) {
            if(checkMonHocChuaQua(maSinhVien, ((ObjectMonHoc)value).getMamh())){
                monHocChuaQua.add(value);
            }
        }
        return monHocChuaQua;
    }

    /**
     * kiểm tra xem môn học đã qua chưa
     * @param maSinhVien mã sinh viên (String)
     * @param maMonHoc mã môn học (String)
     * @return
     */
    public boolean checkMonHocChuaQua(String maSinhVien, String maMonHoc){
        boolean flag = false;
        double diem = -1;
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = " + maSinhVien
                    + " AND " + UserDataEntry.COLUMN_MA_MON_HOC + " = " + maMonHoc, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    diem = mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)) > diem ? mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)) : diem;
                }
                if(diem < 4){
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return flag;
    }

    /**
     * lấy điểm của một môn học
     * @param maSinhVien mã sinh viên (String)
     * @param maMonHoc mã môn học (String)
     * @return
     */
    public float getDiem(String maSinhVien, String maMonHoc){
        float diem = -1;
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = " + maSinhVien
                    + " AND " + UserDataEntry.COLUMN_MA_MON_HOC + " = " + maMonHoc, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    diem = mCursor.getFloat(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)) > diem ? mCursor.getFloat(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)) : diem;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return diem;
    }

    /**
     * lấy danh sách khoa có ngành đào tạo
     * @return
     */
    public ArrayList<Object> getKhoaCoNganh(){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + KhoaEntry.TABLE_NAME
                    + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " NOT IN (1,2,12,13,14)", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectKhoa(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_TEN_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_TRUONG_KHOA))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy danh sách khoa có bộ môn
     * @return
     */
    public ArrayList<Object> getKhoaCoBoMon(){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + KhoaEntry.TABLE_NAME
                    + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " NOT IN (12,13,14)", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectKhoa(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_TEN_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_TRUONG_KHOA))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy danh sách toàn bộ các khoa
     * @return
     */
    public ArrayList<Object> getKhoa(){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + KhoaEntry.TABLE_NAME, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectKhoa(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_TEN_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(KhoaEntry.COLUMN_TRUONG_KHOA))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy danh sách toàn bộ các ngành
     * @return
     */
    public ArrayList<Object> getNganh() {
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + NganhEntry.TABLE_NAME, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectNganh(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_MA_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_TEN_NGANH))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin một ngành
     * @param maNganh mã ngành (String)
     * @return
     */
    public ArrayList<Object> getNganh(String maNganh){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + NganhEntry.TABLE_NAME + " WHERE " + NganhEntry.COLUMN_MA_NGANH + " = " + maNganh, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectNganh(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_MA_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_TEN_NGANH))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin các ngành theo khoa
     * @param maKhoa mã khoa (String)
     * @return
     */
    public ArrayList<Object> getNganhTheoKhoa(String maKhoa){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + NganhEntry.TABLE_NAME
                    + " WHERE " + NganhEntry.COLUMN_MA_KHOA + " = " + maKhoa, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectNganh(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_MA_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(NganhEntry.COLUMN_TEN_NGANH))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin hướng chuyên sâu theo ngành
     * @param manganh mã ngành (String)
     * @return
     */
    public ArrayList<Object> getChuyenSauTheoNganh(String manganh){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + ChuyenSauEntry.TABLE_NAME
                    + " WHERE " + ChuyenSauEntry.COLUMN_MA_BO_MON + " = " + manganh, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectChuyenSau(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuyenSauEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuyenSauEntry.COLUMN_SO)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(ChuyenSauEntry.COLUMN_TEN))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin bộ môn theo khoa
     * @param maKhoa mã khoa (String)
     * @return
     */
    public ArrayList<Object> getBoMonTheoKhoa(String maKhoa){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + BoMonEntry.TABLE_NAME
                    + " WHERE makhoa = " + maKhoa, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectBoMon(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(BoMonEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(BoMonEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(BoMonEntry.COLUMN_TEN_BO_MON))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin toàn bộ người dùng
     * @return
     */
    public ArrayList<Object> getUser(){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectUser(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_SV)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_HO_TEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_NAM_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_EMAIL)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_HOC_KY)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_CHUYEN_SAU))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin của 1 người dùng
     * @param masv mã sinh viên (String)
     * @return
     */
    public ObjectUser getUser(String masv){
        ObjectUser result = new ObjectUser();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME
                    + " WHERE " + UserEntry.COLUMN_MA_SV + " = '" + masv + "'", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result = new ObjectUser(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_SV)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_HO_TEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_NAM_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_EMAIL)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_HOC_KY)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_CHUYEN_SAU))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * tính điểm tổng kết một học kỳ
     * @param masv mã sinh viên (String)
     * @param hocKy học kỳ (int)
     * @param namHoc năm học (int)
     * @return
     */
    public double getDiemHocKy(String masv, int hocKy, int namHoc){
        double result = -1;
        double tongDiem = 0;
        int tongTinChi = 0;
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = '" + masv + "'"
                    + " AND " + UserDataEntry.COLUMN_HOC_KY + " = '" + hocKy + "'"
                    + " AND " + UserDataEntry.COLUMN_NAM_THU + " = '" + namHoc + "'", null);
            if(mCursor != null) {
                int tinchi = 0;
                while(mCursor.moveToNext()){
                    tinchi = ((ObjectMonHoc)getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC))).get(0)).getTinchi();
                    tongDiem += mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)) * tinchi;
                    tongTinChi += tinchi;
                }
                if(tongTinChi != 0){
                    result = (tongDiem/tongTinChi)/2.5;
                    //http://www.wikihow.com/Convert-a-Percentage-into-a-4.0-Grade-Point-Average
                    //https://vi.wikipedia.org/wiki/H%E1%BB%87_th%E1%BB%91ng_t%C3%ADn_ch%E1%BB%89_t%E1%BA%A1i_Vi%E1%BB%87t_Nam
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * kiểm tra xem người dùng có tồn tại không
     * @param masv mã sinh viên (String)
     * @return
     */
    public boolean checkUser(String masv){
        boolean flag = false;
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME
                    + " WHERE " + UserEntry.COLUMN_MA_SV + " = '" + masv + "'", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    if((mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_SV)).equals(masv))) {
                        flag = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return flag;
    }

    /**
     * lấy thông tin điểm số của 1 người dùng
     * @param masv mã sinh viên (String)
     * @return
     */
    public ArrayList<ObjectUserData> getUserData(String masv){
        ArrayList<ObjectUserData> result = new ArrayList<ObjectUserData>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = '" + masv + "'", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectUserData(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_SV)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_HOC_KY)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_NAM_THU)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)),
                            mCursor.getBlob(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_BANG_DIEM))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin điểm số một môn học của 1 người dùng
     * @param masv mã sinh viên (String)
     * @param mamh mã môn học (String)
     * @return
     */
    public ObjectUserData getUserData(String masv, String mamh){
        ObjectUserData result = new ObjectUserData();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserDataEntry.TABLE_NAME
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = '" + masv + "'"
                    + " AND " + UserDataEntry.COLUMN_MA_MON_HOC + " = '" + mamh + "'", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result = new ObjectUserData(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_SV)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_HOC_KY)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_NAM_THU)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)),
                            mCursor.getBlob(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_BANG_DIEM))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy thông tin điểm số trong 1 học kỳ của 1 người dùng
     * @param masv mã sinh viên (String)
     * @param namhoc năm học (int)
     * @param hocky học kỳ (int)
     * @return
     */
    public ArrayList<Object> getUserData(String masv, int namhoc, int hocky) {
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT " + MonHocEntry.TABLE_NAME + ".*,"
                    + UserDataEntry.COLUMN_DIEM_SO + "," + UserDataEntry.COLUMN_BANG_DIEM
                    + " FROM " + UserDataEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + UserDataEntry.TABLE_NAME + "." + UserDataEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = '" + masv + "'"
                    + " AND " + UserDataEntry.COLUMN_HOC_KY + " = " + hocky
                    + " AND " + UserDataEntry.COLUMN_NAM_THU + " = " + namhoc, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null,
                            mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)),
                            (mCursor.getBlob(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_BANG_DIEM)) != null)
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy danh sách môn học chưa qua (điểm số tùy biến)
     * @param masv mã sinh viên (String)
     * @param diemMin điểm số làm mốc dưới (Double)
     * @param diemMax điểm số làm mốc trên (Double)
     * @return
     */
    public ArrayList<Object> getMonHocChuaQua(String masv, double diemMin, double diemMax){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT " + MonHocEntry.TABLE_NAME + ".*,"
                    + UserDataEntry.COLUMN_DIEM_SO + "," + UserDataEntry.COLUMN_BANG_DIEM
                    + " FROM " + UserDataEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + UserDataEntry.TABLE_NAME + "." + UserDataEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + UserDataEntry.COLUMN_MA_SV + " = '" + masv + "'"
                    + " AND " + UserDataEntry.COLUMN_DIEM_SO + " >= " + diemMin
                    + " AND " + UserDataEntry.COLUMN_DIEM_SO + " < " + diemMax, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null,
                            mCursor.getDouble(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_DIEM_SO)),
                            (mCursor.getBlob(mCursor.getColumnIndexOrThrow(UserDataEntry.COLUMN_BANG_DIEM)) != null)
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * lấy tên khoa
     * @param maKhoa mã khoa (String)
     * @return
     */
    public String getTenKhoa(String maKhoa){
        String tenKhoa = null;
        Cursor mCursor = null;
        try {
            // Mở kết nối
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + KhoaEntry.TABLE_NAME
                    + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " = " + maKhoa, null);
            while (mCursor.moveToNext()) {
                tenKhoa = mCursor.getString(mCursor.getColumnIndex(KhoaEntry.COLUMN_TEN_KHOA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return tenKhoa;
    }

    /**
     * lấy tên ngành
     * @param maNganh mã ngành (String)
     * @return
     */
    public String getTenNganh(String maNganh){
        String tenNganh = null;
        Cursor mCursor = null;
        try {
            // Mở kết nối
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + NganhEntry.TABLE_NAME
                    + " WHERE " + NganhEntry.COLUMN_MA_NGANH + " = " + maNganh, null);
            while (mCursor.moveToNext()) {
                tenNganh = mCursor.getString(mCursor.getColumnIndex(NganhEntry.COLUMN_TEN_NGANH));
            }
            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return tenNganh;
    }

    /**
     * lấy tên hướng chuyên sâu
     * @param mabm mã bộ môn (String)
     * @param chuyenSau hướng chuyên sâu (int)
     * @return
     */
    public String getTenChuyenSau(String mabm, int chuyenSau){
        String tenChuyenSau = null;
        Cursor mCursor = null;
        try {
            // Mở kết nối
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + ChuyenSauEntry.TABLE_NAME
                    + " WHERE " + ChuyenSauEntry.COLUMN_MA_BO_MON + " = " + mabm
                    + " AND " + ChuyenSauEntry.COLUMN_SO + " = " + chuyenSau, null);
            while (mCursor.moveToNext()) {
                tenChuyenSau = mCursor.getString(mCursor.getColumnIndex(ChuyenSauEntry.COLUMN_TEN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return tenChuyenSau;
    }

    /**
     * lấy tên môn học theo mã môn học
     * @param mamh mã môn học (String)
     * @return
     */
    public String getTenMonHoc(String mamh){
        String tenMonHoc = null;
        Cursor mCursor = null;
        try {
            // Mở kết nối
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " = " + mamh, null);
            while (mCursor.moveToNext()) {
                tenMonHoc = mCursor.getString(mCursor.getColumnIndex(MonHocEntry.COLUMN_TEN_MON_HOC));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return tenMonHoc;
    }

    public ArrayList<Object> searchMonHoc(String query){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " LIKE '%" + query + "%'"
                    + " OR " + MonHocEntry.COLUMN_TEN_MON_HOC + " LIKE '%" + query + "%'", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getInt(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU)),
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCursor != null){
                mCursor.close();
            }
            //close();
        }
        return result;
    }

    /**
     * xóa toàn bộ dữ liệu người dùng
     * @param masv mã sinh viên (String)
     * @return
     */
    public int deleteUser(String masv){
        int result = -1;
        try{
            // Mở kết nối
            openDataBase();
            database.delete(UserDataEntry.TABLE_NAME,
                    UserDataEntry.COLUMN_MA_SV + " = ?",
                    new String[]{masv});
            result = database.delete(UserEntry.TABLE_NAME,
                    UserEntry.COLUMN_MA_SV + " = ?",
                    new String[]{masv});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return result;
    }

    /**
     * xóa dữ liệu người dùng ứng với học kỳ cụ thể
     * @param masv mã sinh viên (String)
     * @param hocky học kỳ (int)
     * @param namhoc năm học (int)
     * @return
     */
    public int deleteUserData(String masv, int hocky, int namhoc){
        int result = -1;
        try{
            // Mở kết nối
            openDataBase();
            result = database.delete(UserDataEntry.TABLE_NAME,
                    UserDataEntry.COLUMN_MA_SV + " = ? AND " + UserDataEntry.COLUMN_HOC_KY + " = ? AND " + UserDataEntry.COLUMN_NAM_THU + " = ?",
                    new String[]{masv, String.valueOf(hocky), String.valueOf(namhoc)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return result;
    }

    /**
     * xóa dữ liệu người dùng
     * @param masv mã sinh viên (String)
     * @return
     */
    public int deleteUserData(String masv){
        int result = -1;
        try{
            // Mở kết nối
            openDataBase();
            result = database.delete(UserDataEntry.TABLE_NAME,
                    UserDataEntry.COLUMN_MA_SV + " = ?",
                    new String[]{masv});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return result;
    }

    /*public Cursor getAllKhoa(){
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + TB_KHOA, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return mCursor;
    }*/

    /*public Cursor getNganh(String maKhoa){
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + TB_NGANH + " WHERE makhoa = " + maKhoa, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return mCursor;
    }*/

    /*public Cursor getAllUserData(){
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + TB_USER, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return mCursor;
    }*/

    /*public ArrayList<String> getAllNguoiDung(){
        ArrayList<String> mArrayList = new ArrayList<String>();
        try {
            // Mở kết nối
            openDataBase();
            Cursor cursor = database.query(TB_USER, null, null, null,
                    null, null, null);
            while (cursor.moveToNext()) {
                mArrayList.add(cursor.getString(cursor.getColumnIndex("masv")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return mArrayList;
    }*/
}
