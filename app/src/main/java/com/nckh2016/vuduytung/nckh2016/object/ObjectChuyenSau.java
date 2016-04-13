package com.nckh2016.vuduytung.nckh2016.object;

/**
 * Created by Tung on 11/3/2016.
 */
public class ObjectChuyenSau {
    String manganh;
    String machuyensau;
    String tenchuyensau;

    public ObjectChuyenSau(String manganh, String machuyensau, String tenchuyensau) {
        this.manganh = manganh;
        this.machuyensau = machuyensau;
        this.tenchuyensau = tenchuyensau;
    }

    public String getManganh() {
        return manganh;
    }

    public void setManganh(String manganh) {
        this.manganh = manganh;
    }

    public String getMachuyensau() {
        return machuyensau;
    }

    public void setMachuyensau(String machuyensau) {
        this.machuyensau = machuyensau;
    }

    public String getTenchuyensau() {
        return tenchuyensau;
    }

    public void setTenchuyensau(String tenchuyensau) {
        this.tenchuyensau = tenchuyensau;
    }
}
