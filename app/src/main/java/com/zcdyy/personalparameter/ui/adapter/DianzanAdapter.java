package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.CircleImageView;

import java.util.List;

/**
 * Created by zhouchuan on 2017/4/26.
 */

public class DianzanAdapter extends RecyclerView.Adapter<DianzanAdapter.MyViewHolder>{
    private List<PraiseInfo> list;
    private Context context;
    private UserInfo userInfo;
    private List<UserInfo> userInfoList;
    public DianzanAdapter(Context context){
        this.context = context;
    }

    public DianzanAdapter(Context context, List<UserInfo> userInfoList,List<PraiseInfo> list){
        this.context = context;
        this.list = list;
        this.userInfoList = userInfoList;
        userInfo = MyApplication.getInstance().readLoginUser();
    }

    public void addList(List<UserInfo> userInfoList,List<PraiseInfo> list) {
        this.list = list;
        this.userInfoList = userInfoList;
//        notifyDataSetChanged();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_thumbs_up,null),onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context).load(userInfoList.get(position).getHead().getFileUrl()).into(holder.head);
        holder.name.setText(userInfoList.get(position).getName());
        holder.timeStr.setText(list.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClickListener mOnItemClickListener;
        private CircleImageView head;
        private TextView name,timeStr;
        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            head = Utils.findViewsById(itemView,R.id.thumbsUp_civ_head);
            name = Utils.findViewsById(itemView,R.id.thumbsUp_tv_name);
            timeStr = Utils.findViewsById(itemView,R.id.thumbsUp_tv_detail);
            Utils.findViewsById(itemView,R.id.thumbsUp_tv_date).setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
