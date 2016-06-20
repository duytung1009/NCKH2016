using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.DataAccessLayer
{
    static class Contract
    {
        public static class BoMonEntry
        {
            public static String TABLE_NAME = "bomon";

            public static String COLUMN_MA_BO_MON = "mabomon";
            public static String COLUMN_MA_KHOA = "makhoa";
            public static String COLUMN_TEN_BO_MON = "tenbomon";
        }

        public static class ChuongTrinhDaoTaoEntry
        {
            public static String TABLE_NAME = "chuongtrinhdaotao";

            public static String COLUMN_MA_BO_MON = "mabm";
            public static String COLUMN_MA_MON_HOC = "mamh";
            public static String COLUMN_HOC_KY = "hocky";
            public static String COLUMN_CHUYEN_NGANH = "chuyennganh";
            public static String COLUMN_TU_CHON = "tuchon";
        }

        public static class ChuyenSauEntry
        {
            public static String TABLE_NAME = "chuyensau";

            public static String COLUMN_MA_BO_MON = "mabm";
            public static String COLUMN_SO = "so";
            public static String COLUMN_TEN = "ten";
        }

        public static class KhoaEntry
        {
            public static String TABLE_NAME = "khoa";

            public static String COLUMN_MA_KHOA = "makhoa";
            public static String COLUMN_TEN_KHOA = "tenkhoa";
            public static String COLUMN_TRUONG_KHOA = "truongkhoa";
        }

        public static class MonHocEntry
        {
            public static String TABLE_NAME = "monhoc";

            public static String COLUMN_MA_MON_HOC = "mamh";
            public static String COLUMN_MA_BO_MON = "mabm";
            public static String COLUMN_TEN_MON_HOC = "tenmh";
            public static String COLUMN_TIN_CHI = "tinchi";
            public static String COLUMN_DIEU_KIEN = "dieukien";
            public static String COLUMN_NOI_DUNG = "noidung";
            public static String COLUMN_TAI_LIEU = "tailieu";
        }

        public static class NganhEntry
        {
            public static String TABLE_NAME = "nganh";

            public static String COLUMN_MA_NGANH = "manganh";
            public static String COLUMN_MA_KHOA = "makhoa";
            public static String COLUMN_TEN_NGANH = "tennganh";
        }
    }
}
