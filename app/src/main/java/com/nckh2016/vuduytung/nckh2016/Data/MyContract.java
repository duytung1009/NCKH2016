package com.nckh2016.vuduytung.nckh2016.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tung on 24/2/2016.
 */
public class MyContract {
    //dùng sau...
    public static final String CONTENT_AUTHORITY = "com.nckh2016.vuduytung.nckh2016";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class BoMonEntry implements BaseColumns {
        /*public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOMON).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOMON;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOMON;*/

        public static final String TABLE_NAME = "bomon";

        public static final String COLUMN_MA_BO_MON = "mabomon";
        public static final String COLUMN_MA_KHOA = "makhoa";
        public static final String COLUMN_TEN_BO_MON = "tenbomon";

        /*public static Uri buildLocationUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }*/
    }

    public static final class ChuongTrinhDaoTaoEntry implements BaseColumns {
        public static final String TABLE_NAME = "chuongtrinhdaotao";

        public static final String COLUMN_MA_BO_MON = "mabm";
        public static final String COLUMN_MA_MON_HOC = "mamh";
        public static final String COLUMN_HOC_KY = "hocky";
        public static final String COLUMN_CHUYEN_NGANH = "chuyennganh";
        public static final String COLUMN_TU_CHON = "tuchon";
    }

    public static final class ChuyenSauEntry implements BaseColumns {
        public static final String TABLE_NAME = "chuyensau";

        public static final String COLUMN_MA_BO_MON = "mabm";
        public static final String COLUMN_SO = "so";
        public static final String COLUMN_TEN = "ten";
    }

    public static final class KhoaEntry implements BaseColumns {
        public static final String TABLE_NAME = "khoa";

        public static final String COLUMN_MA_KHOA = "makhoa";
        public static final String COLUMN_TEN_KHOA = "tenkhoa";
        public static final String COLUMN_TRUONG_KHOA = "truongkhoa";
    }

    public static final class MonHocEntry implements BaseColumns {
        public static final String TABLE_NAME = "monhoc";

        public static final String COLUMN_MA_MON_HOC = "mamh";
        public static final String COLUMN_MA_BO_MON = "mabm";
        public static final String COLUMN_TEN_MON_HOC = "tenmh";
        public static final String COLUMN_TIN_CHI = "tinchi";
        public static final String COLUMN_DIEU_KIEN = "dieukien";
        public static final String COLUMN_NOI_DUNG = "noidung";
        public static final String COLUMN_TAI_LIEU = "tailieu";
    }

    public static final class NganhEntry implements BaseColumns {
        public static final String TABLE_NAME = "nganh";

        public static final String COLUMN_MA_NGANH = "manganh";
        public static final String COLUMN_MA_KHOA = "makhoa";
        public static final String COLUMN_TEN_NGANH = "tennganh";
    }

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";

        public static final String COLUMN_MA_SV = "masv";
        public static final String COLUMN_MA_KHOA = "makhoa";
        public static final String COLUMN_MA_NGANH = "manganh";
        public static final String COLUMN_HO_TEN = "hoten";
        public static final String COLUMN_NAM_HOC = "namhoc";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_HOC_KY = "hocky";
        public static final String COLUMN_MA_CHUYEN_SAU = "machuyensau";
    }

    public static final class UserDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_data";

        public static final String COLUMN_MA_SV = "masv";
        public static final String COLUMN_MA_MON_HOC = "mamonhoc";
        public static final String COLUMN_HOC_KY = "hocky";
        public static final String COLUMN_NAM_THU = "namthu";
        public static final String COLUMN_DIEM_SO = "diemso";
        public static final String COLUMN_BANG_DIEM = "bangdiem";
    }
}
