package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.bean.ChartEntity;
import com.zcdyy.personalparameter.bean.DataInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.activity.LoginActivity;
import com.zcdyy.personalparameter.ui.activity.SettingActivity;
import com.zcdyy.personalparameter.utils.AppManager;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.LineChart;
import com.zcdyy.personalparameter.views.MyAlertDialog;

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
    private BmobUtils bmobUtils;
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ParaAdapter(Context context, List<DataInfo> list){
        this.context =context;
        this.list = list;
        bmobUtils = new BmobUtils(context);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(position);
            }
        });
    }

    /**
     * 删除提示框
     * @param position
     */
    private void deleteDialog(final int position) {
        MyAlertDialog logDialog = new MyAlertDialog(context);
        logDialog.builder()
                .setTitle("删除之后无法恢复")
                .setMsg("是否要删除此记录？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       bmobUtils.deletePersonalData(list.get(position),4,404,handler);
                    }
                })
                .show();
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
        private TextView name,delete;
        private LineChart lineChart;
        public MyViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            name = Utils.findViewsById(itemView, R.id.name);
            delete = Utils.findViewsById(itemView, R.id.delete);
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
