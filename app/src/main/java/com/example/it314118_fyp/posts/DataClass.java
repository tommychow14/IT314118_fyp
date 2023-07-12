package com.example.it314118_fyp.posts;

public class DataClass {
    private String pId,pTitle,pDetail,pImage,pTime,uid;

    public DataClass(String pId,String pTitle,String pDetail,String pImage,String pTime,String uid){
        this.pId    = pId;
        this.pTitle = pTitle;
        this.pDetail= pDetail;
        this.pImage = pImage;
        this.pTime  = pTime;
        this.uid    = uid;
    }

    public DataClass(){}

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDetail() {
        return pDetail;
    }

    public void setpDetail(String pDetail) {
        this.pDetail = pDetail;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
