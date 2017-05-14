package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.ChartEntity;
import com.zcdyy.personalparameter.bean.DataInfo;
import com.zcdyy.personalparameter.listener.OnItemClickListener;
import com.zcdyy.personalparameter.ui.adapter.LabelAdapter;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.StringUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.views.LineChart;
import com.zcdyy.personalparameter.views.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class DataDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title,right;
    private RecyclerView recyclerView;
    private LabelAdapter adapter;
    private DataInfo dataInfo;
    private BmobUtils bmobUtils;
    private LineChart lineChart;
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        right = findViewsById(R.id.top_tv_right);
        lineChart = findViewsById(R.id.lineChart);
        recyclerView = findViewsById(R.id.recyclerView);
        right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        initData();
        bind();
    }

    @Override
    public void backClick(View view) {
        back();
    }

    /**
     * 退出
     */
    private void back(){
        final MyAlertDialog dialogChoose = new MyAlertDialog(this);
        dialogChoose.builder()
                .setTitle("退出提示")
                .setMsg("需要保存数据么？")
                .setNegativeButton("仅退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setPositiveButton("保存并退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = ProgressDialog.show(DataDetailActivity.this,null,"正在保存数据......");
                        bmobUtils.updateDataInfo(dataInfo,1,handler);
                    }
                })
                .show();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    dialog.dismiss();
                    finish();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            back();
        }
        return false;
    }

    private void bind() {
        findViewsById(R.id.top_rl_right).setOnClickListener(this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                chooseDialog(position);
            }
        });
    }

    /**
     * 选择对话框
     * @param position
     */
    private void chooseDialog(final int position){
        final MyAlertDialog dialogChoose = new MyAlertDialog(this);
        dialogChoose.builder()
                .setTitle("提示")
                .setMsg("删除数据还是修改数据？")
                .setNegativeButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ChartEntity> list = dataInfo.getData();
                        for (int i = position+1;i<list.size();i++){
                            list.get(i).setxLabel((Integer.valueOf(list.get(i).getxLabel())-1)+"");
                        }
                        list.remove(position);
                        dataInfo.setData(list);
                        if (list.size()==0){
                            setEmptyLinerChart();
                        }else {
                            lineChart.setData(list);
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setPositiveButton("修改", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xiugaiDataInfo(position);
                    }
                })
                .show();

    }

    /**
     * 修改数据提示框
     * @param position
     */
    private void xiugaiDataInfo(final int position) {
        final MyAlertDialog logDialog = new MyAlertDialog(this);
        logDialog.builder();
        logDialog.edittxt_result.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        logDialog.setTitle("修改数据")
                .setEditText(""+dataInfo.getData().get(position).getyValue()+"")
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
                            ToastUtils.shortToast(DataDetailActivity.this,"数据不能为空");
                        }else {
                            xiugaiData(position,str);
                        }
                    }
                })
                .show();
    }

    /**
     * 修改数据
     * @param position
     * @param str
     */
    private void xiugaiData(int position, String str) {
        List<ChartEntity> list= dataInfo.getData();
        list.get(position).setyValue(Float.valueOf(str));
        dataInfo.setData(list);
        lineChart.setData(list);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        right.setText("添加");
        bmobUtils = new BmobUtils(this);
        dataInfo = (DataInfo) getIntent().getSerializableExtra("data");
        if (dataInfo.getData()!=null){
            if (dataInfo.getData().size()!=0){
                lineChart.setData(dataInfo.getData());
            }else {
                setEmptyLinerChart();
            }
        }else {
            setEmptyLinerChart();
        }
        title.setText(dataInfo.getName());
        adapter = new LabelAdapter(this,dataInfo.getData());
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(adapter);
    }

    private void setEmptyLinerChart(){
        List<ChartEntity> entityList = new ArrayList<ChartEntity>();
        entityList.add(new ChartEntity("0",0f));
        lineChart.setData(entityList);
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
        final MyAlertDialog logDialog = new MyAlertDialog(this);
        logDialog.builder();
        logDialog.edittxt_result.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        logDialog.setTitle("添加数据")
                .setEditTextHint("请输入数据.....")
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
                            ToastUtils.shortToast(DataDetailActivity.this,"数据不能为空");
                        }else {
                            addDataInfo(str);
                        }
                    }
                })
                .show();
    }

    /**
     * 增加数据
     * @param str
     */
    private void addDataInfo(String str) {
        List<ChartEntity> list;
        if (dataInfo.getData()!=null){
            list = dataInfo.getData();
        }else {
            list = new ArrayList<>();
        }
        ChartEntity chartEntity = new ChartEntity((list.size()+1)+"",Float.valueOf(str));
        list.add(chartEntity);
        dataInfo.setData(list);
        lineChart.setData(list);
        adapter.setList(list);
        adapter.notifyDataSetChanged();

    }
}
