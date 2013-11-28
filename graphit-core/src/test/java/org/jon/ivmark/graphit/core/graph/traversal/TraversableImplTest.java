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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author jon
 *
 */
public class TraversableImplTest {

    private Traversable<Integer> original;

    @Before
    public void setupPipe() {
        this.original = new TraversableImpl<Integer>(1, 2, 3, 4, 5);
    }

    @Test
    public void testHead() {
        Traversable<Integer> pipe = original.head(2);
        assertEquals(2, pipe.size());
        assertEquals(Arrays.asList(1, 2), pipe.asList());
        assertEquals(Collections.emptyList(), original.head(0).asList());
        assertEquals(original.asList(), original.head(20).asList());
    }

    @Test
    public void testTail() {
        Traversable<Integer> pipe = original.tail(2);
        assertEquals(2, pipe.size());
        assertEquals(Arrays.asList(4, 5), pipe.asList());
        assertEquals(Collections.emptyList(), original.tail(0).asList());
        assertEquals(original.asList(), original.tail(20).asList());
    }

    @Test
    public void testSkip() {
        Traversable<Integer> pipe = original.skip(2);
        assertEquals(3, pipe.size());
        assertEquals(Arrays.asList(3, 4, 5), pipe.asList());
        assertEquals(Collections.emptyList(), original.skip(20).asList());
    }

    @Test
    public void testFilter() {
        Traversable<Integer> pipe = original.filter(new Predicate<Integer>() {

            @Override
            public boolean apply(Integer input) {
                return input.intValue() < 3;
            }
        });
        assertEquals(2, pipe.size());
        assertEquals(Arrays.asList(1, 2), pipe.asList());
    }

    @Test
    public void testTransform() {
        Traversable<Integer> pipe = original.transform(new Function<Integer, Integer>() {

            @Override
            public Integer apply(Integer from) {
                return (int) Math.pow(from.intValue(), 2);
            }
        });
        assertEquals(5, pipe.size());
        assertEquals(Arrays.asList(1, 4, 9, 16, 25), pipe.asList());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(original.isEmpty());
        assertTrue(new TraversableImpl<Integer>().isEmpty());
    }

    @Test
    public void testGet() {
        assertEquals(Integer.valueOf(3), original.get(2));
    }

    @Test
    public void testAsSortedCollection() {
        Collection<Integer> col = original.asSortedCollection(new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return -o1.compareTo(o2);
            }
        });
        assertEquals(5, col.size());
        List<Integer> list = new ArrayList<Integer>(col);
        assertEquals(Arrays.asList(5, 4, 3, 2, 1), list);
    }

    @Test
    public void testUnique() {
        Traversable<Integer> pipe = new TraversableImpl<Integer>(1, 2, 3, 4, 5, 1, 2);
        assertEquals(original.asList(), pipe.unique().asList());
    }

    @Test
    public void testForEach() {
        Collector<Integer> procedure = new Collector<Integer>();
        original.forEach(procedure);
        List<Integer> list = procedure.getElements();
        assertEquals(original.asList(), list);
    }

    @Test
    public void testBreakForEach() {
        final AtomicInteger counter = new AtomicInteger();
        Procedure<Integer> procedure = new Procedure<Integer>() {

            @Override
            public boolean apply(Integer element) {
                counter.incrementAndGet();
                return element < 3;
            }
        };
        original.forEach(procedure);
        assertEquals(3, counter.get());
    }

    @Test
    public void testMapReduce() {
        String str = original.mapReduce(new Mapper<Integer, String>() {

            @Override
            public Iterable<String> map(Iterable<Integer> input) {
                List<String> res = new ArrayList<String>();
                for (Integer i : input) {
                    res.add(String.valueOf(i));
                }
                return res;
            }
        }, new Reducer<String, String>() {

            @Override
            public String reduce(Iterable<String> input) {
                StringBuilder sb = new StringBuilder();
                for (String str : input) {
                    sb.append(str);
                }
                return sb.toString();
            }
        });
        assertEquals("12345", str);
    }

    @Test
    public void testPipeLine() {
        List<String> list =
            original
                .head(4)
                .filter(new Predicate<Integer>() {

                    @Override
                    public boolean apply(Integer input) {
                        return input.intValue() != 2;
                    }
                })
                .skip(1)
                .transform(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer from) {
                        return from.toString();
                    }
                })
                .head(1)
                .asList();
        assertEquals(Arrays.asList("3"), list);
    }
}
