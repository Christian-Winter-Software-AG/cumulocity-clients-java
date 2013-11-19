/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client;

import static com.cumulocity.rest.pagination.RestPageRequest.DEFAULT_PAGE_SIZE;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.PageStatisticsRepresentation;

public class EmptyPagedCollectionResource<T extends BaseCollectionRepresentation> implements PagedCollectionResource<T> {

    private final Class<T> type;

    public EmptyPagedCollectionResource(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() throws SDKException {
        return get(DEFAULT_PAGE_SIZE);
    }

    @Override
    public T get(int pageSize) throws SDKException {
        T collectionRepresentaton = newCollectionRepresentationInstance();
        collectionRepresentaton.setPageStatistics(new PageStatisticsRepresentation());
        return collectionRepresentaton;
    }

    @Override
    public T get(QueryParam... queryParams) throws SDKException {
        return get();
    }

    @Override
    public T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) throws SDKException {
        return getPage(null, pageNumber, DEFAULT_PAGE_SIZE);
    }

    @Override
    public T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber, int pageSize) throws SDKException {
        return pageNumber == 1 ? get(pageSize) : null;
    }

    @Override
    public T getNextPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
        return null;
    }

    @Override
    public T getPreviousPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
        return null;
    }

    private T newCollectionRepresentationInstance() throws SDKException {
        try {
            return type.newInstance();
        } catch (Exception ex) {
            throw new SDKException("internal error", ex);
        }
    }
}
