package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.listener.OnItemHealthCircleClick;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouchuan on 2017/5/10.
 */

public class HealthCircleAdapter extends RecyclerView.Adapter<HealthCircleAdapter.MyViewHolder> {
    int w;
    private Context context;
    private List<HealthCircle> list;

    private OnItemHealthCircleClick onItemClickListener;

    public Map<Integer,Boolean> map = new HashMap<>();

    public HealthCircleAdapter(Context context, List<HealthCircle> list){
        this.context = context;
        this.list = list;
        map.clear();
        for (int i = 0;i<list.size();i++){
            map.put(i,false);
        }
    }

    public void setList(List<HealthCircle> list) {
        this.list = list;
        map.clear();
        for (int i = 0;i<list.size();i++){
            map.put(i,false);
        }
    }

    public void setOnItemClickListener(OnItemHealthCircleClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friend_circle,null),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (list.get(position).isPraise()){
//            holder.iv_zan.setImageResource(R.mipmap.dz);
//            map.put(position,true);
//        }
        Glide.with(context).load(list.get(position).getAuther().getHead().getFileUrl())
                .error(R.mipmap.default_head).into(holder.head);
        holder.name.setText(list.get(position).getAuther().getName());

        holder.content.setText(list.get(position).getContent());
        holder.timeStr.setText(list.get(position).getCreatedAt());
        holder.commentCount.setText(""+list.get(position).getCommentCount()+"");
        holder.zanCount.setText(""+list.get(position).getPraiseCount()+"");
//        Log.e("pic",list.get(position).isPic()+"");
        if (list.get(position).isPic()){
            holder.img.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImg().getFileUrl())
                    .error(R.drawable.chat_xe_icon2_03).into(holder.img);
        }else {
            holder.img.setVisibility(View.GONE);
        }
//        if (map.get(position)){
//            holder.iv_zan.setImageResource(R.mipmap.dz);
//        }else {
//            holder.iv_zan.setImageResource(R.drawable.dp_dz_icon_03);
//        }

    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0){
            return 0;
        }
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemHealthCircleClick onItemClickListener;
        private CircleImageView head;
        private TextView name,timeStr,content;
        private ImageView img;
        private ImageView iv_comment,iv_zan;
        private TextView commentCount,zanCount;
        private RelativeLayout ll_item;
        public MyViewHolder(View itemView,OnItemHealthCircleClick onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            head = Utils.findViewsById(itemView, R.id.head);
            ll_item = Utils.findViewsById(itemView, R.id.ll_item);
            name = Utils.findViewsById(itemView, R.id.name);
            timeStr = Utils.findViewsById(itemView, R.id.time);
            content = Utils.findViewsById(itemView, R.id.content);
            img = Utils.findViewsById(itemView, R.id.img);
            iv_comment = Utils.findViewsById(itemView, R.id.iv_comment);
            iv_zan = Utils.findViewsById(itemView, R.id.iv_zan);
            commentCount = Utils.findViewsById(itemView, R.id.commet_count);
            zanCount = Utils.findViewsById(itemView, R.id.dz_count);
            ll_item.setOnClickListener(this);
            content.setOnClickListener(this);
            iv_zan.setOnClickListener(this);
            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null){
                switch (view.getId()){
                    case R.id.ll_item:
                    case R.id.content:
                        onItemClickListener.onItemClick(getAdapterPosition());
                        break;
                    case R.id.iv_zan:
                        onItemClickListener.onPraiseClick(iv_zan,zanCount,getAdapterPosition());
                        break;
                    case R.id.img:
                        onItemClickListener.onImgClick(img,getAdapterPosition());
                        break;
                }
            }
        }
    }
}
