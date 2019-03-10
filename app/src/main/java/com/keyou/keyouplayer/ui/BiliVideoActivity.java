package com.keyou.keyouplayer.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.adapter.BiliVideoFragmentAdapter;
import com.keyou.keyouplayer.tool.Filestool;
import com.keyou.keyouplayer.tool.JsonTool;
import com.keyou.keyouplayer.tool.OkhttpTool;
import com.keyou.keyouplayer.view.KeyouPlayer;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;


import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BiliVideoActivity extends AppCompatActivity {
    private final static int GET=1;
    private final static int WAITURL=2;
    private final static int DANMAKU=3;
    private TabLayout tabLayout;
//
    private ViewPager viewPager;
    private WebView webview;
    private String profileJson;
    private String userJoson;
    private String commentJson;
    private String fans,videoUrl,danMuXml;
    private OkhttpTool okhttpTool;
    private KeyouPlayer videoView;
    private String avNum,picUrl;
    private int cid,playTime,status;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;
    private boolean isDestory;


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
                        picUrl=jsonTool.getPic().get(0);
                        cid=jsonTool.getCid().get(0);
                        //commentTabItem.("评 论 "+jsonTool.getReview().get(0));
                        //biliplus的https无法连接上（暂时使用自带的浏览器获取下载地址）
                        status=1;
                        initWebView(cid);
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
                    videoUrl=msg.obj.toString();
                    status=2;
                    initplayer(videoUrl,picUrl);
                    webview.setVisibility(View.GONE);
                    //initWebView(cid);

                    break;
                case DANMAKU:
                        status=1;
                        //Filestool filestool=new Filestool();
                       // filestool.write(BiliVideoActivity.this,danMuXml);

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
        webview=findViewById(R.id.web);
        videoView = findViewById(R.id.videoView);
        final Bundle bundle = getIntent().getExtras();
        avNum = String.valueOf(bundle.getInt("aid"));
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
        String url;
        if (status==1){
             url="https://www.biliplus.com/BPplayurl.php?cid="+cid+"&platform=html5&qn=80";
        }else {
             url="http://api.bilibili.com/x/v1/dm/list.so?oid="+cid;
        }
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
    if (status==1){
        videoUrl=StringUtils.substringBetween(html,"<![CDATA[","]]></url><");
        playTime= Integer.parseInt(StringUtils.substringBetween(html,"<timelength>","</timelength>"));
        Message msg=Message.obtain();
        msg.what=WAITURL;
        msg.obj=videoUrl;
        handler.sendMessage(msg);
    }else {
        danMuXml=StringUtils.substringBetween(html,"</source>","</d></i>");
        danMuXml=danMuXml+"</d>";
        danMuXml=danMuXml.replace("</d>","</d>\n");
        System.out.println(danMuXml);
        Message msg=Message.obtain();
        msg.what=DANMAKU;
        handler.sendMessage(msg);
    }



    }

    @JavascriptInterface
     public void showDescription(String str) {

    }
    }
    private void initplayer(String url,String picUrl) {
        videoView.setShrinkImageRes(R.drawable.custom_shrink);
        videoView.setEnlargeImageRes(R.drawable.custom_enlarge);
        videoView.setUp(url,true,null,"av"+avNum);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(picUrl).into(imageView);
        videoView.setThumbImageView(imageView);

        resolveNormalVideoUI();


        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, videoView);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        videoView.setIsTouchWiget(true);
        //关闭自动旋转
        videoView.setRotateViewAuto(false);
        videoView.setLockLand(false);
        videoView.setShowFullAnimation(false);
        videoView.setNeedLockFull(true);

        //detailPlayer.setOpenPreView(true);
        videoView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                videoView.startWindowFullscreen(BiliVideoActivity.this, true, true);
            }
        });

        videoView.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
                getDanmu();

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });

        videoView.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

        isDestory = true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            videoView.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private void getDanmu() {
      File file= new File(getFilesDir() + "/233.xml");
      Log.d("TAG", String.valueOf(file));
      ((KeyouPlayer) videoView.getCurrentPlayer()).setDanmaKuStream(file);

    }



    private void resolveNormalVideoUI() {
        //增加title
        videoView.getTitleTextView().setVisibility(View.GONE);
        videoView.getBackButton().setVisibility(View.GONE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (videoView.getFullWindowPlayer() != null) {
            return  videoView.getFullWindowPlayer();
        }
        return videoView;
    }


}


