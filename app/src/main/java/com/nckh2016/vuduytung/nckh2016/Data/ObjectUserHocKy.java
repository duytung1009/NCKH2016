package com.nckh2016.vuduytung.nckh2016.Data;

import java.util.ArrayList;

/**
 * Created by Tung on 8/3/2016.
 */
public class ObjectUserHocKy {
    ArrayList<ObjectHocKy> userData;

    public ObjectUserHocKy() {
        userData = new ArrayList<ObjectHocKy>();
    }

    public ObjectUserHocKy(ArrayList<ObjectHocKy> userData) {
        this.userData = userData;
    }

    public ArrayList<ObjectHocKy> getUserData() {
        return userData;
    }

    public void setUserData(ArrayList<ObjectHocKy> userData) {
        this.userData = userData;
    }

    public void addHocKy(ObjectHocKy value){
        userData.add(value);
    }

    public void removeHocKy(int position){
        userData.remove(position);

    }
}
