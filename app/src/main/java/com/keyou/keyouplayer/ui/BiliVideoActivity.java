package com.keyou.keyouplayer.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.adapter.BiliVideoFragmentAdapter;
import com.keyou.keyouplayer.tool.JsonTool;
import com.keyou.keyouplayer.tool.OkhttpTool;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;
import java.util.List;


public class BiliVideoActivity extends AppCompatActivity {
    private final static int GET=1;
    private final static int WAITURL=2;
    private TabLayout tabLayout;
//
    private ViewPager viewPager;
    private ImageView imageView;
    private WebView webview;
    private String profileJson;
    private String userJoson;
    private String commentJson;
    private String fans,videoUrl,danMuXml;
    private OkhttpTool okhttpTool;
    private VideoPlayer videoView;
    private String avNum;
    private int playTime;

    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET:
                    try {
                        JsonTool jsonTool=new JsonTool(getProfileJson());
                        jsonTool.biliVideo();

                        Glide.with(BiliVideoActivity.this).load(jsonTool.getPic().get(0)).into(imageView);
                        //commentTabItem.("评 论 "+jsonTool.getReview().get(0));
                        initWebView(jsonTool.getCid().get(0));
                        BiliVideoFragmentAdapter adapter = new BiliVideoFragmentAdapter(getSupportFragmentManager(),jsonTool.getReview().get(0));
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                        tabLayout.setOnClickListener(v -> {
                        });
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                case WAITURL:
                    System.out.println(msg.obj.toString());;
                    String url=msg.obj.toString();
                    final HashMap<String, String> options;
                    options = new HashMap<>();
                    options.put("Origin","https://www.bilibili.com");
                    options.put("Referer","https://www.bilibili.com/video/av"+avNum);
                    options.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                            " Chrome/72.0.3626.121 Safari/537.36");
                    initplayer(url,options);
                    imageView.setVisibility(View.GONE);
                    webview.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                 //   playerView.setUp(url,"批哩批哩干杯！");
                   // playerView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bili_video);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.video_container);
        viewPager.setNestedScrollingEnabled(false);
        imageView = findViewById(R.id.bili_iv_go);
        webview=findViewById(R.id.web);
        videoView = findViewById(R.id.videoView);
        final Bundle bundle = getIntent().getExtras();
        avNum= String.valueOf(bundle.getInt("aid"));
        getvideopro(bundle.getInt("aid"));


    }


    private void getvideopro (int aid){
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        okhttpTool = new OkhttpTool(BiliVideoActivity.this);
                        setProfileJson(okhttpTool.getBiliVideo(aid));
                        JsonTool jsonTool = new JsonTool(getProfileJson());
                        jsonTool.biliVideo();
                        setUserJoson(okhttpTool.getUserInfo(jsonTool.getMid().get(0)));
           //             danMuXml=okhttpTool.getDanMu(jsonTool.getCid().get(0));
                        Message msg = Message.obtain();
                        msg.what = GET;
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start(); }




    public String getProfileJson() {
        return profileJson;
    }

    public void setProfileJson(String profileJson) {
        this.profileJson = profileJson;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getUserJoson() {
        return userJoson;
    }

    public void setUserJoson(String userJoson) {
        this.userJoson = userJoson;
    }

    public String getCommentJson() {
        return commentJson;
    }

    public void setCommentJson(String commentJson) {
        this.commentJson = commentJson;
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(int cid) {
         String url="https://www.biliplus.com/BPplayurl.php?cid="+cid+"&platform=html5&qn=80";
         webview.getSettings().setJavaScriptEnabled(true);
         webview.addJavascriptInterface(new InJavaScriptLocalObj(),"java_obj");
         webview.loadUrl(url);
         webview.setWebViewClient(new WebViewClient() {
        //跳转连接
        @Override
     public boolean shouldOverrideUrlLoading(WebView view, String url) {
         // 所有连接强制在当前WeiView加载，不跳服务器
        webview.loadUrl(url);
        return true;
    }

        //加载结束
        @Override
    public void onPageFinished(WebView view, String url) {
         view.loadUrl("javascript:window.java_obj.showSource("+ "document.getElementsByTagName('html')[0].innerHTML);");

         view.loadUrl("javascript:window.java_obj.showDescription(" + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"+");");
         super.onPageFinished(view,url);
         }});
    }

    public final class InJavaScriptLocalObj
    {
    @JavascriptInterface
    public void showSource(String html) {
    System.out.println("====>ahtml="+ html);
        videoUrl=StringUtils.substringBetween(html,"<![CDATA[","]]></url><");
        playTime= Integer.parseInt(StringUtils.substringBetween(html,"<timelength>","</timelength>"));
        Message msg=Message.obtain();
        msg.what=WAITURL;
        msg.obj=videoUrl;
        handler.sendMessage(msg);
    }

    @JavascriptInterface
     public void showDescription(String str) {

    }
    }
    private void initplayer(String url, HashMap<String, String> options) {
      videoView.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_NATIVE);
      videoView.setUp(url, options);
      VideoPlayerController controller = new VideoPlayerController(this);
      controller.setTitle("av"+avNum);
      controller.setHideTime(5000);
      Log.d("TAG",playTime+"");
      controller.setLength(playTime/1000);
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


