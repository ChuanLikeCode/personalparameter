package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.listener.OnItemHealthCircleClick;
import com.zcdyy.personalparameter.ui.adapter.HealthCircleAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;

import java.util.ArrayList;
import java.util.List;

public class MyHomePageActivity extends BaseActivity {
    private final int LIST_SUCCESS = 1;
    private List<HealthCircle> list = new ArrayList<>();
    private TextView title,publish;
    private BmobUtils bmobUtils;
    private HealthCircleAdapter adapter;
    private RecyclerView recyclerView;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRequest = true;
    //    private MaterialRefreshLayout materialRefreshLayout;
    private ImageView ivZan;//点完之后要锁，防止用户多次点击
    private boolean isFirst = true;
    //最后一个可见的item的位置
    private int lastVisibleItemPosition;
    private int skip = 0;
    private List<HealthCircle> listAll = new ArrayList<>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_SUCCESS:
//                    bmobUtils.getPraiseInfo(2,handler);
                    Bundle bundle = msg.getData();
                    List<HealthCircle> healthCircles = (List<HealthCircle>) bundle.getSerializable("list");
                    if (healthCircles != null){
                        listAll.addAll(healthCircles);
                    }
                    setData();
                    break;
                case 2:
//                    Bundle bundle1 = msg.getData();
//                    List<PraiseInfo> praiseInfos = (List<PraiseInfo>) bundle1.getSerializable("list");
//                    setDianzan();
                    break;
                case 123:
                    ivZan.setClickable(true);
                    ToastUtils.shortToast(MyHomePageActivity.this,"操作成功");
                    break;
                case 234:
                    ToastUtils.shortToast(MyHomePageActivity.this,"网络错误，请稍后重试");
                    break;
                case 555:
                    list.clear();
                    listAll.clear();
                    skip = 0;
                    isRequest = true;
                    bmobUtils.queryPersonalCircle(LIST_SUCCESS,handler);
                    break;

            }
        }


    };

    /**
     * 设置分页数据
     */
    private void addData(){
//        Log.e("skip",skip+"");
//        Log.e("listAll",listAll.size()+"");
        for (int i = skip;i < skip+5 ;i++){
            if (i < listAll.size()){
                list.add(listAll.get(i));
            }else {
                isRequest = false;
            }
        }
    }

    /**
     * 添加数据
     */
    private void setData() {
        //判断如果没有数据的话，则显示空提示
        addData();
        if (list.size() == 0) {
            emptyView.setNotify("暂无动态");
        } else {
            emptyView.setEmptyViewGone();
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);

            }
        }, 1000);
        if (isFirst){
            isFirst = false;
            dialog.dismiss();
        }
    }
    @Override
    protected void findViewByIDS() {
        swipeRefreshLayout = findViewsById( R.id.commend_mrl);
        //设置加载图标的颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRed));
        emptyView = new EmptyView(this);
        title = findViewsById(R.id.top_tv_title);
        publish = findViewsById(R.id.top_tv_right);
        recyclerView = findViewsById(R.id.recyclerView);
//        findViewsById(R.id.top_rl_back).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_circle);
        initData();
        bind();
    }

    private void initData() {
        dialog = ProgressDialog.show(this,null,"正在获取数据....");
//        publish.setText("发布");
        title.setText("我的动态");
        bmobUtils = new BmobUtils(this);
//        publish.setVisibility(View.VISIBLE);
        adapter = new HealthCircleAdapter(this, list);
        adapter.w = 1;
        adapter.setHandler(handler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        bmobUtils.queryPersonalCircle(LIST_SUCCESS,handler);
    }

    private void bind() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                list.clear();
                listAll.clear();
                skip = 0;
                isRequest = true;
                bmobUtils.queryPersonalCircle(LIST_SUCCESS,handler);
            }
        });
        adapter.setOnItemClickListener(new OnItemHealthCircleClick() {
            @Override
            public void onPraiseClick(ImageView praise, TextView count,int position) {
//                ivZan = praise;
//                ivZan.setClickable(false);
//                healthCircle = list.get(position);
//                PraiseInfo praiseInfo = null;
//                if (adapter.map.get(position)){//取消赞
//                    for (PraiseInfo p:dataUtils.praiseInfoList){
//                        if (p.getUser().getId().equals(loginuser.getId())){
//                            praiseInfo = p;
//                            break;
//                        }
//                    }
//                    healthCircle.setPraiseCount(healthCircle.getPraiseCount()-1);
//                    praise.setImageResource(R.drawable.dp_dz_icon_03);
//                    bmobUtils.deletePraiseInfo(praiseInfo,456,234,handler);
//                    adapter.map.put(position,false);
//                }else {
//                    praiseInfo = new PraiseInfo();
//                    praiseInfo.setUser(BmobUser.getCurrentUser(UserInfo.class));
//                    praiseInfo.setCircleId(list.get(position).getObjectId());
//                    healthCircle.setPraiseCount(healthCircle.getPraiseCount()+1);
//                    praise.setImageResource(R.mipmap.dz);
//                    bmobUtils.savePraiseInfo(praiseInfo,456,234,handler);
//                    adapter.map.put(position,true);
//                }
//                count.setText(""+healthCircle.getPraiseCount()+"");

            }

            @Override
            public void onItemClick(int position) {
                Log.e("positon",position+"");
                Intent intent = new Intent(MyHomePageActivity.this, HealthCircleDetailActivity.class);
                intent.putExtra("circle",list.get(position));
                startActivityForResult(intent,456);
            }

            @Override
            public void onImgClick(ImageView img, int position) {
                Intent intent = new Intent(MyHomePageActivity.this, ShowImageActivity.class);
                overridePendingTransition(R.anim.anim_show, R.anim.anim_dismiss);
                intent.putExtra("path",list.get(position).getImg().getFileUrl());
                startActivity(intent);
            }

        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
//                    if (mAdapter.getCount() >= mPageSize)
//                        beginFooterRefreshing();
                    if (isRequest){
                        skip += 5;
                        setData();
//                        bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.anim_show,R.anim.anim_dismiss);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 456:
                list.clear();
                skip = 0;
                listAll.clear();
                bmobUtils.queryFriendCircle(LIST_SUCCESS, handler);
                break;
        }
    }
}
