package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.ImageLoaderUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.CircleImageView;
import com.zcdyy.personalparameter.views.showimage.MyImageView;
import com.zcdyy.personalparameter.views.showimage.ShowImageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;

/**
 * Created by zhouchuan on 2017/5/10.
 */

public class HealthCircleAdapter extends RecyclerView.Adapter<HealthCircleAdapter.MyViewHolder> {
    int w;
    private Context context;
    private List<HealthCircle> list;
    private OnItemClickListener onItemClickListener;
    private List<PraiseInfo> praiseInfoList;
    private List<UserInfo> userInfoList;
    private List<String> newsId = new ArrayList<>();
    private List<String> userId = new ArrayList<>();
    private Map<Integer,Boolean> map = new HashMap<>();
    private BmobUtils bmobUtils;
    private UserInfo userInfo;

    public HealthCircleAdapter(Context context, List<HealthCircle> list, List<UserInfo> userInfoList, List<PraiseInfo> praiseInfoList, int w){
        this.context = context;
        this.list = list;
        userInfo = MyApplication.getInstance().readLoginUser();
        bmobUtils = new BmobUtils(context);
        this.praiseInfoList = praiseInfoList;
        this.userInfoList = userInfoList;
        newsId.clear();
        userId.clear();
        map.clear();
        for (int i = 0;i<list.size();i++){
            map.put(i,false);
        }
        for (PraiseInfo p:praiseInfoList){
            newsId.add(p.getNews_id());
            userId.add(p.getUser_id());
        }
//        this.w = w;
    }

    public void setList(List<HealthCircle> list, List<PraiseInfo> praiseInfoList, List<UserInfo> userInfoList) {
        this.list = list;
        this.praiseInfoList = praiseInfoList;
        this.userInfoList = userInfoList;
        newsId.clear();
        userId.clear();
        map.clear();
        for (int i = 0;i<list.size();i++){
            map.put(i,false);
        }
        for (PraiseInfo p:praiseInfoList){
            newsId.add(p.getNews_id());
            userId.add(p.getUser_id());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friend_circle,null),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (newsId.contains(list.get(position).getObjectId())&&
                userId.contains(list.get(position).getId())){
            holder.iv_zan.setImageResource(R.mipmap.dz);
            map.put(position,true);
        }

        Glide.with(context).load(userInfoList.get(position).getHead().getFileUrl())
                .error(R.mipmap.default_head).into(holder.head);
        holder.name.setText(userInfoList.get(position).getName());

        holder.content.setText(list.get(position).getContent());
        holder.timeStr.setText(list.get(position).getCreatedAt());
        holder.commentCount.setText(""+list.get(position).getCommentCount()+"");
        holder.zanCount.setText(""+list.get(position).getPraiseCount()+"");
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

        holder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthCircle healthCircle = list.get(position);
                PraiseInfo praiseInfo = null;
                if (map.get(position)){//取消赞
                    for (PraiseInfo p:praiseInfoList){
                        if (p.getUser_id().equals(userInfo.getId())){
                            praiseInfo = p;
                            break;
                        }
                    }
                    healthCircle.setPraiseCount(healthCircle.getPraiseCount()-1);
                    holder.iv_zan.setImageResource(R.drawable.dp_dz_icon_03);
                    bmobUtils.deletePraiseInfo(praiseInfo,1,2,handler);
                    map.put(position,false);
                }else {
                    praiseInfo = new PraiseInfo();
                    praiseInfo.setUser_id(userInfo.getId());
                    praiseInfo.setNews_id(list.get(position).getObjectId());
                    healthCircle.setPraiseCount(healthCircle.getPraiseCount()+1);
                    holder.iv_zan.setImageResource(R.mipmap.dz);
                    bmobUtils.savePraiseInfo(praiseInfo,1,2,handler);
                    map.put(position,true);
                }
                holder.zanCount.setText(""+healthCircle.getPraiseCount()+"");
                bmobUtils.updateHealCircle(healthCircle,123,2,handler);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 123:
                    ToastUtils.shortToast(context,"操作成功");
                    break;
                case 2:
                    ToastUtils.shortToast(context,"网络错误，请稍后重试");
                    break;
            }
        }
    };


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
