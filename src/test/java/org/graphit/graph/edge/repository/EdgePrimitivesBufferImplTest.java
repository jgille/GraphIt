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

package org.graphit.graph.edge.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.TestEdgeType;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class EdgePrimitivesBufferImplTest {

    @Test
    public void testInsertWeighted() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgePrimitivesBuffer buffer = new EdgePrimitivesBufferImpl(edgeType, 10);
        assertEquals(0, buffer.size());
        buffer.upsert(0, 1, 2, 1.5f);
        assertEquals(1, buffer.size());
        EdgePrimitive edge = buffer.get(0);
        assertNotNull(edge);
        EdgeId edgeId = new EdgeId(edgeType, 0);
        assertEquals(edge.getEdgeId(), edgeId);
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
        assertEquals(1.5f, edge.getWeight(), 0.000001f);
    }

    @Test
    public void testInsertWeightedExpandList() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgePrimitivesBuffer buffer = new EdgePrimitivesBufferImpl(edgeType, 10);
        assertEquals(0, buffer.size());
        buffer.upsert(3, 1, 2, 1.5f);
        assertEquals(1, buffer.size());
        EdgePrimitive edge = buffer.get(3);
        assertNotNull(edge);
        EdgeId edgeId = new EdgeId(edgeType, 3);
        assertEquals(edge.getEdgeId(), edgeId);
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
        assertEquals(1.5f, edge.getWeight(), 0.000001f);

        for (int i = 0; i < 2; i++) {
            assertNull(buffer.get(i));
        }
    }

    @Test
    public void testGetOutOfBounds() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        EdgePrimitivesBuffer buffer = new EdgePrimitivesBufferImpl(edgeType, 10);

        boolean exception = false;
        try {
            buffer.get(-1);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);

        assertNull(buffer.get(10));
    }

    @Test
    public void testRemoveWeighted() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgePrimitivesBuffer buffer = new EdgePrimitivesBufferImpl(edgeType, 10);
        assertEquals(0, buffer.size());
        buffer.upsert(0, 1, 2, 1.5f);
        buffer.upsert(1, 1, 3, 2.5f);
        buffer.upsert(2, 1, 4, 3.5f);

        assertEquals(3, buffer.size());

        EdgePrimitive edge2 = buffer.remove(1);
        assertNotNull(edge2);
        EdgeId edgeId2 = new EdgeId(edgeType, 1);
        assertEquals(edge2.getEdgeId(), edgeId2);
        assertEquals(1, edge2.getStartNodeIndex());
        assertEquals(3, edge2.getEndNodeIndex());
        assertEquals(2.5f, edge2.getWeight(), 0.000001f);

        assertEquals(2, buffer.size());
        assertNull(buffer.get(1));
        assertNull(buffer.remove(1));

        EdgePrimitive edge1 = buffer.get(0);
        assertNotNull(edge1);
        EdgeId edgeId1 = new EdgeId(edgeType, 0);
        assertEquals(edge1.getEdgeId(), edgeId1);
        assertEquals(1, edge1.getStartNodeIndex());
        assertEquals(2, edge1.getEndNodeIndex());
        assertEquals(1.5f, edge1.getWeight(), 0.000001f);

        EdgePrimitive edge3 = buffer.get(2);
        assertNotNull(edge3);
        EdgeId edgeId3 = new EdgeId(edgeType, 2);
        assertEquals(edge3.getEdgeId(), edgeId3);
        assertEquals(1, edge3.getStartNodeIndex());
        assertEquals(4, edge3.getEndNodeIndex());
        assertEquals(3.5f, edge3.getWeight(), 0.000001f);
    }

    @Test
    public void testRemoveOutofBounds() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        EdgePrimitivesBuffer buffer = new EdgePrimitivesBufferImpl(edgeType, 10);

        boolean exception = false;
        try {
            buffer.remove(-1);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
        assertNull(buffer.remove(10));
    }
}
