package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.Comment;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.adapter.DianzanAdapter;
import com.zcdyy.personalparameter.ui.adapter.SellerStateCommentAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DataUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.views.CircleImageView;
import com.zcdyy.personalparameter.views.EmptyView;
import com.zcdyy.personalparameter.views.FullyLinearLayoutManager;
import com.zcdyy.personalparameter.views.WritePopwindows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class HealthCircleDetailActivity extends BaseActivity implements View.OnClickListener {
    private boolean isRequestData = true;
    private boolean isParise = false;
    private boolean dianzan_isRequestData = true;
    private boolean comment_isRequestData = true;
    private boolean dianzanOrCommment = false;
    private boolean commnetOrReplay = true;
    private DianzanAdapter dianzanAdapter;
    private List<PraiseInfo> dianzanList = new ArrayList<>();//点赞列表
    private SellerStateCommentAdapter commentAdapter;
    private List<CommentInfo> commentList = new ArrayList<>();//评论列表
    private MaterialRefreshLayout materialRefreshLayout;
    private EmptyView emptyView;
    private final int READ_DETAIL_SUCCESS = 888;
    private final int DIANZAN_SUCCESS = 777;
    private final int COMMENT_SUCCESS = 666;
    private final int CONCERN_SUCCESS = 555;
    private final int COLLECT_SUCCESS = 444;
    private final int DIAN_SUCCESS = 333;
    private HealthCircle detail;

    private TextView title,name;
    private CircleImageView head;
    private TextView content,timeStr;
    private ImageView cotentImg;
    private TextView commentCount,dianzanCount;
    private RecyclerView recyclerView;
    private ImageView dianzan;
    public TextView comment;
    private String id;
    private RelativeLayout rl_comment;
    private LinearLayout rl_dianzan;
    private View view_dianzan,view_comment;

    private WritePopwindows writePopwindows;
    private InputMethodManager imm;

    private BmobUtils bmobUtils;
    private DataUtils dataUtils;
    private HealthCircle article;

    private SimpleDateFormat dateFormat;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        name = findViewsById(R.id.name);
        head = findViewsById(R.id.head);
        timeStr = findViewsById(R.id.time);
        content = findViewsById(R.id.content);
        cotentImg = findViewsById(R.id.img);
        commentCount = findViewsById(R.id.tv_commentCount);
        dianzanCount = findViewsById(R.id.tv_dianzanCount);
        recyclerView = findViewsById(R.id.recycler);
        dianzan = findViewsById(R.id.iv_dianzan);
        comment = findViewsById(R.id.xiepinglun);
        rl_comment = findViewsById(R.id.rl1);
        rl_dianzan = findViewsById(R.id.rl2);
        view_comment = findViewsById(R.id.comment_xiahuaxian);
        view_dianzan = findViewsById(R.id.dianzan_xiahuaxian);
        emptyView = new EmptyView(this);
        materialRefreshLayout =findViewsById(R.id.articleCollect_refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_circle_detail);
        initData();
        bind();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_dianzan://点赞
                dianzan.setClickable(false);
                writePopwindows.dianzan1.setClickable(false);
                dianZan();
                break;
        }
    }

    /**
     * 点赞
     */
    public void dianZan(){

        PraiseInfo praiseInfo = null;
        if (isParise){//取消赞
            for (PraiseInfo p:dianzanList){
                if (p.getUser().getObjectId().equals(loginuser.getObjectId())){
                    praiseInfo = p;
                    break;
                }
            }
            isParise = false;
            article.setPraiseCount(article.getPraiseCount()-1);
            dianzan.setImageResource(R.drawable.dp_dz_icon_03);
            writePopwindows.dianzan1.setImageResource(R.drawable.dp_dz_icon_03);
            bmobUtils.deletePraiseInfo(praiseInfo,456,234,handler);
        }else {
            isParise = true;
            praiseInfo = new PraiseInfo();
            praiseInfo.setCircleId(article.getObjectId());
            praiseInfo.setUser(BmobUser.getCurrentUser(UserInfo.class));
            article.setPraiseCount(article.getPraiseCount()+1);
            dianzan.setImageResource(R.mipmap.dz);
            writePopwindows.dianzan1.setImageResource(R.mipmap.dz);
            bmobUtils.savePraiseInfo(praiseInfo,456,234,handler);
        }
        dianzanCount.setText(""+article.getPraiseCount()+"");

    }
    private void bind() {
        dianzan.setOnClickListener(this);

        dianzanAdapter = new DianzanAdapter(this,dianzanList);
        commentAdapter = new SellerStateCommentAdapter(this,commentList);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);

        commentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (loginuser.getObjectId().equals(commentList.get(position).getUser().getObjectId())){
                    ToastUtils.shortToast(HealthCircleDetailActivity.this,"不能自己回复自己");
                    return;
                }
                writePopwindows.setReplayUser(commentList.get(position).getUser());
                writePopwindows.setCommnetOrReplay(false);//表示回复评论
                writePopwindows.initData();
                writePopwindows.showAtLocation(getCurrentFocus(), Gravity.BOTTOM,0,0);

                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePopwindows.setCommnetOrReplay(true);//表示评论
                writePopwindows.initData();
                writePopwindows.showAtLocation(getCurrentFocus(), Gravity.BOTTOM,0,0);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        rl_dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(dianzanAdapter);
                view_comment.setVisibility(View.INVISIBLE);
                view_dianzan.setVisibility(View.VISIBLE);
                dianzanOrCommment = true;
                if (dianzanList.size() == 0) {
                    emptyView.setNotify("暂无点赞");
                } else {
                    emptyView.setEmptyViewGone();
                }
            }
        });
        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(commentAdapter);
                view_dianzan.setVisibility(View.INVISIBLE);
                view_comment.setVisibility(View.VISIBLE);
                dianzanOrCommment = false;
                if (commentList.size() == 0) {
                    emptyView.setNotify("暂无评论");
                } else {
                    emptyView.setEmptyViewGone();
                }
            }
        });

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if (dianzanOrCommment){//点赞
                    getDianzan = 0;
                    bmobUtils.getPraiseInfo(article.getObjectId(),3,handler);
                }else {
                    getComment = 0;
                    bmobUtils.getCommnetInfo(article,5,handler);//获取评论
                }
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        materialRefreshLayout.finishRefreshLoadMore();
                    }
                }, 1000);
            }
        });
    }
    private void initData() {
        dialog = ProgressDialog.show(this,null,"加载数据....");
        dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        loginuser = BmobUser.getCurrentUser(UserInfo.class);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        title.setFocusableInTouchMode(true);//是标题获得焦点
        title.requestFocus();
        emptyView.setNotify("暂无评论");
        view_dianzan.setVisibility(View.GONE);
        article = (HealthCircle) getIntent().getSerializableExtra("circle");
        changData();//设置文章详情数据
        writePopwindows = new WritePopwindows(this,article);
        dataUtils = new DataUtils();
        bmobUtils = new BmobUtils(this);
        bmobUtils.setDataUtils(dataUtils);
        writePopwindows.setHandler(handler);
//        bmobUtils.getHealthCircle(id,loginuser.getId(),1,handler);
        bmobUtils.getPraiseInfo(article.getObjectId(),3,handler);//获取点赞
        bmobUtils.getCommnetInfo(article,5,handler);//获取评论
     }

    private int getComment = 0;
    private int getDianzan = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3://点赞获取成功
                    Bundle bundle = msg.getData();
                    List<PraiseInfo> praiseInfoList = (List<PraiseInfo>) bundle.getSerializable("zan");
                    if (praiseInfoList!=null){
                        dianzanList.clear();
                        Collections.reverse(praiseInfoList);
                        dianzanList.addAll(praiseInfoList);
//                        changDianData();
                    }
                    changDianData();
                    break;

                case 5://获取评论信息成功
                    Bundle bundle1 = msg.getData();
                    List<CommentInfo> commentInfoList = (List<CommentInfo>) bundle1.getSerializable("comment");

                    if (commentInfoList!=null){
                        Collections.reverse(commentInfoList);
                        commentList.clear();
                        commentList.addAll(commentInfoList);
                    }
                    changeCommentData();
                    break;

                case 7:
                    dianzan.setClickable(true);
                    writePopwindows.dianzan1.setClickable(true);
