package com.zcdyy.personalparameter.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.KnowLedge;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.adapter.KnowLedgeAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class KnowLedgeActivity extends BaseActivity {
    private TextView title;
    private RecyclerView recyclerView;
    private List<KnowLedge> list = new ArrayList<>();
    private KnowLedgeAdapter adapter;
    private BmobUtils bmobUtils;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        recyclerView = findViewsById(R.id.recyclerView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_ledge);
        initData();
    }

    private void initData() {
        title.setText("健康参数");
        bmobUtils = new BmobUtils(this);
        bmobUtils.getKnowLedge(1,404,handler);
        adapter = new KnowLedgeAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape_1dp);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(KnowLedgeActivity.this,KnowDetailActivity.class);
                intent.putExtra("know",list.get(position));
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    List<KnowLedge> knowLedgeList = (List<KnowLedge>) bundle.getSerializable("list");
                    if (knowLedgeList != null){
                        list.clear();
                        list.addAll(knowLedgeList);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 404:
                    ToastUtils.shortToast(KnowLedgeActivity.this,"网络错误，请稍后重试");
                    break;
            }
        }
    };
}
