package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.bean.ChartEntity;
import com.zcdyy.personalparameter.bean.DataInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chuan on 2017/5/13.
 */

public class ParaAdapter extends RecyclerView.Adapter<ParaAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<DataInfo> list;
    public ParaAdapter(Context context,List<DataInfo> list){
        this.context =context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<DataInfo> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_personal_parameter,null),
                onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        if (list.get(position).getData() != null){
            if (list.get(position).getData().size()!=0){
                holder.lineChart.setData(list.get(position).getData());
            }else {
                List<ChartEntity> entityList = new ArrayList<ChartEntity>();
                entityList.add(new ChartEntity("0",0f));
                holder.lineChart.setData(entityList);
            }
        }else {
            List<ChartEntity> entityList = new ArrayList<ChartEntity>();
            entityList.add(new ChartEntity("0",0f));
            holder.lineChart.setData(entityList);
        }

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
        private TextView name;
        private LineChart lineChart;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            name = Utils.findViewsById(itemView, R.id.name);
//            tip = Utils.findViewsById(itemView, R.id.tip);
            lineChart = Utils.findViewsById(itemView, R.id.lineChart);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
