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

package org.jon.ivmark.graphit.core.graph.edge.repository;

import static org.jon.ivmark.graphit.core.graph.edge.domain.TestEdgeTypes.BOUGHT;
import static org.jon.ivmark.graphit.core.graph.edge.domain.TestEdgeTypes.SIMILAR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.jon.ivmark.graphit.core.graph.edge.domain.EdgeId;
import org.jon.ivmark.graphit.core.graph.edge.domain.EdgePrimitive;
import org.jon.ivmark.graphit.core.graph.edge.domain.EdgeVector;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeType;
import org.jon.ivmark.graphit.core.graph.exception.DuplicateKeyException;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public abstract class AbstractTypedEdgePrimitivesRepositoryTest {

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    protected abstract TypedEdgePrimitivesRepository createRepo(EdgeType edgeType,
                                                                int initialCapacity);

    @Test
    public void testAddSingleEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is(SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddDuplicateEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);
        boolean exception = false;
        try {
            repo.addWeightedEdge(edgeId, 2, 5, 10);
        } catch (DuplicateKeyException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testGetEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(100f));
    }

    @Test
    public void testGetNonExistingEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.getEdge(new EdgeId(SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testRemoveEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveNonExistingEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.removeEdge(new EdgeId(SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.removeEdge(new EdgeId(SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetOutgoingEdgesForNode() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges.asList().isEmpty(), Matchers.is(true));

        EdgeId edgeId1 = repo.addWeightedEdge(1, 2, 100);
        repo.addWeightedEdge(2, 4, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(1, 3, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(1, 4, 10);

        edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId3.getIndex(), edgeId1.getIndex(),
                                             edgeId4.getIndex())));
    }

    @Test
    public void testGetIncomingEdgesForNode() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges.asList().isEmpty(), Matchers.is(true));

        EdgeId edgeId1 = repo.addWeightedEdge(2, 1, 100);
        repo.addWeightedEdge(4, 2, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(3, 1, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(4, 1, 10);

        edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId3.getIndex(), edgeId1.getIndex(),
                                             edgeId4.getIndex())));
    }

    @Test
    public void testExpandCapacityEdges() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        int edgeCount = 20;
        Random random = new Random(1);
        for (int i = 0; i < edgeCount; i++) {
            int startNodeId = Math.abs(random.nextInt());
            int endNodeId = Math.abs(random.nextInt());
            float weight = random.nextFloat();
            EdgeId edgeId =
                repo.addWeightedEdge(startNodeId, endNodeId, weight);
            assertThat(edgeId, Matchers.notNullValue());
            EdgePrimitive edge = repo.getEdge(edgeId);
            assertThat(edge, Matchers.notNullValue());
            assertThat(edge.getEdgeId(), Matchers.is(edgeId));
            assertThat(edge.getStartNodeIndex(), Matchers.is(startNodeId));
            assertThat(edge.getEndNodeIndex(), Matchers.is(endNodeId));
            assertThat(edge.getWeight(), Matchers.is(weight));
        }
    }

    @Test
    public void testGetEdgeWeightForEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        assertTrue(repo.getEdgeWeight(0) < 0);
        repo.addWeightedEdge(0, 1, 10);
        assertEquals(10, repo.getEdgeWeight(0), 0.000001f);
    }

    @Test
    public void testReUseIndex() {
        TypedEdgePrimitivesRepository repo = createRepo(BOUGHT, 10);
        EdgeId edgeId1 = repo.addEdge(2, 1);
        repo.addEdge(3, 1);
        repo.removeEdge(edgeId1);
        EdgeId edgeId3 = repo.addEdge(3, 2);
        assertEquals(edgeId1, edgeId3);
    }

    @Test
    public void testSetEdgeWeight() {
        TypedEdgePrimitivesRepository repo = createRepo(SIMILAR, 10);
        EdgeId edgeId1 = repo.addWeightedEdge(2, 1, 1.5f);
        EdgeId edgeId2 = repo.addWeightedEdge(2, 3, 0.25f);
        EdgeId edgeId3 = repo.addWeightedEdge(2, 4, 0.5f);
        assertEquals(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId2.getIndex()),
                     repo.getOutgoingEdges(2).asList());

        repo.setEdgeWeight(edgeId2, 4.5f);
        assertEquals(4.5f, repo.getEdgeWeight(edgeId2.getIndex()), 0.000001f);
        assertEquals(4.5f, repo.getEdge(edgeId2).getWeight(), 0.000001f);

        assertEquals(Arrays.asList(edgeId2.getIndex(), edgeId1.getIndex(), edgeId3.getIndex()),
                     repo.getOutgoingEdges(2).asList());
    }
}
