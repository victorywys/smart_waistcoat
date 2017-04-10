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

    public double getXinlv() {
        return xinlv;
    }

    public void setXinlv(double xinlv) {
        this.xinlv = xinlv;
    }
}
