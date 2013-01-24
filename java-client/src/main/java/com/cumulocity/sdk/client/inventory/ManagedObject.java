/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.inventory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.SDKException;

/**
 * Java Interface to call the Cumulocity Inventory Rest API.
 */
public interface ManagedObject {

    /**
     * Returns the Managed Object of the Resource.
     *
     * @return ManagedObjectRepresentation
     * @throws SDKException
     */
    public ManagedObjectRepresentation get() throws SDKException;

    /**
     * Deletes the Managed Object from the Cumulocity Server.
     *
     * @throws SDKException
     */
    public void delete() throws SDKException;

    /**
     * This update the ManagedObject for the operationCollection. Cannot update the ID.
     *
     * @param managedObjectRepresentation
     * @return ManagedObjectRepresentation updated ManagedObject.
     * @throws SDKException
     */
    public ManagedObjectRepresentation update(ManagedObjectRepresentation managedObjectRepresentation) throws SDKException;

    /**
     * Adds a child device to the ManagedObject.
     *
     * @param refrenceReprsentation
     * @return ManagedObjectReferenceRepresentation with the id of th child device.
     * @throws SDKException
     */
    public ManagedObjectReferenceRepresentation addChildDevice(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException;


    /**
     * Returns all the child Devices for the Managed Object in paged collection form.
     *
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child devices.
     * @throws SDKException
     */
    public PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> getChildDevices() throws SDKException;

    /**
     * Returns all the child devices for the Managed Object. Max number of child devices limited to Short.MAX_VALUE = 32767
     * This method is deprecated. Please use PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> getChildDevices() throws SDKException;
     *
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child devices.
     * @throws SDKException
     */
    @Deprecated()
    public ManagedObjectReferenceCollectionRepresentation getAllChildDevices() throws SDKException;

    /**
     * Returns the child device with the given id. If it belongs to the ManagedObject.
     *
     * @param deviceId
     * @return ManagedObjectReferenceRepresentation of the child device.
     * @throws SDKException
     */
    public ManagedObjectReferenceRepresentation getChildDevice(GId deviceId) throws SDKException;

    /**
     * Deletes the child device  and its relation to the managed object.
     *
     * @param deviceId
     * @throws SDKException
     */
    public void deleteChildDevice(GId deviceId) throws SDKException;

    /**
     * Adds a child device to the ManagedObject.
     *
     * @param refrenceReprsentation
     * @return ManagedObjectReferenceRepresentation with the id of th child device.
     * @throws SDKException
     */
    public ManagedObjectReferenceRepresentation addChildAssets(ManagedObjectReferenceRepresentation refrenceReprsentation)
            throws SDKException;

    /**
     * Returns all the child Assets for the Managed Object. Max number of child devices limited to Short.MAX_VALUE = 32767
     * This method is deprecated. Please use PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> getChildAssets() throws SDKException;
     *
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child devices.
     * @throws SDKException
     */
    @Deprecated
    public ManagedObjectReferenceCollectionRepresentation getAllChildAssets() throws SDKException;

    /**
     * Returns all the child Assets for the Managed Object  in paged collection form
     *
     * @return ManagedObjectReferenceCollectionRepresentation which contains all the child devices.
     * @throws SDKException
     */
    public PagedCollectionResource<ManagedObjectReferenceCollectionRepresentation> getChildAssets() throws SDKException;

    /**
     * Returns the child Asset with the given id. If it belongs to the ManagedObject.
     *
     * @param assetId
     * @return ManagedObjectReferenceRepresentation of the child device.
     * @throws SDKException
     */
    public ManagedObjectReferenceRepresentation getChildAsset(GId assetId) throws SDKException;

    /**
     * Deletes the child Asset  and its relation to the managed object.
     *
     * @param assetId
     * @throws SDKException
     */
    public void deleteChildAsset(GId assetId) throws SDKException;

}
