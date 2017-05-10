package com.zcdyy.personalparameter.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * Created by chuan on 2017/4/13.
 */

public class HealthCircle extends BmobObject implements Serializable{
    private String id;//用户id
//    private String objectId;//动态Id
    private String content;//内容
    private BmobFile img;//图片
    private boolean isPic;//是否有图片
    private int commentCount;
    private int praiseCount;

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

    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

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
}
