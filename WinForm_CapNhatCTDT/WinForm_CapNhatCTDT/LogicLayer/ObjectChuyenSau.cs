using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class ObjectChuyenSau
    {
        private String manganh;
        private String machuyensau;
        private String tenchuyensau;

        public String MaNganh { get { return manganh; } set { manganh = value; } }
        public String MaChuyenSau { get { return machuyensau; } set { machuyensau = value; } }
        public String TenChuyenSau { get { return tenchuyensau; } set { tenchuyensau = value; } }

        public ObjectChuyenSau(String manganh, String machuyensau, String tenchuyensau)
        {
            this.manganh = manganh;
            this.machuyensau = machuyensau;
            this.tenchuyensau = tenchuyensau;
        }
    }
}
