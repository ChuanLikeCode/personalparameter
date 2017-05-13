package com.zcdyy.personalparameter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.ToastUtils;

import java.util.regex.Pattern;


public class ReseivePasswordActivity extends BaseActivity {
    private EditText et_pre;
    private EditText et_now;
    private EditText et_confirm;
    private TextView title, save;
    private String password_et_pre;
    private String password_et_now;
    private String password_et_confirm;
    private BmobUtils bmobUtils;

    @Override
    protected void findViewByIDS() {
        et_confirm = (EditText) findViewById(R.id.revisePassword_confirm);
        et_pre = (EditText) findViewById(R.id.revisePassword_pre);
        et_now = (EditText) findViewById(R.id.revisePassword_new);
        title = (TextView) findViewById(R.id.top_tv_title);
        save = (TextView) findViewById(R.id.top_tv_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);
        bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra("title");
            if (!str.equals("")) {
                title.setText(str);
            }
        }
    }

    private void bind() {
        bmobUtils = new BmobUtils(this);
        save.setVisibility(View.VISIBLE);
        save.setText("确定");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_et_pre = et_pre.getText().toString();
                password_et_now = et_now.getText().toString();
                password_et_confirm = et_confirm.getText().toString();
//                if (!password_et_pre.equals("") || !password_et_now.equals("") || !password_et_confirm.equals("")) {
//                    if (password_et_pre.equals(loginuser.getPassword())) {
//
//                        if (check()) {
//                            if (password_et_confirm.equals(password_et_now)) {
//                                loginuser.setPassword(password_et_confirm);
//                                MyApplication.getInstance().saveUserInfo(loginuser);
//                                bmobUtils.updateInfo(loginuser);
//                                ToastUtils.shortToast(ReseivePasswordActivity.this, "密码修改成功");
//                                finish();
//                            } else {
//                                ToastUtils.shortToast(ReseivePasswordActivity.this, "两次输入的密码不一致");
//                            }
//                        } else {
//                            ToastUtils.shortToast(ReseivePasswordActivity.this, "请输入6-16位的数字或字母");
//                        }
//                    } else {
//                        ToastUtils.shortToast(ReseivePasswordActivity.this, "原密码输入错误");
//                    }
//                } else {
//                    ToastUtils.shortToast(ReseivePasswordActivity.this, "请输入要修改的密码");
//                }

            }
        });
    }

    /**
     * 验证手机号与密码（6-16位数字字母）
     *
     * @return
     */
    public boolean check() {
        //验证密码（6-16数字字母）
        String regPassword = "^[0-9a-zA-z]{6,16}";
        boolean pswBoolean = Pattern.matches(regPassword, password_et_now);
        if (pswBoolean) {
            return true;
        }
        return false;
    }
}
