/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.inventory;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class ManagedObjectReferenceCollectionImpl extends PagedCollectionResourceImpl implements PagedCollectionResource {

    public ManagedObjectReferenceCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    protected CumulocityMediaType getMediaType() {
        return InventoryMediaType.MANAGED_OBJECT_REFERENCE_COLLECTION;
    }

    protected Class getResponseClass() {
        return ManagedObjectReferenceCollectionRepresentation.class;
    }
}
