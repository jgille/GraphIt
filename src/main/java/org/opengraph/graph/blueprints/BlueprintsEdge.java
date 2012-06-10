package org.opengraph.graph.blueprints;

import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.properties.domain.Properties;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class BlueprintsEdge extends AbstractElement<EdgeId> implements Edge {

    private final Vertex startNode;
    private final Vertex endNode;

    BlueprintsEdge(EdgeId edgeId, Vertex startNode, Vertex endNode,
                   Properties properties) {
        super(edgeId, properties);
        this.startNode = startNode;
        this.endNode = endNode;
    }

    @Override
    public Vertex getVertex(Direction direction) {
        switch (direction) {
        case OUT:
            return startNode;
        case IN:
            return endNode;
        default:
            throw new IllegalArgumentException("Illegal direction: " + direction);
        }
    }

    @Override
    public String getLabel() {
        return getId().getEdgeType().name();
    }

    @Override
    public String toString() {
        return "BlueprintsEdge [startNode=" + startNode + ", endNode=" + endNode + ", getLabel()="
            + getLabel() + "]";
    }

}
