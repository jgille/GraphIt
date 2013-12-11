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

import org.codehaus.jackson.annotate.JsonProperty;
import org.jon.ivmark.graphit.core.properties.filter.PropertyFilterSettings;

public class FallbackSettings {

    private final String id;
    private final PropertyFilterSettings filter;

    public FallbackSettings(@JsonProperty("id") String id,
                            @JsonProperty("filter") PropertyFilterSettings filter) {
        this.id = id;
        this.filter = filter;
    }

    public String getId() {
        return id;
    }

    public PropertyFilterSettings getFilter() {
        return filter;
    }
}
