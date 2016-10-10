/*
 * Copyright (C) 2016. TomTom International BV. All rights reserved.
 */

package com.tomtom.services.configuration;

import com.tomtom.services.configuration.dto.SearchResultsDTO;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * This class defines the methods to query the configurations search tree.
 */
@Path("/tree")
public interface TreeResource {

    public static final String PATH_PARAM = "path";
    public static final String QUERY_PARAM_LEVELS = "levels";
    public static final String QUERY_PARAM_SEARCH = "search";
    public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";

    public static final char SEPARATOR_PATH = '/';
    public static final char SEPARATOR_QUERY = ',';
    public static final char SEPARATOR_WRONG = ';';

    /**
     * Try to match a search query string, which is formatted as a path using separators, with the
     * search tree and return the deepest level parameters found in the tree.
     *
     * Return codes:
     * 200 - Match (exact or partial) found.
     * 304 - Not newer than If-Modified-Since time or no different ETag than If-None-Match.
     * 404 - No match found.
     *
     * The return message has format {@link SearchResultsDTO} and specifies the full
     * path of the deepest node matching the search path.
     *
     * Important: as the URI overlaps with the "getNode" URI for the root node, this method actually
     * returns the root node of the search tree, if the 'search' query parameter was omitted.
     *
     * @param levels          Order of search criteria in URI.
     * @param search          Search criteria values, in specified order.
     * @param ifModifiedSince Return parameters only if the configuration is newer than this.
     * @param ifNoneMatch     Return parameters only if the ETag of the response is different from the supplied ETag.
     * @param uriInfo         URI info incl. parameters.
     * @param response        Deepest level parameters, format {@link SearchResultsDTO}.
     */
    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void findBestMatch(
            @Nonnull @QueryParam(QUERY_PARAM_LEVELS) String levels,
            @Nonnull @QueryParam(QUERY_PARAM_SEARCH) String search,
            @Nullable @HeaderParam(HEADER_IF_MODIFIED_SINCE) String ifModifiedSince,
            @Nullable @HeaderParam(HEADER_IF_NONE_MATCH) String ifNoneMatch,
            @Nonnull @Context UriInfo uriInfo,
            @Suspended @Nonnull AsyncResponse response);

    /**
     * Get a specific node from the search tree, given a search path.
     *
     * Return codes:
     * 200 - Exact match found.
     * 304 - Not newer than If-Modified-Since time or no different ETag than If-None-Match.
     * 404 - No match found.
     *
     * @param fullNodePath    Full path of node.
     * @param levels          Only provided to make sure it's null.
     * @param search          Only provided to make sure it's null.
     * @param ifModifiedSince Return parameters only if the configuration is newer than this.
     * @param ifNoneMatch     Return parameters only if the ETag of the response is different from the supplied ETag.
     * @param uriInfo         URI info incl. parameters.
     * @param response        Deepest level parameters, format {@link SearchResultsDTO}.
     */
    @GET
    @Path('{' + PATH_PARAM + ": [^?]*}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void getNode(
            @Nonnull @PathParam(PATH_PARAM) String fullNodePath,
            @Nonnull @QueryParam(QUERY_PARAM_LEVELS) String levels,
            @Nonnull @QueryParam(QUERY_PARAM_SEARCH) String search,
            @Nullable @HeaderParam(HEADER_IF_MODIFIED_SINCE) String ifModifiedSince,
            @Nullable @HeaderParam(HEADER_IF_NONE_MATCH) String ifNoneMatch,
            @Nonnull @Context UriInfo uriInfo,
            @Suspended @Nonnull AsyncResponse response);
}
