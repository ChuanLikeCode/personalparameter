package com.zcdyy.personalparameter.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.utils.ImageLoaderUtils;
import com.zcdyy.personalparameter.utils.Utils;


/**
 * 底部弹窗2  联系商家
 * Created by zhouchuan on 2017/3/15.
 */

public class BottomPopwindows2 extends PopupWindow implements View.OnClickListener {
    public View view;
    private Context context;
    private TextView tvFriendShow;
    private TextView tvName;
    private TextView tvID;
    private CircleImageView head;
    private int position;
    private LinearLayout llMess;
    private LinearLayout llTel;
    private RelativeLayout rlFriendInfo;


    public BottomPopwindows2(Context context,int position, View.OnClickListener callToSellerListener){
        view = LayoutInflater.from(context).inflate(R.layout.popwindow_friendinfo,null);
        this.context = context;
        this.position = position;
        findViewsByIds(view);
        initData();
        bind(callToSellerListener);
          /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
//        this.setHeight(110*3+26);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
//        this.setWidth(width/3-40);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.ActionSheetDialogAnimation);
    }

    private void initData() {



    }

    private void bind(View.OnClickListener callToSellerListener) {
        tvFriendShow.setOnClickListener(callToSellerListener);

        llMess.setOnClickListener(this);
        llTel.setOnClickListener(this);
        rlFriendInfo.setOnClickListener(this);
    }

    private void findViewsByIds(View view) {
        tvFriendShow = (TextView) view.findViewById(R.id.tv_friend_show);
        tvID = (TextView) view.findViewById(R.id.tv_id);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        head = Utils.findViewsById(view,R.id.head);
        llMess = (LinearLayout) view.findViewById(R.id.ll_mes);
        llTel = (LinearLayout) view.findViewById(R.id.ll_tel);
        rlFriendInfo = (RelativeLayout) view.findViewById(R.id.rl_friend_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tel:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + tvID.getText().toString()));//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
                context.startActivity(intent);

                break;
            case R.id.ll_mes:
                Uri smsToUri = Uri.parse("smsto:" + tvID.getText().toString());
                Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                mIntent.putExtra("sms_body", "好久不见啦，最近怎么样？");
                context.startActivity(mIntent);
                break;
//            case R.id.rl_friend_info:
//                Intent intent1 = new Intent(context, ShowFriendInfoActivity.class);
//                intent1.putExtra("id", friend.getFriendAccount().get(position));
//                context.startActivity(intent1);

        }
    }
}
