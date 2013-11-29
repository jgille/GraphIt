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

import java.util.Collection;
import java.util.regex.Pattern;

public abstract class PropertyFilter<C> implements Predicate<Properties> {

    private final String key;

    public PropertyFilter(String key) {
        this.key = key;
    }

    @Override
    public boolean apply(Properties properties) {
        @SuppressWarnings("unchecked")
        C property = (C) properties.getProperty(key);
        return accepts(property);
    }

    protected abstract boolean accepts(C property);

    static Predicate<Properties> notNull(String key) {
        return Predicates.not(isNull(key));
    }

    static Predicate<Properties> isNull(String key) {
        return new PropertyFilter<Object>(key) {
            @Override
            protected boolean accepts(Object property) {
                return property == null;
            }
        };
    }

    static Predicate<Properties> equalTo(String key, final Object target) {
        return new PropertyFilter<Object>(key) {
            @Override
            protected boolean accepts(Object property) {
                return property != null && property.equals(target);
            }
        };
    }

    static Predicate<Properties> notEqualTo(String key, Object target) {
        return Predicates.not(equalTo(key, target));
    }

    static <C extends Comparable<C>> Predicate<Properties> lessThan(String key, final C limit) {
        return Predicates.and(notNull(key), new PropertyFilter<C>(key) {
            @Override
            protected boolean accepts(C property) {
                return property.compareTo(limit) < 0;
            }
        });
    }

    static <C extends Comparable<C>> Predicate<Properties> lessThanOrEqual(String key, C limit) {
        return Predicates.or(lessThan(key, limit), equalTo(key, limit));
    }

    static <C extends Comparable<C>> Predicate<Properties> greaterThan(String key, C limit) {
        return Predicates.and(notNull(key), Predicates.not(lessThanOrEqual(key, limit)));
    }

    static <C extends Comparable<C>> Predicate<Properties> greaterThanOrEqual(String key, C limit) {
        return Predicates.or(greaterThan(key, limit), equalTo(key, limit));
    }

    static Predicate<Properties> matches(String key, final Pattern pattern) {
        return Predicates.and(notNull(key), new PropertyFilter<String>(key) {
            @Override
            protected boolean accepts(String property) {
                return pattern.matcher(property).matches();
            }
        });
    }

    static <C> Predicate<Properties> in(String key, final Collection<C> target) {
        return Predicates.and(notNull(key), new PropertyFilter<C>(key) {
            @Override
            protected boolean accepts(C property) {
                return target.contains(property);
            }
        });
    }

    static <C> Predicate<Properties> notIn(String key, Collection<C> target) {
        return Predicates.not(in(key, target));
    }
}
