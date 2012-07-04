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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;

/**
 * @author jon
 *
 */
public class CollectorProcedureTest {

    @Test
    public void testWithoutPredicate() {
        CollectorProcedure<String> proc = new CollectorProcedure<String>();
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
        CollectorProcedure<String> proc = new CollectorProcedure<String>(pred);
        proc.apply("A");
        proc.apply("B");
        proc.apply(null);

        List<String> elements = proc.getElements();
        assertEquals(Arrays.asList("A", null), elements);
    }

}
