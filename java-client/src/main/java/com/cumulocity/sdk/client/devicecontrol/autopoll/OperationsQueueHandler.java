/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol.autopoll;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;

/**
 * This class is responsible for polling queue for operations to be performed by
 * OperationProcessor instance. When operations is going to be performed, first
 * status of operationRep in REST is changed to "executing", then
 * OperationProcessor is triggered and after that status of operationRep in REST is
 * changed accordingly to return value of OperationProcessor instance.
 */
public class OperationsQueueHandler {
    public static final String INTERNAL = "Internal";

    private static Logger logger = LoggerFactory.getLogger(OperationsQueueHandler.class);

    long queuePollTimeOut = 1000;

    boolean active = false;

    OperationProcessor operationProcessor;

    AtomicBoolean running = new AtomicBoolean(false);

    OperationsQueue queue;

    DeviceControlApi deviceControlApi;

    class Executor implements Runnable {
        @Override
        public void run() {
            OperationRepresentation op = null;
            running.set(true);
            while (isActive()) {
                try {
                    op = queue.poll(queuePollTimeOut, TimeUnit.MILLISECONDS);
                    if (op != null) {
                        // TODO - refactor. This isn't pretty
                        GId gid = op.getId();
                        Boolean internalOperation = false;
                        if (gid.getValue().startsWith(OperationsQueueHandler.INTERNAL)) {
                            internalOperation = true;
                        }

                        OperationRepresentation executingOperation = null;
                        if (!internalOperation) {
                            // change status of operation in REST to "executing"
                            op.setStatus(OperationStatus.EXECUTING.toString());
                            executingOperation = deviceControlApi.update(op);
                        }

                        boolean result = operationProcessor.process(op);

                        if (!internalOperation) {
                            // change status of operation in REST according to
                            // result
                            if (result) {
                                executingOperation.setStatus(OperationStatus.SUCCESSFUL.toString());
                            } else {
                                executingOperation.setStatus(OperationStatus.FAILED.toString());
                            }
                            deviceControlApi.update(executingOperation);
                        }

                    }
                } catch (InterruptedException e) {
                    logger.warn("Thread interrupted while processing operationRep", e);
                } catch (SDKException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            running.set(false);
        }

    }

    public OperationsQueueHandler(OperationProcessor operationProcessor, OperationsQueue queue, DeviceControlApi deviceControlApi) {
        this.operationProcessor = operationProcessor;
        this.queue = queue;
        this.deviceControlApi = deviceControlApi;
    }

    /**
     * Stops after finishing operation in progress
     */
    synchronized public void stop() {
        active = false;
    }

    /**
     * Starts, if it's not started yet
     */
    synchronized public void start() {
        if (!active) {
            active = true;
            new Thread(new Executor()).start();
        }
    }

    synchronized private boolean isActive() {
        return active;
    }

    boolean isRunning() {
        return running.get();
    }
}
