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

package org.graphit.graph.edge.domain;

import static org.graphit.graph.edge.domain.TestEdgeTypes.SIMILAR;
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
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.EdgeIndexComparator;
import org.graphit.graph.traversal.EdgeDirection;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        EdgeType edgeType = SIMILAR;
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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockDescendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockAscendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        edgeVector.setEdgeComparator(mockEdgeComparator);
        when(mockEdgeComparator.isSorted()).thenReturn(false);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockDescendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13).remove(12);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockAscendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13).remove(12);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        edgeVector.setEdgeComparator(mockEdgeComparator);
        when(mockEdgeComparator.isSorted()).thenReturn(false);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13).remove(12);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockDescendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13);

        when(mockEdgeComparator.compare(anyInt(), eq(12))).thenReturn(-1);
        edgeVector = edgeVector.reindex(12);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        mockAscendingComparator();

        edgeVector.setEdgeComparator(mockEdgeComparator);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13);

        when(mockEdgeComparator.compare(anyInt(), eq(12))).thenReturn(1);
        edgeVector = edgeVector.reindex(12);

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
        EdgeType edgeType = SIMILAR;
        EdgeVectorImpl edgeVector = new EdgeVectorImpl(1, edgeType);
        edgeVector.setEdgeComparator(mockEdgeComparator);
        when(mockEdgeComparator.isSorted()).thenReturn(false);

        edgeVector = edgeVector.add(11).add(12).add(10).add(13).reindex(12);

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
