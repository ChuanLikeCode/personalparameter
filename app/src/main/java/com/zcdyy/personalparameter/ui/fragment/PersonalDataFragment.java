package com.zcdyy.personalparameter.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.base.BaseFragment;
import com.zcdyy.personalparameter.bean.DataInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.activity.DataDetailActivity;
import com.zcdyy.personalparameter.ui.activity.LoginActivity;
import com.zcdyy.personalparameter.ui.activity.SettingActivity;
import com.zcdyy.personalparameter.ui.adapter.ParaAdapter;
import com.zcdyy.personalparameter.utils.AppManager;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.DividerItemDecoration;
import com.zcdyy.personalparameter.utils.StringUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;
import com.zcdyy.personalparameter.views.EmptyView;
import com.zcdyy.personalparameter.views.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 人体参数管理
 * Created by chuan on 2017/4/12.
 */

public class PersonalDataFragment extends BaseFragment implements View.OnClickListener {
    private final int SUCCESS = 1;
    private final int FAILED = 404;
    private final int FENZU_SUCCESS = 123;
    private final int ADD_SUCCESS = 234;
    private final int FIND_SUCCESS = 456;
    private View view;
    private TextView title,right;
    private RecyclerView recyclerView;
    private BmobUtils bmobUtils;
    private UserInfo userInfo;
    private ParaAdapter adapter;
    private EmptyView emptyView;
    private List<DataInfo> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setListData(msg);
                    setData();
                    stopRefresh();
                    break;
                case 2:
                    bmobUtils.getDataInfo(loginuser.getId(),3,handler);
                    break;
                case 3:
                    setListData(msg);
                    setData();
                    dialog.dismiss();
                    break;
            }
        }
    };

    /**
     * 设置List数据
     * @param msg
     */
    private void setListData(Message msg) {
        Bundle bundle = msg.getData();
        List<DataInfo> dataInfoList = (List<DataInfo>) bundle.getSerializable("list");
        if (dataInfoList!=null){
            list.clear();
            list.addAll(dataInfoList);
        }
    }

    private void setData() {
        if (list.size()==0){
            emptyView.setNotify("暂无数据");
        }else {
            emptyView.setEmptyViewGone();
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();

    }

    private void stopRefresh(){
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    protected View onCreateViews(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_friend,container,false);
        findIDs(view);
        initData();
        bind();
        return view;
    }

    private void bind() {
        Utils.findViewsById(view,R.id.top_rl_right).setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                bmobUtils.getDataInfo(loginuser.getId(),1,handler);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DataDetailActivity.class);
                intent.putExtra("data",list.get(position));
                startActivityForResult(intent,123);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 123:
                bmobUtils.getDataInfo(loginuser.getId(),1,handler);
                break;
        }
    }

    private void initData() {
        bmobUtils = new BmobUtils(getActivity());
        bmobUtils.getDataInfo(loginuser.getId(),1,handler);
        adapter = new ParaAdapter(getActivity(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
        decoration.setDivider(R.drawable.divider_shape);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }

    private void findIDs(View view) {
        emptyView = new EmptyView(view);
        title = Utils.findViewsById(view,R.id.top_tv_title);
        recyclerView = Utils.findViewsById(view,R.id.recyclerView);
        title.setText("参数管理");
        Utils.findViewsById(view,R.id.top_rl_back).setVisibility(View.GONE);
        right = Utils.findViewsById(view,R.id.top_tv_right);
        right.setText("添加");
        right.setVisibility(View.VISIBLE);
        swipeRefreshLayout = Utils.findViewsById(view, R.id.commend_mrl);
        //设置加载图标的颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRed));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_rl_right:
                addDialog();
                break;
        }
    }
    /**
     * 添加提示框
     */
    private void addDialog() {
        final MyAlertDialog logDialog = new MyAlertDialog(getActivity());
        logDialog.builder()
                .setTitle("添加数据")
                .setEditTextHint("请输入数据名称.....")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String str = logDialog.getResult();
                        if (StringUtils.isEmpty(str)){
                            ToastUtils.shortToast(getActivity(),"名称不能为空");
                        }else {
                            if (check(str)){
                                addDataInfo(str);
                            }else {
                                ToastUtils.shortToast(getActivity(),"名称已存在");
                            }

                        }
                    }
                })
                .show();
    }

    /**
     * 检测是否重复
     * @param str
     * @return
     */
    private boolean check(String str){
        for (DataInfo d:list){
            if (d.getName().equals(str)){
                return false;
            }
        }
        return true;
    }
    /**
     * 添加数据
     * @param str
     */
    private void addDataInfo(String str) {
        dialog = ProgressDialog.show(getActivity(),null,"正在保存数据.....");
        DataInfo dataInfo = new DataInfo();
        dataInfo.setUserId(loginuser.getId());
        dataInfo.setName(str);
        bmobUtils.saveDataInfo(dataInfo,2,handler);
    }
}
