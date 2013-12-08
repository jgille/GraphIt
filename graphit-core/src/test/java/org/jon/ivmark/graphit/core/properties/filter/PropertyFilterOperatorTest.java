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
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyFilterOperatorTest {

    @Test
    public void testAndAliases() {
        Set<String> aliases = PropertyFilterOperator.AND.aliases();
        Set<String> expected = Sets.newHashSet("AND");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testAndFilter() {
        Map<String, Object> condition = Collections.<String, Object>singletonMap("gt", 10);
        List<Map<String, Object>> conditions = Collections.singletonList(condition);
        Predicate<Object> filter = PropertyFilterOperator.AND.createFilter(conditions);
        assertThat("Unexpected filter: " + filter, filter instanceof AndFilter);
    }

    @Test
    public void testOrAliases() {
        Set<String> aliases = PropertyFilterOperator.OR.aliases();
        Set<String> expected = Sets.newHashSet("OR");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testOrFilter() {
        Map<String, Object> condition = Collections.<String, Object>singletonMap("gt", 10);
        List<Map<String, Object>> conditions = Collections.singletonList(condition);
        Predicate<Object> filter = PropertyFilterOperator.OR.createFilter(conditions);
        assertThat("Unexpected filter: " + filter, filter instanceof OrFilter);
    }

    @Test
    public void testEqualToAliases() {
        Set<String> aliases = PropertyFilterOperator.EQUAL_TO.aliases();
        Set<String> expected = Sets.newHashSet("EQUAL_TO", "EQ", "=");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testEqualToFilter() {
        Predicate<Object> filter = PropertyFilterOperator.EQUAL_TO.createFilter("test");
        assertThat("Unexpected filter: " + filter, filter instanceof EqualToFilter);
    }

    @Test
    public void testNotEqualAliases() {
        Set<String> aliases = PropertyFilterOperator.NOT_EQUAL_TO.aliases();
        Set<String> expected = Sets.newHashSet("NOT_EQUAL_TO", "NEQ", "!=");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testNotEqualToFilter() {
        Predicate<Object> filter = PropertyFilterOperator.NOT_EQUAL_TO.createFilter("test");
        assertThat("Unexpected filter: " + filter, filter instanceof NotEqualToFilter);
    }

    @Test
    public void testExistsAliases() {
        Set<String> aliases = PropertyFilterOperator.EXISTS.aliases();
        Set<String> expected = Sets.newHashSet("EXISTS");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testExistsFilter() {
        Predicate<Object> filter = PropertyFilterOperator.EXISTS.createFilter(true);
        assertThat("Unexpected filter: " + filter, filter instanceof ExistsFilter);
    }

    @Test
    public void testGreaterThanAliases() {
        Set<String> aliases = PropertyFilterOperator.GREATER_THAN.aliases();
        Set<String> expected = Sets.newHashSet("GREATER_THAN", "GT", ">");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testGreaterThanFilter() {
        Predicate<Object> filter = PropertyFilterOperator.GREATER_THAN.createFilter(1);
        assertThat("Unexpected filter: " + filter, filter instanceof GreaterThanFilter);
    }

    @Test
    public void testGreaterThanOrEqualAliases() {
        Set<String> aliases = PropertyFilterOperator.GREATER_THAN_OR_EQUAL.aliases();
        Set<String> expected = Sets.newHashSet("GREATER_THAN_OR_EQUAL", "GTE", ">=");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testGreaterThanOrEqualFilter() {
        Predicate<Object> filter = PropertyFilterOperator.GREATER_THAN_OR_EQUAL.createFilter(1);
        assertThat("Unexpected filter: " + filter, filter instanceof GreaterThanOrEqualFilter);
    }

    @Test
    public void testLessThanAliases() {
        Set<String> aliases = PropertyFilterOperator.LESS_THAN.aliases();
        Set<String> expected = Sets.newHashSet("LESS_THAN", "LT", "<");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testLessThanFilter() {
        Predicate<Object> filter = PropertyFilterOperator.LESS_THAN.createFilter(1);
        assertThat("Unexpected filter: " + filter, filter instanceof LessThanFilter);
    }

    @Test
    public void testLessThanOrEqualAliases() {
        Set<String> aliases = PropertyFilterOperator.LESS_THAN_OR_EQUAL.aliases();
        Set<String> expected = Sets.newHashSet("LESS_THAN_OR_EQUAL", "LTE", "<=");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testLessThanOrEqualFilter() {
        Predicate<Object> filter = PropertyFilterOperator.LESS_THAN_OR_EQUAL.createFilter(1);
        assertThat("Unexpected filter: " + filter, filter instanceof LessThanOrEqualFilter);
    }

    @Test
    public void testInAliases() {
        Set<String> aliases = PropertyFilterOperator.IN.aliases();
        Set<String> expected = Sets.newHashSet("IN");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testInFilter() {
        Predicate<Object> filter = PropertyFilterOperator.IN.createFilter(Sets.newHashSet(1));
        assertThat("Unexpected filter: " + filter, filter instanceof InFilter);
    }

    @Test
    public void testNotInAliases() {
        Set<String> aliases = PropertyFilterOperator.NOT_IN.aliases();
        Set<String> expected = Sets.newHashSet("NOT_IN");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testNotInFilter() {
        Predicate<Object> filter = PropertyFilterOperator.NOT_IN.createFilter(Sets.newHashSet(1));
        assertThat("Unexpected filter: " + filter, filter instanceof NotInFilter);
    }

    @Test
    public void testContainsAliases() {
        Set<String> aliases = PropertyFilterOperator.CONTAINS.aliases();
        Set<String> expected = Sets.newHashSet("CONTAINS");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testContainsFilter() {
        Predicate<Object> filter = PropertyFilterOperator.CONTAINS.createFilter(1);
        assertThat("Unexpected filter: " + filter, filter instanceof ContainsFilter);
    }

    @Test
    public void testDisjointAliases() {
        Set<String> aliases = PropertyFilterOperator.DISJOINT.aliases();
        Set<String> expected = Sets.newHashSet("DISJOINT");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testDisjointFilter() {
        Predicate<Object> filter = PropertyFilterOperator.DISJOINT.createFilter(Sets.newHashSet(1));
        assertThat("Unexpected filter: " + filter, filter instanceof DisjointFilter);
    }

    @Test
    public void testMatchesAliases() {
        Set<String> aliases = PropertyFilterOperator.MATCHES.aliases();
        Set<String> expected = Sets.newHashSet("MATCHES", "LIKE");
        assertThat(aliases, is(expected));
    }

    @Test
    public void testMatchesFilter() {
        Predicate<Object> filter = PropertyFilterOperator.MATCHES.createFilter("test");
        assertThat("Unexpected filter: " + filter, filter instanceof MatchesFilter);
    }

    @Test
    public void testOperator() {
        List<String> supportedAliases = Arrays.asList(
                "and",
                "or",
                "equal_to", "eq", "=",
                "not_equal_to", "neq", "!=",
                "greater_than", "gt", ">",
                "less_than", "lt", "<",
                "greater_than_or_equal", "gte", ">=",
                "less_than_or_equal", "lte", "<=",
                "in",
                "not_in",
                "contains",
                "disjoint",
                "matches", "like"
                );
        for (String alias : supportedAliases) {
            PropertyFilterOperator operator = PropertyFilterOperator.operator(alias);
            assertThat(String.format("Unknown alias: %s", alias), operator, notNullValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalOperator() {
        PropertyFilterOperator.operator("what?");
    }
}
