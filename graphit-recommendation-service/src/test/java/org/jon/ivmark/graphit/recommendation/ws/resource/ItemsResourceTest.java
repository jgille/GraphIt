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

import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.testing.ResourceTest;
import org.jon.ivmark.graphit.core.Json;
import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.jon.ivmark.graphit.recommendation.service.ItemBasedRecommendationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemsResourceTest extends ResourceTest {

    @Mock
    private ItemBasedRecommendationService recommendationService;

    @Mock
    private ItemRepository itemRepository;

    @Override
    protected void setUpResources() throws Exception {
        addResource(new ItemsResource(itemRepository, recommendationService));
    }

    @Test
    public void testGetItem() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("someKey", 3);
        String id = "myItem";
        Item expectedItem = new Item(id, properties);
        when(itemRepository.get(id)).thenReturn(expectedItem);

        String itemJson = client().resource("/recommendation/items/" + id).get(String.class);
        assertThat(itemJson, notNullValue());

        Item item = Json.OBJECT_MAPPER.readValue(itemJson, Item.class);
        assertThat(item, notNullValue());

        assertThat(item.getItemId(), is(id));
        assertThat(item.getProperties(), is(properties));
    }

    @Test
    public void testGetMissingItem() throws Exception {
        String id = "noSuchItem";

        ClientResponse clientResponse = client().resource("/recommendation/items/" + id).get(ClientResponse.class);
        assertThat(clientResponse, notNullValue());
        assertThat(clientResponse.getStatus(), is(NOT_FOUND_404));
    }

    @Test
    public void testRecommend() {
        String itemId = "source";
        String settingsId = "settings";
        List<Item> expected = Arrays.asList(new Item(itemId, new HashMap<String, Object>()));
        when(recommendationService.recommendFor(itemId, settingsId)).thenReturn(expected);

        ClientResponse clientResponse =
                client().resource("/recommendation/items/" + itemId + "/recommend/" + settingsId).get(ClientResponse.class);
        assertThat(clientResponse, notNullValue());
        assertThat(clientResponse.getStatus(), is(OK_200));
        List<?> items = clientResponse.getEntity(List.class);

        assertThat(items, hasSize(1));

        verify(recommendationService).recommendFor(itemId, settingsId);
    }
}
