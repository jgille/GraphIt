package org.opengraph.graph.edge.repository;

import java.util.HashMap;
import java.util.Map;

import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.schema.EdgeTypes;
import org.opengraph.graph.schema.GraphMetadata;
import org.springframework.util.Assert;

public class ByteBufferEdgePrimitivesRepository implements EdgePrimitivesRepository {

    private final Map<EdgeType, TypedEdgePrimitivesRepository> repos;

    public ByteBufferEdgePrimitivesRepository(GraphMetadata metadata) {
        EdgeTypes edgeTypes = metadata.getEdgeTypes();
        this.repos = new HashMap<EdgeType, TypedEdgePrimitivesRepository>(edgeTypes.size(), 1f);
        for (EdgeType edgeType : edgeTypes.elements()) {
            repos.put(edgeType, createRepo(edgeType));
        }
    }

    private TypedEdgePrimitivesRepository createRepo(EdgeType edgeType) {
        return new ByteBufferTypedEdgePrimitivesRepository(edgeType);
    }

    private TypedEdgePrimitivesRepository getRepository(EdgeType edgeType) {
        Assert.isTrue(repos.containsKey(edgeType), "Invalid edge type");
        return repos.get(edgeType);
    }

    @Override
    public EdgeId addEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType) {
        return getRepository(edgeType).addEdge(startNodeIndex, endNodeIndex);
    }

    @Override
    public void addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, EdgeType edgeType) {
        getRepository(edgeType).addEdge(edgeId, startNodeIndex, endNodeIndex);
    }

    @Override
    public EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType,
                                  float weight) {
        return getRepository(edgeType).addWeightedEdge(startNodeIndex, endNodeIndex, weight);
    }

    @Override
    public void addWeightedEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex,
                                EdgeType edgeType, float weight) {
        getRepository(edgeType).addWeightedEdge(edgeId, startNodeIndex, endNodeIndex, weight);
    }

    @Override
    public EdgePrimitive getEdge(EdgeId edgeId) {
        return getRepository(edgeId.getEdgeType()).getEdge(edgeId);
    }

    @Override
    public EdgePrimitive removeEdge(EdgeId edgeId) {
        return getRepository(edgeId.getEdgeType()).removeEdge(edgeId);
    }

    @Override
    public EdgeVector getOutgoingEdges(int startNodeIndex, EdgeType edgeType) {
        return getRepository(edgeType).getOutgoingEdges(startNodeIndex);
    }

    @Override
    public EdgeVector getIncomingEdges(int endNodeIndex, EdgeType edgeType) {
        return getRepository(edgeType).getIncomingEdges(endNodeIndex);
    }

    @Override
    public void removeNode(int nodeIndex) {
        for (TypedEdgePrimitivesRepository repo : repos.values()) {
            repo.removeNode(nodeIndex);
        }
    }

    @Override
    public void setEdgeWeight(EdgeId edgeId, float weight) {
        EdgeType edgeType = edgeId.getEdgeType();
        Assert.isTrue(edgeType.isWeighted(), edgeType + " edges are unweighted.");
        getRepository(edgeType).setEdgeWeight(edgeId, weight);
    }

    @Override
    public String toString() {
        return "ByteBufferEdgeIndex [repos=" + repos + "]";
    }

}
