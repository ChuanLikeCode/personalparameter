package com.zcdyy.personalparameter.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Zhengzhihui on 2017/3/2.
 */

public class LayoutAdapter extends FragmentPagerAdapter {
        private String[] titles;
        private Context context;
        private List<Fragment> fragmentList;
        public LayoutAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, String[] titles) {
            super(fm);
            this.context = context;
            this.fragmentList=fragmentList;
            this.titles=titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList==null?0:fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
}
