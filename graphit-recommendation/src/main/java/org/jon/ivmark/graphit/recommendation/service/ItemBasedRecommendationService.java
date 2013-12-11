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

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.recommendation.CompositeRecommendationSettings;
import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.repository.RecommendationSettingsRepository;

import java.util.List;

public class ItemBasedRecommendationService {

    private final ItemBasedRecommender recommender;
    private final RecommendationSettingsRepository settingsRepository;

    public ItemBasedRecommendationService(ItemBasedRecommender recommender, RecommendationSettingsRepository settingsRepository) {
        this.recommender = recommender;
        this.settingsRepository = settingsRepository;
    }

    public List<Item> recommendFor(String itemId, CompositeRecommendationSettings recommendationSettings) {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    public List<Item> recommendFor(String itemId, String recommendationSettingsId) {
        return recommendFor(itemId, settingsFor(recommendationSettingsId));
    }

    private CompositeRecommendationSettings settingsFor(String recommendationSettingsId) {
        CompositeRecommendationSettings settings = settingsRepository.get(recommendationSettingsId);
        if (settings == null) {
            throw new IllegalArgumentException(String.format("No such settings: '%s'", recommendationSettingsId));
        }
        return settings;
    }

}
