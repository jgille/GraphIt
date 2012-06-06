package org.opengraph.graph.edge.repository;

import org.opengraph.graph.edge.domain.EdgeVector;

public interface EdgeVectorRepository {

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getOutgoingEdges(int startNodeIndex);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getIncomingEdges(int endNodeIndex);

    /**
     * Sets the outgoing edges for a node.
     */
    void setOutgoingEdges(int startNodeIndex, EdgeVector edges);

    /**
     * Sets the outgoing edges for a node.
     */
    void setIncomingEdges(int endNodeIndex, EdgeVector edges);

    /**
     * Removes the outgoing edges for a node.
     */
    EdgeVector removeOutgoingEdges(int startNodeIndex);

    /**
     * Removes the outgoing edges for a node.
     */
    EdgeVector removeIncomingEdges(int endNodeIndex);

}
