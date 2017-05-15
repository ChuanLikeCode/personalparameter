package com.zcdyy.personalparameter.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.YiJian;

public class YiJianDetailActivity extends BaseActivity {
    private TextView title,answer,question;
    private YiJian yiJian;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        answer = findViewsById(R.id.answer);
        question = findViewsById(R.id.question);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_jian_detail);
        initData();
    }

    private void initData() {
        yiJian = (YiJian) getIntent().getSerializableExtra("yijian");
        question.setText(yiJian.getQuestion());
        if (yiJian.getAnswer()!=null){
            answer.setText(yiJian.getAnswer());
        }
    }
}
