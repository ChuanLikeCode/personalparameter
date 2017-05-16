package com.zcdyy.personalparameter.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by chuan on 2017/5/16.
 */

public class KnowLedge extends BmobObject implements Serializable {
    private String title;
    private String shihe;
    private String shenggao;
    private String lingjie;
    private String jigao;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShihe() {
        return shihe;
    }

    public void setShihe(String shihe) {
        this.shihe = shihe;
    }

    public String getShenggao() {
        return shenggao;
    }

    public void setShenggao(String shenggao) {
        this.shenggao = shenggao;
    }

    public String getLingjie() {
        return lingjie;
    }

    public void setLingjie(String lingjie) {
        this.lingjie = lingjie;
    }

    public String getJigao() {
        return jigao;
    }

    public void setJigao(String jigao) {
        this.jigao = jigao;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
