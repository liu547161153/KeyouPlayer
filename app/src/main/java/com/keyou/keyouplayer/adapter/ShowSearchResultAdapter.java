package com.keyou.keyouplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import org.json.JSONObject;


public class ShowSearchResultAdapter extends RecyclerView.Adapter<ShowSearchResultAdapter.ViewHolder> {
    private JsonTool jsonTool;
    private Context context;


    public ShowSearchResultAdapter(Context context,String json) throws JSONException {
        this.context=context;
        jsonTool=new JsonTool(json);
        jsonTool.bili_Search();



    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CustomRoundAngleImageView pic;
        private TextView title,data,auther;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.search_rv_item_title);
            pic=itemView.findViewById(R.id.search_rv_item_pic);
            data=itemView.findViewById(R.id.search_rv_item_data);
            auther=itemView.findViewById(R.id.search_rv_item_auther);
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


    public ShowSearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_result_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowSearchResultAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(jsonTool.getTitle().get(i));
        Glide.with(context).load(jsonTool.getPic().get(i)).into(viewHolder.pic);
        viewHolder.auther.setText("Up主："+jsonTool.getAuthor().get(i));
        viewHolder.data.setText("播放数："+jsonTool.getPlay().get(i)+" 弹幕数："+jsonTool.getDanmaku().get(i));
    }

    @Override
    public int getItemCount() {
        return jsonTool.getAid().size();
    }
}
