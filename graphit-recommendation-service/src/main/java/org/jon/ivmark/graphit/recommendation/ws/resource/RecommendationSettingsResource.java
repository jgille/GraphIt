/*
 * Copyright 2012 Jon Ivmark
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jon.ivmark.graphit.recommendation.ws.resource;

import org.jon.ivmark.graphit.recommendation.CompositeRecommendationSettings;
import org.jon.ivmark.graphit.recommendation.Named;
import org.jon.ivmark.graphit.recommendation.repository.RecommendationSettingsRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("recommendation/settings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationSettingsResource extends BaseResource {

    private final RecommendationSettingsRepository repository;

    public RecommendationSettingsResource(RecommendationSettingsRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("/{id}")
    public Response settingsForId(@PathParam("id") String recommendationSettingsId) {
        CompositeRecommendationSettings settings = repository.get(recommendationSettingsId);
        if (settings == null) {
            return Response.status(NOT_FOUND).build();
        }
        String settingsJson = marshal(settings);
        return Response.ok(settingsJson).build();
    }

    @GET
    public Response allSettings(@Context UriInfo uriInfo) {
        List<Named> settings = repository.allSettings();
        List<RecommendationSettingsInfo> result = new ArrayList<RecommendationSettingsInfo>(settings.size());
        for (Named named : settings) {
            String id = named.getId();
            URI uri = uriInfo.getBaseUriBuilder().path(getClass()).path(getClass(), "settingsForId").build(id);

            result.add(new RecommendationSettingsInfo(named.getName(), new Location("recommendation-settings", uri.toString())));
        }
        String json = marshal(result);
        return Response.ok(json).build();
    }

    @PUT
    @Path("/{id}")
    public Response saveSettings(@PathParam("id") String recommendationSettingsId,
                                 String settingsJson, @Context UriInfo uriInfo) {
        CompositeRecommendationSettings settings = unmarshal(settingsJson, CompositeRecommendationSettings.class);

        if (!recommendationSettingsId.equals(settings.getId())) {
            return Response.status(BAD_REQUEST).build();
        }
        repository.save(settings);
        URI uri = uriInfo.getBaseUriBuilder().path(getClass()).path(getClass(), "settingsForId").build(settings.getId());
        Location location = new Location("recommendation-settings", uri.toString());
        return Response.ok(location).location(uri).build();
    }
}
