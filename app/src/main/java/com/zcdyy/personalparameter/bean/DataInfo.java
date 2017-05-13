package com.zcdyy.personalparameter.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by chuan on 2017/5/13.
 */

public class DataInfo extends BmobObject implements Serializable{
    private String name;
    private List<ChartEntity> data;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChartEntity> getData() {
        return data;
    }

    public void setData(List<ChartEntity> data) {
        this.data = data;
    }
}
