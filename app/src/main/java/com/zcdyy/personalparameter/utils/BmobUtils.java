package com.zcdyy.personalparameter.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.constant.Constants;
import com.zcdyy.personalparameter.ui.activity.EditMyInfoActivity;
import com.zcdyy.personalparameter.ui.activity.LoginActivity;
import com.zcdyy.personalparameter.ui.activity.PublishActivity;
import com.zcdyy.personalparameter.ui.activity.RegisterActivity;
import com.zcdyy.personalparameter.ui.activity.SettingActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by chuan on 2017/4/11.
 */

public class BmobUtils {
    public static boolean loginSuccess = false;
    private Context context;
    public static boolean userHead = false;
    private boolean registerSuccess = true;
    /**
     * 添加朋友
     */
    private boolean cun = false;
    private UserInfo userInfo;
    public BmobUtils(Context context){
        this.context = context;
    }
    private DataUtils dataUtils;

    public void setDataUtils(DataUtils dataUtils) {
        this.dataUtils = dataUtils;
    }

    /**
     * 保存评论
     * @param commentInfo
     * @param resultCode
     * @param handler
     */
    public void saveCommentInfo(CommentInfo commentInfo, final int resultCode, final Handler handler){
        commentInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.e("saveCommentInfo", "ok");
                    handler.sendEmptyMessage(resultCode);
                } else {
                    Log.e("saveCommentInfo", "failed");
                }
            }
        });
    }

    /**
     * 更新User信息--新用户编辑信息
     *
     * @param userInfo
     */
    public void updateFirstInfo(UserInfo userInfo) {
        BmobUser info = BmobUser.getCurrentUser();
        userInfo.update(info.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateFirstInfo", "ok");
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constants.ResultCode.SUCCESS);
                } else {
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(456);
                    e.printStackTrace();
                    Log.e("updateFirstInfo", "failed");
                }
            }
        });
    }
    /**
     * 开始编辑个人信息时的上传图片
     *
     * @param bmobFile
     * @param loginuser
     */
    public void upLoadUserHeadFile(final BmobFile bmobFile, final UserInfo loginuser) {
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    userHead = true;
                    loginuser.setHead(bmobFile);//设置头像
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constants.ResultCode.UPLOAD_SUCCESS);
                    //updateUserInfo(loginuser);//更新用户信息
                    Log.e("upLoadUserHeadFile", "ok");
                } else {
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constants.ResultCode.FAILED);
                    Log.e("upLoadUserHeadFile", e.getMessage());
                }
            }
        });
    }

    /**
     * 更新User信息--设置专用
     *
     * @param userInfo
     */
    public void updateUser(UserInfo userInfo) {
        userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateUserInfo", "ok");
                    ((SettingActivity) context).handler.sendEmptyMessage(Constants.ResultCode.SUCCESS);
                } else {
                    ((SettingActivity) context).handler.sendEmptyMessage(456);
                    e.printStackTrace();
                    Log.e("updateUserInfo", "failed");
                }
            }
        });
    }

    /**
     * 编辑个人信息时的上传图片---设置专用
     *
     * @param bmobFile
     * @param loginuser
     */
    public void upLoadHeadFile(final BmobFile bmobFile, final UserInfo loginuser) {
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    userHead = true;
                    loginuser.setHead(bmobFile);//设置头像
                    ((SettingActivity) context).handler.sendEmptyMessage(Constants.ResultCode.UPLOAD_SUCCESS);
                    Log.e("upLoadUserHeadFile", "ok");
                } else {
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constants.ResultCode.FAILED);
                    Log.e("upLoadUserHeadFile", e.getMessage());
                }
            }
        });
    }

    /**
     * 登录查询是否账号密码输入错误
     */
    public void queryAccount(final UserInfo account, final int resultCode, final int failedCode, final Handler handler) {

        account.login(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e==null){
                    Log.e("queryAccount", "ok");
                    UserInfo userInfo1 = BmobUser.getCurrentUser(UserInfo.class);
                    MyApplication.getInstance().saveUserInfo(userInfo1);
                    handler.sendEmptyMessage(resultCode);
                }else {
                    Log.e("queryAccount", e.getMessage());
                    handler.sendEmptyMessage(failedCode);
                }
            }
        });
    }

    /**
 * 找回密码
 */
    public void getBackYourAccount(final String phone, final String userPassword){
        final BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null){
                    Log.e("getBackYourAccount", "ok");
                    for (UserInfo a :
                            list) {
                        if (a.getUsername().equals(phone)){
                            a.setPassword(userPassword);
                            findPassword(a);
                            break;
                        }
                    }
                }else {
                    Log.e("getBackYourAccount", e.getMessage());
                }
            }
        });
    }

    /**
     * 更新用户密码
     *
     * @param account
     */
    public void findPassword(UserInfo account) {
        account.update(account.getId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateAccountInfo", "ok");
                    ((RegisterActivity) context).handler.sendEmptyMessage(Constants.ResultCode.SUCCESS);
                } else {
                    e.printStackTrace();
                    Log.e("updateAccountInfo", e.getMessage());
                }
            }
        });
    }

    /**
     * 上传图片文件--注册时默认的用户头像
     *
     * @param bmobFile
     * @param account
     */
    public void upMoRenFile(final BmobFile bmobFile, final UserInfo account) {
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    account.setHead(bmobFile);
                    addUserInfo(account);
                    Log.e("upLoadFile", "ok--" + bmobFile.getFileUrl());
                } else {
                    e.printStackTrace();
                    Log.e("upLoadFile", e.getMessage());
                }
            }
        });
    }

    /**
     * 增加用户
     * @param account
     */
    public void addUserInfo(final UserInfo account) {

        account.signUp(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    ((RegisterActivity) context).account.setId(s);
                    updateInfo(((RegisterActivity) context).account);
                    ((RegisterActivity) context).handler.sendEmptyMessage(Constants.ResultCode.SUCCESS);
                }else {
                    Log.e("Bmob-updateInfoFailed", e.getMessage());
                }
            }
        });
    }

    /**
     * 注册新用户验证
     * @param userPhone 账号
     */
    public void registerChecked(final String userPhone) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null){
                    Log.e("list",list+"");
                    Log.e("registerChecked","ok");
                    for (UserInfo a :
                            list) {
                        if (a.getUsername().equals(userPhone)) {
                            registerSuccess = false;
                            break;
                        }
                    }
                    if (!registerSuccess) {
                        ((RegisterActivity) context).handler.sendEmptyMessage(Constants.ResultCode.REGISTER_FAILED);
                    } else {
                        ((RegisterActivity) context).handler.sendEmptyMessage(Constants.ResultCode.REGISTER_SUCCESS);
                    }
                }else {
                    Log.e("registerChecked",e.getMessage());
                }

            }
        });
    }



    /**
     * 更新用户信息
     * @param userInfo
     */
    public void updateInfo(UserInfo userInfo){
        BmobUser userInfo1 = BmobUser.getCurrentUser();
        userInfo.update(userInfo1.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.e("updateInfo","ok");
                }else {
                    Log.e("updateInfo",e.getMessage());
                }
            }
        });
    }

    /**
     * 发布动态页面需要用到的上传图片
     * @param bmobFile
     */
    public void PublishUpLoadFile(final BmobFile bmobFile){
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.e("PublishUpLoadFile","ok");
                    ((PublishActivity)context).healthCircle.setImg(bmobFile);
                    ((PublishActivity)context).isPic = true;
                    ImageLoaderUtils.initImage(context,
                            bmobFile.getFileUrl(),((PublishActivity)context).img, R.drawable.chat_xe_icon2_03);
                    ((PublishActivity)context).handler.sendEmptyMessage(Constants.ResultCode.UPLOAD_SUCCESS);
                }else {
                    Log.e("PublishUpLoadFile",e.getMessage());
                }
            }
        });
    }

    /**
     * 发布动态
     * @param healthCircle
     */
    public void publishFriendCircle(HealthCircle healthCircle){
//        Log.e("publishFriendCircle","publishFriendCircle");
        healthCircle.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.e("publishFriendCircle","ok");
                    ((PublishActivity)context).handler.sendEmptyMessage(Constants.ResultCode.SUCCESS);
                }else {
                    Log.e("publishFriendCircle",e.getMessage());
                }
            }
        });
    }

    /**
     * 点赞或者评论之后的更新数据
     * @param healthCircle
     * @param resultCode
     * @param handler
     */
    public void updateHealCircle(HealthCircle healthCircle, final int resultCode, final int failedCode, final Handler handler){

        healthCircle.update(healthCircle.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.e("updateHealCircle","ok");
                    handler.sendEmptyMessage(resultCode);
                }else {
                    Log.e("updateHealCircle",e.getMessage());
                    handler.sendEmptyMessage(failedCode);
                }
            }
        });
    }

    public void savePraiseInfo(PraiseInfo praiseInfo,final int resultCode,int failedCode, final Handler handler){
        praiseInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.e("savePraiseInfo","ok");
                    handler.sendEmptyMessage(resultCode);
                }else {
                    Log.e("savePraiseInfo",e.getMessage());
                }
            }
        });
    }

    public void deletePraiseInfo(PraiseInfo praiseInfo,final int resultCode, int failedCode,final Handler handler){
        praiseInfo.delete(praiseInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.e("deletePraiseInfo","ok");
                    handler.sendEmptyMessage(resultCode);
                }else {
                    Log.e("deletePraiseInfo",e.getMessage());
                }
            }
        });
    }

    /**
     * 查询动态
     * @param resultCode
     * @param handler
     */
    public void queryFriendCircle( final int resultCode, final Handler handler){

        BmobQuery<HealthCircle> query = new BmobQuery<>();
        query.order("-createAt");
        query.include("auther");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<HealthCircle>() {
            @Override
            public void done(List<HealthCircle> list, BmobException e) {
                if (e==null){
                    Log.e("queryFriendCircle","ok");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    message.what =resultCode;
                    bundle.putSerializable("list", (Serializable) list);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Log.e("queryFriendCircle",e.getMessage());
                }
            }
        });
    }

    /**
     * 获取点赞
     */
    public void getPraiseInfo(final int resultCode, final Handler handler){
        BmobQuery<PraiseInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<PraiseInfo>() {
            @Override
            public void done(List<PraiseInfo> list, BmobException e) {
                if (e==null){
                    Log.e("getPraiseInfo","ok");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    message.what =resultCode;
                    bundle.putSerializable("list", (Serializable) list);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Log.e("getPraiseInfo",e.getMessage());
                }
            }
        });
    }

    /**
     * 获取文章详情
     */
    public void getHealthCircle(final String objectId, final String userId, final int resultCode, final Handler handler){
        BmobQuery<HealthCircle> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId",objectId);
        query.include("auther");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<HealthCircle>() {
            @Override
            public void done(List<HealthCircle> list, BmobException e) {
                if (e==null){
                    Log.e("getHealthCircle","ok");
                    dataUtils.healthCircleList.clear();
                    dataUtils.healthCircleList.addAll(list);
                    getPraiseInfo(objectId,userId,resultCode,handler);
                }else {
                    Log.e("getHealthCircle",e.getMessage());
                }
            }
        });
    }

    /**
     * 获取发布动态的用户的头像名字
     * @param healthCircleList
     * @param resultCode
     * @param handler
     */
    public void getUserInfo(final List<HealthCircle> healthCircleList, final int resultCode, final Handler handler){
        dataUtils.articleUserList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<healthCircleList.size();i++){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BmobQuery<UserInfo> query = new BmobQuery<>();
//                    query.addWhereEqualTo("id",healthCircleList.get(i).getId());
                    query.findObjects(new FindListener<UserInfo>() {
                        @Override
                        public void done(List<UserInfo> list, BmobException e) {
                            if (e==null){
                                Log.e("getUserInfo","ok");
                                dataUtils.articleUserList.addAll(list);
                                if (dataUtils.articleUserList.size() == healthCircleList.size()){
                                    handler.sendEmptyMessage(resultCode);
                                }

                            }else {
                                Log.e("getUserInfo",e.getMessage());
                            }
                        }
                    });
                }
            }
        }).start();

    }



    /**
     * 获取用户此文章是否点赞
     * @param resultCode
     * @param handler
     */
    public static boolean hasPraise = false;
    public void getPraiseInfo(String news_id,String user_id,final int resultCode, final Handler handler){
        BmobQuery<PraiseInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("news_id",news_id);
        query.addWhereEqualTo("user_id",user_id);
        query.findObjects(new FindListener<PraiseInfo>() {
            @Override
            public void done(List<PraiseInfo> list, BmobException e) {
                if (e==null){
                    Log.e("getPraiseInfo","ok");
                    if (list.size()!=0){
                        hasPraise = true;
                    }
                    handler.sendEmptyMessage(resultCode);
                }else {
                    Log.e("getPraiseInfo",e.getMessage());
                }
            }
        });
    }

    /**
     * 获取文章点赞
     * @param resultCode
     * @param handler
     */
    public void getPraiseInfo(String id,final int resultCode, final Handler handler){
        BmobQuery<PraiseInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("circleId",id);
        query.include("user");
        query.findObjects(new FindListener<PraiseInfo>() {
            @Override
            public void done(List<PraiseInfo> list, BmobException e) {
                if (e==null){
                    Log.e("getPraiseInfo","ok");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    message.what = resultCode;
                    bundle.putSerializable("zan", (Serializable) list);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Log.e("getPraiseInfo",e.getMessage());
                }
            }
        });
    }

    /**
     * 获取点赞的用户的信息
     * @param praiseInfoList
     * @param resultCode
     * @param handler
     */
    public void getPraiseUserInfo(final List<PraiseInfo> praiseInfoList, final int resultCode, final Handler handler){
        dataUtils.praiseUserInfo.clear();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0;i<praiseInfoList.size();i++){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BmobQuery<UserInfo> query = new BmobQuery<>();
//                    query.addWhereEqualTo("objectId",praiseInfoList.get(i).getUser_id());
                    query.findObjects(new FindListener<UserInfo>() {
                        @Override
                        public void done(List<UserInfo> list, BmobException e) {
                            if (e==null){
                                Log.e("getPraiseUserInfo","ok");
                                dataUtils.praiseUserInfo.addAll(list);
                                if (dataUtils.praiseUserInfo.size() == praiseInfoList.size()){
                                    handler.sendEmptyMessage(resultCode);
                                }
                            }else {
                                Log.e("getPraiseUserInfo",e.getMessage());
                            }
                        }
                    });
                    }
                }
            }).start();


    }

    /**
     * 获取文章的评论
     * @param resultCode
     * @param handler
     */
    public void getCommnetInfo(HealthCircle circle,final int resultCode, final Handler handler){
        BmobQuery<CommentInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("circle",new BmobPointer(circle));
        query.include("user,replyUser");
        query.findObjects(new FindListener<CommentInfo>() {
            @Override
            public void done(List<CommentInfo> list, BmobException e) {
                if (e==null){
                    Log.e("getCommnetInfo","ok");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    message.what = resultCode;
                    bundle.putSerializable("comment", (Serializable) list);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Log.e("getCommnetInfo",e.getMessage());
                }
            }
        });
    }

    /**
     * 获取评论的用户信息
     * @param commentInfoList
     * @param resultCode
     * @param handler
     */
    public void getCommentUserInfo(final List<CommentInfo> commentInfoList, final int resultCode, final Handler handler){
        dataUtils.commentUserInfo.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<commentInfoList.size();i++){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BmobQuery<UserInfo> query = new BmobQuery<>();
//                    query.addWhereEqualTo("objectId",commentInfoList.get(i).getUser_id());
                    query.findObjects(new FindListener<UserInfo>() {
                        @Override
                        public void done(List<UserInfo> list, BmobException e) {
                            if (e==null){
                                dataUtils.commentUserInfo.addAll(list);
                                if (dataUtils.commentUserInfo.size()==commentInfoList.size()){
//                            Intent intent = new Intent("comment");
                                    Log.e("getCommentUserInfo","ok");
                                    handler.sendEmptyMessage(resultCode);
//                            context.sendBroadcast(intent);
                                }
                            }else {
                                Log.e("getCommentUserInfo",e.getMessage());
                            }
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 获取评论里面回复的人的信息
     * @param commentInfoList
     * @param resultCode
     * @param handler
     */
    public void getCommentReplyUserInfo(final List<CommentInfo> commentInfoList, final int resultCode, final Handler handler){
        dataUtils.replyUserInfo.clear();
        int count = 0;
        for (CommentInfo c:commentInfoList){
            if (c.is_reply()){
                count++;
            }
        }
        if (count==0){
            handler.sendEmptyMessage(resultCode);
            return;
        }
        final int finalCount = count;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<commentInfoList.size();i++){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (commentInfoList.get(i).is_reply()){
                        BmobQuery<UserInfo> query = new BmobQuery<>();
//                        query.addWhereEqualTo("objectId",commentInfoList.get(i).getReply_id());
                        query.findObjects(new FindListener<UserInfo>() {
                            @Override
                            public void done(List<UserInfo> list, BmobException e) {
                                if (e==null){
//                            Log.e("getCommentReplyUserInfo","ok");
                                    dataUtils.replyUserInfo.addAll(list);
                                    if (dataUtils.replyUserInfo.size()== finalCount){
                                        Log.e("getCommentReplyUserInfo","ok");
//                                Intent intent = new Intent("comment");
//                                context.sendBroadcast(intent);
                                        handler.sendEmptyMessage(resultCode);
                                    }
                                }else {
                                    Log.e("getCommentReplyUserInfo",e.getMessage());
                                }
                            }
                        });
                    }

                }
            }
        }).start();


    }


    /**
     * 上传图片
     * @param bmobFile
     * @param resultCode
     * @param handler
     */
    public void upLoadPic(final BmobFile bmobFile, final int resultCode, final Handler handler){
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.e("upLoadPic","ok");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pic", bmobFile);
                    message.setData(bundle);
                    message.what = resultCode;
                    handler.sendMessage(message);
                }else {
                    Log.e("upLoadPic",e.getMessage());
                }
            }
        });
    }

}