//                    addPraiseData();
                    recyclerView.setAdapter(dianzanAdapter);
                    view_comment.setVisibility(View.INVISIBLE);
                    view_dianzan.setVisibility(View.VISIBLE);
                    dianzanOrCommment = true;
                    bmobUtils.getPraiseInfo(article.getObjectId(),3,handler);//获取点赞
                    ToastUtils.shortToast(HealthCircleDetailActivity.this,"操作成功");
                    break;
                case 456://点赞成功
                    bmobUtils.updateHealCircle(article,7,234,handler);
                    break;
                case 234:
                    ToastUtils.shortToast(HealthCircleDetailActivity.this,"网络错误，请稍后重试");
                    break;
                case 888://发表评论成功
                    article.setCommentCount(article.getCommentCount()+1);
                    commentCount.setText(""+article.getCommentCount()+"");
                    bmobUtils.updateHealCircle(article,90,234,handler);
                    break;
                case 90://更新文章详情成功
                    addCommentData();//增加评论列表数据
                    ToastUtils.shortToast(HealthCircleDetailActivity.this,"评论成功");
                    break;
            }
        }

    };

    /**
     * 增加点赞数据
     */
    private void addPraiseData() {
        if (isParise){//取消赞
            isParise = false;
            dianzan.setImageResource(R.drawable.dp_dz_icon_03);
            int pos = 0;
            for (int i = 0;i<dianzanList.size();i++){
                if (dianzanList.get(i).getUser().getName().equals(loginuser.getName())){
                    pos = i;
                }
            }
            dianzanList.remove(pos);
        }else {//点赞
            isParise = true;
            PraiseInfo praise = new PraiseInfo();
            praise.setUser(BmobUser.getCurrentUser(UserInfo.class));
            praise.setCircleId(article.getObjectId());
            dianzanList.add(praise);
        }
        if (dianzanList.size() == 0) {
            emptyView.setNotify("暂无点赞");
        } else {
            emptyView.setEmptyViewGone();
        }
        recyclerView.setAdapter(dianzanAdapter);
        view_comment.setVisibility(View.INVISIBLE);
        view_dianzan.setVisibility(View.VISIBLE);
        dianzanOrCommment = true;
        dianzanAdapter.addList(dianzanList);
        dianzanAdapter.notifyDataSetChanged();
    }

    /**
     * 增加评论列表数据
     */
    private void addCommentData() {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setCircle(article);
        commentInfo.setUser(BmobUser.getCurrentUser(UserInfo.class));
        commentInfo.setContent(writePopwindows.word);
        if (!writePopwindows.isCommnetOrReplay()){
            commentInfo.setIs_reply(!writePopwindows.isCommnetOrReplay());
            commentInfo.setReplyUser(writePopwindows.getReplayUser());
        }
        commentList.add(0,commentInfo);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.addList(commentList);
        commentAdapter.notifyDataSetChanged();
        view_comment.setVisibility(View.VISIBLE);
        view_dianzan.setVisibility(View.INVISIBLE);
        dianzanOrCommment = false;
        writePopwindows.comment1.setText("");
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if (commentList.size() == 0) {
            emptyView.setNotify("暂无评论");
        } else {
            emptyView.setEmptyViewGone();
        }
        this.comment.setClickable(true);
    }

    /**
     * 评论列表
     */
    private void changeCommentData() {
        //判断如果没有数据的话，则显示空提示
        Log.e("changeCommentData",dianzanOrCommment+"");
        if (commentList.size() == 0) {
            emptyView.setNotify("暂无评论");
        } else {
            emptyView.setEmptyViewGone();
        }
        if (!dianzanOrCommment){
            recyclerView.setAdapter(commentAdapter);
            commentAdapter.addList(commentList);
            commentAdapter.notifyDataSetChanged();
            materialRefreshLayout.finishRefreshLoadMore();
            materialRefreshLayout.finishRefresh();
        }

    }
    /**
     * 点赞列表
     */
    private void changDianData() {
        //判断如果没有数据的话，则显示空提示
        if (dianzanList.size() == 0) {
            emptyView.setNotify("暂无点赞");
        } else {
            emptyView.setEmptyViewGone();
        }
        if (dianzanOrCommment){
            recyclerView.setAdapter(dianzanAdapter);
            dianzanAdapter.addList(dianzanList);
            dianzanAdapter.notifyDataSetChanged();
            materialRefreshLayout.finishRefreshLoadMore();
            materialRefreshLayout.finishRefresh();
        }

        for (PraiseInfo p:dianzanList){
            if (p.getUser().getObjectId().equals(loginuser.getObjectId())){
                isParise = true;
                writePopwindows.setParise(true);
                dianzan.setImageResource(R.mipmap.dz);
            }
        }
    }

    private void changData() {
        name.setText(article.getAuther().getName());
        Glide.with(this).load(article.getAuther().getHead().getFileUrl())
                .error(R.mipmap.default_head).into(head);
        timeStr.setText(article.getCreatedAt());
        content.setText(article.getContent());
        if (article.isPic()){
            Glide.with(this).load(article.getImg().getFileUrl())
                    .error(R.drawable.chat_xe_icon2_03).into(cotentImg);
        }else {
            cotentImg.setVisibility(View.GONE);
        }
        commentCount.setText(""+article.getCommentCount()+"");
        dianzanCount.setText(""+article.getPraiseCount()+"");
        dialog.dismiss();
    }
}
