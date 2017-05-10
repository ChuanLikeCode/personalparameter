package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.ImageLoaderUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.CircleImageView;
import com.zcdyy.personalparameter.views.showimage.MyImageView;
import com.zcdyy.personalparameter.views.showimage.ShowImageActivity;

import java.util.List;

/**
 * Created by zhouchuan on 2017/5/10.
 */

public class HealthCircleAdapter extends RecyclerView.Adapter<HealthCircleAdapter.MyViewHolder> {
    int w;
    private Context context;
    private List<HealthCircle> list;
    private OnItemClickListener onItemClickListener;
    public HealthCircleAdapter(Context context,List<HealthCircle> list,int w){
        this.context = context;
        this.list = list;
    }

    public void setList(List<HealthCircle> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friend_circle,null),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageLoaderUtils.initImage(context,list.get(position).getHead().getFileUrl(),
                holder.head,R.drawable.chat_xe_icon2_03);
        holder.name.setText(list.get(position).getName());
        holder.content.setText(list.get(position).getContent());
        holder.timeStr.setText(list.get(position).getCreatedAt());
        if (list.get(position).isPic()){
            holder.img.setAddress(null, list.get(position).getImg().getFileUrl(),
                    false, dip2px(context, w - 200) + "", dip2px(context, 250) + "");
        }else {
            holder.img.setVisibility(View.GONE);
        }


        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtra("isGif", false);
                intent.putExtra("address", list.get(position).getImg().getFileUrl());
                intent.putExtra("width", "700");
                intent.putExtra("height", "900");
                context.startActivity(intent);
            }
        });
    }
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0){
            return 0;
        }
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener onItemClickListener;
        private CircleImageView head;
        private TextView name,timeStr,content;
        private MyImageView img;
        private ImageView iv_comment,iv_zan;
        private TextView commentCount,zanCount;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            head = Utils.findViewsById(itemView, R.id.head);
            name = Utils.findViewsById(itemView, R.id.name);
            timeStr = Utils.findViewsById(itemView, R.id.time);
            content = Utils.findViewsById(itemView, R.id.content);
            img = Utils.findViewsById(itemView, R.id.img);
            iv_comment = Utils.findViewsById(itemView, R.id.iv_comment);
            iv_zan = Utils.findViewsById(itemView, R.id.iv_zan);
            commentCount = Utils.findViewsById(itemView, R.id.commet_count);
            zanCount = Utils.findViewsById(itemView, R.id.dz_count);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
