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

package org.graphit.graph.edge.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author jon
 *
 */
public class EdgeTypesImplTest {

    @Test
    public void testValueOfExisting() {
        EdgeTypesImpl edgeTypes = new EdgeTypesImpl();
        EdgeType aType = new EdgeTypeImpl("A");
        EdgeType bType = new EdgeTypeImpl("B");
        edgeTypes.add(aType).add(bType);
        assertEquals(aType, edgeTypes.valueOf("A"));
    }

    @Test
    public void testValueOfNonExisting() {
        EdgeTypesImpl edgeTypes = new EdgeTypesImpl();
        EdgeType aType = new EdgeTypeImpl("A");
        EdgeType bType = new EdgeTypeImpl("B");
        edgeTypes.add(aType).add(bType);
        assertEquals(aType, edgeTypes.valueOf("A"));
        assertEquals(bType, edgeTypes.valueOf("B"));

        EdgeType cType = new EdgeTypeImpl("C");
        assertEquals(cType, edgeTypes.valueOf("C"));
    }

    @Test
    public void testEmpty() {
        EdgeTypesImpl edgeTypes = new EdgeTypesImpl();
        assertEquals(0, edgeTypes.size());
        assertTrue(edgeTypes.elements().isEmpty());
    }
}
