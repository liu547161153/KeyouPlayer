package com.keyou.keyouplayer.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.adapter.HomeFragmentRecyclerViewAdapter;
import com.keyou.keyouplayer.adapter.RecycleViewDivider;
import com.keyou.keyouplayer.fragment.ShowSearchResultFragment;
import com.keyou.keyouplayer.tool.JsonTool;
import com.keyou.keyouplayer.tool.OkhttpTool;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SearchActivity extends AppCompatActivity {
    private static final  int DEFWORD=1;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private Toolbar mToolBar;
    private JsonTool jsonTool;
    private OkhttpTool okhttpTool;
    private String results;

    @SuppressLint("HandlerLeak")
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DEFWORD:
                    String s=msg.obj.toString();
                    Log.d("TAG",s);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        Log.d("TAG","ASODASODIA");
        mToolBar =findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toobar,menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconified(false);
        mSearchView.onActionViewExpanded();
        mSearchView.setSubmitButtonEnabled(true);
        SearchView.SearchAutoComplete et = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        et.setHint("请输入搜索内容，可输入av号直达");
        et.setHintTextColor(Color.WHITE);
        et.setTextColor(Color.WHITE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.startsWith("av")){
                    query=query.replace("av","");
                    Intent intent=new Intent(SearchActivity.this,BiliVideoActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putInt("aid", Integer.parseInt(query));
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else {
                    setResults(query);
                    mSearchView.clearFocus();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frl_Search, ShowSearchResultFragment.newInstance())
                            .commitNow();
                    return true;
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调
                Log.i("TAG","内容: " + newText);
                return true;
            }
        });
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    finish();

            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    private void tools(final int s) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String json;
                try {
                    okhttpTool=new OkhttpTool(SearchActivity.this);
                    json = okhttpTool.getBiliSearchDefaultWords();
                    jsonTool=new JsonTool(json);
                    json=jsonTool.bili_SearchDefaultWords();
                    Message msg=Message.obtain();
                    msg.what=s;
                    msg.obj=json;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }


    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
