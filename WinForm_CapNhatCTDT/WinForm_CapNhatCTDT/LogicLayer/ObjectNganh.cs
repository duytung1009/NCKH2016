using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class ObjectNganh
    {
        private String manganh;
        private String makhoa;
        private String tennganh;

        public String MaNganh { get { return manganh; } set { manganh = value; } }
        public String MaKhoa { get { return makhoa; } set { makhoa = value; } }
        public String TenNganh { get { return tennganh; } set { tennganh = value; } }

        public ObjectNganh()
        {

        }

        public ObjectNganh(String manganh, String makhoa, String tennganh)
        {
            this.manganh = manganh;
            this.makhoa = makhoa;
            this.tennganh = tennganh;
        }
    }
}
