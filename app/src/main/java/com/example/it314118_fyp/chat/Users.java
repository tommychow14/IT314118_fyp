package com.example.it314118_fyp.chat;

public class Users {
    public Users(){}
    public static String name;
    public static String image;
    public String status;

    public static String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public static String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image=image;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
