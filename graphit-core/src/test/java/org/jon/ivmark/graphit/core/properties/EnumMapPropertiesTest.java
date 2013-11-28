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

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author jon
 *
 */
public class EnumMapPropertiesTest {

    private static enum AbcEnum {
        A, B, C
    }

    private Properties createEmptyProperties() {
        return new EnumMapProperties<AbcEnum>(AbcEnum.class);
    }

    @Test
    public void testEmptyProperties() {
        Properties props = createEmptyProperties();
        assertEquals(0, props.size());
        assertEquals(0, props.getPropertyKeys().size());
        assertTrue(props.isEmpty());
        assertNull(props.getProperty("A"));
        assertNull(props.getProperty("B"));
        assertNull(props.getProperty("C"));
    }

    @Test
    public void testSetGetProperty() {
        Properties props = createEmptyProperties();
        assertNull(props.getProperty("A"));
        props.setProperty("A", "a");
        assertEquals(1, props.size());
        assertFalse(props.isEmpty());
        assertEquals("a", props.getProperty("A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidProperty() {
        Properties props = createEmptyProperties();
        props.getProperty("Foo");
    }

    @Test
    public void testRemove() {
        Properties props = createEmptyProperties();
        props.setProperty("A", "a");
        props.setProperty("B", "b");
        assertEquals("a", props.removeProperty("A"));
        assertEquals(1, props.size());
        assertFalse(props.isEmpty());
        assertNull(props.getProperty("A"));
        assertEquals("b", props.getProperty("B"));
        assertNull(props.removeProperty("A"));
    }

    @Test
    public void testContainsProperty() {
        Properties props = createEmptyProperties();
        assertFalse(props.containsProperty("A"));
        props.setProperty("A", "a");
        assertTrue(props.containsProperty("A"));
    }

    @Test
    public void testGetPropertyKeys() {
        Properties props = createEmptyProperties();
        assertTrue(props.getPropertyKeys().isEmpty());
        props.setProperty("A", "a");
        props.setProperty("B", "b");
        Set<String> keys = props.getPropertyKeys();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("A"));
        assertTrue(keys.contains("B"));
    }

    @Test
    public void testAsPropertyMap() {
        Properties props = createEmptyProperties();
        assertTrue(props.asPropertyMap().isEmpty());
        props.setProperty("A", "a");
        props.setProperty("B", "b");
        Map<String, Object> map = props.asPropertyMap();
        assertEquals(2, map.size());
        assertEquals("a", map.get("A"));
        assertEquals("b", map.get("B"));
    }

}
