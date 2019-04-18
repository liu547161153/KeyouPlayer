package com.keyou.keyouplayer.test;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.tool.OkhttpTool;



import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class TestActivity extends AppCompatActivity {
    private final static int GET = 1;


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
        tools();
    }




    private void tools() {
        new Thread() {
            @Override
            public void run() {
                try {
                    OkhttpTool okhttpTool=new OkhttpTool(TestActivity.this);
                    System.out.println("sadasdas"+okhttpTool.getDanMu(80137045));
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

    }


}
