/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmApiImpl;
import com.cumulocity.sdk.client.audit.AuditRecordApi;
import com.cumulocity.sdk.client.audit.AuditRecordApiImpl;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApiImpl;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.event.EventApiImpl;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.identity.IdentityApiImpl;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.InventoryApiImpl;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.measurement.MeasurementApiImpl;

public class PlatformImpl extends PlatformParameters implements Platform {

    private static final String PLATFORM_URL = "platform";

    public static final String CLIENT_PARAMETERS = "platformParameters";

    public static final String CUMULOCITY_PAGE_SIZE = "cumulocityPageSize";

    public static final String CUMULOCITY_PASSWORD = "cumulocityPassword";

    public static final String CUMULOCITY_USER = "cumulocityUser";

    public static final String CUMULOCITY_TENANT = "cumulocityTenant";

    public static final String CUMULOCITY_PORT = "cumulocityPort";

    public static final String CUMOLOCITY_HOST = "cumolocityHost";

    public static final String CUMOLOCITY_APPLICATION_KEY = "applicationKey";

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(PlatformImpl.class);

    public static final String CUMOLOCITY_PROXY_HOST = "proxyHost";

    public static final String CUMULOCITY_PROXY_PORT = "proxyPort";

    public static final String CUMULOCITY_PROXY_USER = "proxyUser";

    public static final String CUMULOCITY_PROXY_PASSWORD = "proxyPassword";

    public PlatformImpl(String host, String tenantId, String user, String password, String applicationKey) {
        super(host, tenantId, user, password, applicationKey);
    }

    public PlatformImpl(String host, int port, String tenantId, String user, String password, String applicationKey) {
        super(getHostUrl(host, port), tenantId, user, password, applicationKey);
    }

    public PlatformImpl(String host, String tenantId, String user, String password, String applicationKey, int pageSize) {
        super(host, tenantId, user, password, applicationKey, pageSize);
    }

    public PlatformImpl(String host, int port, String tenantId, String user, String password, String applicationKey, int pageSize) {
        super(getHostUrl(host, port), tenantId, user, password, applicationKey, pageSize);
    }

    public PlatformImpl() {
        //empty constructor for spring based initialization
    }

    private static String getHostUrl(String host, int port) {
        return "http://" + host + ":" + port;
    }

    /**
     * This static method creates the Platform from the system parameters.
     * <p/>
     * System Properties
     * cumolocityHost     : ip address or name of the Cumulocity server
     * cumulocityPort     : port number of the Cumulocity server;
     * cumulocityTenant   : Tenant ID ;
     * cumulocityUser     : User ID ;
     * cumulocityPassword : Password ;
     * cumulocityPageSize : Page size for the paging parameters.
     * <p/>
     * proxyHost          : Proxy Host Name;
     * proxyPort          : Proxy Port Name
     * proxyUser          : Proxy User Name
     * proxyPassword      : Proxy Passowrd
     *
     * @return Platform for the handle to get other methods.
     * @throws SDKException
     */

    public static Platform createPlatform() throws SDKException {
        PlatformImpl platform = null;
        try {
            String host = System.getProperty(CUMOLOCITY_HOST);
            int port = Integer.parseInt(System.getProperty(CUMULOCITY_PORT));
            String tenantId = System.getProperty(CUMULOCITY_TENANT);
            String user = System.getProperty(CUMULOCITY_USER);
            String password = System.getProperty(CUMULOCITY_PASSWORD);
            String applicationKey = System.getProperty(CUMOLOCITY_APPLICATION_KEY);
            if (host == null || tenantId == null || user == null || password == null) {
                throw new SDKException("Cannot Create Platform as Mandatory Param are not set");
            }
            if (System.getProperty(CUMULOCITY_PAGE_SIZE) != null) {
                int pageSize = Integer.parseInt(System.getProperty(CUMULOCITY_PAGE_SIZE));
                platform = new PlatformImpl(host, port, tenantId, user, password, applicationKey, pageSize);
            } else {
                platform = new PlatformImpl(host, port, tenantId, user, password, applicationKey);
            }
            String proxyHost = System.getProperty(CUMOLOCITY_PROXY_HOST);
            int proxyPort = -1;
            if (System.getProperty(CUMULOCITY_PROXY_PORT) != null) {
                proxyPort = Integer.parseInt(System.getProperty(CUMULOCITY_PROXY_PORT));
            }
            String proxyUser = System.getProperty(CUMULOCITY_PROXY_USER);
            String proxyPassword = System.getProperty(CUMULOCITY_PROXY_PASSWORD);

            if (proxyHost != null && proxyPort > 0) {
                platform.setProxyHost(proxyHost);
                platform.setProxyPort(proxyPort);
            }

            if (proxyUser != null && proxyPassword != null) {
                platform.setProxyUserId(proxyUser);
                platform.setProxyPassword(proxyPassword);
            }
        } catch (NumberFormatException e) {
            throw new SDKException("Invalid Number :" + e.getMessage());
        }

        return platform;
    }

    @Override
    public InventoryApi getInventoryApi() {
        return new InventoryApiImpl(createRestConnector(), new TemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    @Override
    public IdentityApi getIdentityApi() {
        return new IdentityApiImpl(createRestConnector(), new TemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    @Override
    public MeasurementApi getMeasurementApi() {
        return new MeasurementApiImpl(createRestConnector(), new TemplateUrlParser(),getHost() + PLATFORM_URL, getPageSize());
      }

    @Override
    public DeviceControlApi getDeviceControlApi() {
        return new DeviceControlApiImpl(createRestConnector(), new TemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    @Override
    public EventApi getEventApi() {
        return new EventApiImpl(createRestConnector(), new TemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    @Override
    public AlarmApi getAlarmApi() {
        return new AlarmApiImpl(createRestConnector(), new TemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    @Override
    public AuditRecordApi getAuditRecordApi() {
        return new AuditRecordApiImpl(createRestConnector(), new TemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    private RestConnector createRestConnector() {
        return new RestConnector(this, new ResponseParser());
    }

}
