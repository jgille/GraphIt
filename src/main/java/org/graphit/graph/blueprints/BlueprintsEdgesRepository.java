package org.graphit.graph.blueprints;

import org.graphit.graph.node.domain.NodeId;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * A repository containing methods for getting connected edges and neighbors for
 * a node.
 *
 * @author jon
 *
 */
public interface BlueprintsEdgesRepository {

    /**
     * Gets connected edges for a node.
     *
     * @param nodeId
     *            The node id.
     * @param edgeDirection
     *            The direction of the edges to get.
     * @param edgeLabel
     *            The name of the edge type to get edges for.
     */
    Iterable<Edge> getEdges(NodeId nodeId, Direction edgeDirection, String edgeLabel);

    /**
     * Gets neighbors for a node.
     *
     * @param nodeId
     *            The node id.
     * @param edgeDirection
     *            The direction of the edges to get neighbors for.
     * @param edgeLabel
     *            The name of the edge type to get neighbors for.
     */
    Iterable<Vertex> getNeighbors(NodeId nodeId, Direction edgeDirection, String edgeLabel);

}