/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.page;


import com.cumulocity.me.lang.Collections;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.GenericResourceImpl;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.util.StringUtils;

public abstract class PagedCollectionResourceImpl extends GenericResourceImpl implements PagedCollectionResource {
    
    private static final String PAGE_SIZE_KEY = "pageSize";
    private static final String PAGE_NUMBER_KEY = "currentPage";

    private int pageSize;

    public PagedCollectionResourceImpl(RestConnector restConnector, String url) {
        this(restConnector, url, DEFAULT_PAGE_SIZE);
    }
    
    public PagedCollectionResourceImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url);
        this.pageSize = pageSize;
    }

    public Object getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) {
        if (collectionRepresentation == null || collectionRepresentation.getSelf() == null) {
            throw new SDKException("Unable to determin the Resource URL. ");
        }

        Map params = new HashMap();
        params.put(PAGE_SIZE_KEY, String.valueOf(pageSize));
        params.put(PAGE_NUMBER_KEY, String.valueOf(pageNumber));

        String url = replaceOrAddQueryParam(collectionRepresentation.getSelf(), params);
        return getCollection(url);
    }

    private Object getCollection(String url) {
        return restConnector.get(url, getMediaType(), getResponseClass());
    }

    public Object getNextPage(BaseCollectionRepresentation collectionRepresentation) {
        if (collectionRepresentation == null) {
            throw new SDKException("Unable to determin the Resource URL. ");
        }
        if (collectionRepresentation.getNext() == null) {
            return null;
        }
        return getCollection(collectionRepresentation.getNext());
    }

    public Object getPreviousPage(BaseCollectionRepresentation collectionRepresentation) {
        if (collectionRepresentation == null) {
            throw new SDKException("Unable to determin the Resource URL. ");
        }
        if (collectionRepresentation.getPrev() == null) {
            return null;
        }
        return getCollection(collectionRepresentation.getPrev());
    }

    public CumulocityResourceRepresentation get() {
        String urlToCall = replaceOrAddQueryParam(url, Collections.singletonMap(PAGE_SIZE_KEY, String.valueOf(pageSize)));
        return restConnector.get(urlToCall, getMediaType(), getResponseClass());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PagedCollectionResourceImpl)) {
            return false;
        }

        try {
            PagedCollectionResourceImpl another = (PagedCollectionResourceImpl) obj;
            return getMediaType().equals(another.getMediaType()) && getResponseClass().equals(another.getResponseClass())
                    && pageSize == another.pageSize && url.equals(another.url);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int hashCode() {
        return getMediaType().hashCode() ^ getResponseClass().hashCode() ^ url.hashCode() ^ pageSize;
    }

    private String replaceOrAddQueryParam(String url, Map newParams) {
        String[] urlParts = StringUtils.split(url, "\\?");
        String partOfUrlWithoutQueryParams = urlParts[0];
        String queryParamsString = (urlParts.length == 2) ? urlParts[1] : "";

        Map queryParams = parseQueryParams(queryParamsString);
        queryParams.putAll(newParams);
        return buildUrl(partOfUrlWithoutQueryParams, queryParams);
    }

    private String buildUrl(String urlBeginning, Map queryParams) {
        StringBuffer builder = new StringBuffer(urlBeginning);
        if (queryParams.size() > 0) {
            builder.append("?"); //add ? only if query params exist
            boolean addingfirstPair = true; //this var is used only to make sure & after last pair is not added
            Iterator iterator = queryParams.entrySet().iterator();
            while (iterator.hasNext()) {
            	Map.Entry entry = (Map.Entry) iterator.next();

            	if (!addingfirstPair) {
                    builder.append("&");
                }
                builder.append(entry.getKey()).append("=").append(entry.getValue());
                addingfirstPair = false;
            }
        }

        return builder.toString();
    }

    private Map parseQueryParams(String queryParams) {
        Map result = new HashMap();
        String[] pairs = StringUtils.split(queryParams, "&");
        for (int i = 0; i < pairs.length; i++) {
        	String pair = pairs[i];
            String[] items = StringUtils.split(pair, "=");
            if (items.length == 2) {
                result.put(items[0], items[1]);
            }
        }

        return result;
    }
}
