package com.zcdyy.personalparameter.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouchuan on 2017/5/10.
 */

public class CommentInfo extends BmobObject {
    private String user_id;
    private String news_id;//动态ID
    private String content;
    private boolean is_reply;//是否是回复false
    private String reply_id;//存你要回复的那个人的id
//    private String object_id;//评论ID

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
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

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }
}
