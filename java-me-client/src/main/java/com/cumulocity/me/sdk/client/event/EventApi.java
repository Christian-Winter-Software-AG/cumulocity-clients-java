/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.event;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.event.EventRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

/**
 * API for creating, deleting and retrieving events from the platform.
 */
public interface EventApi {

    /**
     * Gets event by id
     *
     * @param gid id of the event to search for
     * @return the event with the given id
     * @throws SDKException if the event is not found or if the query failed
     */
    EventRepresentation getEvent(GId gid) throws SDKException;

    /**
     * Creates event in the platform. The id of the event must not be set, since it will be generated by the platform
     *
     * @param event event to be created
     * @return the created event with the generated id
     * @throws SDKException if the event could not be created
     */
    EventRepresentation create(EventRepresentation event) throws SDKException;

    /**
     * Deletes event from the platform.
     * The event to be deleted is identified by the id within the given event.
     *
     * @param event to be deleted
     * @throws SDKException if the event could not be deleted
     */
    void delete(EventRepresentation event) throws SDKException;

    /**
     * Gets the all the event in the platform
     *
     * @return collection of events with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getEvents() throws SDKException;

    /**
     * Gets the events from the platform based on specified filter
     *
     * @param filter the filter criteria(s)
     * @return collection of events matched by the filter with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getEventsByFilter(EventFilter filter) throws SDKException;
}
