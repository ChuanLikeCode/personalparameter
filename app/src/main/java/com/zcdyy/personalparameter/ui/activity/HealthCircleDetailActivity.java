package com.zcdyy.personalparameter.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.ui.adapter.DianzanAdapter;
import com.zcdyy.personalparameter.ui.adapter.SellerStateCommentAdapter;
import com.zcdyy.personalparameter.views.CircleImageView;
import com.zcdyy.personalparameter.views.EmptyView;
import com.zcdyy.personalparameter.views.WritePopwindows;
import com.zcdyy.personalparameter.views.showimage.MyImageView;

import java.util.ArrayList;
import java.util.List;

public class HealthCircleDetailActivity extends BaseActivity {
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
    private TextView content;
    private MyImageView cotentImg;
    private TextView commentCount,dianzanCount;
    private RecyclerView recyclerView;
    private ImageView dianzan;
    private TextView comment;
    private String id;
    private RelativeLayout rl_comment;
    private LinearLayout rl_dianzan;
    private View view_dianzan,view_comment;

    private WritePopwindows writePopwindows;
    private InputMethodManager imm;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.title);
        name = findViewsById(R.id.tv_name);
        head = findViewsById(R.id.head);
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

    private void bind() {

    }

    private void initData() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        title.setFocusableInTouchMode(true);//是标题获得焦点
        title.requestFocus();
        emptyView.setNotify("暂无评论");
        view_dianzan.setVisibility(View.GONE);
        id = getIntent().getStringExtra("id");
        writePopwindows = new WritePopwindows(this,id);
        writePopwindows.setHandler(handler);
     }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
}
