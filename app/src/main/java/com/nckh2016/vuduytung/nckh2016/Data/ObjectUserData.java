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
    byte[] bangdiem;

    public ObjectUserData() {
        this.masv = null;
        this.mamonhoc = null;
        this.hocky = null;
        this.namthu = null;
        this.diemso = null;
        this.bangdiem = null;
    }

    public ObjectUserData(String masv, String mamonhoc, String hocky, String namthu, String diemso) {
        this.masv = masv;
        this.mamonhoc = mamonhoc;
        this.hocky = hocky;
        this.namthu = namthu;
        this.diemso = diemso;
        this.bangdiem = null;
    }

    public ObjectUserData(String masv, String mamonhoc, String hocky, String namthu, String diemso, byte[] bangdiem) {
        this.masv = masv;
        this.mamonhoc = mamonhoc;
        this.hocky = hocky;
        this.namthu = namthu;
        this.diemso = diemso;
        this.bangdiem = bangdiem;
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

    public byte[] getBangdiem() {
        return bangdiem;
    }

    public void setBangdiem(byte[] bangdiem) {
        this.bangdiem = bangdiem;
    }
}
