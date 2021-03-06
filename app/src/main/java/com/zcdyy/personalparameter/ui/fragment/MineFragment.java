package com.zcdyy.personalparameter.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseFragment;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.ui.activity.KnowLedgeActivity;
import com.zcdyy.personalparameter.ui.activity.MyHomePageActivity;
import com.zcdyy.personalparameter.ui.activity.SettingActivity;
import com.zcdyy.personalparameter.ui.activity.YiJianActivity;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.CircleImageView;


/**
 * 我的
 * Created by chuan on 2017/4/12.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private RelativeLayout rl_homePage,rl_editInfo,rl_reserve,rl_yijian,rl_zixun;
    private CircleImageView head;
    private TextView name,phone,title;
    private ImageView sex;

    @Override
    protected View onCreateViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_my,container,false);
        findViewsByIds(view);
        initData(loginuser);
        bind();
        return view;
    }

    private void findViewsByIds(View view) {
        rl_homePage = Utils.findViewsById(view,R.id.rl_homePage);
        rl_yijian = Utils.findViewsById(view,R.id.rl_send);
        rl_editInfo = Utils.findViewsById(view,R.id.rl_editInfo);
        rl_reserve = Utils.findViewsById(view,R.id.rl_reserve);
        rl_zixun = Utils.findViewsById(view,R.id.rl_sao);
        head = Utils.findViewsById(view,R.id.head);
        name = Utils.findViewsById(view,R.id.name);
        phone = Utils.findViewsById(view,R.id.tv_phone);
        sex = Utils.findViewsById(view,R.id.sex);
        title = Utils.findViewsById(view,R.id.top_tv_title);
        title.setText("我的");
        Utils.findViewsById(view,R.id.top_rl_back).setVisibility(View.GONE);
    }

    private void initData(UserInfo userInfo) {
        Glide.with(getActivity()).load(userInfo.getHead().getFileUrl())
                .error(R.mipmap.default_head)
                .into(head);
        name.setText(userInfo.getName());
        if (userInfo.getSex().equals("男")){
            sex.setImageResource(R.mipmap.man);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        phone.setText(userInfo.getUsername());
    }



    private void bind() {
        rl_homePage.setOnClickListener(this);
        rl_editInfo.setOnClickListener(this);
        rl_yijian.setOnClickListener(this);
        rl_reserve.setOnClickListener(this);
        rl_zixun.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.rl_homePage://健康参数详情
                    startActivity(new Intent(getActivity(), KnowLedgeActivity.class));
                    break;
                case R.id.rl_send://意见反馈
                    intent = new Intent(getActivity(), YiJianActivity.class);
                    intent.putExtra("type",0);
                    startActivity(intent);
                    break;
                case R.id.rl_sao://健康资讯推送
                    intent = new Intent(getActivity(), YiJianActivity.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                    break;
                case R.id.rl_editInfo:
                    intent = new Intent(getActivity(), SettingActivity.class);
                    startActivityForResult(intent,123);
                    break;
                case R.id.rl_reserve:
                    intent = new Intent(getActivity(), MyHomePageActivity.class);
                    startActivityForResult(intent, 333);
                    break;
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 123:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    UserInfo info = (UserInfo) bundle.getSerializable("save");
                    initData(info);
                }
                break;
        }
    }
}
