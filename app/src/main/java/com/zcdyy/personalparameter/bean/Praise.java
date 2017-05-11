package com.zcdyy.personalparameter.bean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhouchuan on 2017/5/11.
 */

public class Praise {
    private String name;
    private String timeStr;
    private BmobFile head;

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

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }
}
