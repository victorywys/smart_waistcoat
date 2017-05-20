package com.example.wanghf.smartwaistcoat.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wanghf on 2017/5/20.
 */

public class FitlerUtil {
    private static List ecgList = new LinkedList();

    public static double ecgFilter(int in) {
        if (ecgList.size() < 10) {
            ecgList.add(in);
            return in;
        }
        else {
            return (double) ecgList.remove(0);
        }
    }
}
