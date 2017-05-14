package com.zcdyy.personalparameter.ui.fragment;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseFragment;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.listener.OnItemHealthCircleClick;
import com.zcdyy.personalparameter.ui.activity.HealthCircleDetailActivity;
import com.zcdyy.personalparameter.ui.activity.PublishActivity;
import com.zcdyy.personalparameter.ui.activity.ShowImageActivity;
import com.zcdyy.personalparameter.ui.adapter.HealthCircleAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 我的朋友圈
 * Created by chuan on 2017/4/12.
 */

public class HealthCircleFragment extends BaseFragment implements View.OnClickListener {
    private final int LIST_SUCCESS = 1;
    private List<HealthCircle> list = new ArrayList<>();
    private View view;
    private TextView title,publish;
    private BmobUtils bmobUtils;
    private HealthCircleAdapter adapter;
    private RecyclerView recyclerView;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private MaterialRefreshLayout materialRefreshLayout;
    private ImageView ivZan;//点完之后要锁，防止用户多次点击
    private boolean isFirst = true;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_SUCCESS:
//                    bmobUtils.getPraiseInfo(2,handler);
                    Bundle bundle = msg.getData();
                    List<HealthCircle> healthCircles = (List<HealthCircle>) bundle.getSerializable("list");
                    if (healthCircles != null){
                        list.clear();
                        Collections.reverse(healthCircles);
                        list.addAll(healthCircles);
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
                    ToastUtils.shortToast(getActivity(),"操作成功");
                    break;
                case 234:
                    ToastUtils.shortToast(getActivity(),"网络错误，请稍后重试");
                    break;
                case 456:
                    bmobUtils.updateHealCircle(healthCircle,123,234,handler);
                    break;

            }
        }


    };

    private void setDianzan() {

    }


    /**
     * 添加数据
     */
    private void setData() {
        //判断如果没有数据的话，则显示空提示
//        Log.e("list",list.size()+"");
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
    protected View onCreateViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_friend_circle,container,false);
        findViewsByIds(view);
        initData();
        bind();
        return view;
    }

    private void initData() {
        dialog = ProgressDialog.show(getActivity(),null,"正在获取数据....");
        publish.setText("发布");
        title.setText("健康资讯");
        bmobUtils = new BmobUtils(getActivity());
        publish.setVisibility(View.VISIBLE);
        adapter = new HealthCircleAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
    }

    private HealthCircle healthCircle;
    private void bind() {
        Utils.findViewsById(view,R.id.top_rl_right).setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
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
                Intent intent = new Intent(getActivity(), HealthCircleDetailActivity.class);
                intent.putExtra("circle",list.get(position));
                startActivityForResult(intent,456);
            }

            @Override
            public void onImgClick(ImageView img, int position) {
                Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                getActivity().overridePendingTransition(R.anim.anim_show, R.anim.anim_dismiss);
                intent.putExtra("path",list.get(position).getImg().getFileUrl());
                startActivity(intent);
            }

        });
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().overridePendingTransition(R.anim.anim_show, R.anim.anim_dismiss);
    }

    private void findViewsByIds(View view) {
        swipeRefreshLayout = Utils.findViewsById(view, R.id.commend_mrl);
        //设置加载图标的颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRed));
        emptyView = new EmptyView(view);
        title = Utils.findViewsById(view,R.id.top_tv_title);
        publish = Utils.findViewsById(view,R.id.top_tv_right);
        recyclerView = Utils.findViewsById(view,R.id.recyclerView);
        Utils.findViewsById(view,R.id.top_rl_back).setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_rl_right:
                Intent intent = new Intent(getActivity(),PublishActivity.class);
                startActivityForResult(intent,456);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 456:
                bmobUtils.queryFriendCircle(LIST_SUCCESS, handler);
                break;
        }
    }
}
