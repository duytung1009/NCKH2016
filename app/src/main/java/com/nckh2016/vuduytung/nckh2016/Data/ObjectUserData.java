package com.nckh2016.vuduytung.nckh2016.Data;

/**
 * Created by vuduy_000 on 29/02/2016.
 */
public class ObjectUserData {
    String masv;
    String mamonhoc;
    String hocky;
    String namthu;
    String diemso;

    public ObjectUserData(String masv, String mamonhoc, String hocky, String namthu, String diemso) {
        this.masv = masv;
        this.mamonhoc = mamonhoc;
        this.hocky = hocky;
        this.namthu = namthu;
        this.diemso = diemso;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(String mamonhoc) {
        this.mamonhoc = mamonhoc;
    }

    public String getHocky() {
        return hocky;
    }

    public void setHocky(String hocky) {
        this.hocky = hocky;
    }

    public String getNamthu() {
        return namthu;
    }

    public void setNamthu(String namthu) {
        this.namthu = namthu;
    }

    public String getDiemso() {
        return diemso;
    }

    public void setDiemso(String diemso) {
        this.diemso = diemso;
    }
}
