package org.opengraph.graph.node.schema;

import org.opengraph.common.enumeration.DynamicEnumerationSet;

public interface NodeTypes extends DynamicEnumerationSet<NodeType> {

    String getGraphName();
}
