package com.keyou.keyouplayer.tool;


import android.util.Log;

import com.keyou.keyouplayer.bean.VideoMegListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JsonTool extends VideoMegListBean {
    JSONObject json;
    JSONArray jsonArray;
    private List<Integer> aid,typeid,review,video_review,cid,mid;
    private List<String> typename,title,subtitle,author,create,description,pic,duration,play,ava,danmaku,credit,fans,coins,favorites,reply;

    public JsonTool(String s) throws JSONException {
        aid=new ArrayList<>();
        typename=new ArrayList<>();
        title=new ArrayList<>();
        author=new ArrayList<>();
        create=new ArrayList<>();
        duration=new ArrayList<>();
        pic=new ArrayList<>();
        mid=new ArrayList<>();
        play=new ArrayList<>();
        danmaku=new ArrayList<>();
        ava=new ArrayList<>();
        credit=new ArrayList<>();
        favorites=new ArrayList<>();
        description=new ArrayList<>();
        coins=new ArrayList<>();
        mid=new ArrayList<>();
        fans=new ArrayList<>();
        reply=new ArrayList<>();
        cid=new ArrayList<>();
        if(s.startsWith("[")){
            jsonArray=new JSONArray(s);
        }else {

            json=new JSONObject(s);
        }


    }

    public void bili_Recommend() throws JSONException {
        jsonArray=json.getJSONArray("data");
        for (int i = 0;i < jsonArray.length();i++){
            JSONObject lan = jsonArray.getJSONObject(i);
            if(lan.has("tname")){
                aid.add(lan.getInt("param"));
                typename.add(lan.getString("tname"));
                create.add(getStrTime(String.valueOf(lan.getInt("ctime")),1));
                title.add(lan.getString("title"));
                author.add(lan.getString("name"));
                pic.add(lan.getString("cover"));
            }
        }
        setAid(aid);
        setTypename(typename);
        setTitle(title);
        setAuthor(author);
        setCreate(create);
        setPic(pic);
    }

    public void bili_Search()throws JSONException{
        json=json.getJSONObject("data");
        json=json.getJSONObject("items");
        jsonArray=json.getJSONArray("archive");
        Log.d("TAG", String.valueOf(jsonArray.length()));
        for (int i=0;i < jsonArray.length();i++){
            JSONObject lan = jsonArray.getJSONObject(i);
            aid.add(Integer.valueOf(lan.getString("param")));
            title.add(lan.getString("title"));
            pic.add(lan.getString("cover"));
            play.add(intToWan(lan.getInt("play")));
            if (lan.has("danmaku")){
                danmaku.add(intToWan(lan.getInt("danmaku")));
            }else {
                danmaku.add("--");
            }
            author.add(lan.getString("author"));
        }
        setAid(aid);
        setTitle(title);
        setAuthor(author);
        setPic(pic);
        setPlay(play);
        setDanmaku(danmaku);
    }

    public void biliVideo()throws JSONException{
        JSONObject data=json.getJSONObject("data");
        title.add(data.getString("title"));
        create.add(getStrTime(String.valueOf(data.getInt("pubdate")),2));
        description.add(data.getString("desc"));
        pic.add(data.getString("pic"));
        cid.add(data.getInt("cid"));

        JSONObject owner=data.getJSONObject("owner");
        author.add(owner.getString("name"));
        ava.add(owner.getString("face"));
        mid.add(owner.getInt("mid"));

        JSONObject stat=data.getJSONObject("stat");
        play.add(intToWan(stat.getInt("view")));
        danmaku.add(intToWan(stat.getInt("danmaku")));
        favorites.add(intToWan(stat.getInt("favorite")));
        coins.add(intToWan(stat.getInt("coin")));
        credit.add(intToWan(stat.getInt("like")));
        reply.add(intToWan(stat.getInt("reply")));

        setTitle(title);
        setAuthor(author);
        setPlay(play);
        setDanmaku(danmaku);
        setFavorites(favorites);
        setDescription(description);
        setAva(ava);
        setCoins(coins);
        setCredit(credit);
        setCreate(create);
        setPic(pic);
        setMid(mid);
        setReview(reply);
        setCid(cid);
    }

    public void getUserInfo() throws JSONException {
        JSONObject data=json.getJSONObject("data");
        fans.add(intToWan(data.getInt("follower")));
        setFans(fans);

    }





    public String bili_SearchDefaultWords() throws JSONException, UnsupportedEncodingException {
        JSONObject lan=jsonArray.getJSONObject(0);
        return lan.getString("show");
    }




    // 将时间戳转为字符串
    public String getStrTime(String cc_time,int i) {
        String re_StrTime = null;
        Log.d("转换前：",cc_time);
        if(i == 1){
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
            long lcc_time = Long.valueOf(cc_time);
            re_StrTime = sdf.format(new Date(lcc_time * 1000L));
            Log.d("转换后：",re_StrTime);
            return re_StrTime;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            long lcc_time = Long.valueOf(cc_time);
            re_StrTime = sdf.format(new Date(lcc_time * 1000L));
            Log.d("转换后：",re_StrTime);
            return re_StrTime;
        }



    }

    public String intToWan(int i){
        double d=i;
        DecimalFormat df=new DecimalFormat("#.0");
        if(i<10000){
            return String.valueOf(i);
        }else {
            return df.format(d/10000)+"万";
        }


    }
}


