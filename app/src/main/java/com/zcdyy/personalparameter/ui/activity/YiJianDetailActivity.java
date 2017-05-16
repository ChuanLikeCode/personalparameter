package com.zcdyy.personalparameter.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.YiJian;
import com.zcdyy.personalparameter.bean.ZiXun;

public class YiJianDetailActivity extends BaseActivity {
    private TextView title,answer,question;
    private YiJian yiJian;
    private ZiXun ziXun;
    private ImageView img;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        answer = findViewsById(R.id.answer);
        question = findViewsById(R.id.question);
        img = findViewsById(R.id.img);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_jian_detail);
        initData();
    }

    private void initData() {
        title.setText("详情");
        if (getIntent().getIntExtra("type",0)==1){
            ziXun = (ZiXun) getIntent().getSerializableExtra("yijian");
            question.setText(ziXun.getTitle());
            answer.setText(ziXun.getContent());
            if (ziXun.getImg()!=null){
                Glide.with(this).load(ziXun.getImg().getFileUrl()).error(R.drawable.chat_xe_icon2_03).into(img);
            }
        }else {
            yiJian = (YiJian) getIntent().getSerializableExtra("yijian");
            question.setText(yiJian.getQuestion());
            if (yiJian.getAnswer()!=null){
                answer.setText(yiJian.getAnswer());
            }
        }
    }
}
