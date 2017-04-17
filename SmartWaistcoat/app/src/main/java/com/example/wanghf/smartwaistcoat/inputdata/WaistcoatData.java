package com.example.wanghf.smartwaistcoat.inputdata;

import java.io.Serializable;

/**
 * Created by wanghf on 2017/4/10.
 */

public class WaistcoatData implements Serializable, Cloneable {
    @Override
    protected WaistcoatData clone() throws CloneNotSupportedException {
        return (WaistcoatData) super.clone();
    }

    private double xinlv;
    private double zukang;
    private double wendu;
    private double yali;
    private double xueyang;
    private double maibo;
    private double jiasuduX;
    private double jiasuduY;
    private double jiasuduZ;


    public double getXinlv() {
        return xinlv;
    }

    public void setXinlv(double xinlv) {
        this.xinlv = xinlv;
    }

    public double getZukang() {
        return zukang;
    }

    public void setZukang(double zukang) {
        this.zukang = zukang;
    }

    public double getWendu() {
        return wendu;
    }

    public void setWendu(double wendu) {
        this.wendu = wendu;
    }

    public double getYali() {
        return yali;
    }

    public void setYali(double yali) {
        this.yali = yali;
    }

    public double getXueyang() {
        return xueyang;
    }

    public void setXueyang(double xueyang) {
        this.xueyang = xueyang;
    }

    public double getMaibo() {
        return maibo;
    }

    public void setMaibo(double maibo) {
        this.maibo = maibo;
    }

    public double getJiasuduX() {
        return jiasuduX;
    }

    public void setJiasuduX(double jiasuduX) {
        this.jiasuduX = jiasuduX;
    }

    public double getJiasuduY() {
        return jiasuduY;
    }

    public void setJiasuduY(double jiasuduY) {
        this.jiasuduY = jiasuduY;
    }

    public double getJiasuduZ() {
        return jiasuduZ;
    }

    public void setJiasuduZ(double jiasuduZ) {
        this.jiasuduZ = jiasuduZ;
    }
}
