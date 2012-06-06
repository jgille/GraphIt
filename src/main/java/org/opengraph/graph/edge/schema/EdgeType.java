package org.opengraph.graph.edge.schema;

import org.opengraph.common.enumeration.DynamicEnumerableElement;
import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.edge.util.EdgeWeigher;

public interface EdgeType extends DynamicEnumerableElement {

    boolean isWeighted();

    EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher);

}
