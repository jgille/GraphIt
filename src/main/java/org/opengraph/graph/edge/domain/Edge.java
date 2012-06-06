package org.opengraph.graph.edge.domain;

import org.opengraph.graph.domain.GraphEntity;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.node.domain.Node;
import org.opengraph.graph.node.domain.NodeId;

/**
 * An edge in a graph.
 *
 * @author jon
 *
 */
public interface Edge extends GraphEntity<EdgeType> {

    /**
     * Gets the id for this edge.
     */
    EdgeId getEdgeId();

    /**
     * Gets the start node.
     */
    Node getStartNode();

    /**
     * Gets the end node.
     */
    Node getEndNode();

    Node getOppositeNode(NodeId nodeId);

    /**
     * Gets the edge weight.
     */
    float getWeight();

}
