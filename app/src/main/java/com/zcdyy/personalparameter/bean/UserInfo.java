package com.zcdyy.personalparameter.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Huangjinfu on 2016/11/9.
 */
public class UserInfo extends BmobObject implements Serializable{
    private String id;//用户ID
    private String account;//用户账号
    private String password;//用户密码
    private BmobFile head;//用户头像
    private String name;//用户名字
    private String sex;//性别
    private String age;//年龄
    private String height;//用户身高
    private String weight;//用户体重
    private boolean newInfo;//是否编辑过信息

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public boolean isNewInfo() {
        return newInfo;
    }

    public void setNewInfo(boolean newInfo) {
        this.newInfo = newInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BmobFile getHead() {
        return head;
    }

    public void setHead(BmobFile head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


}
