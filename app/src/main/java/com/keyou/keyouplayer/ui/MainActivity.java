package com.keyou.keyouplayer.ui;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.fragment.HomeFragment;
import com.keyou.keyouplayer.test.TestActivity;


public class MainActivity extends AppCompatActivity {

    private Toolbar mHomeToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mHomeToolBar=findViewById(R.id.home_toolbar);
        setSupportActionBar(mHomeToolBar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frl_Home, HomeFragment.newInstance())
                    .commitNow();
        }

    }




        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=null;
        switch (item.getItemId()){
            case R.id.home_search:
                intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivities(new Intent[]{intent});
                break;
            case R.id.home_test:
                intent=new Intent(MainActivity.this,TestActivity.class);
                startActivities(new Intent[]{intent});
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
