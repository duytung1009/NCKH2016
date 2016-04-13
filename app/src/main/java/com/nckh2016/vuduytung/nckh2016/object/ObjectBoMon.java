package com.nckh2016.vuduytung.nckh2016.object;

/**
 * Created by Tung on 2/3/2016.
 */
public class ObjectBoMon {
    String mabomon;
    String makhoa;
    String tenbomon;

    public ObjectBoMon(String mabomon, String makhoa, String tenbomon) {
        this.mabomon = mabomon;
        this.makhoa = makhoa;
        this.tenbomon = tenbomon;
    }

    public String getMabomon() {
        return mabomon;
    }

    public void setMabomon(String mabomon) {
        this.mabomon = mabomon;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }

    public String getTenbomon() {
        return tenbomon;
    }

    public void setTenbomon(String tenbomon) {
        this.tenbomon = tenbomon;
    }
}
