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

package org.jon.ivmark.graphit.core.properties.filter;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CompositeFilter implements Predicate<Object> {

    final List<Predicate<Object>> filters;

    protected CompositeFilter(List<Map<String, Object>> conditions) {
        Preconditions.checkArgument(!conditions.isEmpty(), "No conditions given");

        this.filters = new ArrayList<Predicate<Object>>(conditions.size());
        for (Map<String, Object> condition : conditions) {
            Preconditions.checkArgument(condition.size() == 1, "Expected singleton condition, got: " +
                    condition);
            Map.Entry<String, Object> entry = condition.entrySet().iterator().next();
            PropertyFilterOperator operator = PropertyFilterOperator.operator(entry.getKey());
            Predicate<Object> filter = operator.createFilter(entry.getValue());
            filters.add(filter);
        }
    }
}
