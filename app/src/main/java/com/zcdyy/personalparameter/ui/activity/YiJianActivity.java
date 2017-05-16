package com.zcdyy.personalparameter.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.YiJian;
import com.zcdyy.personalparameter.bean.ZiXun;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.adapter.YiJianAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.StringUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;
import com.zcdyy.personalparameter.views.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class YiJianActivity extends BaseActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView title,right;
    private RecyclerView recyclerView;
    private List<YiJian> yijianList = new ArrayList<>();
    private List<ZiXun> zixunList = new ArrayList<>();
    private YiJianAdapter adapter;
    private BmobUtils bmobUtils;
    private EmptyView emptyView;
    @Override
    protected void findViewByIDS() {
        emptyView = new EmptyView(this);
        title = findViewsById(R.id.top_tv_title);
        right = findViewsById(R.id.top_tv_right);
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
                if (type == 0){
                    intent.putExtra("yijian",yijianList.get(position));
                    intent.putExtra("type",0);
                }else {
                    intent.putExtra("yijian",zixunList.get(position));
                    intent.putExtra("type",1);
                }

                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (type==0){
                    bmobUtils.getYijian(loginuser.getObjectId(),1,2,handler);
                }else {
                    bmobUtils.getZiXun(1,2,handler);
                }

            }
        });
    }

    private int type = 0;
    private void initData() {
        bmobUtils = new BmobUtils(this);
        type = getIntent().getIntExtra("type",0);
        if (getIntent().getIntExtra("type",0)==1){//资讯推送
            bmobUtils.getZiXun(1,2,handler);
            adapter = new YiJianAdapter(this,1);
            title.setText("资讯推送");
        }else {
            bmobUtils.getYijian(loginuser.getObjectId(),1,2,handler);
            adapter = new YiJianAdapter(this,0);
            right.setText("添加");
            title.setText("意见反馈");
            right.setVisibility(View.VISIBLE);
            findViewsById(R.id.top_rl_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionDialog();
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 提交反馈问题
     */
    private void questionDialog() {
        final MyAlertDialog alertDialog = new MyAlertDialog(this);
        alertDialog.builder().setTitle("反馈问题")
                .setEditTextHint("请输入反馈问题")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String result = alertDialog.getResult();
                        if (StringUtils.isEmpty(result)){
                            ToastUtils.shortToast(YiJianActivity.this,"问题不能为空");
                        }else {
                            YiJian yiJian = new YiJian();
                            yiJian.setQuestion(result);
                            yiJian.setUserId(loginuser.getObjectId());
                            bmobUtils.saveYiJian(yiJian,5,2,handler);
                        }
                    }
                }).show();
    }

    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    if (type==0){
                        List<YiJian> yijian = (List<YiJian>) bundle.getSerializable("list");
                        if (yijian != null){
                            yijianList.clear();
                            yijianList.addAll(yijian);
                            if (yijianList.size()==0){
                                emptyView.setNotify("暂无意见反馈");
                            }else {
                                emptyView.setEmptyViewGone();
                            }
                            adapter.setList(yijianList);
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        List<ZiXun> ziXun = (List<ZiXun>) bundle.getSerializable("list");
                        if (ziXun != null){
                            zixunList.clear();
                            zixunList.addAll(ziXun);
                            if (zixunList.size()==0){
                                emptyView.setNotify("暂无资讯推送");
                            }else {
                                emptyView.setEmptyViewGone();
                            }
                            adapter.setZiXunList(zixunList);
                            adapter.notifyDataSetChanged();
                        }
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
                case 5:
                    bmobUtils.getYijian(loginuser.getId(),1,2,handler);
                    break;
            }
        }
    };
}
