package com.example.wanghf.smartwaistcoat.inputdata;

import java.io.Serializable;

/**
 * Created by wanghf on 2017/4/10.
 */

public class WaistcoatData implements Serializable, Cloneable {
    @Override
    protected WaistcoatData clone() {
        try {
            return (WaistcoatData) super.clone();
        }
        catch (Exception e) {
            return new WaistcoatData();
        }
    }

    private double xinlv;
    private double zukang;
    private double wendu;
    private double yali;
    private double xueyang;
    private double dianliang;
    private boolean tuoluo;

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

    public double getDianliang() {
        return dianliang;
    }

    public void setDianliang(double dianliang) {
        this.dianliang = dianliang;
    }

    public boolean isTuoluo() {
        return tuoluo;
    }

    public void setTuoluo(boolean tuoluo) {
        this.tuoluo = tuoluo;
    }
}
