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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.jon.ivmark.graphit.core.graph.traversal.Traversable;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.recommendation.*;
import org.jon.ivmark.graphit.recommendation.repository.Fallbacks;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.jon.ivmark.graphit.recommendation.repository.RecommendationSettingsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemBasedRecommendationService {

    private final ItemBasedRecommender recommender;
    private final RecommendationSettingsRepository settings;
    private final ItemRepository items;
    private final Fallbacks fallbacks;

    public ItemBasedRecommendationService(ItemBasedRecommender recommender,
                                          RecommendationSettingsRepository settings,
                                          ItemRepository items,
                                          Fallbacks fallbacks) {
        this.recommender = recommender;
        this.settings = settings;
        this.items = items;
        this.fallbacks = fallbacks;
    }

    public List<Item> recommendFor(String itemId, String recommendationSettingsId) {
        return recommendFor(itemId, settingsFor(recommendationSettingsId));
    }

    private List<Item> recommendFor(String itemId, CompositeRecommendationSettings recommendationSettings) {
        int maxNumberOfRecommendedItems = recommendationSettings.getMaxNumberOfRecommendedItems();
        List<Item> result = new ArrayList<Item>(maxNumberOfRecommendedItems);

        Predicate<Properties> commonFilter = recommendationSettings.filter();

        for (RecommendationSettings rs : recommendationSettings.getRecommendationSettings()) {
            if (result.size() == maxNumberOfRecommendedItems) {
                break;
            }

            Predicate<Properties> filter = Predicates.and(commonFilter, rs.filter());
            int numItems = Math.min(maxNumberOfRecommendedItems - result.size(), rs.getMaxNumberOfRecommendedItems());
            Recommendation recommendation =
                    recommender.recommend(itemId, rs.getSimilarityType()).filter(filter).limit(numItems);
            addRecommendedItems(result, recommendation);
        }

        Iterable<Item> fallback = getFallback(recommendationSettings, maxNumberOfRecommendedItems - result.size());
        addRecommendedItems(result, fallback);
        return result;
    }

    private Iterable<Item> getFallback(CompositeRecommendationSettings recommendationSettings, int numItems) {
        if (numItems == 0) {
            return Collections.emptyList();
        }

        Predicate<Properties> commonFilter = recommendationSettings.filter();

        FallbackSettings fallbackTo = recommendationSettings.getFallbackTo();
        Fallback fallback = fallback(fallbackTo);
        final Predicate<Properties> fallbackFilter = Predicates.and(commonFilter, fallbackFilter(fallbackTo));

        return new Traversable<String>(fallback).transform(new Function<String, Item>() {
            @Override
            public Item apply(String itemId) {
                return items.get(itemId);
            }
        }).filter(new Predicate<Item>() {
            @Override
            public boolean apply(Item item) {
                return fallbackFilter.apply(new ImmutableItemProperties(item));
            }
        }).head(numItems);
    }

    private Predicate<Properties> fallbackFilter(FallbackSettings fallbackTo) {
        return fallbackTo == null ? Predicates.<Properties>alwaysTrue() : fallbackTo.filter();
    }

    private Fallback fallback(FallbackSettings fallbackTo) {
        if (fallbackTo == null) {
            return new Fallback(Collections.<String>emptyList());
        }
        return fallbacks.fallback(fallbackTo.getId());
    }

    private void addRecommendedItems(List<Item> items, Iterable<Item> recommended) {
        for (Item item : recommended) {
            items.add(item);
        }
    }

    private CompositeRecommendationSettings settingsFor(String recommendationSettingsId) {
        CompositeRecommendationSettings recommendationSettings = settings.get(recommendationSettingsId);
        if (recommendationSettings == null) {
            throw new IllegalArgumentException(String.format("No such settings: '%s'", recommendationSettingsId));
        }
        return recommendationSettings;
    }

}
