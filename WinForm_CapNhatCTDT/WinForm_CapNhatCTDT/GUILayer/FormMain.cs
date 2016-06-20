using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using WinForm_CapNhatCTDT.LogicLayer;

namespace WinForm_CapNhatCTDT
{
    public partial class Form1 : Form
    {
        public static String DATABASE_PATH = "..\\..\\Database\\MainDB.sqlite";

        List<Button> RoundButton = new List<Button>();
        List<Button> KhoaGp1 = new List<Button>();
        List<Button> KhoaGp2 = new List<Button>();
        List<Button> NganhGp1 = new List<Button>();
        List<Button> NganhGp2 = new List<Button>();
        List<Button> BoMonGp1 = new List<Button>();
        List<Button> BoMonGp2 = new List<Button>();
        List<Button> ChuyenSauGp1 = new List<Button>();
        List<Button> ChuyenSauGp2 = new List<Button>();
        List<Button> MonHocGp1 = new List<Button>();
        List<Button> MonHocGp2 = new List<Button>();
        List<TextBox> txtKhoa = new List<TextBox>();
        List<TextBox> txtNganh = new List<TextBox>();
        List<TextBox> txtBoMon = new List<TextBox>();
        List<TextBox> txtChuyenSau = new List<TextBox>();
        List<TextBox> txtMonHoc = new List<TextBox>();
        List<TextBox> txtMonHoc2 = new List<TextBox>();

