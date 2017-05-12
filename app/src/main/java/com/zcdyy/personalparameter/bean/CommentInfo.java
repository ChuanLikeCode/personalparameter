package com.zcdyy.personalparameter.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouchuan on 2017/5/10.
 */

public class CommentInfo extends BmobObject {
    private UserInfo user;
    private HealthCircle circle;//动态ID
    private String content;
    private boolean is_reply;//是否是回复false
    private UserInfo replyUser;//存你要回复的那个人的id

    public HealthCircle getCircle() {
        return circle;
    }

    public void setCircle(HealthCircle circle) {
        this.circle = circle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean is_reply() {
        return is_reply;
    }

    public void setIs_reply(boolean is_reply) {
        this.is_reply = is_reply;
    }

    public UserInfo getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserInfo replyUser) {
        this.replyUser = replyUser;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
