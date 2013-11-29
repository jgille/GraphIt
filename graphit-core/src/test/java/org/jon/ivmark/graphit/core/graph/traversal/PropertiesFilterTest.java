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
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesFilterTest {

    @Mock
    private Properties properties;

    @Test
    public void testIsNull() {
        assertTrue(new PropertiesFilter().isNull("nonExisting").apply(properties));
    }

    @Test
    public void testNegativeIsNull() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().isNull("existing").apply(properties));
    }

    @Test
    public void testNotNull() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().notNull("existing").apply(properties));
    }

    @Test
    public void testNegativeNotNull() {
        assertFalse(new PropertiesFilter().notNull("nonExisting").apply(properties));
    }

    @Test
    public void testEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().equalTo("existing", 1).apply(properties));
    }

    @Test
    public void testNegativeEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().equalTo("existing", 2).apply(properties));
    }

    @Test
    public void testNullDoesNotEqualToAnything() {
        assertFalse(new PropertiesFilter().equalTo("nonExisting", 1).apply(properties));
    }

    @Test
    public void testNotEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().notEqualTo("existing", 2).apply(properties));
    }

    @Test
    public void testNegativeNotEqualTo() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().notEqualTo("existing", 1).apply(properties));
    }

    @Test
    public void testNullNotEqualToEverything() {
        assertTrue(new PropertiesFilter().notEqualTo("nonExisting", 1).apply(properties));
    }

    @Test
    public void testLessThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().lessThan("existing", 2).apply(properties));
    }

    @Test
    public void testNegativeLessThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().lessThan("existing", 0).apply(properties));
        assertFalse(new PropertiesFilter().lessThan("existing", 1).apply(properties));
    }

    @Test
    public void testNullNotLessThanAnything() {
        assertFalse(new PropertiesFilter().lessThan("nonExisting", 2).apply(properties));
    }

    @Test
    public void testLessThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().lessThanOrEqual("existing", 1).apply(properties));
        assertTrue(new PropertiesFilter().lessThanOrEqual("existing", 2).apply(properties));
    }

    @Test
    public void testNegativeLessThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().lessThanOrEqual("existing", 0).apply(properties));
    }

    @Test
    public void testNullNotLessThanOrEqualToAnything() {
        assertFalse(new PropertiesFilter().lessThanOrEqual("nonExisting", 2).apply(properties));
    }

    @Test
    public void testGreaterThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().greaterThan("existing", 0).apply(properties));
    }

    @Test
    public void testNegativeGreaterThan() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().greaterThan("existing", 2).apply(properties));
        assertFalse(new PropertiesFilter().greaterThan("existing", 1).apply(properties));
    }

    @Test
    public void testNullNotGreaterThanAnything() {
        assertFalse(new PropertiesFilter().greaterThan("nonExisting", 2).apply(properties));
    }

    @Test
    public void testGreaterThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().greaterThanOrEqual("existing", 0).apply(properties));
        assertTrue(new PropertiesFilter().greaterThanOrEqual("existing", 1).apply(properties));
    }

    @Test
    public void testNegativeGreaterThanOrEqual() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().greaterThanOrEqual("existing", 2).apply(properties));
    }

    @Test
    public void testNullNotGreaterThanOrEqualToAnything() {
        assertFalse(new PropertiesFilter().greaterThanOrEqual("nonExisting", 2).apply(properties));
    }

    @Test
    public void testMatches() {
        when(properties.getProperty("existing")).thenReturn("Hello world");
        Pattern pattern = Pattern.compile("^Hello\\s\\w*$");
        assertTrue(new PropertiesFilter().matches("existing", pattern).apply(properties));
    }

    @Test
    public void testNegativeMatches() {
        when(properties.getProperty("existing")).thenReturn("Hello big world");
        Pattern pattern = Pattern.compile("^Hello\\s\\w*$");
        assertFalse(new PropertiesFilter().matches("existing", pattern).apply(properties));
    }

    @Test
    public void testNullMatchesNothing() {
        Pattern pattern = Pattern.compile("^Hello\\s\\w*$");
        assertFalse(new PropertiesFilter().matches("nonExisting", pattern).apply(properties));
    }

    @Test
    public void testIn() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertTrue(new PropertiesFilter().in("existing", Arrays.asList(3, 2, 1)).apply(properties));
    }

    @Test
    public void testNegativeIn() {
        when(properties.getProperty("existing")).thenReturn(4);
        assertFalse(new PropertiesFilter().in("existing", Arrays.asList(3, 2, 1)).apply(properties));
    }

    @Test
    public void testNullNotInAnything() {
        assertFalse(new PropertiesFilter().in("nonExisting", Arrays.asList(3, 2, 1)).apply(properties));
    }

    @Test
    public void testNotIn() {
        when(properties.getProperty("existing")).thenReturn(4);
        assertTrue(new PropertiesFilter().notIn("existing", Arrays.asList(3, 2, 1)).apply(properties));
    }

    @Test
    public void testNegativeNotIn() {
        when(properties.getProperty("existing")).thenReturn(1);
        assertFalse(new PropertiesFilter().notIn("existing", Arrays.asList(3, 2, 1)).apply(properties));
    }

    @Test
    public void testNotInWithNull() {
        assertTrue(new PropertiesFilter().notIn("nonExisting", Arrays.asList(3, 2, 1)).apply(properties));
    }

    @Test
    public void testCombination() {
        when(properties.getProperty("anInt")).thenReturn(1);
        when(properties.getProperty("aString")).thenReturn("Hello");

        assertTrue(new PropertiesFilter().lessThan("anInt", 2).equalTo("aString", "Hello").isNull("nonExisting")
                .apply(properties));

    }

    @Test
    public void testNegativeCombination() {
        when(properties.getProperty("anInt")).thenReturn(1);
        when(properties.getProperty("aString")).thenReturn("Hello");

        assertFalse(new PropertiesFilter().lessThan("anInt", 2).equalTo("aString", "Hello world").isNull("nonExisting")
                .apply(properties));

    }
}
