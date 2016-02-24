package com.nckh2016.vuduytung.nckh2016.Data;

/**
 * Created by Tung on 24/2/2016.
 */
public class ObjectNganh {
    String manganh;
    String makhoa;
    String tennganh;

    public ObjectNganh(String manganh, String makhoa, String tennganh) {
        this.manganh = manganh;
        this.makhoa = makhoa;
        this.tennganh = tennganh;
    }

    public String getManganh() {
        return manganh;
    }

    public void setManganh(String manganh) {
        this.manganh = manganh;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }

    public String getTennganh() {
        return tennganh;
    }

    public void setTennganh(String tennganh) {
        this.tennganh = tennganh;
    }
}
