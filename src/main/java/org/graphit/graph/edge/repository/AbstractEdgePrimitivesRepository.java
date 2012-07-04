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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.springframework.util.Assert;

/**
 * An {@link EdgePrimitivesRepository} backed by a collection of
 * {@link ByteBufferTypedEdgePrimitivesRepository} instances.
 *
 * @author jon
 *
 */
public abstract class AbstractEdgePrimitivesRepository implements EdgePrimitivesRepository {

    private final Map<EdgeType, TypedEdgePrimitivesRepository> repos;

    /**
     * Constructs a new repo for the provided edge types.
     */
    public AbstractEdgePrimitivesRepository(EdgeTypes edgeTypes) {
        this.repos = new ConcurrentHashMap<EdgeType, TypedEdgePrimitivesRepository>();
        for (EdgeType edgeType : edgeTypes.elements()) {
            repos.put(edgeType, createRepo(edgeType));
        }
    }

    protected abstract TypedEdgePrimitivesRepository createRepo(EdgeType edgeType);

    private TypedEdgePrimitivesRepository getOrCreateRepository(EdgeType edgeType) {
        if (!repos.containsKey(edgeType)) {
            TypedEdgePrimitivesRepository repo = createRepo(edgeType);
            repos.put(edgeType, repo);
            return repo;
        }
        return repos.get(edgeType);
    }

    @Override
    public EdgeId addEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType) {
        return getOrCreateRepository(edgeType).addEdge(startNodeIndex, endNodeIndex);
    }

    @Override
    public EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType,
                                  float weight) {
        return getOrCreateRepository(edgeType).addWeightedEdge(startNodeIndex, endNodeIndex, weight);
    }

    @Override
    public EdgePrimitive getEdge(EdgeId edgeId) {
        return getOrCreateRepository(edgeId.getEdgeType()).getEdge(edgeId);
    }

    @Override
    public EdgePrimitive removeEdge(EdgeId edgeId) {
        return getOrCreateRepository(edgeId.getEdgeType()).removeEdge(edgeId);
    }

    @Override
    public EdgeVector getOutgoingEdges(int startNodeIndex, EdgeType edgeType) {
        return getOrCreateRepository(edgeType).getOutgoingEdges(startNodeIndex);
    }

    @Override
    public EdgeVector getIncomingEdges(int endNodeIndex, EdgeType edgeType) {
        return getOrCreateRepository(edgeType).getIncomingEdges(endNodeIndex);
    }

    @Override
    public void setEdgeWeight(EdgeId edgeId, float weight) {
        EdgeType edgeType = edgeId.getEdgeType();
        Assert.isTrue(edgeType.isWeighted(), edgeType + " edges are unweighted.");
        getOrCreateRepository(edgeType).setEdgeWeight(edgeId, weight);
    }

    @Override
    public String toString() {
        return "ByteBufferEdgeIndex [repos=" + repos + "]";
    }
}
