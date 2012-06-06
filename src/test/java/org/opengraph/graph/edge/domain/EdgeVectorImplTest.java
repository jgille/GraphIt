package org.opengraph.graph.edge.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.mahout.math.function.IntProcedure;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.opengraph.graph.edge.domain.EdgeDirection;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.domain.EdgeVectorImpl;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.edge.util.TestEdgeType;

/**
 * Junit tests for {@link DeprecatedEdgeVector}.
 *
 * @author jon
 *
 */
public class EdgeVectorImplTest {

    private EdgeIndexComparator mockEdgeComparator;

    @Before
    public void initMockRepos() {
        this.mockEdgeComparator = mock(EdgeIndexComparator.class, "Undefined sort");
        when(mockEdgeComparator.isSorted()).thenReturn(true);
    }

    private void mockDescendingComparator() {

        when(mockEdgeComparator.
            compare(anyInt(), anyInt())).then(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer edgeId1 = (Integer) args[0];
                Integer edgeId2 = (Integer) args[1];
                return edgeId2.compareTo(edgeId1);
            }
        });

    }

    private void mockAscendingComparator() {

        when(mockEdgeComparator.
            compare(anyInt(), anyInt())).then(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Integer edgeId1 = (Integer) args[0];
                Integer edgeId2 = (Integer) args[1];
                return edgeId1.compareTo(edgeId2);
            }
        });

    }

    private static class IntCollectorProcedure implements IntProcedure {

        final List<Integer> collected = new ArrayList<Integer>();

        @Override
        public boolean apply(int element) {
            collected.add(element);
            return true;
        }

        public List<Integer> getElements() {
            return collected;
        }
    }

    @Test
    public void testEmptyEdgeVector() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVector edgeVector = new EdgeVectorImpl(1, edgeType);
        assertThat(edgeVector.size(), Matchers.is(0));
        assertThat(edgeVector.isEmpty(), Matchers.is(true));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);
        assertThat(procedure.getElements(), Matchers.is(Collections.<Integer> emptyList()));
    }

    @Test
    public void testAddWeightedEdgesDescendingSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockDescendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);

        assertThat(edgeVector.size(), Matchers.is(4));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(4));

        assertThat(edges, Matchers.is(Arrays.asList(13, 12, 11, 10)));
    }

    @Test
    public void testAddWeightedEdgesAscendingSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockAscendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);

        assertThat(edgeVector.size(), Matchers.is(4));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(4));

        assertThat(edges, Matchers.is(Arrays.asList(10, 11, 12, 13)));
    }


    @Test
    public void testAddWeightedEdgesUndefinedSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        edgeVector.setEdgeComparator(mockEdgeComparator);
        when(mockEdgeComparator.isSorted()).thenReturn(false);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);

        assertThat(edgeVector.size(), Matchers.is(4));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(4));

        assertThat(edges, Matchers.is(Arrays.asList(11, 12, 10, 13)));
    }

    @Test
    public void testRemoveWeightedEdgeDescendingSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockDescendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);
        edgeVector.remove(12);

        assertThat(edgeVector.size(), Matchers.is(3));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(3));

        assertThat(edges, Matchers.is(Arrays.asList(13, 11, 10)));
    }

    @Test
    public void testRemoveWeightedEdgeAscendingWeightSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockAscendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);
        edgeVector.remove(12);

        assertThat(edgeVector.size(), Matchers.is(3));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(3));

        assertThat(edges, Matchers.is(Arrays.asList(10, 11, 13)));
    }

    @Test
    public void testRemoveWeightedEdgeUndefinedSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        edgeVector.setEdgeComparator(mockEdgeComparator);
        when(mockEdgeComparator.isSorted()).thenReturn(false);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);
        edgeVector.remove(12);

        assertThat(edgeVector.size(), Matchers.is(3));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(3));

        assertThat(edges, Matchers.is(Arrays.asList(11, 10, 13)));
    }

    @Test
    public void testReindexWeightedEdgeDescendingSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockDescendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);

        when(mockEdgeComparator.compare(anyInt(), eq(12))).thenReturn(-1);
        edgeVector.reindex(12);

        assertThat(edgeVector.size(), Matchers.is(4));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(4));

        assertThat(edges, Matchers.is(Arrays.asList(13, 11, 10, 12)));
    }

    @Test
    public void testReindexWeightedEdgeAscendingSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockAscendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);

        when(mockEdgeComparator.compare(anyInt(), eq(12))).thenReturn(1);
        edgeVector.reindex(12);

        assertThat(edgeVector.size(), Matchers.is(4));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(4));

        assertThat(edges, Matchers.is(Arrays.asList(12, 10, 11, 13)));
    }

    @Test
    public void testReindexWeightedEdgeUndefinedSort() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        edgeVector.setEdgeComparator(mockEdgeComparator);
        when(mockEdgeComparator.isSorted()).thenReturn(false);

        edgeVector.add(11);
        edgeVector.add(12);
        edgeVector.add(10);
        edgeVector.add(13);
        edgeVector.reindex(12);

        assertThat(edgeVector.size(), Matchers.is(4));
        assertThat(edgeVector.isEmpty(), Matchers.is(false));
        assertThat(edgeVector.getRootNode(), Matchers.is(1));
        assertThat(edgeVector.getEdgeType(), Matchers.is(edgeType));
        assertThat(edgeVector.getEdgeDirection(), Matchers.is(EdgeDirection.OUTGOING));

        IntCollectorProcedure procedure = new IntCollectorProcedure();
        edgeVector.forEachEdgeId(procedure);

        List<Integer> edges = procedure.getElements();
        assertThat(edges.size(), Matchers.is(4));

        assertThat(edges, Matchers.is(Arrays.asList(11, 12, 10, 13)));
    }
}
