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
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseFragment;
import com.zcdyy.personalparameter.bean.Article;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.listener.OnItemHealthCircleClick;
import com.zcdyy.personalparameter.ui.activity.HealthCircleDetailActivity;
import com.zcdyy.personalparameter.ui.activity.PublishActivity;
import com.zcdyy.personalparameter.ui.adapter.HealthCircleAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DataUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;
import com.zcdyy.personalparameter.views.showimage.MyImageView;
import com.zcdyy.personalparameter.views.showimage.ShowImageActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;


/**
 * 我的朋友圈
 * Created by chuan on 2017/4/12.
 */

public class HealthCircleFragment extends BaseFragment implements View.OnClickListener {
    private final int LIST_SUCCESS = 1;
    private List<Article> list = new ArrayList<>();
    private View view;
    private TextView title,publish;
    private BmobUtils bmobUtils;
    private HealthCircleAdapter adapter;
    private RecyclerView recyclerView;
    private EmptyView emptyView;
    private DataUtils dataUtils;
    private MaterialRefreshLayout materialRefreshLayout;
    private ImageView ivZan;//点完之后要锁，防止用户多次点击
    private boolean isFirst = true;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_SUCCESS:
                    if (dataUtils.healthCircleList.size()==0){
                        handler.sendEmptyMessage(2);
                    }else {
                        bmobUtils.getUserInfo(dataUtils.healthCircleList,2,handler);
                    }
                    break;
                case 2:
                    list.clear();
                    list.addAll(dataUtils.setArticleData(loginuser.getId()));
                    setData();
                    break;
                case 123:
                    ToastUtils.shortToast(getActivity(),"操作成功");
                    break;
                case 234:
                    ToastUtils.shortToast(getActivity(),"网络错误，请稍后重试");
                    break;
                case 456:
                    ivZan.setClickable(true);
                    break;

            }
        }


    };


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
        materialRefreshLayout.finishRefreshLoadMore();
        materialRefreshLayout.finishRefresh();
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
        dataUtils = new DataUtils();
        bmobUtils = new BmobUtils(getActivity());
        bmobUtils.setDataUtils(dataUtils);
        publish.setVisibility(View.VISIBLE);
        adapter = new HealthCircleAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        bmobUtils.queryFriendCircle(LIST_SUCCESS,handler);
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
        adapter.setOnItemClickListener(new OnItemHealthCircleClick() {
            @Override
            public void onPraiseClick(ImageView praise, TextView count,int position) {
                ivZan = praise;
                ivZan.setClickable(false);
                HealthCircle healthCircle = dataUtils.healthCircleList.get(position);
                PraiseInfo praiseInfo = null;
                if (adapter.map.get(position)){//取消赞
                    for (PraiseInfo p:dataUtils.praiseInfoList){
                        if (p.getUser_id().equals(loginuser.getId())){
                            praiseInfo = p;
                            break;
                        }
                    }
                    healthCircle.setPraiseCount(healthCircle.getPraiseCount()-1);
                    praise.setImageResource(R.drawable.dp_dz_icon_03);
                    bmobUtils.deletePraiseInfo(praiseInfo,456,234,handler);
                    adapter.map.put(position,false);
                }else {
                    praiseInfo = new PraiseInfo();
                    praiseInfo.setUser_id(loginuser.getId());
                    praiseInfo.setNews_id(list.get(position).getId());
                    healthCircle.setPraiseCount(healthCircle.getPraiseCount()+1);
                    praise.setImageResource(R.mipmap.dz);
                    bmobUtils.savePraiseInfo(praiseInfo,456,234,handler);
                    adapter.map.put(position,true);
                }
                count.setText(""+healthCircle.getPraiseCount()+"");
                bmobUtils.updateHealCircle(healthCircle,123,234,handler);
            }

            @Override
            public void onItemClick(int position) {
                Log.e("positon",position+"");
                Intent intent = new Intent(getActivity(), HealthCircleDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivityForResult(intent,456);
            }

//            @Override
//            public void onImgClick(MyImageView img, int position) {
//                Intent intent = new Intent(getActivity(), ShowImageActivity.class);
//                intent.putExtra("isGif", false);
//                intent.putExtra("address", list.get(position).getImg().getFileUrl());
//                intent.putExtra("width", "700");
//                intent.putExtra("height", "900");
//                startActivity(intent);
//            }
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
