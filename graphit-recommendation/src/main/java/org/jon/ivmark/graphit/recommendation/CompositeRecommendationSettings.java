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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.core.properties.filter.CompositePropertyFilter;
import org.jon.ivmark.graphit.core.properties.filter.PropertyFilterSettings;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeRecommendationSettings {

    private final String id;
    private final String name;
    private final int maxNumberOfRecommendedItems;
    private final FallbackSettings fallbackTo;
    private final Map<String, Map<String, Object>> filterSettings;
    private final List<RecommendationSettings> recommendationSettings;
    private final Predicate<Properties> filter;

    public CompositeRecommendationSettings(@JsonProperty("id") String id,
                                           @JsonProperty("name") String name,
                                           @JsonProperty("max_number_of_recommended_items")
                                           int maxNumberOfRecommendedItems,
                                           @JsonProperty("fallback_to") FallbackSettings fallbackTo,
                                           @JsonProperty("filter") Map<String, Map<String, Object>> filterSettings,
                                           @JsonProperty("recommendation_settings")
                                           List<RecommendationSettings> recommendationSettings) {
        this.id = id;
        this.name = name;
        this.maxNumberOfRecommendedItems = maxNumberOfRecommendedItems;
        this.fallbackTo = fallbackTo;
        this.filterSettings = filterSettings == null ? null : new HashMap<String, Map<String, Object>>(filterSettings);
        this.filter = new CompositePropertyFilter(filterSettings);
        this.recommendationSettings = recommendationSettings;
        Preconditions.checkArgument(recommendationSettings != null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxNumberOfRecommendedItems() {
        return maxNumberOfRecommendedItems;
    }

    public FallbackSettings getFallbackTo() {
        return fallbackTo;
    }

    public Map<String, Map<String, Object>> getFilterSettings() {
        return Collections.unmodifiableMap(filterSettings);
    }

    @JsonIgnore
    public Predicate<Properties> filter() {
        return filter;
    }

    public List<RecommendationSettings> getRecommendationSettings() {
        return recommendationSettings;
    }
}