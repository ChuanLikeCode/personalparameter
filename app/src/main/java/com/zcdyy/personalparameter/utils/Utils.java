package com.zcdyy.personalparameter.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Huangjinfu on 2016/7/29.
 */
public class Utils {

    /**
     * 通过控件的Id获取对应的控件
     */
    public static <T extends View> T findViewsById(Activity activity, int viewId) {
        View view = activity.findViewById(viewId);
        return (T) view;
    }

    /**
     * 通过控件的Id获取对应的控件
     */
    public static <T extends View> T findViewsById(View parent, int viewId) {
        View view = parent.findViewById(viewId);
        return (T) view;
    }

    /**
     * 状态栏
     * @param activity
     */
    public static void setSteepStatusBar(Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }
}
