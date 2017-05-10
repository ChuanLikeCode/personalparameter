package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.UserInfo;
import com.zcdyy.personalparameter.constant.Constants;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.views.CleanableEditText;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public UserInfo account = new UserInfo();
    private CleanableEditText phone,password;
    private Button login;
    private TextView register,forgetPassword;
    private String userPhone,userPassword;
    private BmobUtils bmobUtils;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.ResultCode.RESULT_SUCCESS){
                if (BmobUtils.loginSuccess){
                    dialog.dismiss();
                   // Log.e("id", account.getId() + "");
//                    account.setNikeName("小明");
//                    account.setRealName("小明");
                    //bmobUtils.updateAccountInfo(account);
                    //MyApplication.getInstance().saveUserInfo(account);
                    startActivity(EditMyInfoActivity.class);
                    finish();
                }else{
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_LONG).show();
                    password.setText("");
                }

            }
        }
    };
    @Override
    protected void findViewByIDS() {
        phone = findViewsById(R.id.phone);
        password = findViewsById(R.id.password);
        login = findViewsById(R.id.loginBtn);
        register = findViewsById(R.id.registertv);
        forgetPassword = findViewsById(R.id.forgetpwd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginuser = MyApplication.getInstance().readLoginUser();
        if (loginuser != null) {
            startActivity(EditMyInfoActivity.class);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        bind();
    }

    private void bind() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        bmobUtils = new BmobUtils(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                checkAccountAndPassword();
                break;
            case R.id.registertv:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("type","register");
                startActivity(intent);
                break;
            case R.id.forgetpwd:
                Intent intent1 = new Intent(LoginActivity.this,RegisterActivity.class);
                intent1.putExtra("type","find");
                startActivity(intent1);
                break;
        }
    }

    /**
     * 检查账号密码是否填写
     */
    private void checkAccountAndPassword() {
        userPhone = phone.getText().toString();
        userPassword = password.getText().toString();
        account.setAccount(userPhone);
        account.setPassword(userPassword);
        Log.e("checkAccount","checkAccount");
        if (userPhone.equals("") || userPassword.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入用户名或密码", Toast.LENGTH_LONG).show();
        } else {
            dialog = ProgressDialog.show(this, null, "正在登录...");
            bmobUtils.queryAccount(account);
        }
    }
}
