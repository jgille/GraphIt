package org.opengraph.graph.edge.schema;

import org.opengraph.common.enumeration.DynamicEnumerableElement;
import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.edge.util.EdgeWeigher;

/**
 * Describes an edge type in a graph.
 *
 * @author jon
 *
 */
public interface EdgeType extends DynamicEnumerableElement {

    /**
     * True if edges of this type are weighted, false otherwise.
     */
    boolean isWeighted();

    /**
     * Gets an {@link EdgeIndexComparator} used to keep outgoing/incoming edges
     * for a node sorted.
     *
     * @param edgeWeigher
     *            An instance that provides edge weight given an edge index.
     */
    EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher);

}
