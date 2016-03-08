package com.nckh2016.vuduytung.nckh2016.Data;

/**
 * Created by Tung on 24/2/2016.
 */
public class ObjectUser {
    String masv;
    String makhoa;
    String manganh;
    String hoten;
    String namhoc;
    String email;
    String hocky;

    public ObjectUser(String masv, String makhoa, String manganh, String hoten, String namhoc, String email, String hocky) {
        this.masv = masv;
        this.makhoa = makhoa;
        this.manganh = manganh;
        this.hoten = hoten;
        this.namhoc = namhoc;
        this.email = email;
        this.hocky = hocky;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }

    public String getManganh() {
        return manganh;
    }

    public void setManganh(String manganh) {
        this.manganh = manganh;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHocky() {
        return hocky;
    }

    public void setHocky(String hocky) {
        this.hocky = hocky;
    }
}
