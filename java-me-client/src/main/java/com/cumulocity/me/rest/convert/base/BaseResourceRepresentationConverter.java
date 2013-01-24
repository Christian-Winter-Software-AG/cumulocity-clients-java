package com.cumulocity.me.rest.convert.base;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.util.BeanUtils;

public abstract class BaseResourceRepresentationConverter extends BaseRepresentationConverter {
    
    public static final String PROP_ID = "id";
    public static final String PROP_SELF = "self";
    
    public JSONObject toJson(Object representation) {
        if (representation == null) {
            return null;
        }
        BaseCumulocityResourceRepresentation baseRepresentation = (BaseCumulocityResourceRepresentation) representation;
        JSONObject json = new JSONObject();
        basePropertiesToJson(baseRepresentation, json);
        instanceToJson(baseRepresentation, json);
        extraPropertiesToJson(baseRepresentation, json);
        return json;
    }
    
    protected abstract void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json);
    
    protected void basePropertiesToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putString(json, PROP_SELF, representation.getSelf());
    }
    
    protected void extraPropertiesToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
    }
    
    protected BaseCumulocityResourceRepresentation newRepresentation() {
        return (BaseCumulocityResourceRepresentation) BeanUtils.newInstance(supportedRepresentationType());
    }
    
    public Object fromJson(JSONObject json) {
        if (json == null) {
            return null;
        }
        BaseCumulocityResourceRepresentation representation = newRepresentation();
        basePropertiesFromJson(json, representation);
        instanceFromJson(json, representation);
        extraPropertiesFromJson(json, representation);
        return representation;
    }
    
    protected abstract void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation);

    protected void basePropertiesFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        representation.setSelf(getString(json, PROP_SELF));
    }
    
    protected void extraPropertiesFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
    }

    protected void putGId(JSONObject json, GId id) {
        putGId(json, PROP_ID, id);
    }

    protected GId getGId(JSONObject json) {
        return getGId(json, PROP_ID);
    }
    
    protected GId getGId(JSONObject json, String propertyName) {
        GId id = new GId();
        id.setValue(getString(json, propertyName));
        return id;
    }

    protected void putGId(JSONObject json, String propertyName, GId id) {
        if (id == null) {
            return;
        }
        json.put(propertyName, id.getValue());
    }
}
