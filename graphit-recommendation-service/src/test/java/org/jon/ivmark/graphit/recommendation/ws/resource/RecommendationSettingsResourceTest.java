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
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.jon.ivmark.graphit.core.Json;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.recommendation.CompositeRecommendationSettings;
import org.jon.ivmark.graphit.recommendation.Named;
import org.jon.ivmark.graphit.recommendation.repository.RecommendationSettingsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationSettingsResourceTest extends ResourceTest {

    @Mock
    private RecommendationSettingsRepository repository;

    @Override
    protected void setUpResources() throws Exception {
        addResource(new RecommendationSettingsResource(repository));
    }

    @Test
    public void testSettingsForNonExistingId() {
        ClientResponse clientResponse = client().resource("/recommendation/settings/no_such_id").get(ClientResponse.class);
        assertThat(clientResponse.getStatus(), is(NOT_FOUND_404));
    }

    @Test
    public void testSettingsForExistingId() throws IOException {
        String settingsId = "sid";
        String settingsJson = settingsFixture();
        CompositeRecommendationSettings settings =
                Json.OBJECT_MAPPER.readValue(settingsJson, CompositeRecommendationSettings.class);
        when(repository.get(settingsId)).thenReturn(settings);
        String recommendationSettingsJson =
                client().resource("/recommendation/settings/" + settingsId).get(String.class);
        CompositeRecommendationSettings recommendationSettings =
                Json.OBJECT_MAPPER.readValue(recommendationSettingsJson, CompositeRecommendationSettings.class);
        assertThat(recommendationSettings, notNullValue());

        verify(repository).get(settingsId);
    }

    @Test
    public void testAllSettingsWhenNoneExist() throws IOException {
        String json = client().resource("/recommendation/settings").get(String.class);
        assertThat(json, notNullValue());
        List<RecommendationSettingsInfo> allSettings =
                Json.OBJECT_MAPPER.readValue(json, new TypeReference<List<RecommendationSettingsInfo>>() {});
        assertThat(allSettings, hasSize(0));
    }

    @Test
    public void testAllSettings() throws IOException {
        String id = "id";
        String name = "name";
        when(repository.allSettings()).thenReturn(Arrays.asList(new Named(id, name)));

        String json = client().resource("/recommendation/settings").get(String.class);
        assertThat(json, notNullValue());
        List<RecommendationSettingsInfo> allSettings =
                Json.OBJECT_MAPPER.readValue(json, new TypeReference<List<RecommendationSettingsInfo>>() {});
        assertThat(allSettings, hasSize(1));

        RecommendationSettingsInfo info = allSettings.get(0);
        assertThat(info.getName(), is(name));
        assertThat(info.getLocation(), notNullValue());
    }

    @Test
    public void testSaveSettings() throws IOException {
        String settingsJson = settingsFixture();
        String response = client().resource("/recommendation/settings/test")
                .header("Content-Type", APPLICATION_JSON_TYPE).put(String.class, settingsJson);
        assertThat(response, notNullValue());
        Location location = Json.OBJECT_MAPPER.readValue(response, Location.class);
        assertThat(location, notNullValue());

        verify(repository).save(Matchers.any(CompositeRecommendationSettings.class));
    }

    @Test
    public void testSaveSettingsForTheWrongId() throws IOException {
        String settingsJson = settingsFixture();
        ClientResponse response = client().resource("/recommendation/settings/someOtherId")
                .header("Content-Type", APPLICATION_JSON_TYPE).put(ClientResponse.class, settingsJson);
        assertThat(response.getStatus(), is(BAD_REQUEST_400));

        verify(repository, never()).save(Matchers.any(CompositeRecommendationSettings.class));
    }

    @Test
    public void testSaveInvalidSettings() throws IOException {
        String illegalJson = "What is this?";
        ClientResponse response = client().resource("/recommendation/settings/someOtherId")
                .header("Content-Type", APPLICATION_JSON_TYPE).put(ClientResponse.class, illegalJson);
        assertThat(response.getStatus(), is(BAD_REQUEST_400));

        verify(repository, never()).save(Matchers.any(CompositeRecommendationSettings.class));
    }

    private String settingsFixture() throws IOException {
        File file = ResourceUtils.resourceFile("fixtures/CompositeRecommendationSettings.json");
        return IOUtils.toString(file.toURI());
    }
}
