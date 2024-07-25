package com.codefinity;

import java.util.LinkedList;
import java.util.List;

public class BlockingQueueImpl {
    private final List<Object> queue = new LinkedList();
    private int limit = 10;

    public BlockingQueueImpl() {
    }

    public BlockingQueueImpl(int limit){
        this.limit = limit;
    }

    public synchronized void put(Object item) throws InterruptedException  {
        //TODO...
    }


    public synchronized Object take() throws InterruptedException {
        //TODO...
        return queue.remove(0);
    }

    public Integer size() {
        return queue.size();
    }
}
