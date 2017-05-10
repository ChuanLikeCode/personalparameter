package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.CircleImageView;


import java.util.List;

/**
 * Created by zhouchuan on 2017/3/16.
 */

public class SellerStateCommentAdapter extends RecyclerView.Adapter<SellerStateCommentAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private UserInfo userInfo;
    private List<CommentInfo> list;
    private List<UserInfo> userInfoList;
    private List<UserInfo> replyUserInfo;
    public SellerStateCommentAdapter(Context context,List<CommentInfo> list,List<UserInfo> userInfoList,List<UserInfo> replyUserInfo){
        this.context = context;
        this.list = list;
        this.userInfoList = userInfoList;
        this.replyUserInfo = replyUserInfo;
    }
    public void addList(List<CommentInfo> list,List<UserInfo> userInfoList,List<UserInfo> replyUserInfo) {
        this.list = list;
        this.userInfoList = userInfoList;
        this.replyUserInfo = replyUserInfo;
//        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_comment_sellerstate,null),onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context).load(userInfoList.get(position).getHead().getFileUrl()).into(holder.head);
        holder.name.setText(userInfoList.get(position).getName());
        holder.timeStr.setText(list.get(position).getCreatedAt());
        if (!list.get(position).is_reply()){
            holder.content.setText(list.get(position).getContent());
        }else {
            for (UserInfo u:replyUserInfo){
                if (u.getId().equals(list.get(position).getReply_id())){
                    holder.content.setText(
                            Html.fromHtml("回复 "+"<html><font color=\"#ff5001\">"+
                                    u.getName()+":</font></html>" +list.get(position).getContent()));
                    break;
                }
            }
        }

//        if (list.get(position).getReplyUserName()==null){
//            holder.content.setText(list.get(position).getContent());
//        }else {
//            holder.content.setText(
//                    Html.fromHtml("回复 "+"<html><font color=\"#ff5001\">"+
//                            list.get(position).getReplyUserName()+":</font></html>" +list.get(position).getContent()));
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener mOnItemClickListener;
        private CircleImageView head;
        private TextView name,timeStr,content;
        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            head = Utils.findViewsById(itemView,R.id.sellerStateDetail_comment_civ_head);
            name = Utils.findViewsById(itemView,R.id.sellerStateDetail_comment_tv_name);
            timeStr = Utils.findViewsById(itemView,R.id.sellerStateDetail_comment_tv_date);
            content = Utils.findViewsById(itemView,R.id.sellerStateDetail_comment_tv_content);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
