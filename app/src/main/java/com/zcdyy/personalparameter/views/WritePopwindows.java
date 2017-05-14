package com.zcdyy.personalparameter.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.ui.activity.HealthCircleDetailActivity;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.StringUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhouchuan on 2017/4/27.
 */

public class WritePopwindows extends PopupWindow{
    public View view;
    private Context context;
    public ImageView dianzan1;
    public EditText comment1;
    private boolean isParise = false;
    private boolean commnetOrReplay = true;//true表示评论
    private HealthCircle circle;
    private UserInfo replayUser;
    private UserInfo loginuser;
    private Handler handler;
    private BmobUtils bmobUtils;
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public UserInfo getReplayUser() {
        return replayUser;
    }

    public void setReplayUser(UserInfo replayUser) {
        this.replayUser = replayUser;
    }


    public boolean isCommnetOrReplay() {
        return commnetOrReplay;
    }



    public void setParise(boolean parise) {
        isParise = parise;
    }

    public void setCommnetOrReplay(boolean commnetOrReplay) {
        this.commnetOrReplay = commnetOrReplay;
    }

    public WritePopwindows(Context context, HealthCircle circle){
        view = LayoutInflater.from(context).inflate(R.layout.write_popwindow,null);
        this.context = context;
        bmobUtils = new BmobUtils(context);
        loginuser = MyApplication.getInstance().readLoginUser();
        this.circle = circle;
        findViewsByIds(view);
        bind();
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

    private void bind() {
        dianzan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HealthCircleDetailActivity)context).dianZan();
            }
        });

        comment1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                boolean isOpen = imm.isActive();
//                imm.set
                if (i== EditorInfo.IME_ACTION_SEND){
//                    Log.e("sdf",commnetOrReplay+"");
                    if (commnetOrReplay){
//                        comment1.setHint("写评论....");
                        writeComment("0");

                    }else {
//                        comment1.setHint("对"+userName+"回复：");
                        writeComment("1");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void initData(){
        if (isParise){//是否点赞
            dianzan1.setImageResource(R.mipmap.dz);
        }
        if (commnetOrReplay){
            comment1.setHint("写评论....");
        }else {
            comment1.setHint("对"+replayUser.getName()+"回复：");
        }
    }

    public String word ="";
    private void writeComment(String replayID){
        word = comment1.getText().toString();
        if (!StringUtils.isEmpty(word)){
            CommentInfo commentInfo = new CommentInfo();
            if (replayID.equals("0")){
                commentInfo.setIs_reply(false);
            }else {
                commentInfo.setIs_reply(true);
                commentInfo.setReplyUser(replayUser);
            }
            commentInfo.setContent(word);
            commentInfo.setCircle(circle);
            UserInfo info = BmobUser.getCurrentUser(UserInfo.class);
            commentInfo.setUser(info);
            bmobUtils.saveCommentInfo(commentInfo,888,404,handler);
            ((HealthCircleDetailActivity)context).comment.setClickable(false);
        }else {
            ToastUtils.shortToast(context,"请输入评论");
        }
        dismiss();
    }

    private void findViewsByIds(View view) {
        dianzan1 = Utils.findViewsById(view,R.id.iv_dianzan1);
        comment1 = Utils.findViewsById(view,R.id.xiepinglun1);
    }

}
