using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WinForm_CapNhatCTDT.LogicLayer
{
    class ObjectKhoa
    {
        private String makhoa;
        private String tenkhoa;
        private String truongkhoa;

        public String MaKhoa { get { return makhoa; } set { makhoa = value; } }
        public String TenKhoa { get { return tenkhoa; } set { tenkhoa = value; } }
        public String TruongKhoa { get { return truongkhoa; } set { truongkhoa = value; } }

        public ObjectKhoa()
        {

        }

        public ObjectKhoa(String makhoa, String tenkhoa, String truongkhoa)
        {
            this.makhoa = makhoa;
            this.tenkhoa = tenkhoa;
            this.truongkhoa = truongkhoa;
        }
    }
}
