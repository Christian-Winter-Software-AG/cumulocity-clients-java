package com.cumulocity.agent.server.repository;

import static com.cumulocity.agent.server.repository.ManagedObjects.fromManagedObjectReference;
import static com.google.common.collect.FluentIterable.from;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.InventoryFilter;
import com.google.common.collect.Lists;

@Component
public class InventoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRepository.class);

    private final InventoryApi inventoryApi;

    private final IdentityRepository identityRepository;

    @Autowired
    public InventoryRepository(InventoryApi inventoryApi, IdentityRepository identityRepository) {
        this.inventoryApi = inventoryApi;
        this.identityRepository = identityRepository;
    }

    public ManagedObjectRepresentation findByExternalId(ID externalID) {
        return findById(identityRepository.find(externalID));
    }

    public ManagedObjectRepresentation findById(GId id) {
        return inventoryApi.get(id);
    }

    public Iterable<ManagedObjectRepresentation> findAllChildDevices(ManagedObjectRepresentation managedObjectRepresentation) {
        return findAllByFilter(search().byIds(Lists.newLinkedList(asIds(asManagedObjects(managedObjectRepresentation.getChildDevices())))));
    }

    private Iterable<ManagedObjectRepresentation> asManagedObjects(final Iterable<ManagedObjectReferenceRepresentation> references) {
        return from(references).transform(fromManagedObjectReference());
    }

    private InventoryFilter search() {
        return new InventoryFilter();
    }

    private Iterable<GId> asIds(Iterable<ManagedObjectRepresentation> mos) {
        return from(mos).transform(ManagedObjects.getId());
    }

    public Iterable<ManagedObjectRepresentation> findAllByFilter(InventoryFilter filter) {
        return inventoryApi.getManagedObjectsByFilter(filter).get().allPages();
    }

    public ManagedObjectRepresentation save(ManagedObjectRepresentation managedObjectRepresentation) {
        logger.debug("Save managed object: {}.", managedObjectRepresentation);
        if (managedObjectRepresentation.getId() == null) {
            return inventoryApi.create(managedObjectRepresentation);
        } else {
            return inventoryApi.update(managedObjectRepresentation);
        }
    }

    public ManagedObjectRepresentation save(ManagedObjectRepresentation managedObjectRepresentation, ID... ids) {
        managedObjectRepresentation = save(managedObjectRepresentation);
        for (ID id : ids) {
            identityRepository.save(managedObjectRepresentation.getId(), id);
        }
        return managedObjectRepresentation;
    }

    public void bindToParent(GId parentId, GId childId) {
        if (parentId == null) {
            return;
        }
        logger.debug("Bind child device: {} to parent {}.", childId, parentId);
        inventoryApi.getManagedObjectApi(parentId).addChildDevice(childId);
    }

}
