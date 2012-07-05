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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author jon
 *
 */
public class IterablePipeImplTest {

    private IterablePipe<Integer> original;

    @Before
    public void setupPipe() {
        this.original = new IterablePipeImpl<Integer>(1, 2, 3, 4, 5);
    }

    @Test
    public void testLimit() {
        IterablePipe<Integer> pipe = original.limit(2);
        assertEquals(2, pipe.size());
        assertEquals(Arrays.asList(1, 2), pipe.asList());
    }

    @Test
    public void testSkip() {
        IterablePipe<Integer> pipe = original.skip(2);
        assertEquals(3, pipe.size());
        assertEquals(Arrays.asList(3, 4, 5), pipe.asList());
    }

    @Test
    public void testFilter() {
        IterablePipe<Integer> pipe = original.filter(new Predicate<Integer>() {

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
        IterablePipe<Integer> pipe = original.transform(new Function<Integer, Integer>() {

            @Override
            public Integer apply(Integer from) {
                return (int) Math.pow(from.intValue(), 2);
            }
        });
        assertEquals(5, pipe.size());
        assertEquals(Arrays.asList(1, 4, 9, 16, 25), pipe.asList());
    }

    @Test
    public void testAppend() {
        IterablePipe<Integer> pipe = original.append(Arrays.asList(6, 7));
        assertEquals(7, pipe.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), pipe.asList());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(original.isEmpty());
        assertTrue(new IterablePipeImpl<Integer>().isEmpty());
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
    public void testPipeLine() {
        List<String> list =
            original
                .limit(4)
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
                .limit(1)
                .asList();
        assertEquals(Arrays.asList("3"), list);
    }
}
