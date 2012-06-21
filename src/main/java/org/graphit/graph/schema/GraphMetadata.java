package org.graphit.graph.schema;

import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.schema.NodeTypes;

/**
 * Describes a graph.
 *
 * @author jon
 *
 */
public interface GraphMetadata {

    /**
     * Gets the name of this graph.
     */
    String getGraphName();

    /**
     * Gets the valid node types for this graph.
     */
    NodeTypes getNodeTypes();

    /**
     * Gets the valid edge types for this graph.
     */
    EdgeTypes getEdgeTypes();
}
