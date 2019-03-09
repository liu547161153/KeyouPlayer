package com.keyou.keyouplayer.test;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.tool.OkhttpTool;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class TestActivity extends AppCompatActivity {
    private final static int GET = 1;
    private VideoPlayer videoView;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET:
                    Log.d("TAG","asdasd");

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        videoView =findViewById(R.id.videoView);
        initplayer("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");

    }




    private void tools() {
        new Thread() {
            @Override
            public void run() {
                try {
                    OkhttpTool okhttpTool=new OkhttpTool(TestActivity.this);
                    System.out.println(okhttpTool.test());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initplayer(String url) {
        videoView.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_NATIVE);
        videoView.setUp(url, null);
        VideoPlayerController controller = new VideoPlayerController(this);
        controller.setHideTime(5000);
        videoView.setController(controller);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().suspendVideoPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()){
            return;
        }else {
            VideoPlayerManager.instance().releaseVideoPlayer();
        }
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        VideoPlayerManager.instance().resumeVideoPlayer();
    }
}
