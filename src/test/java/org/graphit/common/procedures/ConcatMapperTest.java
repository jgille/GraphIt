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

package org.graphit.common.procedures;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author jon
 * 
 */
public class ConcatMapperTest {

    @Test
    public void testEmpty() {
        ConcatMapper<Integer> mapper = new ConcatMapper<Integer>();
        Iterable<Integer> it = mapper.map(Collections.<Iterable<Integer>> emptyList());
        assertEquals(Collections.emptyList(), asList(it));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConcatenated() {
        ConcatMapper<Integer> mapper = new ConcatMapper<Integer>();
        Iterable<Integer> it =
            mapper.map(Arrays.<Iterable<Integer>> asList(Collections.<Integer> singleton(1),
                                                         Collections.<Integer> singleton(2)));
        assertEquals(Arrays.asList(1, 2), asList(it));
    }

    private List<Integer> asList(Iterable<Integer> it) {
        List<Integer> res = new ArrayList<Integer>();
        for (Integer i : it) {
            res.add(i);
        }
        return res;
    }
}
