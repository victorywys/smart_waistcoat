package com.example.wanghf.smartwaistcoat.utils;

import java.nio.charset.Charset;

/**
 * Created by whf on 2015/10/3.
 */
public class ByteUtil {
    public int getInt2(byte[] bytes){
        return ((0xff & bytes[0]) | (0xff00 & bytes[1] << 8));
    }
    public float getFloat2(byte[] bytes){
        return Float.intBitsToFloat(getInt2(bytes));
    }
    public int getInt1(byte bytes){
        return (0xff & bytes);
    }
    public float getFloat1(byte bytes){
        return Float.intBitsToFloat(getInt1(bytes));
    }
    public String getHexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
    public String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++)
        {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }
    public static String strToUnicode(String strText) {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else // 低位在前面补00
                str.append("\\u00" + strHex);
        }
        return str.toString();
    }

    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }
    public static byte[] append(byte[] org, byte[] to) {

        byte[] newByte = new byte[org.length + to.length];

        System.arraycopy(org, 0, newByte, 0, org.length);

        System.arraycopy(to, 0, newByte, org.length, to.length);

        return newByte;

    }
}
