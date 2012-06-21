package org.graphit.graph.edge.repository;

import org.graphit.graph.edge.domain.EdgeVector;

/**
 * A repo containg methods relating to querying/modifying the {@link EdgeVector}
 * (outgoing or incoming) for a node.
 * 
 * @author jon
 * 
 */
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
