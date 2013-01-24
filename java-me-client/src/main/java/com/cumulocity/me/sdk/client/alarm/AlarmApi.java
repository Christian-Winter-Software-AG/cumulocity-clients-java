/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.alarm;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

/**
 * API for creating, updating and retrieving alarms from the platform.
 */
public interface AlarmApi {

    /**
     * Gets an alarm by id
     *
     * @param gid id of the alarm to search for
     * @return the alarm with the given id
     * @throws SDKException if the alarm is not found or if the query failed
     */
    AlarmRepresentation getAlarm(GId gid) throws SDKException;

    /**
     * Creates an alarm in the platform. The id of the alarm must not be set, since it will be generated by the platform
     *
     * @param alarm alarm to be created
     * @return the created alarm with the generated id
     * @throws SDKException if the alarm could not be created
     */
    AlarmRepresentation create(AlarmRepresentation alarm) throws SDKException;

    /**
     * Updates an alarm in the platform.
     * The alarm to be updated is identified by the id within the given alarm.
     *
     * @param alarm to be updated
     * @return the updated alarm
     * @throws SDKException if the alarm could not be updated
     */
    AlarmRepresentation updateAlarm(AlarmRepresentation alarm) throws SDKException;

    /**
     * Gets all alarms from the platform
     *
     * @return collection of alarms with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getAlarms() throws SDKException;

    /**
     * Gets alarms from the platform based on the specified filter
     *
     * @param filter the filter criteria(s)
     * @return collection of alarms matched by the filter with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getAlarmsByFilter(AlarmFilter filter) throws SDKException;
}
