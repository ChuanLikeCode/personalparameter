package com.zcdyy.personalparameter.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseFragment;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的好友
 * Created by chuan on 2017/4/12.
 */

public class PersonalDataFragment extends BaseFragment implements View.OnClickListener {
    private final int SUCCESS = 1;
    private final int FAILED = 404;
    private final int FENZU_SUCCESS = 123;
    private final int ADD_SUCCESS = 234;
    private final int FIND_SUCCESS = 456;
    private View view;
    private TextView title;
    private RecyclerView recyclerView;
    private BmobUtils bmobUtils;
    private UserInfo userInfo;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    };

    @Override
    protected View onCreateViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_friend,container,false);
//        findIDs(view);
//        initData();
//        bind();
        return view;
    }

    private void bind() {


    }

    private void initData() {

    }

    private void findIDs(View view) {
        title = Utils.findViewsById(view,R.id.top_tv_title);
        recyclerView = Utils.findViewsById(view,R.id.recyclerView);
        title.setText("朋友");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

}
