package com.cumulocity.agent.server.repository;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.rest.representation.user.GroupRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.rest.representation.user.UserReferenceRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

@Component
public class GroupRepository {

    private static final String USER_URL = "user/{tenant}/";

    private final RestConnector restConnector;
    
    private final DeviceContextService contextService;

    private final String baseUrl;

    @Autowired
    public GroupRepository(RestConnector restConnector, @Value("${C8Y.baseURL}") String host, DeviceContextService contextService) {
        this.restConnector = restConnector;
        this.contextService = contextService;
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.baseUrl = host;
    }

    public GroupRepresentation findGroupByName(String name) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        String groupByNameURL = userApiWithTenantURL + "groupByName/";
        try {
            return restConnector.get(baseUrl + groupByNameURL + name, UserMediaType.GROUP, GroupRepresentation.class);
        } catch (SDKException ex) {
            if (ex.getHttpStatus() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw ex;
            }
        }
    }

    public GroupRepresentation create(GroupRepresentation groupRepresentation) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        String groupsURL = userApiWithTenantURL + "groups/";
        return restConnector.get(baseUrl + groupsURL, UserMediaType.GROUP, GroupRepresentation.class);
    }

    public void addUserToGroup(String username, Long id) {
        String userApiWithTenantURL = USER_URL.replace("{tenant}", contextService.getContext().getLogin().getTenant());
        String groupMembersURL = userApiWithTenantURL + "groups/" + id + "/users";
        UserReferenceRepresentation userReferenceRepresentation = new UserReferenceRepresentation();
        userReferenceRepresentation.setSelf("/users" + username);
        restConnector.post(baseUrl + groupMembersURL, UserMediaType.USER_REFERENCE, null);
    }
}
