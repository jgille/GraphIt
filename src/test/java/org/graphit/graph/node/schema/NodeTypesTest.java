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

package org.graphit.graph.node.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author jon
 *
 */
public class NodeTypesTest {

    @Test
    public void testAddValidNodeTypes() {
        NodeTypes nodeTypes = new NodeTypes();
        nodeTypes.getOrAdd("A");
        nodeTypes.add(new NodeTypeImpl("B-A"));
        nodeTypes.add("C_1");

        assertEquals(3, nodeTypes.size());

        assertNotNull(nodeTypes.valueOf("A"));
        assertNotNull(nodeTypes.valueOf("B-A"));
        assertNotNull(nodeTypes.valueOf("C_1"));
    }

    @Test
    public void testAddInvalidNodeTypes() {
        NodeTypes nodeTypes = new NodeTypes();
        List<String> invalid = Arrays.asList("A B", "C:", "$", "");
        for (String invalidName : invalid) {
            boolean exceptionWhenAddingName = false;
            try {
                nodeTypes.add(invalidName);
            } catch (IllegalArgumentException e) {
                exceptionWhenAddingName = true;
            }
            assertTrue(exceptionWhenAddingName);

            boolean exceptionOnGetOrAdd = false;
            try {
                nodeTypes.getOrAdd(invalidName);
            } catch (IllegalArgumentException e) {
                exceptionOnGetOrAdd = true;
            }
            assertTrue(exceptionOnGetOrAdd);

            boolean exceptionWhenAddingType = false;
            try {
                nodeTypes.add(new NodeTypeImpl(invalidName));
            } catch (IllegalArgumentException e) {
                exceptionWhenAddingType = true;
            }
            assertTrue(exceptionWhenAddingType);
        }
    }

}
