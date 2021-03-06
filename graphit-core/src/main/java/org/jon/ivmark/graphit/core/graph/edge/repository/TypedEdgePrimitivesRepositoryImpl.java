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
import org.jon.ivmark.graphit.core.ConcurrencyConstants;
import org.jon.ivmark.graphit.core.graph.edge.EdgeId;
import org.jon.ivmark.graphit.core.graph.edge.EdgePrimitive;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.exception.DuplicateKeyException;

/**
 *
 * A {@link TypedEdgePrimitivesRepository} implementation backed by primitive
 * collections.
 *
 * @author jon
 *
 */
public class TypedEdgePrimitivesRepositoryImpl extends AbstractTypedEdgePrimitivesRepository {

    private static final int DEFAULT_CAPACITY = 1000;
    private final EdgePrimitivesBuffer buffer;

    /**
     * Constructs a new repo for the given edge type with a default backing
     * buffer with default initial capacity.
     */
    public TypedEdgePrimitivesRepositoryImpl(EdgeType edgeType) {
        this(edgeType, DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new repo for the given edge type with a default backing
     * buffer with the provided initial capacity
     */
    public TypedEdgePrimitivesRepositoryImpl(EdgeType edgeType, int initialCapacity) {
        this(edgeType,
             new ConcurrentEdgePrimitivesBuffer(edgeType,
                                             ConcurrencyConstants.DEFAULT_CONCURRENCY_LEVEL,
                                             initialCapacity));
    }

    /**
     * Constructs a new repo for the given edge type using a custom backing
     * buffer.
     */
    public TypedEdgePrimitivesRepositoryImpl(EdgeType edgeType, EdgePrimitivesBuffer buffer) {
        super(edgeType);
        this.buffer = buffer;
    }

    @Override
    public EdgeId addEdge(int startNodeIndex, int endNodeIndex) {
        EdgeId edgeId = generateEdgeId();
        addEdge(edgeId, startNodeIndex, endNodeIndex);
        return edgeId;
    }

    @Override
    public void addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex) {
        addWeightedEdge(edgeId, startNodeIndex, endNodeIndex, 0);
    }

    @Override
    public EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, float weight) {
        EdgeId edgeId = generateEdgeId();
        addWeightedEdge(edgeId, startNodeIndex, endNodeIndex, weight);
        return edgeId;
    }

    @Override
    public void addWeightedEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight) {
        validate(edgeId);
        EdgePrimitive previous = getEdge(edgeId);
        if (previous != null) {
            throw new DuplicateKeyException(edgeId);
        }
        buffer.upsert(edgeId.getIndex(), startNodeIndex, endNodeIndex, weight);
        insert(new EdgePrimitive(edgeId, startNodeIndex, endNodeIndex, weight));
    }

    @Override
    public EdgePrimitive getEdge(EdgeId edgeId) {
        validate(edgeId);
        return buffer.get(edgeId.getIndex());
    }

    @Override
    public EdgePrimitive removeEdge(EdgeId edgeId) {
        validate(edgeId);
        EdgePrimitive edge = buffer.remove(edgeId.getIndex());
        if (edge != null) {
            delete(edge);
        }
        return edge;
    }

    @Override
    public void setEdgeWeight(EdgeId edgeId, float weight) {
        EdgePrimitive edge = getEdge(edgeId);
        Preconditions.checkNotNull(edge);
        buffer.upsert(edgeId.getIndex(), edge.getStartNodeIndex(), edge.getEndNodeIndex(), weight);
        reindex(new EdgePrimitive(edgeId, edge.getStartNodeIndex(), edge.getEndNodeIndex(), weight));
    }

    @Override
    public float getEdgeWeight(int edgeIndex) {
        EdgePrimitive edge = buffer.get(edgeIndex);
        if (edge == null) {
            return -1;
        }
        return edge.getWeight();
    }

    private void validate(EdgeId edgeId) {
        Preconditions.checkArgument(getEdgeType().equals(edgeId.getEdgeType()), "Illegal edge type");
    }

    @Override
    public int size() {
        return buffer.size();
    }
}
