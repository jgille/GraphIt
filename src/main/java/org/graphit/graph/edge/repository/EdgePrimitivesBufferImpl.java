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

import org.apache.mahout.math.list.FloatArrayList;
import org.apache.mahout.math.list.LongArrayList;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;

/**
 * A buffer of edge primitives.
 * 
 * This buffer is thread safe.
 * 
 * @author jon
 * 
 */
public class EdgePrimitivesBufferImpl implements EdgePrimitivesBuffer {
    private final EdgeType edgeType;
    private final LongArrayList edges;
    private final FloatArrayList weights;

    private int numInserted = 0;
    private int numRemoved = 0;

    private final long nullEdge = createEdge(-1, -1);

    /**
     * Creates a new buffer with the given type and capacity.
     */
    public EdgePrimitivesBufferImpl(EdgeType edgeType, int capacity) {
        this.edgeType = edgeType;
        this.edges = new LongArrayList(capacity);
        this.weights = new FloatArrayList(capacity);
    }

    @Override
    public synchronized void upsert(int index, int startNode, int endNode, float weight) {
        addEdge(index, startNode, endNode);
        setWeight(index, weight);
    }

    @Override
    public synchronized EdgePrimitive get(int index) {
        if (index >= edges.size()) {
            return null;
        }
        EdgeId edgeId = new EdgeId(edgeType, index);
        long edge = edges.get(index);
        if (edge == nullEdge) {
            return null;
        }
        int startNode = getStartNode(edge);
        int endNode = getEndNode(edge);
        float weight = getWeight(index);
        return new EdgePrimitive(edgeId, startNode, endNode, weight);
    }

    @Override
    public synchronized EdgePrimitive remove(int index) {
        EdgePrimitive edge = get(index);
        if (edge != null) {
            upsert(index, -1, -1, 0);
            numRemoved++;
        }
        return edge;
    }

    @Override
    public EdgeType getEdgeType() {
        return edgeType;
    }

    private void addEdge(int index, int startNode, int endNode) {
        long edge = createEdge(startNode, endNode);
        if (index < edges.size()) {
            edges.set(index, edge);
            return;
        }

        for (int i = edges.size(); i < index; i++) {
            edges.add(nullEdge);
        }
        edges.add(edge);
        numInserted++;
    }

    private void setWeight(int index, float weight) {
        if (index < weights.size()) {
            weights.set(index, weight);
            return;
        }

        for (int i = weights.size(); i < index; i++) {
            weights.add(-1);
        }
        weights.add(weight);
    }

    private long createEdge(int startNode, int endNode) {
        long s = (long) startNode << 32;
        return s | endNode;
    }

    private int getStartNode(long edge) {
        return (int) (edge >> 32);
    }

    private int getEndNode(long edge) {
        return (int) (edge & 0x7fffffff);
    }

    private float getWeight(int index) {
        return weights.get(index);
    }

    @Override
    public synchronized int size() {
        return numInserted - numRemoved;
    }

    @Override
    public String toString() {
        return "EdgePrimitivesBufferImpl [edgeType=" + edgeType + ", edges=" + edges + ", weights="
            + weights + ", numInserted=" + numInserted + ", numRemoved=" + numRemoved + "]";
    }

}