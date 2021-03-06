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

import cn.bmob.v3.BmobUser;


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
            dialog.dismiss();
            switch (msg.what){
                case 1:
//                    startActivity(EditMyInfoActivity.class);
                    UserInfo userInfo = MyApplication.getInstance().readLoginUser();
                    userInfo.setLogin(true);
                    MyApplication.getInstance().saveUserInfo(userInfo);
                    bmobUtils.updateInfo(userInfo,3,handler);
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_LONG).show();
                    password.setText("");
                    break;
                case 3:
                    startActivity(EditMyInfoActivity.class);
                    finish();
                    break;
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
            Log.e("isLogin",loginuser.isLogin()+"");
            if (loginuser.isLogin()){
                startActivity(EditMyInfoActivity.class);
                finish();
                return;
            }
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
        account.setUsername(userPhone);
        account.setPassword(userPassword);
        Log.e("checkAccount","checkAccount");
        if (userPhone.equals("") || userPassword.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入用户名或密码", Toast.LENGTH_LONG).show();
        } else {
            dialog = ProgressDialog.show(this, null, "正在登录...");
            bmobUtils.queryAccount(account,1,2,handler);
        }
    }
}
