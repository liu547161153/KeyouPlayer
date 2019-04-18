package com.keyou.keyouplayer.tool;

import android.content.Context;

import com.keyou.keyouplayer.bean.BiliJsonMegBean;

import org.jsoup.Connection;

import java.io.IOException;

import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyManagementException;

import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

import master.flame.danmaku.danmaku.util.IOUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpTool {
    private BiliJsonMegBean biliJsonMegBean;
    private OkHttpClient client;
    private Connection connection;
    private OkHttpClient.Builder builder;
    private Context context;


    public OkhttpTool(Context s) throws KeyManagementException, NoSuchAlgorithmException {
          biliJsonMegBean = new BiliJsonMegBean();
          builder=new OkHttpClient.Builder();
          client= builder.build();
          context=s;

    }


    public String getBiliRecommend() throws IOException {
        biliJsonMegBean.setUrl("http://app.bilibili.com/x/feed/index?access_key=9437975bea519f64b033a1dff2218fab&appkey=1d8b6e7d45233436&build=501000&idx=1489893927&mobi_app=android&network=wifi&platform=android&pull=true&style=2&ts=1490426321000&sign=a5bb120db95b0a03b1ef6c68edabc2ae");
        System.out.println("链接是：" + biliJsonMegBean.getUrl());
        Request request = new Request.Builder()
                .url(biliJsonMegBean.getUrl())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getBiliSearchDefaultWords() throws IOException {
        biliJsonMegBean.setUrl("http://www.bilibili.com/widget/getSearchDefaultWords");
        System.out.println("链接是：" + biliJsonMegBean.getUrl());
        Request request = new Request.Builder()
                .url(biliJsonMegBean.getUrl())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getBiliSearch(String keyword,int page,String order) throws IOException {

        biliJsonMegBean.setUrl("http://app.bilibili.com/x/v2/search?access_key=9c342000cd7c353a9b5bd27f03e0028d&appkey=1d8b6e7d45233436&b" +
                "uild=504001&duration=0&keyword="+URLEncoder.encode(keyword,"utf-8")+"&order="+order+"&mobi_app=android&" +
                "platform=android&pn="+page+"&ps=10&ts="+timeStamp()+"&sign=4640b012ccd74ab1363cb94e756c258f");
        System.out.println("链接是：" + biliJsonMegBean.getUrl());
        Request request = new Request.Builder()
                .url(biliJsonMegBean.getUrl())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    public String getBiliVideo(int aid) throws IOException {

        biliJsonMegBean.setUrl("http://api.bilibili.com/x/web-interface/view?aid="+aid);
        System.out.println("链接是：" + biliJsonMegBean.getUrl());
        Request request = new Request.Builder()
                .url(biliJsonMegBean.getUrl())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getUserInfo(int mid) throws IOException {
        biliJsonMegBean.setUrl("http://api.bilibili.com/x/web-interface/card?mid="+mid);
        Request request = new Request.Builder()
                .url(biliJsonMegBean.getUrl())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getDanMu(int cid) throws IOException {
        biliJsonMegBean.setUrl("http://api.bilibili.com/x/v1/dm/list.so?oid="+cid);
        Request request = new Request.Builder()
                .url(biliJsonMegBean.getUrl())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }





////?act=search&word=%E5%95%8A&o=default&n=20&p=1&source=bilibili





    public String timeStamp(){
        long timeStampSec = System.currentTimeMillis()/1000;
        String timestamp = String.format("%010d", timeStampSec);
        return timestamp;
    }



}

