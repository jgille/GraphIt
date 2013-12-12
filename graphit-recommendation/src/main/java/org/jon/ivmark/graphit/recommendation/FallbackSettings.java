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
import org.jon.ivmark.graphit.core.properties.filter.CompositePropertyFilter;

import java.util.HashMap;
import java.util.Map;

public class FallbackSettings {

    private final String id;
    private final Map<String, Map<String, Object>> filterSettings;
    private final Predicate<Properties> filter;

    public FallbackSettings(@JsonProperty("id") String id,
                            @JsonProperty("filter") Map<String, Map<String, Object>> filterSettings) {
        this.id = id;
        this.filterSettings = filterSettings == null ? null : new HashMap<String, Map<String, Object>>(filterSettings);
        this.filter = new CompositePropertyFilter(filterSettings);
    }

    public String getId() {
        return id;
    }

    public Map<String, Map<String, Object>> getFilterSettings() {
        return filterSettings;
    }

    @JsonIgnore
    public Predicate<Properties> filter() {
        return filter;
    }
}
