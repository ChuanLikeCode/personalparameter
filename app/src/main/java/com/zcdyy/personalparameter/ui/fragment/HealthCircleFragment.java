package com.zcdyy.personalparameter.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseFragment;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.ui.activity.PublishActivity;
import com.zcdyy.personalparameter.ui.adapter.HealthCircleAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的朋友圈
 * Created by chuan on 2017/4/12.
 */

public class HealthCircleFragment extends BaseFragment implements View.OnClickListener {
    private final int LIST_SUCCESS = 1;
    public List<HealthCircle> list = new ArrayList<>();
    public List<PraiseInfo> praiseInfoList = new ArrayList<>();
    public List<UserInfo> userInfoList = new ArrayList<>();
    private View view;
    private TextView title,publish;
    private BmobUtils bmobUtils;
    private HealthCircleAdapter adapter;
    private RecyclerView recyclerView;
    private EmptyView emptyView;
    private boolean isRequestData = true;
    private MaterialRefreshLayout materialRefreshLayout;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_SUCCESS:
                    bmobUtils.getUserInfo(userInfoList,list,2,handler);
                    break;
                case 2:
                    setData();
                    break;

            }
        }


    };

    /**
     * 添加数据
     */
    private void setData() {
        //判断如果没有数据的话，则显示空提示
        Log.e("list",list.size()+"");
        if (list.size() == 0) {
            isRequestData = false;
            emptyView.setNotify("暂无动态");
        } else {
            isRequestData = true;
            emptyView.setEmptyViewGone();
        }
        adapter.setList(list,praiseInfoList,userInfoList);
        adapter.notifyDataSetChanged();
        materialRefreshLayout.finishRefreshLoadMore();
        materialRefreshLayout.finishRefresh();
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
        publish.setText("发布");
        title.setText("健康资讯");
        bmobUtils = new BmobUtils(getActivity());
//        dialog = ProgressDialog.show(getActivity(),null,"加载数据.....");

        publish.setVisibility(View.VISIBLE);
        adapter = new HealthCircleAdapter(getActivity(), list,userInfoList,praiseInfoList, getVmWidth());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
    }

    @Override
    public void onResume() {
        super.onResume();
//        bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
    }

    private void bind() {
        Utils.findViewsById(view,R.id.top_rl_right).setOnClickListener(this);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        materialRefreshLayout.finishRefreshLoadMore();
//                        materialRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });
    }


    private void findViewsByIds(View view) {
        emptyView = new EmptyView(view);
        title = Utils.findViewsById(view,R.id.top_tv_title);
        materialRefreshLayout = Utils.findViewsById(view,R.id.materialRefreshLayout);
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
