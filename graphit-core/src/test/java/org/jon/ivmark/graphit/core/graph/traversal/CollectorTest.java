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
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jon
 * 
 */
public class Collector  Test {

    @Test
    public void testWithoutPredicate() {
        Collector<String> proc = new Collector<String>();
        proc.apply("A");
        proc.apply("B");
        proc.apply(null);

        List<String> elements = proc.getElements();
        assertEquals(Arrays.asList("A", "B", null), elements);
    }

    @Test
    public void testWithPredicate() {
        Predicate<String> pred = new Predicate<String>() {

            @Override
            public boolean apply(String value) {
                return value == null || !value.equals("B");
            }
        };
        Collector<String> proc = new Collector<String>(pred);
        proc.apply("A");
        proc.apply("B");
        proc.apply(null);

        List<String> elements = proc.getElements();
        assertEquals(Arrays.asList("A", null), elements);
    }

}
