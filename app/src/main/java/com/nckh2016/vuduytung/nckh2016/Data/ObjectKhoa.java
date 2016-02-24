package com.nckh2016.vuduytung.nckh2016.Data;

/**
 * Created by Tung on 24/2/2016.
 */
public class ObjectKhoa {
    String makhoa;
    String tenkhoa;
    String truongkhoa;

    public ObjectKhoa(String makhoa, String tenkhoa, String truongkhoa) {
        this.makhoa = makhoa;
        this.tenkhoa = tenkhoa;
        this.truongkhoa = truongkhoa;
    }

    public String getMakhoa() {
        return makhoa;
    }

    public void setMakhoa(String makhoa) {
        this.makhoa = makhoa;
    }

    public String getTenkhoa() {
        return tenkhoa;
    }

    public void setTenkhoa(String tenkhoa) {
        this.tenkhoa = tenkhoa;
    }

    public String getTruongkhoa() {
        return truongkhoa;
    }

    public void setTruongkhoa(String truongkhoa) {
        this.truongkhoa = truongkhoa;
    }
}
