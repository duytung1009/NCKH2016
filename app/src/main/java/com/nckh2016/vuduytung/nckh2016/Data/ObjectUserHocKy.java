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

    public ObjectHocKy getHocKy(int position){
        return userData.get(position);
    }

    public boolean removeHocKy(ObjectHocKy value){
        boolean result = false;
        for(ObjectHocKy obj : userData){
            if(obj.getHocKy() == value.getHocKy() && obj.getNamHoc() == value.getNamHoc() && obj.getNganh().equals(value.getNganh())){
                result = userData.remove(obj);
                break;
            }
        }
        return result;
    }
}
