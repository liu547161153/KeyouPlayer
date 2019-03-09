package com.keyou.keyouplayer.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.keyou.keyouplayer.fragment.BiliVideoCommentFragment;
import com.keyou.keyouplayer.fragment.BiliVideoProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class BiliVideoFragmentAdapter extends FragmentPagerAdapter {
    private String[] title;
    private List<Fragment> fragmentList = new ArrayList<>();

    public BiliVideoFragmentAdapter(FragmentManager fm, String i) {
        super(fm);
        this.fragmentList.add(new BiliVideoProfileFragment());
        this.fragmentList.add(new BiliVideoCommentFragment());
        System.out.println("adjosid"+i);
        title= new String[]{"简介","评论 "+i};
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
