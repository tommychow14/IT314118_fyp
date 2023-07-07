package com.example.it314118_fyp.posts;

public class DataClass {
    private String ImageUrl;
    private String About;
    private String Userid;
    private long Time;

    public DataClass(String ImageUrl, String About, String Userid,long Time){
        this.ImageUrl=ImageUrl;
        this.About=About;
        this.Userid=Userid;
        this.Time=Time;
    }

    public DataClass(){}

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public long getTime() {return Time;}

    public void setTime(long time) {Time = time;}
}
