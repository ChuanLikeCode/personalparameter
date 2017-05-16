package com.zcdyy.personalparameter.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.views.WheelPicker;

import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by zhouchuan on 2017/03/1.
 */
public abstract class BaseFragment extends Fragment {
    protected View main_view;
    protected UserInfo loginuser;
    protected ProgressDialog dialog;
    private View scrollViewLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginuser = BmobUser.getCurrentUser(UserInfo.class);
        scrollViewLayout = getActivity().getLayoutInflater().inflate(R.layout.scrollview_select, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (main_view == null){
            main_view  = onCreateViews(inflater,container);
        }

        return main_view;
    }


    protected abstract View onCreateViews(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        ((ViewGroup) main_view.getParent()).removeView(main_view);
        }
    /**
     * 获取屏幕宽度
     */
    public int getVmWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels; // 屏幕宽度（像素）

    }

    /**
     * 获取屏幕高度
     */
    public int getVmHeight() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels; // 屏幕高度（像素）

    }


    protected PopupWindow mPopupWindow;
    protected WheelPicker datePicker;
    //初始化popupwindow
    protected void initPopUpWindow(List<String> list, View.OnClickListener onClickListener) {
        final List<String> userTypeNameList = list;
        View convertView = null;
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.subscription_pop, null);
        TextView cancel = (TextView) convertView.findViewById(R.id.wheelpicker_tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        datePicker = (WheelPicker) convertView.findViewById(R.id.wheelpicker_picker);
        datePicker.setData(userTypeNameList);

        TextView confirm = (TextView) convertView.findViewById(R.id.wheelpicker_tv_confirm);
        confirm.setOnClickListener(onClickListener);
        mPopupWindow = new PopupWindow(convertView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_background));
        mPopupWindow.setAnimationStyle(R.style.timepopwindow_anim_style);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(this.getActivity().getCurrentFocus(), Gravity.BOTTOM, 0, 0);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
    }

}
