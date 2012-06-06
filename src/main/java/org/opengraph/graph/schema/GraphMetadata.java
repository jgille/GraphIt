package org.opengraph.graph.schema;

import org.opengraph.graph.edge.schema.EdgeTypes;
import org.opengraph.graph.node.schema.NodeTypes;

public interface GraphMetadata {

    String getGraphName();

    NodeTypes getNodeTypes();

    EdgeTypes getEdgeTypes();
}
