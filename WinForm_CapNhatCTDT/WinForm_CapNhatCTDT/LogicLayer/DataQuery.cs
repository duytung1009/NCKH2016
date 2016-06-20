using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinForm_CapNhatCTDT.DataAccessLayer;
using static WinForm_CapNhatCTDT.DataAccessLayer.Contract;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class DataQuery
    {

        public static void CloseConnection()
        {
            Connection.CloseConnection();
        }

        public static List<ObjectMonHoc> getMonHoc()
        {
            List<ObjectMonHoc> result = new List<ObjectMonHoc>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + MonHocEntry.TABLE_NAME;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectMonHoc(
                    dr[MonHocEntry.COLUMN_MA_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    Int64.Parse(dr[MonHocEntry.COLUMN_TIN_CHI].ToString()),
                    dr[MonHocEntry.COLUMN_DIEU_KIEN].ToString(),
                    dr[MonHocEntry.COLUMN_NOI_DUNG].ToString(),
                    dr[MonHocEntry.COLUMN_TAI_LIEU].ToString()));
            }
            return result;
        }

        public static ObjectMonHoc getMonHoc(String maMonHoc)
        {
            ObjectMonHoc result = new ObjectMonHoc();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " = " + maMonHoc;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result = new ObjectMonHoc(
                    dr[MonHocEntry.COLUMN_MA_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    Int64.Parse(dr[MonHocEntry.COLUMN_TIN_CHI].ToString()),
                    dr[MonHocEntry.COLUMN_DIEU_KIEN].ToString(),
                    dr[MonHocEntry.COLUMN_NOI_DUNG].ToString(),
                    dr[MonHocEntry.COLUMN_TAI_LIEU].ToString());
            }
            return result;
        }

        public static List<ObjectMonHoc> getMonHocTheoBoMon(String maBoMon)
        {
            List<ObjectMonHoc> result = new List<ObjectMonHoc>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_BO_MON + " = " + maBoMon;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectMonHoc(
                    dr[MonHocEntry.COLUMN_MA_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    Int64.Parse(dr[MonHocEntry.COLUMN_TIN_CHI].ToString()),
                    dr[MonHocEntry.COLUMN_DIEU_KIEN].ToString(),
                    dr[MonHocEntry.COLUMN_NOI_DUNG].ToString(),
                    dr[MonHocEntry.COLUMN_TAI_LIEU].ToString()));
            }
            return result;
        }

        public static List<ObjectMonHoc> getTuChonA(String manganh)
        {
            List<ObjectMonHoc> result = new List<ObjectMonHoc>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + MonHocEntry.TABLE_NAME
                    + " WHERE " + MonHocEntry.COLUMN_MA_BO_MON + " = " + manganh;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(getMonHoc(dr[MonHocEntry.COLUMN_MA_MON_HOC].ToString()));
            }
            return result;
        }

        public static List<ObjectCTDT> getCTDT(String manganh)
        {
            List<ObjectCTDT> result = new List<ObjectCTDT>();
            Connection.OpenConnection();
            string sql = "SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + manganh;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectCTDT(
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC].ToString(),
                    Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY].ToString()),
                    Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH].ToString()),
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_TU_CHON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_TIN_CHI].ToString()));
            }
            return result;
        }

        public static List<ObjectCTDT> getCTDT(String manganh, Int64 hocKy, Int64 chuyenSau)
        {
            List<ObjectCTDT> result = new List<ObjectCTDT>();
            Connection.OpenConnection();
            string sql = "SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = " + manganh
                    + " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " NOT IN (-1,-2,-3)";
            if(hocKy != 0)
            {
                sql += " AND " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = " + hocKy;
            }
            if(chuyenSau != 0)
            {
                sql += " AND " + ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH + " = " + chuyenSau;
            }
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectCTDT(
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC].ToString(),
                    Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY].ToString()),
                    Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH].ToString()),
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_TU_CHON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_TIN_CHI].ToString()));
            }
            return result;
        }

        public static List<ObjectCTDT> getTuChon(String maKhoa, String maNganh, Int64 tuChon)
        {
            List<ObjectCTDT> result = new List<ObjectCTDT>();
            Connection.OpenConnection();
            String sql = "";
            if(tuChon == 0)
            {
                sql = "SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = (-3)"
                    + " AND " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = '" + maNganh + "'";
            } 
            else if (tuChon == 1)
            {
                sql = "SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = (-2)"
                    + " AND " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON + " = '" + (maKhoa + "0") + "'";
            }
            else if (tuChon == 2)
            {
                sql = "SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                    + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                    + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                    + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                    + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                    + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " = (-1)";                    
            }
            if (!sql.Equals(String.Empty))
            {
                SQLiteDataReader dr = Connection.GetData(sql);
                while (dr.Read())
                {
                    result.Add(new ObjectCTDT(
                        dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON].ToString(),
                        dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC].ToString(),
                        Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY].ToString()),
                        Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH].ToString()),
                        dr[ChuongTrinhDaoTaoEntry.COLUMN_TU_CHON].ToString(),
                        dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                        dr[MonHocEntry.COLUMN_TIN_CHI].ToString()));
                }                
            }
            return result;
        }

        public static List<ObjectKhoa> getKhoaCoNganh()
        {
            List<ObjectKhoa> result = new List<ObjectKhoa>();
            Connection.OpenConnection();
            string sql = "SELECT rowid as _id,* FROM " + KhoaEntry.TABLE_NAME
                    + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " NOT IN (1,2,12,13,14)";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectKhoa(
                    dr[KhoaEntry.COLUMN_MA_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TEN_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TRUONG_KHOA].ToString()));
            }
            return result;
        }

        public static List<ObjectKhoa> getKhoaCoBoMon()
        {
            List<ObjectKhoa> result = new List<ObjectKhoa>();
            Connection.OpenConnection();
            string sql = "SELECT rowid as _id,* FROM " + KhoaEntry.TABLE_NAME
                    + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " NOT IN (12,13,14)";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectKhoa(
                    dr[KhoaEntry.COLUMN_MA_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TEN_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TRUONG_KHOA].ToString()));
            }
            return result;
        }

        public static List<ObjectKhoa> getKhoa()
        {
            List<ObjectKhoa> result = new List<ObjectKhoa>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + KhoaEntry.TABLE_NAME;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectKhoa(
                    dr[KhoaEntry.COLUMN_MA_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TEN_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TRUONG_KHOA].ToString()));
            }
            return result;
        }

        public static ObjectKhoa getKhoa(String makhoa)
        {
            ObjectKhoa result = new ObjectKhoa();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + KhoaEntry.TABLE_NAME
                + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " = " + makhoa;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result = new ObjectKhoa(
                    dr[KhoaEntry.COLUMN_MA_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TEN_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TRUONG_KHOA].ToString());
            }
            return result;
        }

        public static List<ObjectNganh> getNganh()
        {
            List<ObjectNganh> result = new List<ObjectNganh>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + NganhEntry.TABLE_NAME;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectNganh(
                    dr[NganhEntry.COLUMN_MA_NGANH].ToString(),
                    dr[NganhEntry.COLUMN_MA_KHOA].ToString(),
                    dr[NganhEntry.COLUMN_TEN_NGANH].ToString()));
            }
            return result;
        }

        public static ObjectNganh getNganh(String manganh)
        {
            ObjectNganh result = new ObjectNganh();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + NganhEntry.TABLE_NAME
                + " WHERE " + NganhEntry.COLUMN_MA_NGANH + " = " + manganh;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result = new ObjectNganh(
                    dr[NganhEntry.COLUMN_MA_NGANH].ToString(),
                    dr[NganhEntry.COLUMN_MA_KHOA].ToString(),
                    dr[NganhEntry.COLUMN_TEN_NGANH].ToString());
            }
            return result;
        }

        public static List<ObjectNganh> getNganhTheoKhoa(String maKhoa)
        {
            List<ObjectNganh> result = new List<ObjectNganh>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + NganhEntry.TABLE_NAME
                    + " WHERE " + NganhEntry.COLUMN_MA_KHOA + " = " + maKhoa;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectNganh(
                    dr[NganhEntry.COLUMN_MA_NGANH].ToString(),
                    dr[NganhEntry.COLUMN_MA_KHOA].ToString(),
                    dr[NganhEntry.COLUMN_TEN_NGANH].ToString()));
            }
            return result;
        }

        public static List<ObjectChuyenSau> getChuyenSau()
        {
            List<ObjectChuyenSau> result = new List<ObjectChuyenSau>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + ChuyenSauEntry.TABLE_NAME;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectChuyenSau(
                    dr[ChuyenSauEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[ChuyenSauEntry.COLUMN_SO].ToString(),
                    dr[ChuyenSauEntry.COLUMN_TEN].ToString()));
            }
            return result;
        }

        public static List<ObjectChuyenSau> getChuyenSauTheoNganh(String manganh)
        {
            List<ObjectChuyenSau> result = new List<ObjectChuyenSau>();
            result.Add(new ObjectChuyenSau(
                    "",
                    "0",
                    "Chưa chọn"));
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + ChuyenSauEntry.TABLE_NAME
                    + " WHERE " + ChuyenSauEntry.COLUMN_MA_BO_MON + " = " + manganh;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectChuyenSau(
                    dr[ChuyenSauEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[ChuyenSauEntry.COLUMN_SO].ToString(),
                    dr[ChuyenSauEntry.COLUMN_TEN].ToString()));
            }
            return result;
        }

        public static List<ObjectBoMon> getBoMon()
        {
            List<ObjectBoMon> result = new List<ObjectBoMon>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + BoMonEntry.TABLE_NAME;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectBoMon(
                    dr[BoMonEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[BoMonEntry.COLUMN_MA_KHOA].ToString(),
                    dr[BoMonEntry.COLUMN_TEN_BO_MON].ToString()));
            }
            return result;
        }

        public static List<ObjectBoMon> getBoMonTheoKhoa(String maKhoa)
        {
            List<ObjectBoMon> result = new List<ObjectBoMon>();
            Connection.OpenConnection();
            string sql = "SELECT rowid as _id,* FROM " + BoMonEntry.TABLE_NAME
                    + " WHERE makhoa = " + maKhoa;
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectBoMon(
                    dr[BoMonEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[BoMonEntry.COLUMN_MA_KHOA].ToString(),
                    dr[BoMonEntry.COLUMN_TEN_BO_MON].ToString()));
            }
            return result;
        }



        //search
        public static List<ObjectCTDT> searchTuChon(String query)
        {
            List<ObjectCTDT> result = new List<ObjectCTDT>();
            Connection.OpenConnection();
            string sql = "SELECT " + ChuongTrinhDaoTaoEntry.TABLE_NAME + ".*,"
                + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_TIN_CHI
                + " FROM " + ChuongTrinhDaoTaoEntry.TABLE_NAME
                + " LEFT JOIN " + MonHocEntry.TABLE_NAME
                + " ON " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " = " + MonHocEntry.TABLE_NAME + "." + MonHocEntry.COLUMN_MA_MON_HOC
                + " WHERE " + ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY + " IN (-1,-2,-3)"
                + " AND (" + MonHocEntry.COLUMN_TEN_MON_HOC + " LIKE '%" + query + "%'"
                + " OR " + ChuongTrinhDaoTaoEntry.TABLE_NAME + "." + ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC + " LIKE '%" + query + "%')";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectCTDT(
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_MA_MON_HOC].ToString(),
                    Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_HOC_KY].ToString()),
                    Int64.Parse(dr[ChuongTrinhDaoTaoEntry.COLUMN_CHUYEN_NGANH].ToString()),
                    dr[ChuongTrinhDaoTaoEntry.COLUMN_TU_CHON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_TIN_CHI].ToString()));
            }
            return result;
        }

        public static List<ObjectKhoa> searchKhoa(String query)
        {
            List<ObjectKhoa> result = new List<ObjectKhoa>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + KhoaEntry.TABLE_NAME
                + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " LIKE '%" + query + "%'"
                + " OR " + KhoaEntry.COLUMN_TEN_KHOA + " LIKE '%" + query + "%'"
                + " OR " + KhoaEntry.COLUMN_TRUONG_KHOA + " LIKE '%" + query + "%'";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectKhoa(
                    dr[KhoaEntry.COLUMN_MA_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TEN_KHOA].ToString(),
                    dr[KhoaEntry.COLUMN_TRUONG_KHOA].ToString()));
            }
            return result;
        }

        public static List<ObjectNganh> searchNganh(String query)
        {
            List<ObjectNganh> result = new List<ObjectNganh>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + NganhEntry.TABLE_NAME
                + " WHERE " + NganhEntry.COLUMN_MA_KHOA + " LIKE '%" + query + "%'"
                + " OR " + NganhEntry.COLUMN_MA_NGANH + " LIKE '%" + query + "%'"
                + " OR " + NganhEntry.COLUMN_TEN_NGANH + " LIKE '%" + query + "%'";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectNganh(
                    dr[NganhEntry.COLUMN_MA_NGANH].ToString(),
                    dr[NganhEntry.COLUMN_MA_KHOA].ToString(),
                    dr[NganhEntry.COLUMN_TEN_NGANH].ToString()));
            }
            return result;
        }

        public static List<ObjectBoMon> searchBoMon(String query)
        {
            List<ObjectBoMon> result = new List<ObjectBoMon>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + BoMonEntry.TABLE_NAME
                + " WHERE " + BoMonEntry.COLUMN_MA_KHOA + " LIKE '%" + query + "%'"
                + " OR " + BoMonEntry.COLUMN_MA_BO_MON + " LIKE '%" + query + "%'"
                + " OR " + BoMonEntry.COLUMN_TEN_BO_MON + " LIKE '%" + query + "%'";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectBoMon(
                    dr[BoMonEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[BoMonEntry.COLUMN_MA_KHOA].ToString(),
                    dr[BoMonEntry.COLUMN_TEN_BO_MON].ToString()));
            }
            return result;
        }

        public static List<ObjectChuyenSau> searchChuyenSau(String query)
        {
            List<ObjectChuyenSau> result = new List<ObjectChuyenSau>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + ChuyenSauEntry.TABLE_NAME
                + " WHERE " + ChuyenSauEntry.COLUMN_MA_BO_MON + " LIKE '%" + query + "%'"
                + " OR " + ChuyenSauEntry.COLUMN_SO + " LIKE '%" + query + "%'"
                + " OR " + ChuyenSauEntry.COLUMN_TEN + " LIKE '%" + query + "%'";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectChuyenSau(
                    dr[ChuyenSauEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[ChuyenSauEntry.COLUMN_SO].ToString(),
                    dr[ChuyenSauEntry.COLUMN_TEN].ToString()));
            }
            return result;
        }

        public static List<ObjectMonHoc> searchMonHoc(String query)
        {
            List<ObjectMonHoc> result = new List<ObjectMonHoc>();
            Connection.OpenConnection();
            string sql = "SELECT * FROM " + MonHocEntry.TABLE_NAME
                + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " LIKE '%" + query + "%'"
                + " OR " + MonHocEntry.COLUMN_MA_BO_MON + " LIKE '%" + query + "%'"
                + " OR " + MonHocEntry.COLUMN_TEN_MON_HOC + " LIKE '%" + query + "%'"
                + " OR " + MonHocEntry.COLUMN_TIN_CHI + " LIKE '%" + query + "%'"
                + " OR " + MonHocEntry.COLUMN_DIEU_KIEN + " LIKE '%" + query + "%'"
                + " OR " + MonHocEntry.COLUMN_NOI_DUNG + " LIKE '%" + query + "%'"
                + " OR " + MonHocEntry.COLUMN_TAI_LIEU + " LIKE '%" + query + "%'";
            SQLiteDataReader dr = Connection.GetData(sql);
            while (dr.Read())
            {
                result.Add(new ObjectMonHoc(
                    dr[MonHocEntry.COLUMN_MA_MON_HOC].ToString(),
                    dr[MonHocEntry.COLUMN_MA_BO_MON].ToString(),
                    dr[MonHocEntry.COLUMN_TEN_MON_HOC].ToString(),
                    Int64.Parse(dr[MonHocEntry.COLUMN_TIN_CHI].ToString()),
                    dr[MonHocEntry.COLUMN_DIEU_KIEN].ToString(),
                    dr[MonHocEntry.COLUMN_NOI_DUNG].ToString(),
                    dr[MonHocEntry.COLUMN_TAI_LIEU].ToString()));
            }
            return result;
        }



        //insert
        public static bool insertKhoa(ObjectKhoa value)
        {
            Connection.OpenConnection();
            string sql = "INSERT INTO " + KhoaEntry.TABLE_NAME + " ("
                + KhoaEntry.COLUMN_MA_KHOA + ","
                + KhoaEntry.COLUMN_TEN_KHOA + ","
                + KhoaEntry.COLUMN_TRUONG_KHOA + ") VALUES ('"
                + value.MaKhoa + "','"
                + value.TenKhoa + "','"
                + value.TruongKhoa + "')";
            return Connection.SetData(sql);
        }

        public static bool insertNganh(ObjectNganh value)
        {
            Connection.OpenConnection();
            string sql = "INSERT INTO " + NganhEntry.TABLE_NAME + " ("
                + NganhEntry.COLUMN_MA_KHOA + ","
                + NganhEntry.COLUMN_MA_NGANH + ","
                + NganhEntry.COLUMN_TEN_NGANH + ") VALUES ('"
                + value.MaKhoa + "','"
                + value.MaNganh + "','"
                + value.TenNganh + "')";
            return Connection.SetData(sql);
        }

        public static bool insertBoMon(ObjectBoMon value)
        {
            Connection.OpenConnection();
            string sql = "INSERT INTO " + BoMonEntry.TABLE_NAME + " ("
                + BoMonEntry.COLUMN_MA_KHOA + ","
                + BoMonEntry.COLUMN_MA_BO_MON + ","
                + BoMonEntry.COLUMN_TEN_BO_MON + ") VALUES ('"
                + value.MaKhoa + "','"
                + value.MaBoMon + "','"
                + value.TenBoMon + "')";
            return Connection.SetData(sql);
        }

        public static bool insertChuyenSau(ObjectChuyenSau value)
        {
            Connection.OpenConnection();
            string sql = "INSERT INTO " + ChuyenSauEntry.TABLE_NAME + " ("
                + ChuyenSauEntry.COLUMN_MA_BO_MON + ","
                + ChuyenSauEntry.COLUMN_SO + ","
                + ChuyenSauEntry.COLUMN_TEN + ") VALUES ('"
                + value.MaNganh + "','"
                + value.MaChuyenSau + "','"
                + value.TenChuyenSau + "')";
            return Connection.SetData(sql);
        }

        public static bool insertMonHoc(ObjectMonHoc value)
        {
            Connection.OpenConnection();
            string sql = "INSERT INTO " + MonHocEntry.TABLE_NAME + " ("
                + MonHocEntry.COLUMN_MA_MON_HOC + ","
                + MonHocEntry.COLUMN_MA_BO_MON + ","
                + MonHocEntry.COLUMN_TEN_MON_HOC + ","
                + MonHocEntry.COLUMN_TIN_CHI + ","
                + MonHocEntry.COLUMN_DIEU_KIEN + ","
                + MonHocEntry.COLUMN_NOI_DUNG + ","
                + MonHocEntry.COLUMN_TAI_LIEU + ") VALUES ('"
                + value.MaMonHoc + "','"
                + value.MaBoMon + "','"
                + value.TenMonHoc + "','"
                + value.TinChi + "','"
                + value.DieuKien + "','"
                + value.NoiDung + "','"
                + value.TaiLieu + "')";
            return Connection.SetData(sql);
        }



        //update
        public static bool updateKhoa(ObjectKhoa value)
        {
            Connection.OpenConnection();
            string sql = "UPDATE " + KhoaEntry.TABLE_NAME + " SET "
                + KhoaEntry.COLUMN_TEN_KHOA + " = '" + value.TenKhoa + "',"
                + KhoaEntry.COLUMN_TRUONG_KHOA + " = '" + value.TruongKhoa + "'"
                + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " = '" + value.MaKhoa + "'";
            return Connection.SetData(sql);
        }

        public static bool updateNganh(ObjectNganh value)
        {
            Connection.OpenConnection();
            string sql = "UPDATE " + NganhEntry.TABLE_NAME + " SET "
                + NganhEntry.COLUMN_MA_KHOA + " = '" + value.MaKhoa + "',"
                + NganhEntry.COLUMN_TEN_NGANH + " = '" + value.TenNganh + "'"
                + " WHERE " + NganhEntry.COLUMN_MA_NGANH + " = '" + value.MaNganh + "'";
            return Connection.SetData(sql);
        }

        public static bool updateBoMon(ObjectBoMon value)
        {
            Connection.OpenConnection();
            string sql = "UPDATE " + BoMonEntry.TABLE_NAME + " SET "
                + BoMonEntry.COLUMN_MA_KHOA + " = '" + value.MaKhoa + "',"
                + BoMonEntry.COLUMN_TEN_BO_MON + " = '" + value.TenBoMon + "'"
                + " WHERE " + BoMonEntry.COLUMN_MA_BO_MON + " = '" + value.MaBoMon + "'";
            return Connection.SetData(sql);
        }

        public static bool updateChuyenSau(ObjectChuyenSau value)
        {
            Connection.OpenConnection();
            string sql = "UPDATE " + ChuyenSauEntry.TABLE_NAME + " SET "
                + ChuyenSauEntry.COLUMN_TEN + " = '" + value.TenChuyenSau + "'"
                + " WHERE " + ChuyenSauEntry.COLUMN_MA_BO_MON + " = '" + value.MaNganh + "'"
                + " AND " + ChuyenSauEntry.COLUMN_SO + " = '" + value.MaChuyenSau +"'";
            return Connection.SetData(sql);
        }

        public static bool updateMonHoc(ObjectMonHoc value)
        {
            Connection.OpenConnection();
            string sql = "UPDATE " + MonHocEntry.TABLE_NAME + " SET "
                + MonHocEntry.COLUMN_MA_BO_MON + " = '" + value.MaBoMon + "',"
                + MonHocEntry.COLUMN_TEN_MON_HOC + " = '" + value.TenMonHoc + "',"
                + MonHocEntry.COLUMN_TIN_CHI + " = '" + value.TinChi + "',"
                + MonHocEntry.COLUMN_DIEU_KIEN + " = '" + value.DieuKien + "',"
                + MonHocEntry.COLUMN_NOI_DUNG + " = '" + value.NoiDung + "',"
                + MonHocEntry.COLUMN_TAI_LIEU + " = '" + value.TaiLieu + "'"
                + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " = '" + value.MaMonHoc + "'";
            return Connection.SetData(sql);
        }



        //delete
        public static bool deleteKhoa(String makhoa)
        {
            Connection.OpenConnection();
            string sql = "DELETE FROM " + KhoaEntry.TABLE_NAME
                + " WHERE " + KhoaEntry.COLUMN_MA_KHOA + " = " + makhoa;
            return Connection.SetData(sql);
        }

        public static bool deleteNganh(String manganh)
        {
            Connection.OpenConnection();
            string sql = "DELETE FROM " + NganhEntry.TABLE_NAME
                + " WHERE " + NganhEntry.COLUMN_MA_NGANH + " = " + manganh;
            return Connection.SetData(sql);
        }

        public static bool deleteBoMon(String mabomon)
        {
            Connection.OpenConnection();
            string sql = "DELETE FROM " + BoMonEntry.TABLE_NAME
                + " WHERE " + BoMonEntry.COLUMN_MA_BO_MON + " = " + mabomon;
            return Connection.SetData(sql);
        }

        public static bool deleteChuyenSau(String manganh, String machuyensau)
        {
            Connection.OpenConnection();
            string sql = "DELETE FROM " + ChuyenSauEntry.TABLE_NAME
                + " WHERE " + ChuyenSauEntry.COLUMN_MA_BO_MON + " = '" + manganh + "'"
                + " AND " + ChuyenSauEntry.COLUMN_SO + " = '" + machuyensau + "'";
            return Connection.SetData(sql);
        }

        public static bool deleteMonHoc(String mamonhoc)
        {
            Connection.OpenConnection();
            string sql = "DELETE FROM " + MonHocEntry.TABLE_NAME
                + " WHERE " + MonHocEntry.COLUMN_MA_MON_HOC + " = " + mamonhoc;
            return Connection.SetData(sql);
        }
    }
}
