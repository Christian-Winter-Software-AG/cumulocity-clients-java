/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryURLBuilder {

    private final Map<String, String> filters;

    private final List<String> uriTemplates;

    private final String[] optionalParameters;

    private final TemplateUrlParser templateUrlParser;

    public QueryURLBuilder(TemplateUrlParser templateUrlParser, Map<String, String> filters, List<String> uriTemplates,
            String[] optionalParameters) {
        this.filters = filters;
        this.uriTemplates = uriTemplates;
        this.optionalParameters = optionalParameters;
        this.templateUrlParser = templateUrlParser;
    }

    public String build() {
        String queryUri = findQueryURI(uriTemplates, filters.keySet());
        String url = null;
        if (null != queryUri) {
            url = templateUrlParser.replacePlaceholdersWithParams(queryUri, filters);
        }
        return url;
    }

    private String findQueryURI(List<String> uriTemplates, Set<String> parameters) {
        for (String uri : uriTemplates) {
            List<String> queryParams = getQueryParams(uri);

            List<String> queryDelta = findDelta(parameters, queryParams);
            List<String> paramDelta = findDelta(queryParams, parameters);

            // if no extra parameters found then both are matching
            if (queryDelta.size() == 0 && paramDelta.size() == 0) {
                return uri;
            }

            // if more parameters then definitely not matching
            if (paramDelta.size() > 0) {
                continue;
            }

            // if query has more parameters and the delta is optional we can use this uri.
            if (queryDelta.size() == getOptionalCount(queryDelta)) {
                return removeOptionals(uri, queryDelta);
            }
        }
        return null;
    }

    private String removeOptionals(String uri, List<String> optionals) {
        for (String param : optionals) {
            uri = uri.replaceAll(param + "=\\{" + param + "\\}", "");
        }
        // remove duplicate &
        uri = uri.replaceAll("&&", "&");
        uri = uri.replaceAll("\\?&", "\\?");
        if (uri.endsWith("&")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        return uri;
    }

    private List<String> findDelta(Collection<String> in, Collection<String> from) {
        List<String> delta = new ArrayList<String>();
        for (String p : from) {
            boolean found = false;
            for (String i : in) {
                if (p.equals(i)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                delta.add(p);
            }
        }
        return delta;
    }

    private int getOptionalCount(Collection<String> parameters) {
        int count = 0;
        for (String param : parameters) {
            if (isOptional(param)) {
                count++;
            }
        }
        return count;
    }

    private List<String> getQueryParams(String queryUri) {
        String tUri = queryUri.substring(queryUri.indexOf('?') + 1);
        String[] uriParams = tUri.split("&");
        List<String> queryParams = new ArrayList<String>();
        for (String up : uriParams) {
            String[] params = up.split("=");
            queryParams.add(params[0]);
        }
        return queryParams;
    }

    private boolean isOptional(String param) {
        boolean isOptional = false;
        for (String optional : optionalParameters) {
            if (param.equals(optional)) {
                isOptional = true;
                break;
            }
        }
        return isOptional;
    }
}
