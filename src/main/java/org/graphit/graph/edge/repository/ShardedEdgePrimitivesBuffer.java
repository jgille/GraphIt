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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.graphit.common.procedures.Procedure;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;
import org.springframework.util.Assert;

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
     *            The number of shards to use (should typically be 1 or 2 x the
     *            number of processors or so).
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
        Assert.isTrue(index >= 0, "Index must not be negative");
        int i = index % shards.size();
        synchronized (this) {
            return shards.get(i);
        }
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

    @Override
    public synchronized void forEach(final Procedure<EdgePrimitive> procedure) {
        final AtomicInteger shardIndex = new AtomicInteger(0);
        final int nShards = shards.size();
        for (EdgePrimitivesBuffer buffer : shards) {
            buffer.forEach(new Procedure<EdgePrimitive>() {

                @Override
                public boolean apply(EdgePrimitive edge) {
                    int index = edge.getIndex();
                    int convertedIndex = index * nShards + shardIndex.get();
                    EdgePrimitive converted =
                        new EdgePrimitive(new EdgeId(edge.getEdgeType(),
                                                     convertedIndex),
                                          edge.getStartNodeIndex(),
                                          edge.getEndNodeIndex(),
                                          edge.getWeight());
                    return procedure.apply(converted);
                }
            });
            shardIndex.incrementAndGet();
        }
    }

}