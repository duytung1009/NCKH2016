package com.nckh2016.vuduytung.nckh2016.object;

/**
 * Created by Tung on 4/3/2016.
 */
public class ObjectHocKy2 implements Items {
    String nganh;
    int hocky;
    int chuyensau;
    String tenchuyensau;

    @Override
    public boolean isHocKy() {
        return true;
    }

    public ObjectHocKy2(String nganh, int hocky, int chuyensau, String tenchuyensau) {
        this.nganh = nganh;
        this.hocky = hocky;
        this.chuyensau = chuyensau;
        this.tenchuyensau = tenchuyensau;
    }

    public String getNganh() {
        return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }

    public int getHocky() {
        return hocky;
    }

    public void setHocky(int hocky) {
        this.hocky = hocky;
    }

    public int getChuyensau() {
        return chuyensau;
    }

    public void setChuyensau(int chuyensau) {
        this.chuyensau = chuyensau;
    }

    public String getTenchuyensau() {
        return tenchuyensau;
    }

    public void setTenchuyensau(String tenchuyensau) {
        this.tenchuyensau = tenchuyensau;
    }
}
