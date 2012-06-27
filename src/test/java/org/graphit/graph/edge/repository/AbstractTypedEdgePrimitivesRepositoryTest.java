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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.TestEdgeType;
import org.graphit.graph.exception.DuplicateKeyException;
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
    public void testAddSingleWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddSingleUnweightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.BOUGHT));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddDuplicateWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
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
    public void testAddDuplicateUnweightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        boolean exception = false;
        try {
            repo.addEdge(edgeId, 2, 5);
        } catch (DuplicateKeyException e) {
            exception = true;
        }
        assertTrue(exception);
    }


    @Test
    public void testAddSingleUnweightedEdgeForWeightedEdgeType() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testGetWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(100f));
    }

    @Test
    public void testGetUnweightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(0f));
    }

    @Test
    public void testGetNonExistingWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.getEdge(new EdgeId(TestEdgeType.SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(TestEdgeType.SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetNonExistingUnweightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        repo.addEdge(1, 2);

        assertThat(repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetUnweightedEdgeForWeightedEdgeType() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(0f));
    }

    @Test
    public void testRemoveWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveUnWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveNonExistingWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.removeEdge(new EdgeId(TestEdgeType.SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.removeEdge(new EdgeId(TestEdgeType.SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetOutgoingWeightedEdgesForNode() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges.asList().isEmpty(), Matchers.is(true));

        EdgeId edgeId1 = repo.addWeightedEdge(1, 2, 100);
        repo.addWeightedEdge(2, 4, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(1, 3, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(1, 4, 10);

        edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId4.getIndex(), edgeId3.getIndex(),
                                             edgeId1.getIndex())));
    }

    @Test
    public void testGetOutgoingUnweightedEdgesForNode() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges.asList().isEmpty(), Matchers.is(true));

        EdgeId edgeId1 = repo.addEdge(1, 2);
        repo.addEdge(2, 4);
        EdgeId edgeId3 = repo.addEdge(1, 3);
        EdgeId edgeId4 = repo.addEdge(1, 4);

        edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetIncomingWeightedEdgesForNode() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges.asList().isEmpty(), Matchers.is(true));

        EdgeId edgeId1 = repo.addWeightedEdge(2, 1, 100);
        repo.addWeightedEdge(4, 2, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(3, 1, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(4, 1, 10);

        edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId4.getIndex(), edgeId3.getIndex(),
                                             edgeId1.getIndex())));
    }

    @Test
    public void testGetIncomingUnweightedEdgesForNode() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges.asList().isEmpty(), Matchers.is(true));

        EdgeId edgeId1 = repo.addEdge(2, 1);
        repo.addEdge(4, 2);
        EdgeId edgeId3 = repo.addEdge(3, 1);
        EdgeId edgeId4 = repo.addEdge(4, 1);

        edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testExpandCapacityWeightedEdges() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
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
    public void testExpandCapacityUnweightedEdges() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        int edgeCount = 20;
        Random random = new Random(1);
        for (int i = 0; i < edgeCount; i++) {
            int startNodeId = Math.abs(random.nextInt());
            int endNodeId = Math.abs(random.nextInt());
            EdgeId edgeId =
                repo.addEdge(startNodeId, endNodeId);
            assertThat(edgeId, Matchers.notNullValue());
            EdgePrimitive edge = repo.getEdge(edgeId);
            assertThat(edge, Matchers.notNullValue());
            assertThat(edge.getEdgeId(), Matchers.is(edgeId));
            assertThat(edge.getStartNodeIndex(), Matchers.is(startNodeId));
            assertThat(edge.getEndNodeIndex(), Matchers.is(endNodeId));
        }
    }

    @Test
    public void testGetEdgeWeightForWeightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        assertTrue(repo.getEdgeWeight(0) < 0);
        repo.addWeightedEdge(0, 1, 10);
        assertEquals(10, repo.getEdgeWeight(0), 0.000001f);
    }

    @Test
    public void testGetEdgeWeightForUnweightedEdge() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        assertEquals(0, repo.getEdgeWeight(0), 0.000001f);
        repo.addEdge(0, 1);
        assertEquals(0, repo.getEdgeWeight(0), 0.000001f);
    }

    @Test
    public void testReUseIndex() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId1 = repo.addEdge(2, 1);
        repo.addEdge(3, 1);
        repo.removeEdge(edgeId1);
        EdgeId edgeId3 = repo.addEdge(3, 2);
        assertEquals(edgeId1, edgeId3);
    }

    @Test
    public void testSetEdgeWeight() {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(2, 1, 1.5f);
        EdgeId edgeId2 = repo.addWeightedEdge(3, 1, 2.5f);
        repo.addWeightedEdge(2, 1, 0.5f);
        repo.setEdgeWeight(edgeId2, 4.5f);
        assertEquals(4.5f, repo.getEdgeWeight(edgeId2.getIndex()), 0.000001f);
        assertEquals(4.5f, repo.getEdge(edgeId2).getWeight(), 0.000001f);
    }
}
