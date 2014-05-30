package com.cumulocity.sdk.client.buffering;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;


public class MemoryBasedPersistentProvider extends PersistentProvider {

    private final AtomicLong counter = new AtomicLong(1);
    private final BlockingQueue<ProcessingRequest> requestQueue = new LinkedBlockingQueue<ProcessingRequest>();
    
    public MemoryBasedPersistentProvider() {
    }
    
    public MemoryBasedPersistentProvider(long bufferLimit) {
        super(bufferLimit);
    }
    
    @Override
    public long generateId() {
        return counter.getAndIncrement();
    }
    
    @Override
    public void offer(ProcessingRequest request) {
        if (requestQueue.size() >= bufferLimit) {
            throw new IllegalStateException("Queue is full");
        }
        
        try {
            requestQueue.put(request);
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }

    @Override
    public ProcessingRequest poll() {
        try {
            return requestQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
    }
}
