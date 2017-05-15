package com.zcdyy.personalparameter.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by chuan on 2017/5/15.
 */

public class ZiXun extends BmobObject implements Serializable {
    private String title;
    private String content;
    private BmobFile img;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
