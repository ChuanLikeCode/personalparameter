package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.utils.BmobUtils;

import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.zcdyy.personalparameter.constant.Constants.ResultCode.REGISTER_FAILED;
import static com.zcdyy.personalparameter.constant.Constants.ResultCode.REGISTER_SUCCESS;
import static com.zcdyy.personalparameter.constant.Constants.ResultCode.SMSDDK_HANDLER;
import static com.zcdyy.personalparameter.constant.Constants.ResultCode.SUCCESS;


public class RegisterActivity extends BaseActivity {

    public UserInfo account = new UserInfo();
    private EditText phone,password,testcode;
    private Button register,testbtn;
    private String userPhone,userPassword,code;
    private BmobUtils bmobUtils;
    private EventHandler eventHandler;
    private TextView goLogin;
    private int type;
    private CountDownTimer timer = new CountDownTimer(1000 * 60, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            testbtn.setText("重新发送 " + (millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            testbtn.setEnabled(true);
            testbtn.setText("验证");
        }
    };
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_SUCCESS://发送短信
                    sendSMS();
                    break;
                case REGISTER_FAILED:
                    if (type == 1){
                        sendSMS();
                    }else {
                        Toast.makeText(RegisterActivity.this, "用户名已被注册", Toast.LENGTH_LONG).show();

                    }
                    break;
                case SUCCESS://上传用户信息完成
                    dialog.dismiss();
                    //bmobUtils.updateAccountInfo(account);
                    startActivity(LoginActivity.class);
                    finish();
                    break;
                case SMSDDK_HANDLER://短信发送完成处理信息回调
                    getSMS(msg);
                    break;
            }
        }
    };

    /**
     * 处理收到短信之后
     * @param msg
     */
    private void getSMS(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        //回调完成
        if (result == SMSSDK.RESULT_COMPLETE) {
            //验证码验证成功
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (type == 1){//找回密码
                    Toast.makeText(RegisterActivity.this, "找回成功", Toast.LENGTH_LONG).show();
                    userPassword = password.getText().toString();
                    userPhone = phone.getText().toString();
                    bmobUtils.getBackYourAccount(userPhone,userPassword);
                }else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    userPassword = password.getText().toString();
                    userPhone = phone.getText().toString();
                    account.setAccount(userPhone);
                    account.setPassword(userPassword);
                    account.setNewInfo(true);
                    bmobUtils.addUserInfo(account);
                }

            }
            //已发送验证码
            else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "验证码已经发送",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
//            dialog.dismiss();
            Toast.makeText(RegisterActivity.this, "验证码错误请重新输入！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void findViewByIDS() {
        phone = findViewsById(R.id.phone);
        password = findViewsById(R.id.password);
        testcode = findViewsById(R.id.testcode);
        register = findViewsById(R.id.registerbtn);
        testbtn = findViewsById(R.id.testbtn);
        goLogin = findViewsById(R.id.goLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initData();
        initSMS();
    }

    /**
     * 初始化短信注册回调方法
     * 获得验证码时会调用这个方法
     * 注册时也会调用这个方法
     */
    private void initSMS() {
        eventHandler = new EventHandler() {
            /**
             * EVENT	                                  DATA类型	                          说明
             * EVENT_GET_SUPPORTED_COUNTRIES	ArrayList<HashMap<String,Object>>	返回支持发送验证码的国家列表
             * EVENT_GET_VERIFICATION_CODE	    Boolean	                            true为智能验证，false为普通下发短信
             * EVENT_SUBMIT_VERIFICATION_CODE	HashMap<String,Object>	校验验证码，返回校验的手机和国家代码
             * EVENT_GET_CONTACTS	            ArrayList<HashMap<String,Object>>	获取手机内部的通信录列表
             * EVENT_SUBMIT_USER_INFO	                null	                    提交应用内的用户资料
             * EVENT_GET_FRIENDS_IN_APP	        ArrayList<HashMap<String,Object>>	获取手机通信录在当前应用内的用户列表
             * EVENT_GET_VOICE_VERIFICATION_CODE	    null	                    请求发送语音验证码，无返回
             * afterEvent在操作结束时被触发，同样具备event和data参数，但是data是事件操作结果
             *
             * @param event  事件返回的类型
             * @param result 事件返回的结果
             * @param data   事件执行结束返回的操作结果
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                message.what = SMSDDK_HANDLER;
                handler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);//短信回调注册
    }

    private void initData() {
        bmobUtils = new BmobUtils(this);
        Intent intent = getIntent();
        if (intent != null){
            String str = intent.getStringExtra("type");
            if (!str.equals("")){
                if (str.equals("register")){
                    type = 0;//注册
                    register.setText("注册");
                }else if (str.equals("find")){
                    type = 1;//找回密码
                    register.setText("确定找回");
                    goLogin.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * 前往登录
     *
     * @param view
     */
    public void goLogin(View view) {
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 发送手机验证码的处理
     */
    private void prepareSendSMS() {
        userPhone = phone.getText().toString();
        userPassword = password.getText().toString();
        if (!(userPhone.equals("") || userPassword.equals(""))) {
            if (check()) {
                bmobUtils.registerChecked(userPhone);
            } else {
                Toast.makeText(RegisterActivity.this, "用户名不对或者密码只能为数字/字母(6-16位)",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证手机号与密码（6-16位数字字母）
     *
     * @return
     */
    public boolean check() {
        //验证手机号码与密码（6-16数字字母）
        String regPhone = "^1[3|4|5|7|8][0-9]\\d{8}$";
        String regPassword = "^[0-9a-zA-z]{6,16}";
        boolean phoneBoolean = Pattern.matches(regPhone, userPhone);
        boolean pswBoolean = Pattern.matches(regPassword, userPassword);
        if (phoneBoolean && pswBoolean) {
            return true;
        }
        return false;
    }

    /**
     * 点击注册
     *
     * @param view
     */
    public void goRegister(View view) {
        if (type == 0){
            dialog = ProgressDialog.show(this, null, "正在注册...");
        }else {
            dialog = ProgressDialog.show(this, null, "正在找回...");
        }

        code = testcode.getText().toString();
        userPhone = phone.getText().toString();
        userPassword = password.getText().toString();
        if (!(userPhone.equals("") || userPassword.equals(""))) {
            if (!code.equals("")) {
                SMSSDK.submitVerificationCode("86", userPhone, code);
            } else {
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "验证码不能为空",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            dialog.dismiss();
            Toast.makeText(RegisterActivity.this, "用户名或者密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取验证码
     *
     * @param view
     */

    public void getTestCode(View view) {
        prepareSendSMS();
    }

    /**
     * 发送短信
     */
    private void sendSMS() {
        SMSSDK.getVerificationCode("86", userPhone);
        testbtn.setEnabled(false);
        timer.start();
//        dialog = ProgressDialog.show(this, null, "正在发送...");
    }
}


