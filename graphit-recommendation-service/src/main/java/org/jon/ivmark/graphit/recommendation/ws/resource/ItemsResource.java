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

import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.jon.ivmark.graphit.recommendation.service.ItemBasedRecommendationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("recommendation/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemsResource extends BaseResource {

    private final ItemRepository itemRepository;
    private final ItemBasedRecommendationService recommendationService;

    public ItemsResource(ItemRepository itemRepository, ItemBasedRecommendationService recommendationService) {
        this.itemRepository = itemRepository;
        this.recommendationService = recommendationService;
    }

    @GET
    @Path("/{itemId}")
    public Response itemWithId(@PathParam("itemId") String itemId) {
        Item item = itemRepository.get(itemId);
        if (item == null) {
            return Response.status(NOT_FOUND).build();
        }
        String json = marshal(item);
        return Response.ok(json).build();
    }

    @GET
    @Path("/{itemId}/recommend/{settingsId}")
    public Response recommend(@PathParam("itemId") String itemId,
                              @PathParam("settingsId") String settingsId) {
        List<Item> items = recommendationService.recommendFor(itemId, settingsId);
        String json = marshal(items);
        return Response.ok(json).build();
    }
}
