package org.opengraph.graph.blueprints;

import org.opengraph.graph.node.domain.NodeId;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public interface BlueprintsEdgesRepository {

    Iterable<Edge> getEdges(NodeId nodeId, Direction edgeDirection, String edgeLabel);

    Iterable<Vertex> getNeighbors(NodeId nodeId, Direction edgeDirection, String edgeLabel);

}