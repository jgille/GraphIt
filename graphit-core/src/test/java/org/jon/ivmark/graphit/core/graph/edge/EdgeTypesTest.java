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

package org.jon.ivmark.graphit.core.graph.edge;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author jon
 * 
 */
public class EdgeTypesTest {

    @Test
    public void testValueOfExisting() {
        EdgeTypes edgeTypes = new EdgeTypes();
        EdgeType aType = new EdgeType("A");
        EdgeType bType = new EdgeType("B");
        edgeTypes.add(aType);
        edgeTypes.add(bType);
        assertEquals(aType, edgeTypes.valueOf("A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNonExisting() {
        EdgeTypes edgeTypes = new EdgeTypes();
        edgeTypes.valueOf("C");
    }

    @Test
    public void testEmpty() {
        EdgeTypes edgeTypes = new EdgeTypes();
        assertEquals(0, edgeTypes.size());
        assertTrue(edgeTypes.elements().isEmpty());
    }

    @Test
    public void testAddValidEdgeTypes() {
        EdgeTypes edgeTypes = new EdgeTypes();
        edgeTypes.getOrAdd("A");
        edgeTypes.add(new EdgeType("B-A"));
        edgeTypes.add("C_1");

        assertEquals(3, edgeTypes.size());

        assertNotNull(edgeTypes.valueOf("A"));
        assertNotNull(edgeTypes.valueOf("B-A"));
        assertNotNull(edgeTypes.valueOf("C_1"));
    }

    @Test
    public void testAddInvalidEdgeTypes() {
        EdgeTypes edgeTypes = new EdgeTypes();
        // TODO: Use parameterized test instead
        List<String> invalid = Arrays.asList("A B", "C:", "$", "");
        for (String invalidName : invalid) {
            boolean exceptionWhenAddingName = false;
            try {
                edgeTypes.add(invalidName);
            } catch (IllegalArgumentException e) {
                exceptionWhenAddingName = true;
            }
            assertTrue(exceptionWhenAddingName);

            boolean exceptionOnGetOrAdd = false;
            try {
                edgeTypes.getOrAdd(invalidName);
            } catch (IllegalArgumentException e) {
                exceptionOnGetOrAdd = true;
            }
            assertTrue(exceptionOnGetOrAdd);

            boolean exceptionWhenAddingType = false;
            try {
                edgeTypes.add(new EdgeType(invalidName));
            } catch (IllegalArgumentException e) {
                exceptionWhenAddingType = true;
            }
            assertTrue(exceptionWhenAddingType);
        }
    }

}
