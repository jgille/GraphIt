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

import com.google.common.base.Preconditions;
import org.apache.mahout.math.function.IntProcedure;
import org.apache.mahout.math.list.IntArrayList;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.EdgeIndexComparator;
import org.graphit.graph.edge.util.UnsortedEdgeIndexComparator;
import org.graphit.graph.traversal.EdgeDirection;

import java.util.Iterator;
import java.util.List;

/**
 * An implementation of an edge vector.
 *
 * @author jon
 */
public class EdgeVectorImpl implements EdgeVector {

    private EdgeIndexComparator edgeComparator;

    private EdgeDirection direction;

    private final int rootNodeId;
    private final EdgeType edgeType;
    private final IntArrayList edges;

    private static final IntArrayList EMPTY_LIST = new IntArrayList(0);

    /**
     * Creates an edge vector with a default initial capacity rooted at
     * rootNodeId.
     *
     * @param rootNodeId
     *            The root node.
     * @param edgeType
     *            The type of edges in this vector.
     */
    public EdgeVectorImpl(int rootNodeId, EdgeType edgeType) {
        this(rootNodeId, edgeType, EMPTY_LIST);
    }

    private EdgeVectorImpl(int rootNodeId, EdgeType edgeType, IntArrayList sortedEdges) {
        this.rootNodeId = rootNodeId;
        this.edgeType = edgeType;
        this.edges = sortedEdges;
        this.direction = EdgeDirection.OUTGOING;
        this.edgeComparator = new UnsortedEdgeIndexComparator();
    }

    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    @Override
    public int getRootNode() {
        return rootNodeId;
    }

    @Override
    public void forEachEdgeId(IntProcedure procedure) {
        edges.forEach(procedure);
    }

    private static int findIndexForNewEdge(int edgeId, IntArrayList edges,
                                           EdgeIndexComparator edgeComparator) {
        if (edges.isEmpty()) {
            return 0;
        }

        if (!edgeComparator.isSorted()) {
            return edges.size();
        }

        int low = 0;
        int high = edges.size() - 1;
        int mid = 0;

        while (low <= high) {
            mid = (low + high) / 2;
            int midEdgeId = edges.get(mid);
            int comparedToNewEdge = edgeComparator.compare(midEdgeId, edgeId);
            if (comparedToNewEdge < 0) {
                low = mid + 1;
            } else if (comparedToNewEdge > 0) {
                high = mid - 1;
            } else {
                return mid + 1;
            }
        }
        int midEdgeId = edges.get(mid);
        int comparedToNewEdge = edgeComparator.compare(midEdgeId, edgeId);
        if (comparedToNewEdge <= 0) {
            return mid + 1;
        }
        return mid;
    }

    @Override
    public EdgeVectorImpl add(int edgeId) {
        int[] newElements = new int[edges.size() + 1];
        int index = findIndexForNewEdge(edgeId, edges, edgeComparator);
        System.arraycopy(edges.elements(), 0, newElements, 0, index);
        newElements[index] = edgeId;
        System.arraycopy(edges.elements(), index, newElements, index + 1,
                         newElements.length - index - 1);

        IntArrayList newEdges = new IntArrayList(newElements);
        EdgeVectorImpl newEdgeVector = new EdgeVectorImpl(rootNodeId, edgeType, newEdges);
        newEdgeVector.setEdgeComparator(edgeComparator);
        newEdgeVector.setEdgeDirection(direction);
        return newEdgeVector;
    }

    @Override
    public EdgeVectorImpl remove(final int edgeId) {
        int index = edges.lastIndexOf(edgeId);
        if (index < 0) {
            return this;
        }
        int[] newElements = new int[edges.size() - 1];
        System.arraycopy(edges.elements(), 0, newElements, 0, index);
        System.arraycopy(edges.elements(), index + 1, newElements, index,
                         edges.size() - index - 1);

        IntArrayList newEdges = new IntArrayList(newElements);
        EdgeVectorImpl newEdgeVector = new EdgeVectorImpl(rootNodeId, edgeType, newEdges);
        newEdgeVector.setEdgeComparator(edgeComparator);
        newEdgeVector.setEdgeDirection(direction);
        return newEdgeVector;
    }

    @Override
    public EdgeVectorImpl reindex(int edgeId) {
        if (!edgeComparator.isSorted()) {
            return this;
        }
        // Removing and then re-adding will keep things sorted.
        // TODO: This could be made more efficient.
        return remove(edgeId).add(edgeId);
    }

    @Override
    public EdgeType getEdgeType() {
        return edgeType;
    }

    @Override
    public int size() {
        return edges.size();
    }

    @Override
    public boolean isEmpty() {
        return edges.isEmpty();
    }

    public void setEdgeDirection(EdgeDirection direction) {
        this.direction = direction;
    }

    @Override
    public EdgeDirection getEdgeDirection() {
        return direction;
    }

    @Override
    public List<Integer> asList() {
        return edges.toList();
    }

    @Override
    public Iterable<EdgeId> iterable() {
        return new Iterable<EdgeId>() {

            @Override
            public Iterator<EdgeId> iterator() {
                return new EdgeIdIterator(edgeType, edges);
            }
        };
    }

    @Override
    public String toString() {
        return "EdgeVectorImpl [edgeComparator=" + edgeComparator + ", direction=" + direction
            + ", rootNodeId=" + rootNodeId + ", edgeType=" + edgeType + ", edges=" + edges + "]";
    }

    private static final class EdgeIdIterator implements Iterator<EdgeId> {

        private final EdgeType edgeType;
        private final IntArrayList edgeIds;

        private int index = 0;
        private EdgeId next = null;

        protected EdgeIdIterator(EdgeType edgeType, IntArrayList edgeIds) {
            this.edgeType = edgeType;
            this.edgeIds = edgeIds;
        }

        @Override
        public boolean hasNext() {
            while (next == null && index < edgeIds.size()) {
                next = new EdgeId(edgeType, edgeIds.get(index++));
            }
            return next != null;
        }

        @Override
        public EdgeId next() {
            Preconditions.checkState(hasNext(), "No more elements in this edge iterator");
            EdgeId res = next;
            next = null;
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

}
