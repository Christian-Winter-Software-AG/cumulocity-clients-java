/*
 * Copyright (c) 2024 Cumulocity GmbH, Düsseldorf, Germany and/or its affiliates and/or their licensors. *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Cumulocity GmbH.
 */

package com.cumulocity.lpwan.codec.exception;

public class LpwanCodecServiceException extends Exception {
    public LpwanCodecServiceException(String message) {
        super(message);
    }

    public LpwanCodecServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
