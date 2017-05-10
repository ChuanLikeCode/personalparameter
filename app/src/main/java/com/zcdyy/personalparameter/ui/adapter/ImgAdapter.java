package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.ImageLoaderUtils;
import com.zcdyy.personalparameter.utils.Utils;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;


/**
 * Created by chuan on 2017/4/13.
 */

public class ImgAdapter extends  RecyclerView.Adapter<ImgAdapter.MyViewHolder>{
    private Context context;
    private List<BmobFile> list;
    private OnItemClickListener onItemClickListener;
    public ImgAdapter(Context context,List<BmobFile> list){
        this.context = context;
        this.list = list;
    }

    public void setList(List<BmobFile> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_img,null),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoaderUtils.initImage(context,list.get(position).getFileUrl(),
                holder.img, R.drawable.chat_xe_icon2_03);
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
        private ImageView img;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            img = Utils.findViewsById(itemView,R.id.img);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener !=null){
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
