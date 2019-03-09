package com.keyou.keyouplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.adapter.ShowSearchResultAdapter;
import com.keyou.keyouplayer.tool.JsonTool;
import com.keyou.keyouplayer.ui.BiliVideoActivity;
import com.keyou.keyouplayer.view.CircleImageView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;

import necer.expandtextview.ExpandTextView;

public class BiliVideoProfileFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener{
    private View viewCourse;
    private static final int GET = 1;
    private static final int NEXT_PAGE = 2;
    private PullLoadMoreRecyclerView mRecyclerView;
    private ShowSearchResultAdapter adapter;
    private String json,userInfo;
    private ExpandTextView exTvtit,exTvpro;
    private CircleImageView ava;
    private TextView author,play,danmaku,creat,credit,coin,fav,fans;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCourse = inflater.inflate(R.layout.fragment_bilivideo_profile,container,false);
        exTvtit =viewCourse.findViewById(R.id.expand_title);
        exTvpro =viewCourse.findViewById(R.id.expand_profile);
        ava = viewCourse.findViewById(R.id.bilivideo_ava);
        author = viewCourse.findViewById(R.id.bilivideo_name);
        play = viewCourse.findViewById(R.id.profile_play);
        danmaku = viewCourse.findViewById(R.id.profile_danmu);
        creat = viewCourse.findViewById(R.id.profile_date);
        credit = viewCourse.findViewById(R.id.tv_good);
        coin = viewCourse.findViewById(R.id.tv_coin);
        fav = viewCourse.findViewById(R.id.tv_fav);
        fans = viewCourse.findViewById(R.id.fans);

        try {
            intdata();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viewCourse;
    }

    private void intdata() throws JSONException {
        JsonTool jsonTool=new JsonTool(json);
        jsonTool.biliVideo();
        exTvtit.setText(jsonTool.getTitle().get(0));
        exTvpro.setText(jsonTool.getDescription().get(0));
        Glide.with(getContext()).load(jsonTool.getAva().get(0)).into(ava);
        author.setText(jsonTool.getAuthor().get(0));
        play.setText("播放数:"+jsonTool.getPlay().get(0));
        danmaku.setText("弹幕数:"+jsonTool.getDanmaku().get(0));
        creat.setText(jsonTool.getCreate().get(0));
        credit.setText(jsonTool.getCredit().get(0));
        coin.setText(jsonTool.getCoins().get(0)+"");
        fav.setText(jsonTool.getFavorites().get(0)+"");
        jsonTool=new JsonTool(userInfo);
        jsonTool.getUserInfo();
        fans.setText(jsonTool.getFans().get(0)+"粉丝");




    }



    public void onAttach(Context context) {
        super.onAttach(context);
        json=((BiliVideoActivity) context).getProfileJson();
        userInfo=((BiliVideoActivity) context).getUserJoson();
    }







    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
