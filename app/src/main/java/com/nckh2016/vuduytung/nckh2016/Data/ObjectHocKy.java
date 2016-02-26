package com.nckh2016.vuduytung.nckh2016.Data;

/**
 * Created by Tung on 26/2/2016.
 */
public class ObjectHocKy {
    int namHoc;
    int hocKy;
    String nganh;

    public ObjectHocKy(int namHoc, int hocKy, String nganh) {
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.nganh = nganh;
    }

    public int getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(int namHoc) {
        this.namHoc = namHoc;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public String getNganh() {
        return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }
}
