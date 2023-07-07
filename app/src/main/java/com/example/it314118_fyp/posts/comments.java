package com.example.it314118_fyp.posts;

public class comments {
    private int Like;
    private String Text;
    private String Uid;
    private long Time;

    public comments(int Like, String Text, String Uid, long Time){
        this.Like=Like;
        this.Text=Text;
        this.Uid=Uid;
        this.Time=Time;
    }

    public comments(){}

    public int getlike() {
        return Like;
    }

    public void setlike(int like) {Like = like;}

    public String gettext() {
        return Text;
    }

    public void settext(String text) {Text = text;}

    public String getuid() {
        return Uid;
    }

    public void setuid(String uid) {Uid = uid;}

    public long gettime() {
        return Time;
    }

    public void settime(long time) {Time = time;}

}
