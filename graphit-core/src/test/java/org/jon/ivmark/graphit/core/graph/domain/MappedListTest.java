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

package org.jon.ivmark.graphit.core.graph.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jon.ivmark.graphit.core.graph.exception.DuplicateKeyException;
import org.junit.Test;

/**
 * @author jon
 *
 */
public abstract class MappedListTest {

    protected abstract MappedList<String> createEmptyList();

    @Test
    public void testAddAndGet() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        int index = list.add(str);
        assertEquals(0, index);
        assertEquals(str, list.get(index));
    }

    @Test
    public void testInsert() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        list.insert(1, str);
        assertEquals(1, list.indexOf(str));
    }

    @Test
    public void testAddDuplicate() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        int index = list.add(str);
        boolean exception = false;
        try {
            list.add(str);
        } catch (DuplicateKeyException e) {
            exception = true;
        }
        assertTrue(exception);
        assertEquals(0, index);
        assertEquals(str, list.get(index));
    }

    @Test
    public void testInsertDuplicate() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        int index = list.add(str);
        boolean exception = false;
        try {
            list.insert(1, str);
        } catch (DuplicateKeyException e) {
            exception = true;
        }
        assertTrue(exception);
        assertEquals(0, index);
        assertEquals(str, list.get(index));
    }

    @Test
    public void testGetNonExisting() {
        MappedList<String> list = createEmptyList();
        assertNull(list.get(0));
        assertNull(list.get(-1));
    }

    @Test
    public void testSetAndGet() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        list.set(1, str);
        assertNull(list.get(0));
        assertEquals(str, list.get(1));

        boolean exception = false;
        try {
            list.set(-1, "A");
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testAddAndRemove() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        int index = list.add(str);
        list.remove(index);
        assertNull(list.get(0));
    }

    @Test
    public void testIndexOf() {
        MappedList<String> list = createEmptyList();
        String str = "A";
        assertEquals(-1, list.indexOf(str));
        list.add(str);
        assertEquals(0, list.indexOf(str));
    }

    @Test
    public void testMany() {
        MappedList<String> list = createEmptyList();
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        for (String el : elements) {
            list.add(el);
        }
        int i = 0;
        for (String el : elements) {
            assertEquals(el, list.get(i));
            assertEquals(i, list.indexOf(el));
            list.set(i, el + "-");
            assertEquals(el + "-", list.get(i));
            assertEquals(i, list.indexOf(el + "-"));
            list.remove(i);
            assertNull(list.get(i));
            assertEquals(-1, list.indexOf(el + "-"));
            i++;
        }
    }

    @Test
    public void testIterable() {
        MappedList<String> list = createEmptyList();
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        for (String el : elements) {
            list.add(el);
        }
        list.remove(2);
        list.set(0, "");
        List<String> it = new ArrayList<String>();
        for (String el : list.iterable()) {
            it.add(el);
        }
        assertEquals(4, it.size());
        assertTrue(it.containsAll(Arrays.asList("", "B", "D", "E")));
    }
}