        public Form1()
        {
            InitializeComponent();
            txtKhoa.Add(textBoxKhoa_MaKhoa);
            txtKhoa.Add(textBoxKhoa_TenKhoa);
            txtKhoa.Add(textBoxKhoa_TruongKhoa);
            txtNganh.Add(textBoxNganh_MaNganh);
            txtNganh.Add(textBoxNganh_TenNganh);
            txtBoMon.Add(textBoxBoMon_MaBoMon);
            txtBoMon.Add(textBoxBoMon_TenBoMon);
            txtChuyenSau.Add(textBoxChuyenSau_MaChuyenSau);
            txtChuyenSau.Add(textBoxChuyenSau_TenChuyenSau);
            txtMonHoc.Add(textBoxMonHoc_MaMonHoc);
            txtMonHoc.Add(textBoxMonHoc_TenMonHoc);
            txtMonHoc.Add(textBoxMonHoc_TinChi);
            txtMonHoc.Add(textBoxMonHoc_DieuKien);
            txtMonHoc.Add(textBoxMonHoc_NoiDung);
            txtMonHoc.Add(textBoxMonHoc_TaiLieu);
            txtMonHoc2.Add(textBoxMonHoc_MaMonHoc);
            txtMonHoc2.Add(textBoxMonHoc_TenMonHoc);
            txtMonHoc2.Add(textBoxMonHoc_TinChi);
            //ctdt
            RoundButton.Add(buttonCTDTInsert);
            RoundButton.Add(buttonCTDTUpdate);
            RoundButton.Add(buttonCTDTDelete);
            //tuchon
            RoundButton.Add(buttonTuChonInsert);
            RoundButton.Add(buttonTuChonUpdate);
            RoundButton.Add(buttonTuChonDelete);
            RoundButton.Add(buttonTuChonSearch);
            //khoa
            RoundButton.Add(buttonKhoaInsert);
            RoundButton.Add(buttonKhoaUpdate);
            RoundButton.Add(buttonKhoaDelete);
            RoundButton.Add(buttonKhoaCancel);
            RoundButton.Add(buttonKhoaAccept);
            RoundButton.Add(buttonKhoaSearch);
            KhoaGp1.Add(buttonKhoaInsert);
            KhoaGp1.Add(buttonKhoaUpdate);
            KhoaGp1.Add(buttonKhoaDelete);
            KhoaGp2.Add(buttonKhoaCancel);
            KhoaGp2.Add(buttonKhoaAccept);
            //nganh
            RoundButton.Add(buttonNganhInsert);
            RoundButton.Add(buttonNganhUpdate);
            RoundButton.Add(buttonNganhDelete);
            RoundButton.Add(buttonNganhCancel);
            RoundButton.Add(buttonNganhAccept);
            RoundButton.Add(buttonNganhSearch);
            NganhGp1.Add(buttonNganhInsert);
            NganhGp1.Add(buttonNganhUpdate);
            NganhGp1.Add(buttonNganhDelete);
            NganhGp2.Add(buttonNganhCancel);
            NganhGp2.Add(buttonNganhAccept);
            //bo mon
            RoundButton.Add(buttonBoMonInsert);
            RoundButton.Add(buttonBoMonUpdate);
            RoundButton.Add(buttonBoMonDelete);
            RoundButton.Add(buttonBoMonCancel);
            RoundButton.Add(buttonBoMonAccept);
            RoundButton.Add(buttonBoMonSearch);
            BoMonGp1.Add(buttonBoMonInsert);
            BoMonGp1.Add(buttonBoMonUpdate);
            BoMonGp1.Add(buttonBoMonDelete);
            BoMonGp2.Add(buttonBoMonCancel);
            BoMonGp2.Add(buttonBoMonAccept);
            //chuyen sau
            RoundButton.Add(buttonChuyenSauInsert);
            RoundButton.Add(buttonChuyenSauUpdate);
            RoundButton.Add(buttonChuyenSauDelete);
            RoundButton.Add(buttonChuyenSauCancel);
            RoundButton.Add(buttonChuyenSauAccept);
            RoundButton.Add(buttonChuyenSauSearch);
            ChuyenSauGp1.Add(buttonChuyenSauInsert);
            ChuyenSauGp1.Add(buttonChuyenSauUpdate);
            ChuyenSauGp1.Add(buttonChuyenSauDelete);
            ChuyenSauGp2.Add(buttonChuyenSauCancel);
            ChuyenSauGp2.Add(buttonChuyenSauAccept);
            //mon hoc
            RoundButton.Add(buttonMonHocInsert);
            RoundButton.Add(buttonMonHocUpdate);
            RoundButton.Add(buttonMonHocDelete);
            RoundButton.Add(buttonMonHocCancel);
            RoundButton.Add(buttonMonHocAccept);
            RoundButton.Add(buttonMonHocSearch);
            RoundButton.Add(buttonMonHoc_DieuKien);
            MonHocGp1.Add(buttonMonHocInsert);
            MonHocGp1.Add(buttonMonHocUpdate);
            MonHocGp1.Add(buttonMonHocDelete);
            MonHocGp2.Add(buttonMonHoc_DieuKien);
            MonHocGp2.Add(buttonMonHocCancel);
            MonHocGp2.Add(buttonMonHocAccept);
        }

        private void Form1_Shown(object sender, EventArgs e)
        {
            foreach(Button button in RoundButton)
            {
                button.FlatAppearance.BorderSize = 0;
                button.FlatAppearance.MouseOverBackColor = Color.Transparent;
                button.FlatAppearance.MouseDownBackColor = Color.Transparent;
            }
            DoubleBuffered = true;
        }

        private void changeEdit(List<TextBox> controls, Boolean flag)
        {
            foreach(TextBox txt in controls)
            {
                txt.Enabled = flag;
            }
        }

        private void emptyEdit(List<TextBox> controls)
        {
            foreach (TextBox txt in controls)
            {
                txt.Text = "";
            }
        }

        private void changeButtonVisible(List<Button> controls, Boolean flag)
        {
            foreach (Button btn in controls)
            {
                btn.Visible = flag;
            }
        }

        private bool checkEmpty(List<TextBox> controls)
        {
            foreach (TextBox txt in controls)
            {
                if((txt.Text == null) || (txt.Text.Equals(String.Empty)))
                {
                    return false;
                }
            }
            return true;
        }

