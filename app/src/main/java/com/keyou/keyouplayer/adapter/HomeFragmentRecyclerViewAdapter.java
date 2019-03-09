package com.keyou.keyouplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.keyou.keyouplayer.MyApp;
import com.keyou.keyouplayer.R;
import com.keyou.keyouplayer.tool.JsonTool;
import com.keyou.keyouplayer.ui.BiliVideoActivity;
import com.keyou.keyouplayer.view.CustomRoundAngleImageView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;


public class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<HomeFragmentRecyclerViewAdapter.ViewHolder>{
    private final Context context;
    private JsonTool jsonTool;


    public HomeFragmentRecyclerViewAdapter(Context context,String json) throws JSONException, UnsupportedEncodingException {
        this.context = context;
        jsonTool=new JsonTool(json);
        jsonTool.bili_Recommend();

    }

    public void addAllData(String json) throws JSONException, UnsupportedEncodingException {
        jsonTool=new JsonTool(json);
        jsonTool.bili_Recommend();
        notifyDataSetChanged();

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTv,tagTv,namedateTv;
        private CustomRoundAngleImageView picIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv=itemView.findViewById(R.id.home_rv_item_title);
            tagTv=itemView.findViewById(R.id.home_rv_item_tag);
            namedateTv=itemView.findViewById(R.id.home_rv_item_namedate);
            picIv=itemView.findViewById(R.id.home_rv_item_pic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,BiliVideoActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putInt("aid",jsonTool.getAid().get(getLayoutPosition()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }



    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home_item,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder myViewHolder, int i) {

        Log.d("TAG","当前位置"+i);
        myViewHolder.titleTv.setText("   "+jsonTool.getTitle().get(i));
        myViewHolder.namedateTv.setText("Up主： "+jsonTool.getAuthor().get(i)+" 上传日期："+jsonTool.getCreate().get(i));
        Glide.with(context).load(jsonTool.getPic().get(i)).into(myViewHolder.picIv);
        myViewHolder.tagTv.setText(jsonTool.getTypename().get(i));

    }




    @Override
    public int getItemCount() {
        return jsonTool.getAid().size();
    }



}
