using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class ObjectCTDT
    {
        String mabm;
        String mamh;
        Int64 hocky;
        Int64 chuyennganh;
        String tuchon;
        String tenmh;
        String tinchi;

        public String MaBoMon { get { return mabm; } set { mabm = value; } }
        public String MaMonHoc { get { return mamh; } set { mamh = value; } }
        public Int64 HocKy { get { return hocky; } set { hocky = value; } }
        public Int64 ChuyenNganh { get { return chuyennganh; } set { chuyennganh = value; } }
        public String TuChon { get { return tuchon; } set { tuchon = value; } }
        public String TenMonHoc { get { return tenmh; } set { tenmh = value; } }
        public String TinChi { get { return tinchi; } set { tinchi = value; } }

        public ObjectCTDT(String mabm, String mamh, Int64 hocky, Int64 chuyennganh, String tuchon, String tenmh, String tinchi)
        {
            this.mabm = mabm;
            this.mamh = mamh;
            this.hocky = hocky;
            this.chuyennganh = chuyennganh;
            this.tuchon = tuchon;
            this.tenmh = tenmh;
            this.tinchi = tinchi;
        }
    }
}
