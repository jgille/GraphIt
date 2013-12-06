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

package org.jon.ivmark.graphit.core.properties;

import com.google.common.base.Predicate;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Pattern;

import static org.jon.ivmark.graphit.core.properties.PropertiesFilterBuilder.where;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesFilterBuilderTest {

    @Mock
    private Properties properties;

    @Test
    public void testIsNull() {
        Predicate<Properties> filter = where("nonExisting").isNull().build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeIsNull() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").isNull().build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNotNull() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").notNull().build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeNotNull() {
        Predicate<Properties> filter = where("nonExisting").notNull().build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").equalTo(1).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").equalTo(2).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNullDoesNotEqualToAnything() {
        Predicate<Properties> filter = where("nonExisting").equalTo(1).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNotEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").notEqualTo(2).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeNotEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").notEqualTo(1).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNullNotEqualToEverything() {
        Predicate<Properties> filter = where("nonExisting").notEqualTo(1).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testLessThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").lessThan(2).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeLessThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(where("existing").lessThan(0).build().apply(properties));
        assertFalse(where("existing").lessThan(1).build().apply(properties));
    }

    @Test
    public void testNullNotLessThanAnything() {
        Predicate<Properties> filter = where("nonExisting").lessThan(2).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testLessThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(where("existing").lessThanOrEqualTo(1).build().apply(properties));
        assertTrue(where("existing").lessThanOrEqualTo(2).build().apply(properties));
    }

    @Test
    public void testNegativeLessThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").lessThanOrEqualTo(0).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNullNotLessThanOrEqualToAnything() {
        Predicate<Properties> filter = where("nonExisting").lessThanOrEqualTo(2).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testGreaterThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").greaterThan(0).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeGreaterThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(where("existing").greaterThan(2).build().apply(properties));
        assertFalse(where("existing").greaterThan(1).build().apply(properties));
    }

    @Test
    public void testNullNotGreaterThanAnything() {
        Predicate<Properties> filter = where("nonExisting").greaterThan(0).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testGreaterThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(where("existing").greaterThanOrEqualTo(0).build().apply(properties));
        assertTrue(where("existing").greaterThanOrEqualTo(1).build().apply(properties));
    }

    @Test
    public void testNegativeGreaterThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").greaterThanOrEqualTo(2).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNullNotGreaterThanOrEqualToAnything() {
        Predicate<Properties> filter = where("nonExisting").greaterThanOrEqualTo(2).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testMatches() {
        when(properties.getProperty("existing")).thenReturn("Hello world");
        Pattern pattern = Pattern.compile("^Hello\\s\\w*$");
        Predicate<Properties> filter = where("existing").matches(pattern).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeMatches() {
        when(properties.getProperty("existing")).thenReturn("Hello big world");
        Pattern pattern = Pattern.compile("^Hello\\s\\w*$");
        Predicate<Properties> filter = where("existing").matches(pattern).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNullMatchesNothing() {
        Pattern pattern = Pattern.compile("^Hello\\s\\w*$");
        Predicate<Properties> filter = where("nonExisting").matches(pattern).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testIn() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").in(Arrays.asList(3, 2, 1)).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeIn() {
        when(properties.getProperty("existing")).thenReturn(4);
        Predicate<Properties> filter = where("existing").in(Arrays.asList(3, 2, 1)).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNullNotInAnything() {
        Predicate<Properties> filter = where("nonExisting").in(Arrays.asList(3, 2, 1)).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNotIn() {
        when(properties.getProperty("existing")).thenReturn(4);
        Predicate<Properties> filter = where("existing").notIn(Arrays.asList(3, 2, 1)).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeNotIn() {
        when(properties.getProperty("existing")).thenReturn(1);
        Predicate<Properties> filter = where("existing").notIn(Arrays.asList(3, 2, 1)).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNotInWithNull() {
        Predicate<Properties> filter = where("nonExisting").notIn(Arrays.asList(3, 2, 1)).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testDisjoint() {
        when(properties.getProperty("existing")).thenReturn(Collections.singletonList(4));
        Predicate<Properties> filter = where("existing").disjoint(new HashSet<Integer>(Arrays.asList(3, 2, 1))).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeDisjoint() {
        when(properties.getProperty("existing")).thenReturn(Collections.singletonList(1));
        Predicate<Properties> filter = where("existing").disjoint(new HashSet<Integer>(Arrays.asList(3, 2, 1))).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testDisjointWithNull() {
        Predicate<Properties> filter = where("existing").disjoint(new HashSet<Integer>(Arrays.asList(3, 2, 1))).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testContains() {
        when(properties.getProperty("existing")).thenReturn(new HashSet<Integer>(Arrays.asList(3, 2, 1)));
        Predicate<Properties> filter = where("existing").contains(1).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeContains() {
        when(properties.getProperty("existing")).thenReturn(new HashSet<Integer>(Arrays.asList(3, 2, 1)));
        Predicate<Properties> filter = where("existing").contains(4).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void tesContainsWithNull() {
        Predicate<Properties> filter = where("existing").contains(1).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testCombination() {
        when(properties.getProperty("anInt")).thenReturn(1);
        when(properties.getProperty("aString")).thenReturn("Hello");

        Predicate<Properties> filter = where("anInt").lessThan(2)
                        .and("aString").equalTo("Hello")
                        .and("nonExisting").isNull()
                        .build();

        assertTrue(filter.apply(properties));
    }

    @Test
    public void testNegativeCombination() {
        when(properties.getProperty("anInt")).thenReturn(1);
        when(properties.getProperty("aString")).thenReturn("Hello");

        Predicate<Properties> filter = where("anInt").lessThan(2)
                .and("aString").equalTo("Hello world")
                .and("nonExisting").isNull()
                .build();

        assertFalse(filter.apply(properties));
    }
}
