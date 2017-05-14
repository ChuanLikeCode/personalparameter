package com.zcdyy.personalparameter.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.ui.adapter.PagerAdapter;
import com.zcdyy.personalparameter.ui.fragment.HealthCircleFragment;
import com.zcdyy.personalparameter.ui.fragment.PersonalDataFragment;
import com.zcdyy.personalparameter.ui.fragment.MineFragment;
import com.zcdyy.personalparameter.views.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouchuan on 2017/2/27.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private MyViewPager viewPager;
    private PagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    public HealthCircleFragment healthCircleFragment;
    public PersonalDataFragment personalDataFragment;
    public MineFragment mineFragment;
    private ImageView[] iv_bottom = new ImageView[3];
//    private TextView[] tv_bottom = new TextView[3];
    private LinearLayout[] bottom = new LinearLayout[3];
    //底部的三个控件
//    private int[] tv_tab_ids = {R.id.actionBar_tv_bottom1,R.id.actionBar_tv_bottom2,R.id.actionBar_tv_bottom3};
    private int[] iv_tab_ids = {R.id.actionBar_iv_bottom1,R.id.actionBar_iv_bottom2,R.id.actionBar_iv_bottom3};
    private int[] tab_ids = {R.id.bottom_tab_1,R.id.bottom_tab_2,R.id.bottom_tab_3};
    //未选中
    private int[]tab_unSelect ={R.drawable.footer_care_icon1,R.drawable.footer_news_icon1,R.drawable.footer_my_icon1};
    //选中
    private int[]tab_Select ={R.drawable.footer_care_icon2,R.drawable.footer_news_icon2,R.drawable.footer_my_icon2};
    //初始化三个控件的状态
    private boolean[] selected={true,false,false};

    protected void findViewByIDS() {
        viewPager = (MyViewPager) findViewById(R.id.main_viewPager);
        for(int i=0;i<3;i++) {
            bottom[i] = (LinearLayout) findViewById(tab_ids[i]);
            iv_bottom[i] = (ImageView) findViewById(iv_tab_ids[i]);
//            tv_bottom[i] = (TextView) findViewById(tv_tab_ids[i]);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIDS();
        addFragment();
        bind();
    }

    private void addFragment() {
        healthCircleFragment = new HealthCircleFragment();
        personalDataFragment = new PersonalDataFragment();
        mineFragment = new MineFragment();
        fragmentList.add(healthCircleFragment);
        fragmentList.add(personalDataFragment);
        fragmentList.add(mineFragment);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.anim_show, R.anim.anim_dismiss);
    }

    private void bind() {
        //底部导航栏三个控件的监听事件
        for(int i = 0;i<3;i++){
            bottom[i].setOnClickListener(this);
        }
        //初始化适配器
        adapter = new PagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    /**
     * 这个是点击底部导航栏切换页面的方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        for(int i=0;i<3;i++){
            if(v.getId() == tab_ids[i]){//判断点击的图片ID是不是底部导航栏图片ID中的一个
                if(!selected[i]){//判断是否选中
                    resetImg();//重置底部导航所有图片
                    iv_bottom[i].setImageResource(tab_Select[i]);
//                    iv_bottom[i].setImageResource(tab_Select[i]);//设置选中的背景图片
//                    tv_bottom[i].setTextColor(getResources().getColor(R.color.colorBlue));
                    viewPager.setCurrentItem(i);//切换显示区域
                    selected[i] = true;//设置选中
                    break;
                }
            }
        }
    }

    public void resetImg() {
        for(int i=0;i<3;i++){
            iv_bottom[i].setImageResource(tab_unSelect[i]);
//            iv_bottom[i].setImageResource(tab_unSelect[i]);
//            tv_bottom[i].setTextColor(getResources().getColor(R.color.colorBlack));
            selected[i] = false;
        }
    }
}

