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

package org.jon.ivmark.graphit.recommendation.repository;

import org.jon.ivmark.graphit.recommendation.CompositeRecommendationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryRecommendationSettingsRepositoryTest {

    private static final String ID = "Test";
    @Mock
    private CompositeRecommendationSettings recommendationSettings;

    @Before
    public void initMock() {
        when(recommendationSettings.getId()).thenReturn(ID);
    }

    @Test
    public void testSave() {
        new InMemoryRecommendationSettingsRepository().save(recommendationSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveWithoutId() {
        when(recommendationSettings.getId()).thenReturn(null);
        new InMemoryRecommendationSettingsRepository().save(recommendationSettings);
    }

    @Test
    public void testGetMissing() {
        CompositeRecommendationSettings settings = new InMemoryRecommendationSettingsRepository().get("Any");
        assertThat(settings, nullValue());
    }

    @Test
    public void testSaveAndGet() {
        InMemoryRecommendationSettingsRepository repository = new InMemoryRecommendationSettingsRepository();
        repository.save(recommendationSettings);
        CompositeRecommendationSettings settings = repository.get(ID);
        assertThat(settings, is(recommendationSettings));
    }
}
