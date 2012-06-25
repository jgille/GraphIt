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

package org.graphit.properties.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.graphit.properties.domain.MapProperties;
import org.graphit.properties.domain.Properties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class WriteThroughPropertiesTest {

    private PropertiesRepository<Integer> repo;

    @SuppressWarnings("unchecked")
    @Before
    public void setupRepo() {
        this.repo = mock(PropertiesRepository.class);
    }

    @Test
    public void testGetProperty() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, properties, repo);
        assertEquals("B", wtProperties.getProperty("A"));
        assertNull(wtProperties.getProperty("AB"));
    }

    @Test
    public void testLazyGetProperty() {
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, repo);
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        when(repo.getProperties(1)).thenReturn(properties);
        assertEquals("B", wtProperties.getProperty("A"));
        assertNull(wtProperties.getProperty("AB"));
    }

    @Test
    public void testSetProperty() {
        Properties properties = new MapProperties();
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, properties, repo);
        final AtomicReference<Integer> id = new AtomicReference<Integer>();
        final AtomicReference<String> key = new AtomicReference<String>();
        final AtomicReference<Object> value = new AtomicReference<Object>();
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                id.set((Integer) invocation.getArguments()[0]);
                key.set((String) invocation.getArguments()[1]);
                value.set(invocation.getArguments()[2]);
                return null;
            }
        }).when(repo).setProperty(anyInt(), anyString(), anyObject());
        wtProperties.setProperty("A", "B");
        assertEquals("B", wtProperties.getProperty("A"));
        assertEquals(Integer.valueOf(1), id.get());
        assertEquals("A", key.get());
        assertEquals("B", value.get());
    }

    @Test
    public void testLazySetProperty() {
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, repo);
        final AtomicReference<Integer> id = new AtomicReference<Integer>();
        final AtomicReference<String> key = new AtomicReference<String>();
        final AtomicReference<Object> value = new AtomicReference<Object>();
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                id.set((Integer) invocation.getArguments()[0]);
                key.set((String) invocation.getArguments()[1]);
                value.set(invocation.getArguments()[2]);
                return null;
            }
        }).when(repo).setProperty(anyInt(), anyString(), anyObject());
        wtProperties.setProperty("A", "B");
        assertEquals("B", wtProperties.getProperty("A"));
        assertEquals(Integer.valueOf(1), id.get());
        assertEquals("A", key.get());
        assertEquals("B", value.get());
    }

    @Test
    public void testRemoveProperty() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");

        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, properties, repo);
        final AtomicReference<Integer> id = new AtomicReference<Integer>();
        final AtomicReference<String> key = new AtomicReference<String>();
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                id.set((Integer) invocation.getArguments()[0]);
                key.set((String) invocation.getArguments()[1]);
                return null;
            }
        }).when(repo).removeProperty(anyInt(), anyString());
        assertEquals("B", wtProperties.removeProperty("A"));
        assertNull(wtProperties.getProperty("A"));
        assertEquals(Integer.valueOf(1), id.get());
        assertEquals("A", key.get());
    }

    @Test
    public void testLazyRemoveProperty() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");

        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, repo);

        final AtomicReference<Integer> id = new AtomicReference<Integer>();
        final AtomicReference<String> key = new AtomicReference<String>();
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                id.set((Integer) invocation.getArguments()[0]);
                key.set((String) invocation.getArguments()[1]);
                return null;
            }
        }).when(repo).removeProperty(anyInt(), anyString());

        when(repo.getProperties(1)).thenReturn(properties);

        assertEquals("B", wtProperties.removeProperty("A"));
        assertNull(wtProperties.getProperty("A"));
        assertEquals(Integer.valueOf(1), id.get());
        assertEquals("A", key.get());
    }

    @Test
    public void testContainsProperty() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, properties, repo);
        assertTrue(wtProperties.containsProperty("A"));
        assertFalse(wtProperties.containsProperty("AB"));
    }

    @Test
    public void testLazyContainsProperty() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, repo);

        when(repo.getProperties(1)).thenReturn(properties);

        assertTrue(wtProperties.containsProperty("A"));
        assertFalse(wtProperties.containsProperty("AB"));
    }

    @Test
    public void testGetPropertyKeys() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, properties, repo);
        Set<String> keys = wtProperties.getPropertyKeys();
        assertEquals(1, keys.size());
        assertEquals("A", keys.iterator().next());
    }

    @Test
    public void testLazyGetPropertyKeys() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, repo);

        when(repo.getProperties(1)).thenReturn(properties);

        Set<String> keys = wtProperties.getPropertyKeys();
        assertEquals(1, keys.size());
        assertEquals("A", keys.iterator().next());
    }

    @Test
    public void testAsPropertyMap() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, properties, repo);
        Map<String, Object> map = wtProperties.asPropertyMap();
        assertEquals(1, map.size());
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        assertEquals("A", entry.getKey());
        assertEquals("B", entry.getValue());
    }

    @Test
    public void testLazyAsPropertyMap() {
        Properties properties = new MapProperties();
        properties.setProperty("A", "B");
        WriteThroughProperties<Integer> wtProperties =
            new WriteThroughProperties<Integer>(1, repo);

        when(repo.getProperties(1)).thenReturn(properties);

        Map<String, Object> map = wtProperties.asPropertyMap();
        assertEquals(1, map.size());
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        assertEquals("A", entry.getKey());
        assertEquals("B", entry.getValue());
    }
}
