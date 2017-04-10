package com.example.wanghf.smartwaistcoat.inputdata;

/**
 * Created by wanghf on 2017/4/10.
 */

public class ByteFifo {
    private static final String TAG = "ByteFifo";
    private static final int fifoCapacity = 8192;
    private byte[] queue;
    private int capacity;
    private int size;
    private int head;
    private int tail;
    private static ByteFifo mByteFifo =null;

    private ByteFifo(int cap) {
        capacity = ( cap > 0 ) ? cap : 1; // at least 1
        queue = new byte[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * 获得fifo实例
     */
    public static ByteFifo getInstance() {
        if (mByteFifo == null) {
            mByteFifo = new ByteFifo(fifoCapacity);
        }
        return mByteFifo;
    }

    /**
     *获得fifo容量
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 获得fifo有效大小
     */
    public synchronized int getSize() {
        return size;
    }

    /**
     * 是否为空
     */
    public synchronized boolean isEmpty() {
        return ( size == 0 );
    }

    public synchronized boolean isFull() {
        return ( size == capacity );
    }

    public synchronized void add(byte b)
            throws InterruptedException {
        waitWhileFull();
        queue[head] = b;
        head = ( head + 1 ) % capacity;
        size++;
        notifyAll();
    }


    public synchronized void add(byte[] list)
            throws InterruptedException {
        int ptr = 0;
        while ( ptr < list.length ) {
            waitWhileFull();
            int space = capacity - size;
            int distToEnd = capacity - head;
            int blockLen = Math.min(space, distToEnd);
            int bytesRemaining = list.length - ptr;
            int copyLen = Math.min(blockLen, bytesRemaining);
            System.arraycopy(list, ptr, queue, head, copyLen);
            head = ( head + copyLen ) % capacity;
            size += copyLen;
            ptr += copyLen;
            notifyAll();
        }
    }

    public synchronized byte remove()
            throws InterruptedException {
        waitWhileEmpty();
        byte b = queue[tail];
        tail = ( tail + 1 ) % capacity;
        size  --;
        notifyAll();
        return b;
    }

    public synchronized byte[] removeAll() {
        if ( isEmpty() ) {
            return new byte[0];
        }

        byte[] list = new byte[size];

        int distToEnd = capacity - tail;
        int copyLen = Math.min(size, distToEnd);
        System.arraycopy(queue, tail, list, 0, copyLen);

        if ( size > copyLen ) {
            System.arraycopy(queue, 0, list, copyLen, size - copyLen);
        }
        tail = ( tail + size ) % capacity;
        size = 0;
        notifyAll();
        return list;
    }
    public synchronized byte[] removeAtLeastOne()
            throws InterruptedException {
        waitWhileEmpty();
        return removeAll();
    }

    public synchronized boolean waitUntilEmpty(long msTimeout)
            throws InterruptedException {
        if ( msTimeout == 0L ) {
            waitUntilEmpty();
            return true;
        }

        long endTime = System.currentTimeMillis() + msTimeout;
        long msRemaining = msTimeout;

        while ( !isEmpty() && ( msRemaining > 0L ) ) {
            wait(msRemaining);
            msRemaining = endTime - System.currentTimeMillis();
        }

        return isEmpty();

    }

    public synchronized void waitUntilEmpty()
            throws InterruptedException {
        while ( !isEmpty() ) {
            wait();
        }
    }

    public synchronized void waitWhileEmpty()
            throws InterruptedException {
        while ( isEmpty() ) {
            wait();
        }
    }

    public synchronized void waitUntilFull()
            throws InterruptedException {
        while ( !isFull() ) {
            wait();
        }
    }

    public synchronized void waitWhileFull()
            throws InterruptedException {
        while ( isFull() ) {
            wait();
        }
    }
}
