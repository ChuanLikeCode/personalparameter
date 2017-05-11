package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.zcdyy.personalparameter.bean.Article;
import com.zcdyy.personalparameter.bean.Comment;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.Praise;
import com.zcdyy.personalparameter.bean.PraiseInfo;
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
import com.zcdyy.personalparameter.views.showimage.MyImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthCircleDetailActivity extends BaseActivity implements View.OnClickListener {
    private boolean isRequestData = true;
    private boolean isParise = false;
    private boolean dianzan_isRequestData = true;
    private boolean comment_isRequestData = true;
    private boolean dianzanOrCommment = false;
    private boolean commnetOrReplay = true;
    private DianzanAdapter dianzanAdapter;
    private List<Praise> dianzanList = new ArrayList<>();//点赞列表
    private SellerStateCommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();//评论列表
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
    private Article article;

    private Receiver receiver;
    private CommentReceiver commentReceiver;
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
        HealthCircle healthCircle = dataUtils.healthCircleList.get(0);
        PraiseInfo praiseInfo = null;
        if (isParise){//取消赞
            for (PraiseInfo p:dataUtils.praiseInfoList){
                if (p.getUser_id().equals(loginuser.getId())){
                    praiseInfo = p;
                    break;
                }
            }
            healthCircle.setPraiseCount(healthCircle.getPraiseCount()-1);
            dianzan.setImageResource(R.drawable.dp_dz_icon_03);
            writePopwindows.dianzan1.setImageResource(R.drawable.dp_dz_icon_03);
            bmobUtils.deletePraiseInfo(praiseInfo,456,234,handler);
        }else {
            praiseInfo = new PraiseInfo();
            praiseInfo.setUser_id(loginuser.getId());
            praiseInfo.setNews_id(article.getId());
            healthCircle.setPraiseCount(healthCircle.getPraiseCount()+1);
            dianzan.setImageResource(R.mipmap.dz);
            writePopwindows.dianzan1.setImageResource(R.mipmap.dz);
            bmobUtils.savePraiseInfo(praiseInfo,456,234,handler);
        }
        dianzanCount.setText(""+healthCircle.getPraiseCount()+"");
        bmobUtils.updateHealCircle(healthCircle,456,234,handler);
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
                writePopwindows.setReplyName(commentList.get(position).getReplyName());
                writePopwindows.setReplayID(commentList.get(position).getUserId());
                writePopwindows.setCommnetOrReplay(false);//表示回复评论
                writePopwindows.initData();
                writePopwindows.showAtLocation(getCurrentFocus(), Gravity.BOTTOM,0,0);

                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePopwindows.initData();
                writePopwindows.setCommnetOrReplay(true);//表示评论
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
                    bmobUtils.getPraiseInfo(id,3,handler);
                }else {
                    bmobUtils.getCommnetInfo(id,5,handler);//获取评论
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(commentReceiver);
    }

    private void initData() {
        dialog = ProgressDialog.show(this,null,"加载数据....");
        receiver = new Receiver();
        dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        commentReceiver  = new CommentReceiver();
        IntentFilter filter = new IntentFilter("zan");
        IntentFilter filter1 = new IntentFilter("comment");
        registerReceiver(receiver,filter);
        registerReceiver(commentReceiver,filter1);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        title.setFocusableInTouchMode(true);//是标题获得焦点
        title.requestFocus();
        emptyView.setNotify("暂无评论");
        view_dianzan.setVisibility(View.GONE);
        id = getIntent().getStringExtra("id");
        writePopwindows = new WritePopwindows(this,id);
        dataUtils = new DataUtils();
        bmobUtils = new BmobUtils(this);
        bmobUtils.setDataUtils(dataUtils);
        writePopwindows.setHandler(handler);
        bmobUtils.getHealthCircle(id,loginuser.getId(),1,handler);
        bmobUtils.getPraiseInfo(id,3,handler);//获取点赞
        bmobUtils.getCommnetInfo(id,5,handler);//获取评论
     }

    private int getComment = 0;
    private int getDianzan = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1://获取文章详情
                    bmobUtils.getUserInfo(dataUtils.healthCircleList,2,handler);//获取发布的用户信息
                    break;
                case 2://获取发布的用户信息和文章详情成功
                    article = dataUtils.getArticle();
                    changData();//设置文章详情数据
                    break;
                case 3://点赞获取成功
                    bmobUtils.getPraiseUserInfo(dataUtils.praiseInfoList,4,handler);//点赞用户信息获取
                    break;
                case 4://获取点赞和用户信息成功
                    dianzanList.addAll(dataUtils.getPraiseList());
                    changDianData();//设置点赞数据
                    break;
                case 5://获取评论信息成功
                    if (dataUtils.commentInfoList.size()!=0){//没有人评论就直接设置数据
                        bmobUtils.getCommentUserInfo(dataUtils.commentInfoList,6,handler);//获取评论的用户信息
                        bmobUtils.getCommentReplyUserInfo(dataUtils.commentInfoList,6,handler);//获取回复的用户信息
                    }else {
                        handler.sendEmptyMessage(6);
                    }
                    break;
                case 6://获取评论信息成功和用户信息成功
                    Intent intent = new Intent("comment");
                    sendBroadcast(intent);
                    break;
                case 7://获取的评论设置数据
                    commentList.addAll(dataUtils.getCommetList());
                    changeCommentData();
                    break;
                case 456://点赞成功
                    Intent intent1 = new Intent("zan");
                    sendBroadcast(intent1);
                    break;
                case 999://点赞设置数据
                    dianzan.setClickable(true);
                    writePopwindows.dianzan1.setClickable(true);
                    addPraiseData();
                    ToastUtils.shortToast(HealthCircleDetailActivity.this,"操作成功");
                    break;
                case 234:
                    ToastUtils.shortToast(HealthCircleDetailActivity.this,"网络错误，请稍后重试");
                    break;
                case 888://发表评论成功
                    HealthCircle healthCircle = dataUtils.healthCircleList.get(0);
                    healthCircle.setCommentCount(healthCircle.getCommentCount()+1);
                    commentCount.setText(""+healthCircle.getCommentCount()+"");
                    bmobUtils.updateHealCircle(healthCircle,90,234,handler);
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
                if (dianzanList.get(i).getName().equals(loginuser.getName())){
                    pos = i;
                }
            }
            dianzanList.remove(pos);
        }else {//点赞
            isParise = true;
            Praise praise = new Praise();
            praise.setTimeStr(dateFormat.format(new Date()));
            praise.setHead(loginuser.getHead());
            praise.setName(loginuser.getName());
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
        Comment comment = new Comment();
        comment.setTimeStr(dateFormat.format(new Date()));
        comment.setUserId(loginuser.getId());
        comment.setReply(!writePopwindows.isCommnetOrReplay());//false 为评论 true为回复
        if (!writePopwindows.isCommnetOrReplay()){//true为评论 false为回复
            comment.setReplyId(writePopwindows.getReplayID());
            comment.setReplyName(writePopwindows.getReplyName());
        }
        comment.setName(loginuser.getName());
        comment.setHead(loginuser.getHead());
        comment.setContent(writePopwindows.word);
        commentList.add(0,comment);
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

    }

    private void changData() {
        name.setText(article.getName());
        Glide.with(this).load(article.getHead().getFileUrl())
                .error(R.mipmap.default_head).into(head);
        timeStr.setText(article.getTimeStr());
        content.setText(article.getContent());
        if (article.isPic()){
            Glide.with(this).load(article.getImg().getFileUrl())
                    .error(R.drawable.chat_xe_icon2_03).into(cotentImg);
        }else {
            cotentImg.setVisibility(View.GONE);
        }
        commentCount.setText(""+article.getCommentCount()+"");
        dianzanCount.setText(""+article.getPraiseCount()+"");
        if (article.isPraise()){
            isParise = true;
            writePopwindows.setParise(true);
            dianzan.setImageResource(R.mipmap.dz);
        }
        dialog.dismiss();
    }

    /**
     * 点赞的receiver
     */
    public class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("zan")){
                getDianzan++;
                if (getDianzan==2){
                    handler.sendEmptyMessage(999);//点赞完成之后，信息全部更新
                }
            }
        }
    }

    /**
     * 评论的receiver
     */
    public class CommentReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("comment")){
                getComment++;
                if (getComment==2){
                    handler.sendEmptyMessage(7);//全部评论信息获取完成
                }
            }
        }
    }

}
