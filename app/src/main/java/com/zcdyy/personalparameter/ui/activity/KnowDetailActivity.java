package com.zcdyy.personalparameter.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.KnowLedge;

public class KnowDetailActivity extends BaseActivity {
    private TextView title;
    private TextView name,shihe,shenggao,lingjie,jigao,content;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        name = findViewsById(R.id.title);
        shihe = findViewsById(R.id.shihe);
        shenggao = findViewsById(R.id.shenggao);
        lingjie = findViewsById(R.id.lingjie);
        jigao = findViewsById(R.id.jigao);
        content = findViewsById(R.id.content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_detail);
        initData();
    }
    private void initData() {
        KnowLedge knowLedge = (KnowLedge) getIntent().getSerializableExtra("know");
        title.setText(knowLedge.getTitle());
        name.setText(knowLedge.getTitle());
        shihe.setText(knowLedge.getShihe());
        if (knowLedge.getLingjie()!=null){
            lingjie.setText(knowLedge.getLingjie());
            lingjie.setVisibility(View.VISIBLE);
        }
        if (knowLedge.getShenggao()!=null){
            shenggao.setText(knowLedge.getShenggao());
            shenggao.setVisibility(View.VISIBLE);
        }
        if (knowLedge.getJigao()!=null){
            jigao.setText(knowLedge.getJigao());
            jigao.setVisibility(View.VISIBLE);
        }
        content.setText(knowLedge.getContent());
    }
}
