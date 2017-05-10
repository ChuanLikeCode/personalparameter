package com.zcdyy.personalparameter.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragment;

    public PagerAdapter(FragmentManager fm, List<Fragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return mFragment.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFragment.size();
    }

    /**
     * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
     */
    @Override
    public void finishUpdate(View container) {
        // TODO Auto-generated method stub
        super.finishUpdate(container);
    }
}
