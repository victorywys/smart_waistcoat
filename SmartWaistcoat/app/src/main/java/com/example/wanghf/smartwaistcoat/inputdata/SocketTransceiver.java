package com.example.wanghf.smartwaistcoat.inputdata;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public abstract class SocketTransceiver implements Runnable {

    protected Socket socket;
    protected InetAddress addr;
    //protected InputStreamReader in;
    private InputStream inputStream;
    protected DataOutputStream out;
    private PrintWriter printWriter;
    //private BufferedReader bufferedReader;
    private boolean runFlag;
    private final int BUFFER_SIZE = 8096;
    byte[] buff = new byte[BUFFER_SIZE];
    /**
     * 实例化
     * 
     * @param socket
     *            已经建立连接的socket
     */
    public SocketTransceiver(Socket socket) {
        this.socket = socket;
        this.addr = socket.getInetAddress();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        this.addr = socket.getInetAddress();
    }

    /**
     * 获取连接到的Socket地址
     * 
     * @return InetAddress对象
     */
    public InetAddress getInetAddress() {
        return addr;
    }

    /**
     * 开启Socket收发
     * <p>
     * 如果开启失败，会断开连接并回调{@code onDisconnect()}
     */
    public void start() {
        runFlag = true;
        new Thread(this).start();
    }

    /**
     * 断开连接(主动)
     * <p>
     * 连接断开后，会回调{@code onDisconnect()}
     */
    public void stop() {
        runFlag = false;
        try {
            if (socket != null) {
                socket.shutdownInput();
                //in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送字符串
     * 
     * @param s
     *            字符串
     * @return 发送成功返回true
     */
    public boolean send(String s) {
        if (out != null && printWriter != null) {
            try {
                printWriter.write(s);
                printWriter.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 监听Socket接收的数据(新线程中运行)
     */
    @Override
    public void run() {
        try {
            //in = new InputStreamReader(this.socket.getInputStream());
            inputStream = this.socket.getInputStream();
            //socket.setSoTimeout(3000);
            //bufferedReader = new BufferedReader(in);
            out = new DataOutputStream(this.socket.getOutputStream());
            printWriter = new PrintWriter(out);
        } catch (Exception e) {
            e.printStackTrace();
            runFlag = false;
        }
        while (runFlag) {
            try {

                //String msg = bufferedReader.readLine();
                int code = inputStream.read(buff);
                if (code != -1) {
                    String msg = new String(buff, "UTF-8");
                    String[] splitStr = msg.split("\n");
                    if (splitStr.length > 1) {
                        msg = splitStr[0];
                        this.onReceive(addr, msg);
                    }
                }
                //final String s = in.readUTF();
                //this.onReceive(addr, s);
            } catch (Exception e) {
                // 连接被断开(被动)
                runFlag = false;
            }
        }
        // 断开连接
        try {
            //in.close();
            //bufferedReader.close();
            if (printWriter != null) {
                printWriter.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
            //in = null;
            //bufferedReader = null;
            printWriter = null;
            out = null;
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.onDisconnect(addr);
    }

    /**
     * 接收到数据
     * <p>
     * 注意：此回调是在新线程中执行的
     * 
     * @param addr
     *            连接到的Socket地址
     * @param s
     *            收到的字符串
     */
    public abstract void onReceive(InetAddress addr, String s);

    /**
     * 连接断开
     * <p>
     * 注意：此回调是在新线程中执行的
     * 
     * @param addr
     *            连接到的Socket地址
     */
    public abstract void onDisconnect(InetAddress addr);

}
