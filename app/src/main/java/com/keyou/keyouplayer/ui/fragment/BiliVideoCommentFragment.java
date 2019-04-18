package com.keyou.keyouplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keyou.keyouplayer.R;

public class BiliVideoCommentFragment extends Fragment {
    private View viewCourse;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCourse = inflater.inflate(R.layout.fragment_bilivideo_comment,container,false);
        return viewCourse;
    }

}
