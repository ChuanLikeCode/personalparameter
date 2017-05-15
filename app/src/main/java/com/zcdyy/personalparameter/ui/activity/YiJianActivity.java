package com.zcdyy.personalparameter.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.YiJian;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.adapter.YiJianAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;

import java.util.ArrayList;
import java.util.List;

public class YiJianActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView title;
    private RecyclerView recyclerView;
    private List<YiJian> list = new ArrayList<>();
    private YiJianAdapter adapter;
    private BmobUtils bmobUtils;
    private EmptyView emptyView;
    @Override
    protected void findViewByIDS() {
        emptyView = new EmptyView(this);
        title = findViewsById(R.id.top_tv_title);
        recyclerView = findViewsById(R.id.recyclerView);
        swipeRefreshLayout = findViewsById( R.id.commend_mrl);
        //设置加载图标的颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRed));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_jian);
        initData();
        bind();
    }

    private void bind() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(YiJianActivity.this,YiJianDetailActivity.class);
                intent.putExtra("yijian",list.get(position));
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                bmobUtils.getYijian(loginuser.getId(),1,2,handler);
            }
        });
    }

    private int type = 0;
    private void initData() {
        bmobUtils = new BmobUtils(this);
        type = getIntent().getIntExtra("type",0);
        if (getIntent().getIntExtra("type",0)==1){

        }else {
            bmobUtils.getYijian(loginuser.getId(),1,2,handler);
            adapter = new YiJianAdapter(this,0);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }

    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    if (type==0){
                        List<YiJian> yijianList = (List<YiJian>) bundle.getSerializable("list");
                        if (yijianList != null){
                            list.clear();
                            list.addAll(yijianList);
                            if (list.size()==0){
                                emptyView.setNotify("暂无意见反馈");
                            }else {
                                emptyView.setEmptyViewGone();
                            }
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                        }
                    }else {

                    }
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }, 1000);
                    break;
                case 2:
                    ToastUtils.shortToast(YiJianActivity.this,"网络错误，请稍后重试");
                    break;
            }
        }
    };
}
