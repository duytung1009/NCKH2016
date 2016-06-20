using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class ObjectBoMon
    {
        private String mabomon;
        private String makhoa;
        private String tenbomon;

        public String MaBoMon { get { return mabomon; } set { mabomon = value; } }
        public String MaKhoa { get { return makhoa; } set { makhoa = value; } }
        public String TenBoMon { get { return tenbomon; } set { tenbomon = value; } }

        public ObjectBoMon(String mabomon, String makhoa, String tenbomon)
        {
            this.mabomon = mabomon;
            this.makhoa = makhoa;
            this.tenbomon = tenbomon;
        }
    }
}
