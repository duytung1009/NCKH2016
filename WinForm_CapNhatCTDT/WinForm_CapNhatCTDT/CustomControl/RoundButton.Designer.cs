namespace WinForm_CapNhatCTDT.CustomControl
{
    partial class RoundButton
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.button = new System.Windows.Forms.Button();
            this.label = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // button
            // 
            this.button.BackColor = System.Drawing.Color.Transparent;
            this.button.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.button.Cursor = System.Windows.Forms.Cursors.Hand;
            this.button.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.button.Location = new System.Drawing.Point(25, 25);
            this.button.Name = "button";
            this.button.Size = new System.Drawing.Size(50, 50);
            this.button.TabIndex = 0;
            this.button.UseVisualStyleBackColor = false;
            // 
            // label
            // 
            this.label.Font = new System.Drawing.Font("Segoe UI", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label.Location = new System.Drawing.Point(0, 75);
            this.label.Name = "label";
            this.label.Size = new System.Drawing.Size(100, 25);
            this.label.TabIndex = 1;
            this.label.Text = "text";
            this.label.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // RoundButton
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.None;
            this.BackColor = System.Drawing.Color.Transparent;
            this.Controls.Add(this.label);
            this.Controls.Add(this.button);
            this.Name = "RoundButton";
            this.Size = new System.Drawing.Size(100, 100);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button button;
        private System.Windows.Forms.Label label;
    }
}
