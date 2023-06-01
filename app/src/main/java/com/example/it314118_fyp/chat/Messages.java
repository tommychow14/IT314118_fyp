package com.example.it314118_fyp.chat;

public class Messages {
    public Messages(String from, int messageType, String message, boolean seen, long time, String type){
        this.from=from;
        this.type = type;
        this.message=message;
        this.seen=seen;
        this.time=time;
    }
    public Messages(){}
    private String message;

    private String secretKey;
    private long time;
    private boolean seen;
    private String from;
    private String type;

    public String getSecretKey() {return secretKey;}
    public void setSecretKey(String secretKey) {this.secretKey = secretKey;}

    public String getMessage(){return message;}
    public void setMessage(String message){this.message=message;}

    public boolean getSeen(){return seen;}
    public void setSeen(boolean seen){this.seen=seen;}

    public long getTime(){return time;}
    public void setTime(long time){this.time=time;}


    public String getFrom(){return from;}
    public void setFrom(String from){this.from=from;}

    public String getType(){return type;}
    public void setType(String type){this.type=type;}

}
