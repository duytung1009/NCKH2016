using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WinForm_CapNhatCTDT.CustomControl
{
    public partial class RoundButton : UserControl
    {    
        public RoundButton()
        {
            InitializeComponent();
            button.FlatAppearance.BorderSize = 0;
            button.FlatAppearance.MouseOverBackColor = Color.Transparent;
            button.FlatAppearance.MouseDownBackColor = Color.Transparent;
        }

        public void setBackground(Image background)
        {
            button.BackgroundImage = background;
        }

        public void setText(String text)
        {
            label.Text = text;
        }  

        
    }
}
