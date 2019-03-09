package com.keyou.keyouplayer;


import android.app.Application;
import android.content.Context;

import org.yczbj.ycvideoplayerlib.utils.VideoLogUtil;


public class MyApp extends Application {

    public static MyApp mInstance;
    private int theAid;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        VideoLogUtil.setIsLog(false);
    }

    public int getTheAid() {
        return theAid;
    }

    public void setTheAid(int theAid) {
        this.theAid = theAid;
    }
}




