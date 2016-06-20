using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SQLite;
using System.Data;

namespace WinForm_CapNhatCTDT.DataAccessLayer
{
    class Connection
    {
        private static String chuoiKetNoi = "Data Source=..\\..\\Database\\MainDB.sqlite;Version=3;New=True;Compress=True;";
        private static SQLiteConnection conn;

        public SQLiteConnection Conn
        {
            get { return conn; }
            set { conn = value; }
        }

        public Connection()
        {
            conn = new SQLiteConnection(chuoiKetNoi);
        }

        public static SQLiteConnection getInstance()
        {
            if(conn == null)
            {
                conn = new SQLiteConnection(chuoiKetNoi);
            }
            return conn;
        }

        /// <summary>
        /// Hàm mở kết nối
        /// </summary>
        public static bool OpenConnection()
        {
            try
            {
                if(conn == null)
                {
                    getInstance();
                }
                if (conn.State == ConnectionState.Closed || conn.State == ConnectionState.Broken)
                    conn.Open();
            }
            catch (Exception ex)
            {
                ex.Data["UserMessage"] += "\nKhông thể tạo kết nối đến CSDL";
                throw;
            }
            return true;
        }

        /// <summary>
        /// Hàm đóng kết nối
        /// </summary>
        public static bool CloseConnection()
        {
            try
            {
                if (conn == null)
                {
                    getInstance();
                }
                if (conn.State == ConnectionState.Open)
                    conn.Close();
            }
            catch (Exception ex)
            {
                ex.Data["UserMessage"] += "\nKhông thể đóng kết nối đến CSDL";
                throw;
            }
            return true;
        }

        /// <summary>
        /// Hàm này trả về dữ liệu sau khi thực thi lệnh sql
        /// </summary>
        /// <param name="cmd">Câu lệnh sql</param>
        /// <returns>SQLiteDataReader</returns>
        public static SQLiteDataReader GetData(string cmd)
        {
            SQLiteDataReader dr;
            try
            {
                Connection.OpenConnection();
                SQLiteCommand com = new SQLiteCommand(cmd, conn);
                dr = com.ExecuteReader();
            }
            catch (SQLiteException sqlException)
            {
                throw new Exception(sqlException.Message);
            }
            catch (Exception ex)
            {
                ex.Data["UserMessage"] += "\nKhông thể thực thi tác vụ lấy dữ liệu";
                throw ex;
            }
            return dr;
        }

        /// <summary>
        /// Hàm này thực thi lệnh sql tác động lên database
        /// </summary>
        /// <param name="cmd">Câu lệnh sql</param>
        /// /// <returns>bool</returns>
        public static bool SetData(string cmd)
        {
            try
            {
                Connection.OpenConnection();
                SQLiteCommand com = new SQLiteCommand(cmd, conn);
                com.ExecuteNonQuery();
            }
            catch (SQLiteException sqlException)
            {
                sqlException.Data["UserMessage"] += "\nLỗi SQL - " + sqlException.Message;
                throw sqlException;
            }
            catch (Exception ex)
            {
                ex.Data["UserMessage"] += "\nKhông thể thực thi tác vụ thay đổi dữ liệu";
                throw ex;
            }
            return true;
        }
    }
}
