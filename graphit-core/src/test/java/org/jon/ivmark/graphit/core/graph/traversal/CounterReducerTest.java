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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jon.ivmark.graphit.core.graph.traversal.CountSortOrder;
import org.jon.ivmark.graphit.core.graph.traversal.CountedElement;
import org.jon.ivmark.graphit.core.graph.traversal.Counter;
import org.jon.ivmark.graphit.core.graph.traversal.ElementCounter;
import org.junit.Test;

/**
 * @author jon
 * 
 */
public class CounterReducerTest {

    @Test
    public void testEmpty() {
        ElementCounter<Integer> reducer = new ElementCounter<Integer>();
        Counter<Integer> counter = reducer.reduce(Collections.<Integer> emptyList());
        assertEquals(Collections.emptyList(),
                     asList(counter.iterable(CountSortOrder.INSERTION_ORDER)));
    }

    @Test
    public void testSorted() {
        ElementCounter<Integer> reducer = new ElementCounter<Integer>();
        Counter<Integer> counter = reducer.reduce(Arrays.asList(1, 2, 1));
        assertEquals(2, counter.count(1));
        assertEquals(1, counter.count(2));
        assertEquals(Arrays.asList(2, 1), asList(counter.iterable(CountSortOrder.ASCENDING_COUNT)));
    }

    private List<Integer> asList(Iterable<CountedElement<Integer>> it) {
        List<Integer> res = new ArrayList<Integer>();
        for (CountedElement<Integer> i : it) {
            res.add(i.getElement());
        }
        return res;
    }

}
