package com.nckh2016.vuduytung.nckh2016.Data;

/**
 * Created by Tung on 24/2/2016.
 */
public class ObjectMonHoc implements Items{
    String mamh;
    String mabm;
    String tenmh;
    Integer tinchi;
    String dieukien;
    String noidung;
    String tailieu;
    String tuchon;
    boolean isHocKy = false;
    double diem = -1;
    boolean bangdiem = false;

    @Override
    public boolean isHocKy() {
        return isHocKy;
    }

    public ObjectMonHoc() {
    }

    public ObjectMonHoc(String mamh, String mabm, String tenmh, Integer tinchi, String dieukien, String noidung, String tailieu, String tuchon){
        this.mamh = mamh;
        this.mabm = mabm;
        this.tenmh = tenmh;
        this.tinchi = tinchi;
        this.dieukien = dieukien;
        this.noidung = noidung;
        this.tailieu = tailieu;
        this.tuchon = tuchon;
    }

    public ObjectMonHoc(String mamh, String mabm, String tenmh, Integer tinchi, String dieukien, String noidung, String tailieu, String tuchon, double diem, boolean bangdiem){
        this.mamh = mamh;
        this.mabm = mabm;
        this.tenmh = tenmh;
        this.tinchi = tinchi;
        this.dieukien = dieukien;
        this.noidung = noidung;
        this.tailieu = tailieu;
        this.tuchon = tuchon;
        this.diem = diem;
        this.bangdiem = bangdiem;
    }

    public ObjectMonHoc(String mamh, double diem, Integer tinchi) {
        this.mamh = mamh;
        this.diem = diem;
        this.tinchi = tinchi;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public String getMabm() {
        return mabm;
    }

    public void setMabm(String mabm) {
        this.mabm = mabm;
    }

    public String getTenmh() {
        return tenmh;
    }

    public void setTenmh(String tenmh) {
        this.tenmh = tenmh;
    }

    public Integer getTinchi() {
        return tinchi;
    }

    public void setTinchi(Integer tinchi) {
        this.tinchi = tinchi;
    }

    public String getDieukien() {
        return dieukien;
    }

    public void setDieukien(String dieukien) {
        this.dieukien = dieukien;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTailieu() {
        return tailieu;
    }

    public void setTailieu(String tailieu) {
        this.tailieu = tailieu;
    }

    public String getTuchon() {
        return tuchon;
    }

    public void setTuchon(String tuchon) {
        this.tuchon = tuchon;
    }

    public void setIsHocKy(boolean isHocKy) {
        this.isHocKy = isHocKy;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public boolean isBangdiem() {
        return bangdiem;
    }

    public void setBangdiem(boolean bangdiem) {
        this.bangdiem = bangdiem;
    }
}
