package com.zcdyy.personalparameter.views;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.utils.Utils;

/**
 * Created by Huangjinfu on 2017/2/7.
 */
public class EmptyView {
    private TextView tv_notify;//提示语
    private LinearLayout empty_layout;

    public EmptyView(View view) {
        empty_layout = Utils.findViewsById(view, R.id.empty_layout);
        tv_notify = Utils.findViewsById(view, R.id.empty_iv_notify);
    }

    public EmptyView(Activity view) {
        empty_layout = Utils.findViewsById(view, R.id.empty_layout);
        tv_notify = Utils.findViewsById(view, R.id.empty_iv_notify);
    }


    public void setEmptyViewGone() {
        empty_layout.setVisibility(View.GONE);
    }


    public void setNotify(String nofity) {
        empty_layout.setVisibility(View.VISIBLE);
        tv_notify.setText(nofity);
    }




}
