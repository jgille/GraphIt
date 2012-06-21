package org.graphit.graph.edge.repository;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.repository.GraphRepository;

/**
 * A repo containing methods related to storing {@link EdgePrimitive}s.
 * 
 * @author jon
 * 
 */
public interface EdgePrimitivesRepository extends GraphRepository {

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
     * Gets the outgoing edges for a node.
     */
    EdgeVector getOutgoingEdges(int startNodeIndex, EdgeType edgeType);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getIncomingEdges(int endNodeIndex, EdgeType edgeType);
}
