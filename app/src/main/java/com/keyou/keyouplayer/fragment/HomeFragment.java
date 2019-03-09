package com.keyou.keyouplayer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.adapter.HomeFragmentRecyclerViewAdapter;
import com.keyou.keyouplayer.adapter.RecycleViewDivider;
import com.keyou.keyouplayer.tool.OkhttpTool;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HomeFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private static final int GET = 1;
    private static final int REFRESH = 2;
    private static final int LOADMORE=3;
    private String json;
    private  int page=1;
    private OkhttpTool okhttpTool;
    private View fragmentHome;
    
    private PullLoadMoreRecyclerView homeRecyclerView;
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
                        adapter = new HomeFragmentRecyclerViewAdapter(getContext(),json);
                        homeRecyclerView.setAdapter(adapter);
                        homeRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL));
                        homeRecyclerView.setPullLoadMoreCompleted();
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
//                case REFRESH:
//                    json=msg.obj.toString();
//                    try {
//                        adapter.addAllData(json);
//                        homeRecyclerView.setPullLoadMoreCompleted();
//                        break;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                case LOADMORE:
//                    json=msg.obj.toString();
//                    try {
//                        adapter.addAllData(json);
//                        homeRecyclerView.setPullLoadMoreCompleted();
//                        break;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
            }
        }
    };
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHome= inflater.inflate(R.layout.fragment_home,container,false);
        homeRecyclerView=fragmentHome.findViewById(R.id.fragment_home_rv);
        mRecyclerView = homeRecyclerView.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        homeRecyclerView.setRefreshing(false);
        homeRecyclerView.setFooterViewText("loading");
        homeRecyclerView.setLinearLayout();
        homeRecyclerView.setOnPullLoadMoreListener(this);
        getData(GET);
        return fragmentHome;
    }

    
    private void getData(final int s){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    okhttpTool=new OkhttpTool(getContext());
                    String jsondata=okhttpTool.getBiliRecommend();
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
    @Override
    public void onRefresh() {
        getData(GET);

    }

    @Override
    public void onLoadMore() {
        getData(GET);

    }
    
}
