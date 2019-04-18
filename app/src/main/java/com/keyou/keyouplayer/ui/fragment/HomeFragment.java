package com.keyou.keyouplayer.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.adapter.HomeFragmentRecyclerViewAdapter;
import com.keyou.keyouplayer.view.RecycleViewDivider;
import com.keyou.keyouplayer.tool.OkhttpTool;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HomeFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private static final int GET = 1;
    private static final int LOADMORE=2;
    private String json;
    private OkhttpTool okhttpTool;
    private View fragmentHome;

    private PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    private RecyclerView mRecyclerView;
    private HomeFragmentRecyclerViewAdapter adapter;

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET:
                    json=msg.obj.toString();
                    try {
                        init_two();
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                case LOADMORE:
                    json=msg.obj.toString();
                    try {
                        Log.d("TAG","加载更多数据:");
                        adapter.loadMore(json);
                        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init(inflater,container);
        getData(GET);
        return fragmentHome;
    }
    //初始化
    private void init(LayoutInflater inflater, ViewGroup container) {
        fragmentHome = inflater.inflate(R.layout.fragment_home,container,false);
        pullLoadMoreRecyclerView = fragmentHome.findViewById(R.id.rv);
        mRecyclerView = pullLoadMoreRecyclerView.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        pullLoadMoreRecyclerView.setRefreshing(false);
        pullLoadMoreRecyclerView.setFooterViewText("loading");
        pullLoadMoreRecyclerView.setLinearLayout();
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
    }

    //获取首页数据
    private void init_two() throws UnsupportedEncodingException, JSONException {
        adapter = new HomeFragmentRecyclerViewAdapter(getContext(),json);
        pullLoadMoreRecyclerView.setAdapter(adapter);
        pullLoadMoreRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL));
        pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
    }


    @Override
    public void onRefresh() {
        getData(GET);
    }
    @Override
    public void onLoadMore() {
        getData(LOADMORE);
    }

    private void getData(final int s){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    okhttpTool=new OkhttpTool(getContext());
                    String jsondata=okhttpTool.getBiliRecommend();
                    while (jsondata == null){
                        jsondata=okhttpTool.getBiliRecommend();
                    }
                    Message msg=Message.obtain();
                    msg.what = s;
                    msg.obj =jsondata;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
