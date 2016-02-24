package com.nckh2016.vuduytung.nckh2016.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nckh2016.vuduytung.nckh2016.Data.MyContract.CTDTEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.MonHocEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.KhoaEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.NganhEntry;
import com.nckh2016.vuduytung.nckh2016.Data.MyContract.UserEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Tung on 20/2/2016.
 */
public class SQLiteDataController extends SQLiteOpenHelper {

    // database
    public static String DB_PATH = "/data/data/com.nckh2016.vuduytung.nckh2016/databases/";
    private static String DB_NAME = "nckh2016db.sqlite";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private final Context mContext;
    //nien giam
    public static final String TB_CTDT = "CTDT";
    public static final String TB_BOMON = "bomon";
    public static final String TB_CHUYENSAU = "chuyensau";
    public static final String TB_KHOA = "khoa";
    public static final String TB_MONHOC = "monhoc";
    public static final String TB_NGANH = "nganh";
    //user
    public static final String TB_USER = "user";
    public static final String TB_USERDATA = "user_data";

    public SQLiteDataController(Context cont) {
        super(cont, DB_NAME, null, DATABASE_VERSION);
        this.mContext = cont;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
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
                copyDataBase();
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
        database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    //sql methods

    public long insertNguoiDung(ContentValues values){
        long flag = -1;
        try{
            openDataBase();
            flag = database.insert(TB_USER, null, values);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
            return flag;
        }
    }

    /**
     * Lấy danh sách tất cả các môn học
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
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            close();
        }
        return result;
    }
    public ArrayList<Object> getMonHoc(String maMonHoc){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + MonHocEntry.TABLE_NAME + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " = " + maMonHoc, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectMonHoc(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_MA_BO_MON)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TEN_MON_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TIN_CHI)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_DIEU_KIEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_NOI_DUNG)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(MonHocEntry.COLUMN_TAI_LIEU))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            close();
        }
        return result;
    }

    public ArrayList<Object> getCTDT(String mabm, String hocky){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + CTDTEntry.TABLE_NAME + " WHERE " + CTDTEntry.COLUMN_MA_BO_MON + " = " + mabm + " and " + CTDTEntry.COLUMN_HOC_KY + " = " + hocky, null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(getMonHoc(mCursor.getString(mCursor.getColumnIndexOrThrow(CTDTEntry.COLUMN_MA_MON_HOC))).get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            close();
        }
        return result;
    }

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
            mCursor.close();
            close();
        }
        return result;
    }

    public ArrayList<Object> getNganhTheoKhoa(String maKhoa){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT rowid as _id,* FROM " + NganhEntry.TABLE_NAME + " WHERE makhoa = " + maKhoa, null);
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
            mCursor.close();
            close();
        }
        return result;
    }

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
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_KY_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_EMAIL))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            close();
        }
        return result;
    }
    public ArrayList<Object> getUser(String masv){
        ArrayList<Object> result = new ArrayList<Object>();
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry.COLUMN_MA_SV + " = '" + masv + "'", null);
            if(mCursor != null) {
                while(mCursor.moveToNext()){
                    result.add(new ObjectUser(
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_SV)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_KHOA)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_MA_NGANH)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_HO_TEN)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_NAM_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_KY_HOC)),
                            mCursor.getString(mCursor.getColumnIndexOrThrow(UserEntry.COLUMN_EMAIL))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCursor.close();
            close();
        }
        return result;
    }
    public boolean checkUser(String masv){
        boolean flag = false;
        Cursor mCursor = null;
        try{
            openDataBase();
            mCursor = database.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry.COLUMN_MA_SV + " = '" + masv + "'", null);
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
            mCursor.close();
            close();
        }
        return flag;
    }

    public String getTenKhoa(String maKhoa){
        String tenKhoa = null;
        Cursor cursor = null;
        try {
            // Mở kết nối
            openDataBase();
            cursor = database.rawQuery("SELECT rowid as _id,* FROM " + TB_KHOA + " WHERE makhoa = " + maKhoa, null);
            while (cursor.moveToNext()) {
                tenKhoa = cursor.getString(cursor.getColumnIndex("tenkhoa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            close();
        }
        return tenKhoa;
    }
    public String getTenNganh(String maNganh){
        String tenNganh = null;
        Cursor cursor = null;
        try {
            // Mở kết nối
            openDataBase();
            cursor = database.rawQuery("SELECT rowid as _id,* FROM " + TB_NGANH + " WHERE manganh = " + maNganh, null);
            while (cursor.moveToNext()) {
                tenNganh = cursor.getString(cursor.getColumnIndex("tennganh"));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            close();
        }
        return tenNganh;
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
