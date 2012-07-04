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

import java.util.Iterator;
import java.util.List;

import org.apache.mahout.math.function.IntProcedure;
import org.apache.mahout.math.list.IntArrayList;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.EdgeIndexComparator;
import org.graphit.graph.edge.util.UnsortedEdgeIndexComparator;
import org.graphit.graph.traversal.EdgeDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/**
 * An implementation of an edge vector.
 *
 * Note: This class is not thread safe.
 *
 * @author jon
 */
public class EdgeVectorImpl implements EdgeVector {

    private static final int DEFAULT_CAPACITY = 10;

    private EdgeIndexComparator edgeComparator;

    private EdgeDirection direction;

    private final int rootNodeId;
    private final EdgeType edgeType;
    private final IntArrayList edges;

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
        this(rootNodeId, edgeType, DEFAULT_CAPACITY);
    }

    /**
     * Creates an edge vector with the provided initial capacity rooted at
     * rootNodeId.
     *
     * @param rootNodeId
     *            The root node.
     * @param edgeType
     *            The type of edges in this vector.
     * @param capacity
     *            The initial capacity of the vector.
     */
    public EdgeVectorImpl(int rootNodeId, EdgeType edgeType, int capacity) {
        this(rootNodeId, edgeType, new IntArrayList(capacity));
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
    public EdgeVectorImpl copy() {
        EdgeVectorImpl copy = new EdgeVectorImpl(rootNodeId, edgeType, edges.copy());
        copy.setEdgeDirection(direction);
        copy.setEdgeComparator(edgeComparator);
        return copy;
    }

    @Override
    public int getRootNode() {
        return rootNodeId;
    }

    @Override
    public void forEachEdgeId(IntProcedure procedure) {
        edges.forEach(procedure);
    }

    private int getEdgeIndex(int id) {
        return edges.lastIndexOf(id);
    }

    private int findIndexForNewEdge(int edgeId) {
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
    public void add(int edgeId) {
        int index = findIndexForNewEdge(edgeId);
        edges.beforeInsert(index, edgeId);
    }

    @Override
    public void remove(int id) {
        int index = getEdgeIndex(id);
        edges.remove(index);
    }

    @Override
    public void reindex(int edgeId) {
        if (!edgeComparator.isSorted()) {
            return;
        }
        // Removing and then re-adding will keep things sorted.
        remove(edgeId);
        add(edgeId);
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
    public <T> Iterable<T> iterable(final Converter<EdgeId, T> converter) {
        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return new EdgeIterator<T>(edgeType, edges, converter);
            }
        };
    }

    @Override
    public String toString() {
        return "EdgeVectorImpl [edgeComparator=" + edgeComparator + ", direction=" + direction
            + ", rootNodeId=" + rootNodeId + ", edgeType=" + edgeType + ", edges=" + edges + "]";
    }

    private static final class EdgeIterator<T> implements Iterator<T> {

        private final EdgeType edgeType;
        private final IntArrayList edgeIds;
        private final Converter<EdgeId, T> converter;

        private int index = 0;
        private T next = null;

        protected EdgeIterator(EdgeType edgeType, IntArrayList edgeIds,
                               Converter<EdgeId, T> converter) {
            this.edgeType = edgeType;
            this.edgeIds = edgeIds;
            this.converter = converter;
        }

        @Override
        public boolean hasNext() {
            while (next == null && index < edgeIds.size()) {
                EdgeId edgeId = new EdgeId(edgeType, edgeIds.get(index++));
                next = converter.convert(edgeId);
            }
            return next != null;
        }

        @Override
        public T next() {
            Assert.isTrue(hasNext(), "No more elements in this edge iterator");
            T res = next;
            next = null;
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

}
