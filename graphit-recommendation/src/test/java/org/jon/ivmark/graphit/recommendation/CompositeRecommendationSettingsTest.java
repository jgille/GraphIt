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

package org.jon.ivmark.graphit.recommendation;

import org.jon.ivmark.graphit.core.Json;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompositeRecommendationSettingsTest {

    @Test
    public void testReadFromJson() throws IOException {
        File file = ResourceUtils.resourceFile("fixtures/CompositeRecommendationSettings.json");
        CompositeRecommendationSettings compositeRecommendationSettings =
                Json.OBJECT_MAPPER.readValue(file, CompositeRecommendationSettings.class);

        assertThat(compositeRecommendationSettings, notNullValue());
        assertThat(compositeRecommendationSettings.getId(), is("test"));
        assertThat(compositeRecommendationSettings.getName(), is("Test settings"));
        assertThat(compositeRecommendationSettings.getMaxNumberOfRecommendedItems(), is(10));
        assertThat(compositeRecommendationSettings.getFallbackTo(), isA(FallbackSettings.class));
        assertThat(compositeRecommendationSettings.getFallbackTo().getId(), is("SomeFallback"));
        assertThat(compositeRecommendationSettings.getFilter(), notNullValue());
        assertThat(compositeRecommendationSettings.getRecommendationSettings(), notNullValue());
        assertThat(compositeRecommendationSettings.getRecommendationSettings().size(), is(2));
    }

}
