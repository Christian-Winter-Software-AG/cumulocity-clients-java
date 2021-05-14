package com.cumulocity.sdk.client.proxy;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import org.assertj.core.condition.Join;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class RestProxyIT extends BaseProxyIT {
    @Test
    public void shouldSendRequestThroughNotAuthenticatedProxy() {
        // Given
        givenAuthenticatedProxyAndProxiedPlatform(null, null);

        // When
        List<ManagedObjectRepresentation> managedObjects = proxiedPlatform.getInventoryApi()
                .getManagedObjects()
                .get(5)
                .getManagedObjects();

        // Then
        assertNotNull(managedObjects);
        assertFalse(managedObjects.isEmpty());
    }

    @Test
    public void shouldSendRequestThroughAuthenticatedProxy() {
        // Given
        givenAuthenticatedProxyAndProxiedPlatform(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);

        // When
        List<ManagedObjectRepresentation> managedObjects = proxiedPlatform.getInventoryApi()
                .getManagedObjects()
                .get(5)
                .getManagedObjects();

        // Then
        assertNotNull(managedObjects);
        assertFalse(managedObjects.isEmpty());
    }

    @Test
    public void shouldFailToSendRequestWhenInvalidAuthentication() {
        // Given
        givenAuthenticatedProxy(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);
        givenAuthenticatedProxiedPlatform("invalid-user", "invalid-password");

        // When
        assertThatThrownBy(() -> proxiedPlatform.getInventoryApi()
                .getManagedObjects()
                .get(5)
                .getManagedObjects())
                // Then
                .isInstanceOf(SDKException.class)
                .extracting(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) {
                        return ((SDKException) throwable).getHttpStatus();
                    }
                })
                .isEqualTo(407);
    }
}
