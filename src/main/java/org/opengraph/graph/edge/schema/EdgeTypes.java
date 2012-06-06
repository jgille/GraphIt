package org.opengraph.graph.edge.schema;

import org.opengraph.common.enumeration.DynamicEnumerationSet;

public interface EdgeTypes extends DynamicEnumerationSet<EdgeType> {

    String getGraphName();
}
