package com.keyou.keyouplayer.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.keyou.keyouplayer.adapter.RecycleViewDivider;
import com.keyou.keyouplayer.adapter.ShowSearchResultAdapter;
import com.keyou.keyouplayer.tool.OkhttpTool;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.ui.SearchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ShowSearchResultFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener{
    private static final int REFRESH = 1;
    private static final int NEXT_PAGE = 2;
    private View showSearch;
    private String results;
    private Spinner spin1;
    private PullLoadMoreRecyclerView mRecyclerView;
    private RecyclerView recyclerView;
    private ShowSearchResultAdapter adapter;
    private OkhttpTool okhttpTool;
    private int page=1;
    private String order="default";


    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json;
            switch (msg.what){
                case REFRESH:
                    json=msg.obj.toString();
                    try {
                        JSONObject jsonObject=new JSONObject(json);
                        jsonObject=jsonObject.getJSONObject("data");
                        jsonObject=jsonObject.getJSONObject("items");
                        if(jsonObject.has("archive")){
                            adapter = new ShowSearchResultAdapter(getContext(),json);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL));
                            mRecyclerView.setPullLoadMoreCompleted();
                            break;
                        }else {
                            Toast.makeText(getContext(),"到底了！",Toast.LENGTH_LONG).show();
                            page=page-1;
                            mRecyclerView.setPullLoadMoreCompleted();
                            break;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    public ShowSearchResultFragment() {

    }

    public static Fragment newInstance() {
        return new ShowSearchResultFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showSearch=inflater.inflate(R.layout.fragment_show_search_result,container,false);

        spin1=showSearch.findViewById(R.id.result_spinner);
        spin1.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,new String[]{"默认排序","播放多","新发布","弹幕多"}));
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        page=1;
                        order="default";
                        getSearch(order);
                    break;
                    case 1:
                        page=1;
                        order="click";
                        getSearch(order);
                        break;
                    case 2:
                        page=1;
                        order="damku";
                        getSearch(order);
                        break;
                    case 3:
                        page=1;
                        order="pubdate";
                        getSearch(order);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecyclerView = showSearch.findViewById(R.id.fragment_search_rv);
        recyclerView = mRecyclerView.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setFooterViewText("loading");
        mRecyclerView.setLinearLayout();
        mRecyclerView.setOnPullLoadMoreListener(this);
        getSearch(order);
        return showSearch;
    }

    private void getSearch(String order){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    okhttpTool=new OkhttpTool(getContext());
                    String jsondata;
                    jsondata=okhttpTool.getBiliSearch(results,page,order);
                    Message msg=Message.obtain();
                    msg.what = REFRESH;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        results=((SearchActivity) context).getResults();
    }

    @Override
    public void onRefresh() {
        page=page-1;
        if(page < 1){
            page=1;
            Toast.makeText(getContext(),"到顶了！",Toast.LENGTH_LONG).show();
            mRecyclerView.setPullLoadMoreCompleted();
        }else {
            getSearch(order);
        }

    }

    @Override
    public void onLoadMore() {
        page++;
        getSearch(order);
    }
}
