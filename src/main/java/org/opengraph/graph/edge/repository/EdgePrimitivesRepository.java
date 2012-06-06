package org.opengraph.graph.edge.repository;

import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.schema.EdgeType;

public interface EdgePrimitivesRepository {

    /**
     * Adds an edge an returns the id of the new edge.
     */
    EdgeId addEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType);

    /**
     * Adds an edge an returns the id of the new edge.
     *
     * Note: This method should normally not be used, it's intended usage is
     * replaying operations from file.
     *
     * Use {@link #addEdge(int, int, EdgeType)} instead.
     */
    void addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, EdgeType edgeType);

    /**
     * Adds a weighted edge an returns the id of the new edge.
     */
    EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType, float weight);

    /**
     * Adds a weighted edge an returns the id of the new edge.
     *
     * Note: This method should normally not be used, it's intended usage is
     * replaying operations from file.
     *
     * Use {@link #addWeightedEdge(int, int, EdgeType, float)} instead.
     */
    void addWeightedEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, EdgeType edgeType,
                         float weight);

    /**
     * Gets the edge with the provided id.
     */
    EdgePrimitive getEdge(EdgeId edgeId);

    /**
     * Removes the edge with the provided id.
     */
    EdgePrimitive removeEdge(EdgeId edgeId);

    /**
     * Updates the weight for an edge.
     */
    void setEdgeWeight(EdgeId edge, float weight);

    /**
     * Removes all edges connected to a node.
     */
    void removeNode(int nodeIndex);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getOutgoingEdges(int startNodeIndex, EdgeType edgeType);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getIncomingEdges(int endNodeIndex, EdgeType edgeType);
}
