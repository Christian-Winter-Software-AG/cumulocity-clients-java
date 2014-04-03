package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;

/**
 * Api for device bootstrap
 * 
 * @author dombiel
 *
 */
public interface DeviceCredentialsApi {
	
	/**
	 * Device register as connected
	 * 
	 * @param deviceId
	 */
	void hello(String deviceId);
	
	/**
	 * Device poll credentials
	 * 
	 * @param deviceId
	 * @param interval - how often request is sent in seconds
	 * @param timeout - after how many seconds polling process will expire
	 * 
	 */
	DeviceCredentialsRepresentation pollCredentials(String deviceId, int interval, int timeout);
	
}
