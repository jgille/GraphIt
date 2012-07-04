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

package org.graphit.common.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CombinedIterableTest {

    @Test
    public void testNoIterators() {
        List<Iterable<String>> iterables = new ArrayList<Iterable<String>>();
        CombinedIterables<String> it = new CombinedIterables<String>(iterables);
        List<String> list = asList(it.iterable());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testOneIterator() {
        List<Iterable<String>> iterables = new ArrayList<Iterable<String>>();
        iterables.add(Arrays.asList("A", "B"));
        CombinedIterables<String> it = new CombinedIterables<String>(iterables);
        List<String> list = asList(it.iterable());
        assertEquals(list.size(), 2);
        assertEquals(Arrays.asList("A", "B"), list);
    }

    @Test
    public void testTwoIterators() {
        List<Iterable<String>> iterables = new ArrayList<Iterable<String>>();
        iterables.add(Arrays.asList("A", "B"));
        iterables.add(Arrays.asList("C"));

        CombinedIterables<String> it = new CombinedIterables<String>(iterables);
        List<String> list = asList(it.iterable());
        assertEquals(list.size(), 3);
        assertEquals(Arrays.asList("A", "B", "C"), list);
    }

    @Test
    public void testRemove() {
        List<Iterable<String>> iterables = new ArrayList<Iterable<String>>();
        iterables.add(Arrays.asList("A", "B"));
        iterables.add(Arrays.asList("C"));

        CombinedIterables<String> it = new CombinedIterables<String>(iterables);
        boolean exception = false;
        Iterator<String> iterator = it.iterable().iterator();
        try {
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    private <T> List<T> asList(Iterable<T> it) {
        List<T> res = new ArrayList<T>();
        Iterator<T> iterator = it.iterator();
        while (iterator.hasNext()) {
            res.add(iterator.next());
        }
        return res;
    }
}
