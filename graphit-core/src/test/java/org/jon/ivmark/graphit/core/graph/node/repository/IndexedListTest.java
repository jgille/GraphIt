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

package org.jon.ivmark.graphit.core.graph.node.repository;

import org.jon.ivmark.graphit.core.graph.exception.DuplicateKeyException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author jon
 *
 */
public abstract class IndexedListTest {

    protected abstract IndexedList<String> createEmptyList();

    @Test
    public void testAddAndGet() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        int index = list.add(str);
        assertEquals(0, index);
        assertEquals(str, list.get(index));
    }

    @Test
    public void testInsert() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        list.insert(1, str);
        assertEquals(1, list.indexOf(str));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddDuplicate() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        list.add(str);
        list.add(str);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testInsertDuplicate() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        list.add(str);
        list.insert(1, str);
    }

    @Test
    public void testGetNonExisting() {
        IndexedList<String> list = createEmptyList();
        assertNull(list.get(0));
        assertNull(list.get(-1));
    }

    @Test
    public void testSetAndGet() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        list.set(1, str);
        assertNull(list.get(0));
        assertEquals(str, list.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSet() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        list.set(-1, str);
    }

    @Test
    public void testAddAndRemove() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        int index = list.add(str);
        list.remove(index);
        assertNull(list.get(0));
    }

    @Test
    public void testIndexOf() {
        IndexedList<String> list = createEmptyList();
        String str = "A";
        assertEquals(-1, list.indexOf(str));
        list.add(str);
        assertEquals(0, list.indexOf(str));
    }

    @Test
    public void testMany() {
        IndexedList<String> list = createEmptyList();
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
        IndexedList<String> list = createEmptyList();
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
