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
import com.google.common.collect.Sets;
import org.codehaus.jackson.type.TypeReference;
import org.jon.ivmark.graphit.core.Json;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum PropertyFilterOperator {
    AND {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            List<Map<String, Object>> conditions = Json.OBJECT_MAPPER.convertValue(condition,
                    new TypeReference<List<Map<String, Object>>>() {});

            return new AndFilter(conditions);
        }
    },
    OR {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            List<Map<String, Object>> conditions = Json.OBJECT_MAPPER.convertValue(condition,
                    new TypeReference<List<Map<String, Object>>>() {});

            return new OrFilter(conditions);
        }
    },
    EQUAL_TO {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), "=", "EQ");
        }

        @Override
        public Predicate<Object> createFilter(Object target) {
            return new EqualToFilter(target);
        }
    },
    NOT_EQUAL_TO {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), "!=", "NEQ");
        }

        @Override
        public Predicate<Object> createFilter(Object target) {
            return new NotEqualToFilter(target);
        }
    },
    EXISTS {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            return new ExistsFilter(convertValue(condition, Boolean.class));
        }
    },
    GREATER_THAN {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), ">", "GT");
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            return new GreaterThanFilter(condition, new NumberComparatorFactory());
        }
    },
    GREATER_THAN_OR_EQUAL {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), ">=", "GTE");
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            return new GreaterThanOrEqualFilter(condition, new NumberComparatorFactory());
        }
    },
    LESS_THAN {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), "<", "LT");
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            return new LessThanFilter(condition, new NumberComparatorFactory());
        }
    },
    LESS_THAN_OR_EQUAL {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), "<=", "LTE");
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            return new LessThanOrEqualFilter(condition, new NumberComparatorFactory());
        }
    },
    IN {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object target) {
            Set<Object> set = Json.OBJECT_MAPPER.convertValue(target, new TypeReference<Set<Object>>() {
            });
            return new InFilter(set);
        }
    },
    NOT_IN {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object target) {
            Set<Object> set = Json.OBJECT_MAPPER.convertValue(target, new TypeReference<Set<Object>>() {
            });
            return new NotInFilter(set);
        }
    },
    CONTAINS {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object target) {
            return new ContainsFilter(target);
        }
    },
    DISJOINT {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name());
        }

        @Override
        public Predicate<Object> createFilter(Object target) {
            Set<Object> set = Json.OBJECT_MAPPER.convertValue(target, new TypeReference<Set<Object>>() {
            });
            return new DisjointFilter(set);
        }
    },
    MATCHES {
        @Override
        public Set<String> aliases() {
            return Sets.newHashSet(name(), "LIKE");
        }

        @Override
        public Predicate<Object> createFilter(Object condition) {
            return new MatchesFilter(convertValue(condition, String.class));
        }
    };

    private static <C> C convertValue(Object value, Class<C> cls) {
        return Json.OBJECT_MAPPER.convertValue(value, cls);
    }

    public abstract Set<String> aliases();

    public abstract Predicate<Object> createFilter(Object condition);

    public static PropertyFilterOperator operator(String alias) {
        for (PropertyFilterOperator operator : EnumSet.allOf(PropertyFilterOperator.class)) {
            if (operator.aliases().contains(alias.toUpperCase())) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown operator alias: " + alias);
    }
}
