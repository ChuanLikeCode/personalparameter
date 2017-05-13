package com.zcdyy.personalparameter.bean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhouchuan on 2017/5/11.
 */

public class Comment {
    private BmobFile head;//用户头像
    private String name;//用户名字
    private String timeStr;
    private String userId;//评论用户ID
    private String replyName;//回复的那个人的名字
    private String replyId;//回复的那个人的ID
    private boolean isReply;//是否为回复
    private String content;//如果为回复，这个内容四回复的内容

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
