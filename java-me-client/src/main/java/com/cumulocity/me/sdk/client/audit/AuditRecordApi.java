/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.audit;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

/**
 * API for creating and retrieving audit records from the platform
 */
public interface AuditRecordApi {

    /**
     * Gets an audit record by id
     *
     * @param gid id of the audit record to search for
     * @return the audit record with the given id
     * @throws SDKException if the audit record is not found
     */
    AuditRecordRepresentation getAuditRecord(GId gid) throws SDKException;

    /**
     * Creates an audit record in the platform. The id of the audit record must not be set, since it will be generated by the platform
     *
     * @param auditRecord the audit record to be created
     * @return the created audit record with the generated id
     * @throws SDKException if the audit record could not be generated
     */
    AuditRecordRepresentation create(AuditRecordRepresentation auditRecord) throws SDKException;

    /**
     * Gets all audit records from the platform
     *
     * @return collection of audit records with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getAuditRecords() throws SDKException;

    /**
     * Gets audit records from the platform based on the specified filter
     *
     * @param filter the filter criteria(s)
     * @return collection of audit records matched by the filter with paging functionality
     * @throws SDKException if the query failed
     */
    PagedCollectionResource getAuditRecordsByFilter(AuditRecordFilter filter) throws SDKException;
}
