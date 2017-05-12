package com.zcdyy.personalparameter.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouchuan on 2017/5/12.
 */

public class CommentS extends BmobObject {
    private String content;//评论内容

    private UserInfo user;//评论的用户，Pointer类型，一对一关系


}
