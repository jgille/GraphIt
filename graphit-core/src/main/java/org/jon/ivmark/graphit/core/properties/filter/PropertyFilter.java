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
import org.jon.ivmark.graphit.core.properties.Properties;

import java.util.Set;
import java.util.regex.Pattern;

public class PropertyFilter implements Predicate<Properties> {

    private static final NumberComparatorFactory NUMBER_COMPARATOR_FACTORY = new NumberComparatorFactory();
    private final String key;
    private final Predicate<Object> filter;

    public PropertyFilter(String key, Predicate<Object> filter) {
        this.key = key;
        this.filter = filter;
    }

    @Override
    public boolean apply(Properties properties) {
        Object property = properties.getProperty(key);
        return filter.apply(property);
    }

    static Predicate<Properties> notNull(String key) {
        return new PropertyFilter(key, new ExistsFilter(true));
    }

    static Predicate<Properties> isNull(String key) {
        return new PropertyFilter(key, new ExistsFilter(false));
    }

    static Predicate<Properties> equalTo(String key, Object target) {
        return new PropertyFilter(key, new EqualToFilter(target));
    }

    static Predicate<Properties> notEqualTo(String key, Object target) {
        return new PropertyFilter(key, new NotEqualToFilter(target));
    }

    static Predicate<Properties> lessThan(String key, Object limit) {
        return new PropertyFilter(key, new LessThanFilter(limit, NUMBER_COMPARATOR_FACTORY));
    }

    static Predicate<Properties> lessThanOrEqual(String key, Object limit) {
        return new PropertyFilter(key, new LessThanOrEqualFilter(limit, NUMBER_COMPARATOR_FACTORY));
    }

    static Predicate<Properties> greaterThan(String key, Object limit) {
        return new PropertyFilter(key, new GreaterThanFilter(limit, NUMBER_COMPARATOR_FACTORY));
    }

    static Predicate<Properties> greaterThanOrEqual(String key, Object limit) {
        return new PropertyFilter(key, new GreaterThanOrEqualFilter(limit, NUMBER_COMPARATOR_FACTORY));
    }

    static Predicate<Properties> matches(String key, Pattern pattern) {
        return new PropertyFilter(key, new MatchesFilter(pattern));
    }

    static Predicate<Properties> in(String key, Set<Object> target) {
        return new PropertyFilter(key, new InFilter(target));
    }

    static Predicate<Properties> notIn(String key, Set<Object> target) {
        return new PropertyFilter(key, new NotInFilter(target));
    }

    static Predicate<Properties> disjoint(String key, Set<Object> target) {
        return new PropertyFilter(key, new DisjointFilter(target));
    }

    static Predicate<Properties> contains(String key, Object target) {
        return new PropertyFilter(key, new ContainsFilter(target));

    }

}
