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
    private String object_id;//评论ID
}
