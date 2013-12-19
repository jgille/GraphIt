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

import com.google.common.base.Predicate;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.core.properties.filter.PropertiesFilter;
import org.jon.ivmark.graphit.core.properties.filter.PropertyFilterSettings;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationSettings {

    private final String similarityType;
    private final int maxNumberOfRecommendedItems;
    private final List<PropertyFilterSettings> filterSettings;
    private final Predicate<Properties> filter;

    public RecommendationSettings(@JsonProperty("similarity_type") String similarityType,
                                  @JsonProperty("max_number_of_recommended_items") int maxNumberOfRecommendedItems,
                                  @JsonProperty("filter") List<PropertyFilterSettings> filterSettings) {
        this.similarityType = similarityType;
        this.maxNumberOfRecommendedItems = maxNumberOfRecommendedItems;
        this.filterSettings = filterSettings;
        this.filter = new PropertiesFilter(filterSettings);
    }

    public String getSimilarityType() {
        return similarityType;
    }

    public int getMaxNumberOfRecommendedItems() {
        return maxNumberOfRecommendedItems;
    }

    public List<PropertyFilterSettings> getFilterSettings() {
        return filterSettings;
    }

    @JsonIgnore
    public Predicate<Properties> filter() {
        return filter;
    }
}
