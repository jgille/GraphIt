package org.graphit.graph.node.domain;

import org.graphit.graph.domain.GraphEntity;
import org.graphit.graph.node.schema.NodeType;

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