        private void tabControl1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if(tabControl1.SelectedIndex == 0)
            {
                
            }
            else if(tabControl1.SelectedIndex == 1)
            {
                tabCTDT_load();
            }
            else if (tabControl1.SelectedIndex == 2)
            {
                tabTuChon_load();               
            }
            else if (tabControl1.SelectedIndex == 3)
            {
                tabKhoa_load();
            }
            else if (tabControl1.SelectedIndex == 4)
            {
                tabChuyenNganh_load();
            }
            else if (tabControl1.SelectedIndex == 5)
            {
                tabBoMon_load();
            }
            else if (tabControl1.SelectedIndex == 6)
            {
                tabHuongChuyenSau_load();
            }
            else if (tabControl1.SelectedIndex == 7)
            {
                tabMonHoc_load();
            }
        }



        //tab khoa
        int khoaState;
        private void khoaControlChange(bool flag)
        {
            changeButtonVisible(KhoaGp1, flag);
            changeButtonVisible(KhoaGp2, !flag);
            changeEdit(txtKhoa, !flag);
        }

        private void tabKhoa_load()
        {
            khoaControlChange(true);
            dataGridView_Khoa.DataSource = DataQuery.getKhoa();
        }

        private void dataGridView_Khoa_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                DataGridViewRow row = dataGridView_Khoa.Rows[e.RowIndex];
                textBoxKhoa_MaKhoa.Text = row.Cells[0].Value.ToString();
                textBoxKhoa_TenKhoa.Text = row.Cells[1].Value.ToString();
                textBoxKhoa_TruongKhoa.Text = row.Cells[2].Value.ToString();
            }
            catch
            {

            }
        }

        private void buttonKhoaSearch_Click(object sender, EventArgs e)
        {
            dataGridView_Khoa.DataSource = DataQuery.searchKhoa(textBoxKhoaSearch.Text);
        }

        private void buttonKhoaInsert_Click(object sender, EventArgs e)
        {
            khoaState = 1;
            khoaControlChange(false);
            emptyEdit(txtKhoa);       
        }

        private void buttonKhoaUpdate_Click(object sender, EventArgs e)
        {
            khoaState = 2;
            khoaControlChange(false);
        }

        private void buttonKhoaDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Xóa trường đã chọn", "Xác nhận", MessageBoxButtons.YesNo);
            if (dialogResult == DialogResult.Yes)
            {
                if (!((textBoxKhoa_MaKhoa.Text == null) || (textBoxKhoa_MaKhoa.Text.Equals(String.Empty))))
                {
                    DataQuery.deleteKhoa(textBoxKhoa_MaKhoa.Text);
                    tabKhoa_load();
                }
                else
                {
                    MessageBox.Show("Nhập đầy đủ các trường");
                }
            }
            else if (dialogResult == DialogResult.No)
            {
                //do something else
            }            
        }

        private void buttonKhoaCancel_Click(object sender, EventArgs e)
        {
            khoaControlChange(true);
        }

        private void buttonKhoaAccept_Click(object sender, EventArgs e)
        {
            khoaControlChange(true);
            if (checkEmpty(txtKhoa))
            {
                ObjectKhoa value = new ObjectKhoa(textBoxKhoa_MaKhoa.Text, textBoxKhoa_TenKhoa.Text, textBoxKhoa_TruongKhoa.Text);                
                if (khoaState == 1)
                {
                    DataQuery.insertKhoa(value);
                }
                else if (khoaState == 2)
                {
                    DataQuery.updateKhoa(value);
                }
                tabKhoa_load();
            }       
            else
            {
                MessageBox.Show("Nhập đầy đủ các trường");
            }    
        }



        //tab nganh
        int nganhState;
        private void nganhControlChange(bool flag)
        {
            changeButtonVisible(NganhGp1, flag);
            changeButtonVisible(NganhGp2, !flag);
            changeEdit(txtNganh, !flag);
            comboBoxNganh_MaKhoa.Enabled = !flag;
        }

        private void tabChuyenNganh_load()
        {
            nganhControlChange(true);
            dataGridView_Nganh.DataSource = DataQuery.getNganh();
            comboBoxNganh_MaKhoa.DataSource = DataQuery.getKhoa();
            comboBoxNganh_MaKhoa.DisplayMember = "TenKhoa";
            comboBoxNganh_MaKhoa.ValueMember = "MaKhoa";
        }

        private void dataGridView_Nganh_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                DataGridViewRow row = dataGridView_Nganh.Rows[e.RowIndex];
                comboBoxNganh_MaKhoa.SelectedValue = row.Cells[1].Value.ToString();
                textBoxNganh_MaNganh.Text = row.Cells[0].Value.ToString();
                textBoxNganh_TenNganh.Text = row.Cells[2].Value.ToString();
            }
            catch
            {

            }
        }

        private void buttonNganhSearch_Click(object sender, EventArgs e)
        {
            dataGridView_Nganh.DataSource = DataQuery.searchNganh(textBoxNganhSearch.Text);
        }

        private void buttonNganhInsert_Click(object sender, EventArgs e)
        {
            nganhState = 1;
            nganhControlChange(false);
            emptyEdit(txtNganh);
        }

        private void buttonNganhUpdate_Click(object sender, EventArgs e)
        {
            nganhState = 2;
            nganhControlChange(false);
        }

        private void buttonNganhDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Xóa trường đã chọn", "Xác nhận", MessageBoxButtons.YesNo);
            if (dialogResult == DialogResult.Yes)
            {
                if (!((textBoxNganh_MaNganh.Text == null) || (textBoxNganh_MaNganh.Text.Equals(String.Empty))))
                {
                    DataQuery.deleteNganh(textBoxNganh_MaNganh.Text);
                    tabChuyenNganh_load();
                }
                else
                {
                    MessageBox.Show("Nhập đầy đủ các trường");
                }
            }
            else if (dialogResult == DialogResult.No)
            {
                //do something else
            }           
        }

        private void buttonNganhCancel_Click(object sender, EventArgs e)
        {
            nganhControlChange(true);
        }

        private void buttonNganhAccept_Click(object sender, EventArgs e)
        {
            nganhControlChange(true);
            if (checkEmpty(txtNganh))
            {
                ObjectNganh value = new ObjectNganh(textBoxNganh_MaNganh.Text, (comboBoxNganh_MaKhoa.SelectedItem as ObjectKhoa).MaKhoa, textBoxNganh_TenNganh.Text);
                if (nganhState == 1)
                {
                    DataQuery.insertNganh(value);
                }
                else if (nganhState == 2)
                {
                    DataQuery.updateNganh(value);
                }
                tabChuyenNganh_load();
            }
            else
            {
                MessageBox.Show("Nhập đầy đủ các trường");
            }
        }



        //tab bo mon
        int bomonState;
        private void bomonControlChange(bool flag)
        {
            changeButtonVisible(BoMonGp1, flag);
            changeButtonVisible(BoMonGp2, !flag);
            changeEdit(txtBoMon, !flag);
            comboBoxBoMon_MaKhoa.Enabled = !flag;
        }

        private void tabBoMon_load()
        {
            bomonControlChange(true);
            dataGridView_BoMon.DataSource = DataQuery.getBoMon();
            comboBoxBoMon_MaKhoa.DataSource = DataQuery.getKhoa();
            comboBoxBoMon_MaKhoa.DisplayMember = "TenKhoa";
            comboBoxBoMon_MaKhoa.ValueMember = "MaKhoa";           
        }

        private void dataGridView_BoMon_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                DataGridViewRow row = dataGridView_BoMon.Rows[e.RowIndex];
                comboBoxBoMon_MaKhoa.SelectedValue = row.Cells[1].Value.ToString();
                textBoxBoMon_MaBoMon.Text = row.Cells[0].Value.ToString();
                textBoxBoMon_TenBoMon.Text = row.Cells[2].Value.ToString();
            }
            catch
            {

            }
        }

        private void buttonBoMonSearch_Click(object sender, EventArgs e)
        {
            dataGridView_BoMon.DataSource = DataQuery.searchBoMon(textBoxBoMonSearch.Text);
        }

        private void buttonBoMonInsert_Click(object sender, EventArgs e)
        {
            bomonState = 1;
            bomonControlChange(false);
            emptyEdit(txtBoMon);
        }

        private void buttonBoMonUpdate_Click(object sender, EventArgs e)
        {
            bomonState = 2;
            bomonControlChange(false);
        }

        private void buttonBoMonDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Xóa trường đã chọn", "Xác nhận", MessageBoxButtons.YesNo);
            if (dialogResult == DialogResult.Yes)
            {
                if (!((textBoxBoMon_MaBoMon.Text == null) || (textBoxBoMon_MaBoMon.Text.Equals(String.Empty))))
                {
                    DataQuery.deleteBoMon(textBoxBoMon_MaBoMon.Text);
                    tabBoMon_load();
                }
                else
                {
                    MessageBox.Show("Nhập đầy đủ các trường");
                }
            }
            else if (dialogResult == DialogResult.No)
            {
                //do something else
            }            
        }

        private void buttonBoMonCancel_Click(object sender, EventArgs e)
        {
            bomonControlChange(true);
        }

        private void buttonBoMonAccept_Click(object sender, EventArgs e)
        {
            bomonControlChange(true);
            if (checkEmpty(txtBoMon))
            {
                ObjectBoMon value = new ObjectBoMon(textBoxBoMon_MaBoMon.Text, (comboBoxBoMon_MaKhoa.SelectedItem as ObjectKhoa).MaKhoa, textBoxBoMon_TenBoMon.Text);
                if (bomonState == 1)
                {
                    DataQuery.insertBoMon(value);
                }
                else if (bomonState == 2)
                {
                    DataQuery.updateBoMon(value);
                }
                tabBoMon_load();
            }
            else
            {
                MessageBox.Show("Nhập đầy đủ các trường");
            }
        }



        //chuyen sau
        int chuyensauState;
        private void chuyensauControlChange(bool flag)
        {
            changeButtonVisible(ChuyenSauGp1, flag);
            changeButtonVisible(ChuyenSauGp2, !flag);           
            changeEdit(txtChuyenSau, !flag);
            comboBoxChuyenSau_MaNganh.Enabled = !flag;
        }

        private void tabHuongChuyenSau_load()
        {                      
            chuyensauControlChange(true);
            dataGridView_ChuyenSau.DataSource = DataQuery.getChuyenSau();
            comboBoxChuyenSau_MaNganh.DataSource = DataQuery.getNganh();
            comboBoxChuyenSau_MaNganh.DisplayMember = "TenNganh";
            comboBoxChuyenSau_MaNganh.ValueMember = "MaNganh";
        }

        private void dataGridView_ChuyenSau_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                DataGridViewRow row = dataGridView_ChuyenSau.Rows[e.RowIndex];
                comboBoxChuyenSau_MaNganh.SelectedValue = row.Cells[0].Value.ToString();
                textBoxChuyenSau_MaChuyenSau.Text = row.Cells[1].Value.ToString();
                textBoxChuyenSau_TenChuyenSau.Text = row.Cells[2].Value.ToString();
            }
            catch 
            {

            }            
        }

        private void buttonChuyenSauSearch_Click(object sender, EventArgs e)
        {
            dataGridView_ChuyenSau.DataSource = DataQuery.searchChuyenSau(textBoxChuyenSauSearch.Text);
        }

        private void buttonChuyenSauInsert_Click(object sender, EventArgs e)
        {
            chuyensauState = 1;
            chuyensauControlChange(false);
            emptyEdit(txtChuyenSau);
        }

        private void buttonChuyenSauUpdate_Click(object sender, EventArgs e)
        {
            chuyensauState = 2;
            chuyensauControlChange(false);
            comboBoxChuyenSau_MaNganh.Enabled = false;
            textBoxChuyenSau_MaChuyenSau.Enabled = false;
        }

        private void buttonChuyenSauDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Xóa trường đã chọn", "Xác nhận", MessageBoxButtons.YesNo);
            if (dialogResult == DialogResult.Yes)
            {
                if (!((textBoxChuyenSau_MaChuyenSau.Text == null) || (textBoxChuyenSau_MaChuyenSau.Text.Equals(String.Empty))))
                {
                    DataQuery.deleteChuyenSau((comboBoxChuyenSau_MaNganh.SelectedItem as ObjectNganh).MaNganh, textBoxChuyenSau_MaChuyenSau.Text);
                    tabHuongChuyenSau_load();
                }
                else
                {
                    MessageBox.Show("Nhập đầy đủ các trường");
                }
            }
            else if (dialogResult == DialogResult.No)
            {
                //do something else
            }           
        }

        private void buttonChuyenSauCancel_Click(object sender, EventArgs e)
        {
            chuyensauControlChange(true);
        }

        private void buttonChuyenSauAccept_Click(object sender, EventArgs e)
        {
            chuyensauControlChange(true);
            if (checkEmpty(txtChuyenSau))
            {
                ObjectChuyenSau value = new ObjectChuyenSau((comboBoxChuyenSau_MaNganh.SelectedItem as ObjectNganh).MaNganh, textBoxChuyenSau_MaChuyenSau.Text, textBoxChuyenSau_TenChuyenSau.Text);
                if (chuyensauState == 1)
                {
                    DataQuery.insertChuyenSau(value);
                }
                else if (chuyensauState == 2)
                {
                    DataQuery.updateChuyenSau(value);
                }
                tabHuongChuyenSau_load();
            }
            else
            {
                MessageBox.Show("Nhập đầy đủ các trường");
            }
        }



        //mon hoc      
        int monhocState;
        private void monhocControlChange(bool flag)
        {
            changeButtonVisible(MonHocGp1, flag);
            changeButtonVisible(MonHocGp2, !flag);
            changeEdit(txtMonHoc, !flag);
            comboBoxMonHoc_MaBoMon.Enabled = !flag;
        }

        private void tabMonHoc_load()
        {
            monhocControlChange(true);
            dataGridView_MonHoc.DataSource = DataQuery.getMonHoc();
            comboBoxMonHoc_MaBoMon.DataSource = DataQuery.getBoMon();
            comboBoxMonHoc_MaBoMon.DisplayMember = "TenBoMon";
            comboBoxMonHoc_MaBoMon.ValueMember = "MaBoMon";            
        }

        private void dataGridView_MonHoc_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            try
            {
                DataGridViewRow row = dataGridView_MonHoc.Rows[e.RowIndex];
                textBoxMonHoc_MaMonHoc.Text = row.Cells[0].Value.ToString();
                comboBoxMonHoc_MaBoMon.SelectedValue = row.Cells[1].Value.ToString();
                textBoxMonHoc_TenMonHoc.Text = row.Cells[2].Value.ToString();
                textBoxMonHoc_TinChi.Text = row.Cells[3].Value.ToString();
                textBoxMonHoc_DieuKien.Text = row.Cells[4].Value.ToString();
                textBoxMonHoc_NoiDung.Text = row.Cells[5].Value.ToString();
                textBoxMonHoc_TaiLieu.Text = row.Cells[6].Value.ToString();
            }
            catch
            {

            }
        }

        private void buttonSearchMonHoc_Click(object sender, EventArgs e)
        {
            dataGridView_MonHoc.DataSource = DataQuery.searchMonHoc(textBoxSearchMonHoc.Text);
        }

        private void buttonMonHocInsert_Click(object sender, EventArgs e)
        {
            monhocState = 1;
            monhocControlChange(false);
            emptyEdit(txtMonHoc);
        }

        private void buttonMonHocUpdate_Click(object sender, EventArgs e)
        {
            monhocState = 2;
            monhocControlChange(false);
            textBoxMonHoc_MaMonHoc.Enabled = false;
        }

        private void buttonMonHocDelete_Click(object sender, EventArgs e)
        {
            DialogResult dialogResult = MessageBox.Show("Xóa trường đã chọn", "Xác nhận", MessageBoxButtons.YesNo);
            if (dialogResult == DialogResult.Yes)
            {
                if (!((textBoxMonHoc_MaMonHoc.Text == null) || (textBoxMonHoc_MaMonHoc.Text.Equals(String.Empty))))
                {
                    DataQuery.deleteMonHoc(textBoxMonHoc_MaMonHoc.Text);
                    tabMonHoc_load();
                }
                else
                {
                    MessageBox.Show("Nhập đầy đủ các trường");
                }
            }
            else if (dialogResult == DialogResult.No)
            {
                //do something else
            }            
        }

        private void buttonMonHocCancel_Click(object sender, EventArgs e)
        {
            monhocControlChange(true);
        }

        private void buttonMonHocAccept_Click(object sender, EventArgs e)
        {
            monhocControlChange(true);
            if (checkEmpty(txtMonHoc2))
            {
                ObjectMonHoc value = new ObjectMonHoc(
                    textBoxMonHoc_MaMonHoc.Text, 
                    (comboBoxMonHoc_MaBoMon.SelectedItem as ObjectBoMon).MaBoMon,
                    textBoxMonHoc_TenMonHoc.Text,
                    Int64.Parse(textBoxMonHoc_TinChi.Text),
                    textBoxMonHoc_DieuKien.Text,
                    textBoxMonHoc_NoiDung.Text,
                    textBoxMonHoc_TaiLieu.Text);
                if (monhocState == 1)
                {
                    DataQuery.insertMonHoc(value);
                }
                else if (monhocState == 2)
                {
                    DataQuery.updateMonHoc(value);
                }
                tabMonHoc_load();
            }
            else
            {
                MessageBox.Show("Nhập đầy đủ các trường");
            }
        }

        private void textBoxMonHoc_TinChi_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar) && (e.KeyChar != '.'))
            {
                e.Handled = true;
            }
            // only allow one decimal point
            if ((e.KeyChar == '.') && ((sender as TextBox).Text.IndexOf('.') > -1))
            {
                e.Handled = true;
            }
        }



        //tab CTDT
        private void tabCTDT_load()
        {           
            comboBoxCTDT_MaNganh.DataSource = DataQuery.getNganh();
            comboBoxCTDT_MaNganh.DisplayMember = "TenNganh";
            comboBoxCTDT_MaNganh.ValueMember = "MaNganh";
        }

        private void comboBoxCTDT_MaNganh_SelectedIndexChanged(object sender, EventArgs e)
        {
            ObjectNganh current = comboBoxCTDT_MaNganh.SelectedItem as ObjectNganh;
            //load hoc ky
            int maxHocKy = 10;
            Dictionary<Int32, String> hocKy = new Dictionary<int, string>();
            hocKy.Add(0, "Tất cả");
            for (int i = 1; i <= maxHocKy; i++)
            {
                hocKy.Add(i, i.ToString());
            }
            comboBoxCTDT_HocKy.DataSource = new BindingSource(hocKy, null);
            comboBoxCTDT_HocKy.DisplayMember = "Value";
            comboBoxCTDT_HocKy.ValueMember = "Key";
            //load chuyen sau
            if(current != null)
            {
                comboBoxCTDT_ChuyenSau.DataSource = DataQuery.getChuyenSauTheoNganh(current.MaNganh);
                comboBoxCTDT_ChuyenSau.DisplayMember = "TenChuyenSau";
                comboBoxCTDT_ChuyenSau.ValueMember = "MaChuyenSau";
            }
            loadCTDT();
        }

        private void comboBoxCTDT_HocKy_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadCTDT();
        }

        private void comboBoxCTDT_ChuyenSau_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadCTDT();
        }

        private void loadCTDT()
        {
            try
            {
                String maNganh = (comboBoxCTDT_MaNganh.SelectedItem as ObjectNganh).MaNganh;
                Int64 hocKy = Int64.Parse(comboBoxCTDT_HocKy.SelectedValue.ToString());
                Int64 chuyenSau = Int64.Parse((comboBoxCTDT_ChuyenSau.SelectedItem as ObjectChuyenSau).MaChuyenSau);
                dataGridView_CTDT.DataSource = DataQuery.getCTDT(maNganh, hocKy, chuyenSau);
            }
            catch
            {

            }
        }



        //tab tu chon
        private void tabTuChon_load()
        {
            Dictionary<Int32, String> tuChon = new Dictionary<int, string>();
            tuChon.Add(-3, "Tự chọn A");
            tuChon.Add(-2, "Tự chọn B");
            tuChon.Add(-1, "Tự chọn C");
            comboBoxTuChon_TuChon.DataSource = new BindingSource(tuChon, null);
            comboBoxTuChon_TuChon.DisplayMember = "Value";
            comboBoxTuChon_TuChon.ValueMember = "Key";
            comboBoxTuChon_MaKhoa.DataSource = DataQuery.getKhoa();
            comboBoxTuChon_MaKhoa.DisplayMember = "TenKhoa";
            comboBoxTuChon_MaKhoa.ValueMember = "MaKhoa";
            comboBoxTuChon_MaNganh.DataSource = DataQuery.getNganh();
            comboBoxTuChon_MaNganh.DisplayMember = "TenNganh";
            comboBoxTuChon_MaNganh.ValueMember = "MaNganh";
        }

        private void comboBoxTuChon_TuChon_SelectedIndexChanged(object sender, EventArgs e)
        {
            int hocKy = comboBoxTuChon_TuChon.SelectedIndex;
            if (hocKy == 2)
            {
                comboBoxTuChon_MaKhoa.Enabled = false;
                comboBoxTuChon_MaNganh.Enabled = false;
            }
            else if (hocKy == 1)
            {
                comboBoxTuChon_MaKhoa.Enabled = true;
                comboBoxTuChon_MaNganh.Enabled = false;
            }
            else if (hocKy == 0)
            {
                comboBoxTuChon_MaKhoa.Enabled = false;
                comboBoxTuChon_MaNganh.Enabled = true;
            }
            loadTuChon();
        }

        private void comboBoxTuChon_MaKhoa_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadTuChon();
        }

        private void comboBoxTuChon_MaNganh_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadTuChon();
        }

        private void loadTuChon()
        {
            try
            {
                String maKhoa = (comboBoxTuChon_MaKhoa.SelectedItem as ObjectKhoa).MaKhoa;
                String maNganh = (comboBoxTuChon_MaNganh.SelectedItem as ObjectNganh).MaNganh;
                Int64 tuChon = comboBoxTuChon_TuChon.SelectedIndex;
                dataGridView_TuChon.DataSource = DataQuery.getTuChon(maKhoa, maNganh, tuChon);
            }
            catch
            {

            }
        }

        private void buttonTuChonSearch_Click(object sender, EventArgs e)
        {
            dataGridView_TuChon.DataSource = DataQuery.searchTuChon(textBoxTuChonSearch.Text);
        }



        //tab system
        private void button1_Click(object sender, EventArgs e)
        {
            DataQuery.CloseConnection();
            // Prepare a dummy string, thos would appear in the dialog
            string dummyFileName = "nckh2016.sqlite";
            SaveFileDialog sf = new SaveFileDialog();
            // Feed the dummy name to the save dialog
            sf.FileName = dummyFileName;
            if (sf.ShowDialog() == DialogResult.OK)
            {
                try
                {
                    if (File.Exists(sf.FileName))
                    {
                        File.Delete(sf.FileName);
                    }
                    File.Copy(DATABASE_PATH, sf.FileName);
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Lỗi " + ex.Message);
                }
            }
        }     
    }
}
