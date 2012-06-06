package org.opengraph.graph.node.domain;

import org.opengraph.graph.domain.GraphEntity;
import org.opengraph.graph.node.schema.NodeType;

/**
 * A node in a graph.
 *
 * @author jon
 *
 */
public interface Node extends GraphEntity<NodeType> {

    /**
     * Gets the unique node id.
     */
    NodeId getNodeId();

}
