package org.graphit.graph.edge.util;

/**
 * Provides edge weight given an edge index
 *
 * @author jon
 *
 */
public interface EdgeWeigher {

    /**
     * Gets the weight of an edge.
     */
    float getEdgeWeight(int edgeIndex);

}
