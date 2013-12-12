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

package org.jon.ivmark.graphit.recommendation.service;

import com.google.common.base.Predicates;
import org.hamcrest.Matchers;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.recommendation.CompositeRecommendationSettings;
import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.repository.Fallbacks;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.jon.ivmark.graphit.recommendation.repository.RecommendationSettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemBasedRecommendationServiceTest {

    @Mock
    private ItemBasedRecommender recommender;

    @Mock
    private RecommendationSettingsRepository settings;

    @Mock
    private ItemRepository items;

    @Mock
    private Fallbacks fallbacks;

    @Mock
    private CompositeRecommendationSettings recommendationSettings;

    private ItemBasedRecommendationService recommendationService;

    @Before
    public void init() {
        this.recommendationService = new ItemBasedRecommendationService(recommender, settings, items, fallbacks);
    }

    @Test
    public void testEmptyRecommendationWithoutFallback() {
        when(settings.get("settings")).thenReturn(recommendationSettings);
        when(recommendationSettings.getMaxNumberOfRecommendedItems()).thenReturn(5);
        when(recommendationSettings.filter()).thenReturn(Predicates.<Properties>alwaysTrue());
        List<Item> recommended = recommendationService.recommendFor("item", "settings");
        assertThat(recommended, empty());
    }
}
