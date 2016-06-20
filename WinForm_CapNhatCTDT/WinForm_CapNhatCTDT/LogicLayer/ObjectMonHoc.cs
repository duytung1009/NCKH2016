using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class ObjectMonHoc
    {
        private String mamh;
        private String mabm;
        private String tenmh;
        private Int64 tinchi;
        private String dieukien;
        private String noidung;
        private String tailieu;

        public String MaMonHoc { get { return mamh; } set { mamh = value; } }
        public String MaBoMon { get { return mabm; } set { mabm = value; } }
        public String TenMonHoc { get { return tenmh; } set { tenmh = value; } }
        public Int64 TinChi { get { return tinchi; } set { tinchi = value; } }
        public String DieuKien { get { return dieukien; } set { dieukien = value; } }
        public String NoiDung { get { return noidung; } set { noidung = value; } }
        public String TaiLieu { get { return tailieu; } set { tailieu = value; } }

        public ObjectMonHoc()
        {

        }

        public ObjectMonHoc(String mamh, String mabm, String tenmh, Int64 tinchi, String dieukien, String noidung, String tailieu)
        {
            this.mamh = mamh;
            this.mabm = mabm;
            this.tenmh = tenmh;
            this.tinchi = tinchi;
            this.dieukien = dieukien;
            this.noidung = noidung;
            this.tailieu = tailieu;
        }
    }
}
