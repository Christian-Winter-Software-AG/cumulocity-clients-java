package com.cumulocity.me.agent.plugin.impl;

import com.cumulocity.me.agent.plugin.AgentInfo;
import com.cumulocity.me.agent.provider.MidletInformationProvider;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;

public class InternalAgentInfo extends AgentInfo{

    private String credentials;
    private SmartHttpConnection bootstrapConnection;

    public InternalAgentInfo() {
    }

    public InternalAgentInfo(String deviceId) {
        super(deviceId);
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials){
        this.credentials = credentials;
    }
    
    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    public void setBootstrapConnection(SmartHttpConnection bootstrapConnection) {
        this.bootstrapConnection = bootstrapConnection;
    }

    public SmartHttpConnection getBootstrapConnection() {
        return bootstrapConnection;
    }

    public void setMidletInfoProvider(MidletInformationProvider midletInfoProvider) {
        this.midletInformationProvider = midletInfoProvider;
    }

}
