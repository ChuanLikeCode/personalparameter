package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.bean.YiJian;
import com.zcdyy.personalparameter.bean.ZiXun;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by chuan on 2017/5/15.
 */

public class YiJianAdapter extends RecyclerView.Adapter<YiJianAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<YiJian> yiJianList = new ArrayList<>();
    private List<ZiXun> ziXunList = new ArrayList<>();
    private int type;//0为意见反馈 1为资讯推送
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setZiXunList(List<ZiXun> ziXunList) {
        this.ziXunList = ziXunList;
    }

    public void setList(List<YiJian> list) {
        this.yiJianList = list;
    }

    public YiJianAdapter(Context context,int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_yijian,null),
                onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (type ==0){//意见反馈
            holder.question.setText(yiJianList.get(position).getQuestion());
            if (yiJianList.get(position).getAnswer()!=null){
                holder.answer.setText(yiJianList.get(position).getAnswer());
            }else {
                holder.answer.setText("请耐心等待客服回复哟~~~");
            }
        }else {
            holder.question.setText(ziXunList.get(position).getTitle());
            holder.answer.setText(ziXunList.get(position).getContent());
            if (ziXunList.get(position).getImg()!=null){
                holder.img.setVisibility(View.VISIBLE);
                Glide.with(context).load(ziXunList.get(position).getImg().getFileUrl())
                        .error(R.drawable.chat_xe_icon2_03).into(holder.img);
            }else {
                holder.img.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (type==0){
            if (yiJianList == null ||yiJianList.size()==0)
                return 0;
            return  yiJianList.size();
        }else {
            if (ziXunList == null ||ziXunList.size()==0)
                return 0;
            return  ziXunList.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener onItemClickListener;
        private TextView question,answer;
        private ImageView img;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            question = Utils.findViewsById(itemView,R.id.question);
            answer = Utils.findViewsById(itemView,R.id.answer);
            img = Utils.findViewsById(itemView,R.id.img);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
