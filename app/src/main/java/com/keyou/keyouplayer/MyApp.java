package com.keyou.keyouplayer;


import android.app.Application;
import android.content.Context;



public class MyApp extends Application {

    public static MyApp mInstance;
    private int theAid;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public int getTheAid() {
        return theAid;
    }

    public void setTheAid(int theAid) {
        this.theAid = theAid;
    }
}




