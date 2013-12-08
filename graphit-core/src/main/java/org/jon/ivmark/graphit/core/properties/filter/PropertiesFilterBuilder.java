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
import java.util.Set;
import java.util.regex.Pattern;

public final class PropertiesFilterBuilder {

    private final List<Predicate<Properties>> filters;

    private PropertiesFilterBuilder() {
        this.filters = new ArrayList<Predicate<Properties>>();
        filters.add(Predicates.<Properties>notNull());
    }

    public static PropertyFilterBuilder where(String key) {
        return new PropertyFilterBuilder(new PropertiesFilterBuilder(), key);
    }

    public PropertyFilterBuilder and(String key) {
        return new PropertyFilterBuilder(this, key);
    }

    public Predicate<Properties> build() {
        return Predicates.and(filters);
    }

    private PropertiesFilterBuilder addFilter(Predicate<Properties> filter) {
        filters.add(filter);
        return this;
    }

    public static final class PropertyFilterBuilder {

        private final PropertiesFilterBuilder builder;
        private final String key;

        private PropertyFilterBuilder(PropertiesFilterBuilder builder, String key) {
            this.builder = builder;
            this.key = key;
        }

        public PropertiesFilterBuilder isNull() {
            return builder.addFilter(PropertyFilter.isNull(key));
        }

        public PropertiesFilterBuilder notNull() {
            return builder.addFilter(PropertyFilter.notNull(key));
        }

        public PropertiesFilterBuilder equalTo(Object target) {
            return builder.addFilter(PropertyFilter.equalTo(key, target));
        }

        public PropertiesFilterBuilder notEqualTo(Object target) {
            return builder.addFilter(PropertyFilter.notEqualTo(key, target));
        }

        public PropertiesFilterBuilder lessThan(Number limit) {
            return builder.addFilter(PropertyFilter.lessThan(key, limit));
        }

        public PropertiesFilterBuilder lessThanOrEqualTo(Number limit) {
            return builder.addFilter(PropertyFilter.lessThanOrEqual(key, limit));
        }

        public PropertiesFilterBuilder greaterThan(Number limit) {
            return builder.addFilter(PropertyFilter.greaterThan(key, limit));
        }

        public PropertiesFilterBuilder greaterThanOrEqualTo(Number limit) {
            return builder.addFilter(PropertyFilter.greaterThanOrEqual(key, limit));
        }

        public PropertiesFilterBuilder matches(Pattern pattern) {
            return builder.addFilter(PropertyFilter.matches(key, pattern));
        }

        public PropertiesFilterBuilder in(Set<Object> target) {
            return builder.addFilter(PropertyFilter.in(key, target));
        }

        public PropertiesFilterBuilder notIn(Set<Object> target) {
            return builder.addFilter(PropertyFilter.notIn(key, target));
        }

        public PropertiesFilterBuilder disjoint(Set<Object> target) {
            return builder.addFilter(PropertyFilter.disjoint(key, target));
        }

        public PropertiesFilterBuilder contains(Object target) {
            return builder.addFilter(PropertyFilter.contains(key, target));
        }
    }
}
