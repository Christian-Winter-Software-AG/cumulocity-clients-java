package com.cumulocity.sdk.client.notification;

import org.eclipse.jetty.client.HttpClient;

import com.cumulocity.sdk.client.SDKException;

public interface HttpClientProvider {
    HttpClient get() throws SDKException;
}
