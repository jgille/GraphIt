package org.opengraph.graph.edge.repositories;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Random;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.repository.ByteBufferTypedEdgePrimitivesRepository;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.util.TestEdgeType;

public class ByteBufferEdgePrimitivesRepositoryTest {

    @Test
    public void testAddSingleWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddSingleUnweightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.BOUGHT));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddSingleUnweightedEdgeForWeightedEdgeType() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testGetWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
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
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
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
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.getEdge(new EdgeId(TestEdgeType.SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(TestEdgeType.SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetNonExistingUnweightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        repo.addEdge(1, 2);

        assertThat(repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetUnweightedEdgeForWeightedEdgeType() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
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
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveUnWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveNonExistingWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.removeEdge(new EdgeId(TestEdgeType.SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.removeEdge(new EdgeId(TestEdgeType.SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetOutgoingWeightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId1 = repo.addWeightedEdge(1, 2, 100);
        repo.addWeightedEdge(2, 4, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(1, 3, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(1, 4, 10);

        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId3.getIndex(), edgeId1.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetOutgoingUnweightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId1 = repo.addEdge(1, 2);
        repo.addEdge(2, 4);
        EdgeId edgeId3 = repo.addEdge(1, 3);
        EdgeId edgeId4 = repo.addEdge(1, 4);

        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetIncomingWeightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId1 = repo.addWeightedEdge(2, 1, 100);
        repo.addWeightedEdge(4, 2, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(3, 1, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(4, 1, 10);

        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId3.getIndex(), edgeId1.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetIncomingUnweightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId1 = repo.addEdge(2, 1);
        repo.addEdge(4, 2);
        EdgeId edgeId3 = repo.addEdge(3, 1);
        EdgeId edgeId4 = repo.addEdge(4, 1);

        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testExpandBufferListWeightedEdges() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
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
    public void testExpandBufferLisUnweightedEdges() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
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

}
