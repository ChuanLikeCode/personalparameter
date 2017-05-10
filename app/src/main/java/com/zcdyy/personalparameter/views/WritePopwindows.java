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
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.StringUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.utils.Utils;

/**
 * Created by zhouchuan on 2017/4/27.
 */

public class WritePopwindows extends PopupWindow{
    public View view;
    private Context context;
    private ImageView dianzan1;
    public EditText comment1;
    private boolean isParise = false;
    private boolean commnetOrReplay = true;//true表示评论
    private String id;
    private String replayID;
    private String userName;
    private UserInfo loginuser;
    private Handler handler;
    private BmobUtils bmobUtils;
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setParise(boolean parise) {
        isParise = parise;
    }

    public void setReplayID(String replayID) {
        this.replayID = replayID;
    }

    public void setCommnetOrReplay(boolean commnetOrReplay) {
        this.commnetOrReplay = commnetOrReplay;
    }

    public WritePopwindows(Context context, String id){
        view = LayoutInflater.from(context).inflate(R.layout.write_popwindow,null);
        this.context = context;
        bmobUtils = new BmobUtils(context);
        loginuser = MyApplication.getInstance().readLoginUser();
        this.id = id;
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
                        writeComment(replayID);
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
            comment1.setHint("对"+userName+"回复：");
        }
    }

    private void writeComment(String replayID){
        String word = comment1.getText().toString();
        if (!StringUtils.isEmpty(word)){
            CommentInfo commentInfo = new CommentInfo();
            if (replayID.equals("0")){
                commentInfo.setIs_reply(false);
            }else {
                commentInfo.setIs_reply(true);
            }
            commentInfo.setContent(comment1.getText().toString());
            commentInfo.setNews_id(id);
            commentInfo.setUser_id(loginuser.getId());
            commentInfo.setReply_id(replayID);
            bmobUtils.saveCommentInfo(commentInfo,123,handler);
        }else {
            ToastUtils.shortToast(context,"请输入评论");
        }
    }

    private void findViewsByIds(View view) {
        dianzan1 = Utils.findViewsById(view,R.id.iv_dianzan1);
        comment1 = Utils.findViewsById(view,R.id.xiepinglun1);
    }

}
