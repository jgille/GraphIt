package org.opengraph.graph.edge.repository;

import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.util.EdgeWeigher;
import org.opengraph.graph.repository.GraphRepository;

/**
 * Repository containing {@link EdgePrimitive}s for all edges of an
 * {@link EdgeType} in a graph.
 *
 * @author jon
 *
 */
public interface TypedEdgePrimitivesRepository extends EdgeWeigher, GraphRepository {

    EdgeType getEdgeType();

    /**
     * Adds an edge an returns the id of the new edge.
     */
    EdgeId addEdge(int startNodeIndex, int endNodeIndex);

    /**
     * Adds an edge.
     *
     * Note: This method should normally not be used, it's intended usage is
     * replaying operations from file.
     *
     * Use {@link #addEdge(int, int)} instead.
     */
    void addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex);

    /**
     * Adds a weighted edge an returns the id of the new edge.
     */
    EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, float weight);

    /**
     * Adds a weighted edge.
     *
     *
     * Note: This method should normally not be used, it's intended usage is
     * replaying operations from file.
     *
     * Use {@link #addWeightedEdge(int, int, float)} instead.
     */
    void addWeightedEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight);

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
    void setEdgeWeight(EdgeId edgeId, float weight);

    /**
     * Gets outgoing edges for a node.
     */
    EdgeVector getOutgoingEdges(int startNodeIndex);

    /**
     * Gets incoming edges for a node.
     */
    EdgeVector getIncomingEdges(int endNodeIndex);

    /**
     * Removes all edges connected to a node.
     */
    void removeNode(int nodeIndex);

    void setBaseDirectory(String directory);
}
