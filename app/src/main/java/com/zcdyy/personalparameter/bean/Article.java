package com.zcdyy.personalparameter.bean;

import java.io.Serializable;

import cn.bmob.v3.datatype.BmobFile;

/**
 * 动态
 * Created by zhouchuan on 2017/5/11.
 */

public class Article implements Serializable{
    private String id;//文章ID
    private String content;//内容
    private BmobFile img;//图片
    private boolean isPic;//是否有图片
    private int commentCount;
    private int praiseCount;
    private BmobFile head;//用户头像
    private String name;//用户名字
    private boolean isPraise;//是否点赞
    private String timeStr;//文章发表时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
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

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
