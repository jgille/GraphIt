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

package org.jon.ivmark.graphit.core.graph.edge.domain;

import static org.jon.ivmark.graphit.core.graph.edge.domain.TestEdgeTypes.SIMILAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * @author jon
 *
 */
public class EdgePrimitiveTest {

    @Test
    public void testGetters() {
        EdgeId edgeId = new EdgeId(SIMILAR, 1);
        EdgePrimitive edge = new EdgePrimitive(edgeId, 2, 3, 4.5f);
        assertEquals(edgeId, edge.getEdgeId());
        assertEquals(edgeId.getIndex(), edge.getIndex());
        assertEquals(edgeId.getEdgeType(), edge.getEdgeType());
        assertEquals(2, edge.getStartNodeIndex());
        assertEquals(3, edge.getEndNodeIndex());
        assertEquals(4.5f, edge.getWeight(), 0.000001f);
    }

    @Test
    public void testReverse() {
        EdgeId edgeId = new EdgeId(SIMILAR, 1);
        EdgePrimitive edge = new EdgePrimitive(edgeId, 2, 3, 4.5f);
        EdgePrimitive reversed = edge.reverse();

        assertNotSame(edge, reversed);

        assertEquals(edgeId, reversed.getEdgeId());
        assertEquals(edgeId.getIndex(), reversed.getIndex());
        assertEquals(edgeId.getEdgeType(), reversed.getEdgeType());
        assertEquals(3, reversed.getStartNodeIndex());
        assertEquals(2, reversed.getEndNodeIndex());
        assertEquals(4.5f, reversed.getWeight(), 0.000001f);
    }

    @Test
    public void testEquals() {
        EdgeId edgeId1 = new EdgeId(SIMILAR, 1);
        EdgePrimitive edge1 = new EdgePrimitive(edgeId1, 2, 3, 4.5f);

        EdgeId edgeId2 = new EdgeId(SIMILAR, 1);
        EdgePrimitive edge2 = new EdgePrimitive(edgeId2, 2, 3, 4.5f);

        EdgeId edgeId3 = new EdgeId(SIMILAR, 2);
        EdgePrimitive edge3 = new EdgePrimitive(edgeId3, 2, 3, 4.5f);

        assertEquals(edge1, edge1);
        assertEquals(edge1, edge2);
        assertFalse(edge1.equals(edge3));
    }

    @Test
    public void testHashCode() {
        EdgeId edgeId1 = new EdgeId(SIMILAR, 1);
        EdgePrimitive edge1 = new EdgePrimitive(edgeId1, 2, 3, 4.5f);

        EdgeId edgeId2 = new EdgeId(SIMILAR, 2);
        EdgePrimitive edge2 = new EdgePrimitive(edgeId2, 2, 3, 4.5f);

        assertEquals(edge1.hashCode(), edge1.hashCode());
        assertFalse(edge1.hashCode() == edge2.hashCode());
    }
}
