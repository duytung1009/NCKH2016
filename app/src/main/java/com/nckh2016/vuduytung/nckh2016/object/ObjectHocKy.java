package com.nckh2016.vuduytung.nckh2016.object;

/**
 * Created by Tung on 26/2/2016.
 */
public class ObjectHocKy implements Items {
    int namHoc;
    int hocKy;
    String nganh;
    double diem = -1;

    @Override
    public boolean isHocKy() {
        return true;
    }

    public ObjectHocKy(int namHoc, int hocKy, String nganh) {
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.nganh = nganh;
    }

    public ObjectHocKy(int namHoc, int hocKy, String nganh, double diem) {
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.nganh = nganh;
        this.diem = diem;
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

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }
}
