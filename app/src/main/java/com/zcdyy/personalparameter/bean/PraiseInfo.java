package com.zcdyy.personalparameter.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouchuan on 2017/5/10.
 */

public class PraiseInfo extends BmobObject{
    private String user_id;
        private String news_id;
//    private String object_id;

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
}
