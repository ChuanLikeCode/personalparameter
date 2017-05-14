package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.bean.KnowLedge;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.Utils;

import java.util.List;

/**
 * Created by chuan on 2017/5/14.
 */

public class KnowLedgeAdapter extends RecyclerView.Adapter<KnowLedgeAdapter.MyViewHolder>{
    private Context context;
    private List<KnowLedge> list;
    private OnItemClickListener onItemClickListener;

    public KnowLedgeAdapter(Context context, List<KnowLedge> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<KnowLedge> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_know,null),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (list == null||list.size()==0)
            return 0;
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener onItemClickListener;
        private TextView name;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            name = Utils.findViewsById(itemView, R.id.name);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener !=null){
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
