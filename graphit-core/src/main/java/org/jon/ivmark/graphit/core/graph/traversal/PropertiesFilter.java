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

package org.jon.ivmark.graphit.core.graph.traversal;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.jon.ivmark.graphit.core.properties.Properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class PropertiesFilter implements Predicate<Properties> {

    private final List<Predicate<Properties>> andFilters;

    public PropertiesFilter() {
        this.andFilters = new ArrayList<Predicate<Properties>>();
        andFilters.add(Predicates.<Properties>notNull());
    }

    public PropertiesFilter isNull(String key) {
        andFilters.add(PropertyFilter.isNull(key));
        return this;
    }

    public PropertiesFilter notNull(String key) {
        andFilters.add(PropertyFilter.notNull(key));
        return this;
    }

    public PropertiesFilter equalTo(String key, Object target) {
        andFilters.add(PropertyFilter.equalTo(key, target));
        return this;
    }

    public PropertiesFilter notEqualTo(String key, Object target) {
        andFilters.add(PropertyFilter.notEqualTo(key, target));
        return this;
    }

    public <C extends Comparable<C>> PropertiesFilter lessThan(String key, C limit) {
        andFilters.add(PropertyFilter.lessThan(key, limit));
        return this;
    }

    public <C extends Comparable<C>> PropertiesFilter lessThanOrEqual(String key, C limit) {
        andFilters.add(PropertyFilter.lessThanOrEqual(key, limit));
        return this;
    }

    public <C extends Comparable<C>> PropertiesFilter greaterThan(String key, C limit) {
        andFilters.add(PropertyFilter.greaterThan(key, limit));
        return this;
    }

    public <C extends Comparable<C>> PropertiesFilter greaterThanOrEqual(String key, C limit) {
        andFilters.add(PropertyFilter.greaterThanOrEqual(key, limit));
        return this;
    }

    public PropertiesFilter matches(String key, Pattern pattern) {
        andFilters.add(PropertyFilter.matches(key, pattern));
        return this;
    }

    public <C> PropertiesFilter in(String key, Collection<C> target) {
        andFilters.add(PropertyFilter.in(key, target));
        return this;
    }

    public <C> PropertiesFilter notIn(String key, Collection<C> target) {
        andFilters.add(PropertyFilter.notIn(key, target));
        return this;
    }

    @Override
    public boolean apply(Properties input) {
        return Predicates.and(andFilters).apply(input);
    }
}
