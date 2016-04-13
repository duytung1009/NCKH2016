package com.nckh2016.vuduytung.nckh2016.object;

/**
 * Created by Tung on 3/3/2016.
 */
public class ObjectCTDT implements Items {
    String mabm;
    String mamh;
    int hocky;
    int chuyennganh;
    String tuchon;

    String tenmh;
    String tinchi;

    @Override
    public boolean isHocKy() {
        return false;
    }

    public ObjectCTDT(String mabm, String mamh, int hocky, int chuyennganh, String tuchon, String tenmh, String tinchi) {
        this.mabm = mabm;
        this.mamh = mamh;
        this.hocky = hocky;
        this.chuyennganh = chuyennganh;
        this.tuchon = tuchon;
        this.tenmh = tenmh;
        this.tinchi = tinchi;
    }

    public String getMabm() {
        return mabm;
    }

    public void setMabm(String mabm) {
        this.mabm = mabm;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public int getHocky() {
        return hocky;
    }

    public void setHocky(int hocky) {
        this.hocky = hocky;
    }

    public int getChuyennganh() {
        return chuyennganh;
    }

    public void setChuyennganh(int chuyennganh) {
        this.chuyennganh = chuyennganh;
    }

    public String getTuchon() {
        return tuchon;
    }

    public void setTuchon(String tuchon) {
        this.tuchon = tuchon;
    }

    public String getTenmh() {
        return tenmh;
    }

    public void setTenmh(String tenmh) {
        this.tenmh = tenmh;
    }

    public String getTinchi() {
        return tinchi;
    }

    public void setTinchi(String tinchi) {
        this.tinchi = tinchi;
    }
}
