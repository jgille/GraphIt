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

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.edge.EdgeId;
import org.jon.ivmark.graphit.core.graph.edge.EdgePrimitive;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link EdgePrimitivesBuffer} that consists of multiple buffers for
 * improved concurrency.
 *
 * This buffer is thread safe.
 *
 * @author jon
 *
 */
public class ShardedEdgePrimitivesBuffer implements EdgePrimitivesBuffer {

    private final EdgeType edgeType;
    private final List<EdgePrimitivesBuffer> shards;

    /**
     * Creates a new buffer.
     *
     * @param edgeType
     *            The type of the edges in this buffer.
     * @param nShards
     *            The number of shards to use (typically related to the number
     *            of processors) .
     * @param inititalCapacity
     *            The initial capaicity, i.e. an estimation of how many edges
     *            this buffer will contain.
     */
    public ShardedEdgePrimitivesBuffer(EdgeType edgeType, int nShards, int inititalCapacity) {
        this.edgeType = edgeType;
        this.shards = new ArrayList<EdgePrimitivesBuffer>(nShards);
        for (int i = 0; i < nShards; i++) {
            shards.add(new EdgePrimitivesBufferImpl(edgeType, inititalCapacity / nShards));
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (EdgePrimitivesBuffer buffer : shards) {
            size += buffer.size();
        }
        return size;
    }

    @Override
    public void upsert(int index, int startNode, int endNode, float weight) {
        EdgePrimitivesBuffer buffer = getShard(index);
        buffer.upsert(mapIndex(index), startNode, endNode, weight);
    }

    @Override
    public EdgePrimitive get(int index) {
        EdgePrimitivesBuffer buffer = getShard(index);
        EdgePrimitive edge = buffer.get(mapIndex(index));
        if (edge == null) {
            return null;
        }
        return new EdgePrimitive(new EdgeId(edge.getEdgeType(), index), edge.getStartNodeIndex(),
                                 edge.getEndNodeIndex(), edge.getWeight());
    }

    @Override
    public EdgePrimitive remove(int index) {
        EdgePrimitivesBuffer buffer = getShard(index);
        EdgePrimitive edge = buffer.remove(mapIndex(index));
        if (edge == null) {
            return null;
        }
        return new EdgePrimitive(new EdgeId(edge.getEdgeType(), index), edge.getStartNodeIndex(),
                                 edge.getEndNodeIndex(), edge.getWeight());

    }

    private EdgePrimitivesBuffer getShard(int index) {
        Preconditions.checkArgument(index >= 0, "Index must not be negative");
        int i = index % shards.size();
        return shards.get(i);
    }

    private int mapIndex(int index) {
        return index / shards.size();
    }

    @Override
    public String toString() {
        return "ShardedEdgePrimitivesBuffer [shards=" + shards + "]";
    }

    @Override
    public EdgeType getEdgeType() {
        return edgeType;
    }
}
