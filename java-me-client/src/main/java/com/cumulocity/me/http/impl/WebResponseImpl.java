package com.cumulocity.me.http.impl;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.util.IOUtils;

public class WebResponseImpl implements WebResponse {
    
    private final int status;
    private final String message;
    private final Map headers;
    private final byte[] data;

    public WebResponseImpl(HttpConnection connection) throws IOException {
        this.status = connection.getResponseCode();
        this.message = connection.getResponseMessage();
        this.headers = readHeaders(connection);
        this.data = readData(connection);
    }

    public boolean isSuccessful() {
        return status < 300;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Map getHeaders() {
        return headers;
    }
    
    public byte[] getData() {
        return data;
    }
    
    private Map readHeaders(HttpConnection connection) throws IOException {
        Map headers = new HashMap();
        String key = null;
        int i = 0;
        while ((key = connection.getHeaderFieldKey(i)) != null) {
            headers.put(key, connection.getHeaderField(i));
            i++;
        }
        return headers;
    }
    
    private byte[] readData(HttpConnection connection) throws IOException {
        if (!isSuccessful()) {
            return null;
        }
        int length = (int) connection.getLength();
        if (length > 0) {
            return IOUtils.readData(length, connection.openInputStream());
        } else {
            return null;
        }
    }
}