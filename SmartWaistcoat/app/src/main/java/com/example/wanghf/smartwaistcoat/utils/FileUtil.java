package com.example.wanghf.smartwaistcoat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by whf on 2015/9/17.
 */
public class FileUtil {
    private String SDCardRoot;

    public FileUtil() {
        SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        System.out.println("SD dir:" + SDCardRoot);
    }

    synchronized public File createFileInSDCard(String fileName, String dir)throws IOException {
        if(!isFileExist(fileName,dir)){
            createSDDir(dir);
        }
//        File file = new File(SDCardRoot + dir + File.separator + fileName);
//        file.createNewFile();
        return new File(SDCardRoot + dir + File.separator + fileName);
    }

    synchronized public File createSDDir(String dir) {
        File dirFile = new File(SDCardRoot + dir + File.separator);
        System.out.println("create dir:" + dirFile.mkdirs());
        return dirFile;
    }

    synchronized public boolean isFileExist(String fileName, String path) {
        File file = new File(SDCardRoot + path + File.separator + fileName);
        return file.exists();
    }

    synchronized public File write2SDFromInputString(String path, String fileName, String input) {
        File file = null;
        try {
            file = createFileInSDCard(fileName, path);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(input);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    synchronized public File write2SDFromInputByte(String path, String fileName, byte[] bytes){
        File file = null;
        try {
            file = createFileInSDCard(fileName, path);
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 拷贝流，可用于文件拷贝
     */
    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
    }

    /**
     * 发广播，扫描文件
     */
    public static void broadcastScanFileInSdCard(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File dbPath = new File(Environment.getExternalStorageDirectory(), path);
        Uri uri = Uri.fromFile(dbPath);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}