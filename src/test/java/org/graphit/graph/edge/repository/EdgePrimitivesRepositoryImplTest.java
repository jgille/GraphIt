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

import static org.graphit.graph.edge.domain.TestEdgeTypes.BOUGHT;
import static org.graphit.graph.edge.domain.TestEdgeTypes.SIMILAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.domain.TestEdgeTypes;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class EdgePrimitivesRepositoryImplTest {

    private EdgePrimitivesRepositoryImpl repo;

    @Before
    public void setupRepo() {
        this.repo = new EdgePrimitivesRepositoryImpl(TestEdgeTypes.getEdgeTypes());
    }

    @Test
    public void testGetNonExistingEdge() {
        assertNull(repo.getEdge(new EdgeId(BOUGHT, 0)));
    }

    @Test
    public void testAddAndGetEdge() {
        EdgeId edgeId = repo.addEdge(1, 2, BOUGHT);
        EdgePrimitive edge = repo.getEdge(edgeId);
        assertNotNull(edge);
        assertEquals(edgeId, edge.getEdgeId());
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
    }

    @Test
    public void testAddAndGetWeightedEdge() {
        EdgeId edgeId = repo.addEdge(1, 2, SIMILAR, 1.5f);
        EdgePrimitive edge = repo.getEdge(edgeId);
        assertNotNull(edge);
        assertEquals(edgeId, edge.getEdgeId());
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
        assertEquals(1.5f, edge.getWeight(), 0.00001f);
    }

    @Test
    public void testAddAndRemoveEdge() {
        EdgeId edgeId = repo.addEdge(1, 2, BOUGHT);
        EdgePrimitive edge = repo.removeEdge(edgeId);
        assertNotNull(edge);
        assertEquals(edgeId, edge.getEdgeId());
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
        assertNull(repo.getEdge(edgeId));
    }

    @Test
    public void testAddAndRemoveWeightedEdge() {
        EdgeId edgeId = repo.addEdge(1, 2, SIMILAR, 1.5f);
        EdgePrimitive edge = repo.removeEdge(edgeId);
        assertNotNull(edge);
        assertEquals(edgeId, edge.getEdgeId());
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
        assertEquals(1.5f, edge.getWeight(), 0.00001f);
        assertNull(repo.getEdge(edgeId));
    }

    @Test
    public void testSetEdgeWeight() {
        EdgeId edgeId = repo.addEdge(1, 2, SIMILAR, 1.5f);
        repo.setEdgeWeight(edgeId, 2.5f);
        EdgePrimitive edge = repo.getEdge(edgeId);
        assertNotNull(edge);
        assertEquals(edgeId, edge.getEdgeId());
        assertEquals(1, edge.getStartNodeIndex());
        assertEquals(2, edge.getEndNodeIndex());
        assertEquals(2.5f, edge.getWeight(), 0.00001f);
    }

    @Test
    public void testGetOutgoingEdges() {
        EdgeId edgeId1 = repo.addEdge(1, 2, BOUGHT);
        EdgeId edgeId2 = repo.addEdge(1, 3, BOUGHT);
        EdgeVector nonExisting = repo.getOutgoingEdges(2, BOUGHT);
        assertNotNull(nonExisting);
        EdgeVector edges = repo.getOutgoingEdges(1, BOUGHT);
        assertNotNull(edges);
        assertEquals(2, edges.size());
        assertEquals(Arrays.asList(edgeId1.getIndex(), edgeId2.getIndex()), edges.asList());
    }

    @Test
    public void testGetOutgoingWeightedEdges() {
        EdgeId edgeId1 = repo.addEdge(1, 2, SIMILAR, 1f);
        EdgeId edgeId2 = repo.addEdge(1, 3, SIMILAR, 2f);
        EdgeVector nonExisting = repo.getOutgoingEdges(2, SIMILAR);
        assertNotNull(nonExisting);
        EdgeVector edges = repo.getOutgoingEdges(1, SIMILAR);
        assertNotNull(edges);
        assertEquals(2, edges.size());
        assertEquals(Arrays.asList(edgeId2.getIndex(), edgeId1.getIndex()), edges.asList());
    }

    @Test
    public void testGetIncomingEdges() {
        EdgeId edgeId1 = repo.addEdge(2, 1, BOUGHT);
        EdgeId edgeId2 = repo.addEdge(3, 1, BOUGHT);
        EdgeVector nonExisting = repo.getIncomingEdges(2, BOUGHT);
        assertNotNull(nonExisting);
        EdgeVector edges = repo.getIncomingEdges(1, BOUGHT);
        assertNotNull(edges);
        assertEquals(2, edges.size());
        assertEquals(Arrays.asList(edgeId1.getIndex(), edgeId2.getIndex()), edges.asList());
    }

    @Test
    public void testGetIncomingWeightedEdges() {
        EdgeId edgeId1 = repo.addEdge(2, 1, SIMILAR, 1f);
        EdgeId edgeId2 = repo.addEdge(3, 1, SIMILAR, 2f);
        EdgeVector nonExisting = repo.getIncomingEdges(2, SIMILAR);
        assertNotNull(nonExisting);
        EdgeVector edges = repo.getIncomingEdges(1, SIMILAR);
        assertNotNull(edges);
        assertEquals(2, edges.size());
        assertEquals(Arrays.asList(edgeId2.getIndex(), edgeId1.getIndex()), edges.asList());
    }

}
