package com.zcdyy.personalparameter.constant;


/**
 * Created by Huangjinfu on 2016/9/6.
 */
public class Constants {
    public static final boolean Debug = true;//false为线上

    public static class ResultCode {
        public static final int RESULT_SUCCESS = 666;
        public static final int RESULT_ERROR = 999;
        public static final int SUCCESS= 888;
        public static final int FAILED= 789;

        public static final int CODE_REPEAT = 2;  //重新发送
        public static final int SMSDDK_HANDLER = 3;  //短信回调
        public static final int REGISTER_SUCCESS = 200;  //用户名不存在
        public static final int REGISTER_FAILED = 404;  //用户名已存在
        public static final int UPLOAD_SUCCESS = 520;  //用户名已存在
    }//接口请求成功返回参数

    public static class OrderResultCode {
        public static final int RESULT_SUCCESS = 666;
        public static final int RESULT_OPERATOR = 2;
        public static final int RESULT_DETAIL = 3;
    }//接口请求成功返回参数



}
