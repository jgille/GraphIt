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
public class ShardedEdgePrimitivesBufferTest {

    @Test
    public void testInsertAndGetFromMultipleShards() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        EdgePrimitivesBuffer buffer = new ShardedEdgePrimitivesBuffer(edgeType, 3, 10);

        for (int i = 0; i < 4; i++) {
            buffer.upsert(i, i + 1, i + 2, 0);
        }

        assertEquals(4, buffer.size());

        for (int i = 0; i < 4; i++) {
            EdgePrimitive edge = buffer.get(i);
            assertNotNull(edge);
            assertEquals(new EdgeId(edgeType, i), edge.getEdgeId());
            assertEquals(i + 1, edge.getStartNodeIndex());
            assertEquals(i + 2, edge.getEndNodeIndex());
            assertEquals(0f, edge.getWeight(), 0.000001f);
        }
    }

    @Test
    public void testInsertAndRemoveFromMultipleShards() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        EdgePrimitivesBuffer buffer = new ShardedEdgePrimitivesBuffer(edgeType, 3, 10);

        for (int i = 0; i < 4; i++) {
            buffer.upsert(i, i + 1, i + 2, 0);
        }

        assertEquals(4, buffer.size());

        for (int i = 0; i < 4; i++) {
            EdgePrimitive edge = buffer.remove(i);
            assertNotNull(edge);
            assertEquals(new EdgeId(edgeType, i), edge.getEdgeId());
            assertEquals(i + 1, edge.getStartNodeIndex());
            assertEquals(i + 2, edge.getEndNodeIndex());
            assertEquals(0f, edge.getWeight(), 0.000001f);
        }

        for (int i = 0; i < 4; i++) {
            assertNull(buffer.get(i));
        }
    }

    @Test
    public void testGetOutOfBounds() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        EdgePrimitivesBuffer buffer = new ShardedEdgePrimitivesBuffer(edgeType, 3, 10);

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
    public void testRemoveOutOfBounds() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        EdgePrimitivesBuffer buffer = new ShardedEdgePrimitivesBuffer(edgeType, 3, 10);

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
