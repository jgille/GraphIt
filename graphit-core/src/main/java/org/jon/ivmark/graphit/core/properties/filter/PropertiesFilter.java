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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.jon.ivmark.graphit.core.properties.Properties;

import java.util.ArrayList;
import java.util.List;

public class PropertiesFilter implements Predicate<Properties> {

    private final Predicate<Properties> composite;

    public PropertiesFilter(List<PropertyFilterSettings> filterSettings) {
        if (filterSettings == null) {
            this.composite = Predicates.alwaysTrue();
            return;
        }
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        for (PropertyFilterSettings propertyFilterSettings : filterSettings) {
            String key = propertyFilterSettings.getKey();
            for (FilterCondition fc : propertyFilterSettings.getConditions()) {
                Predicate<Object> filter = fc.filter();
                filters.add(new PropertyFilter(key, filter));
            }

        }
        this.composite = Predicates.and(filters);
    }

    @Override
    public boolean apply(Properties properties) {
        return composite.apply(properties);
    }
}
