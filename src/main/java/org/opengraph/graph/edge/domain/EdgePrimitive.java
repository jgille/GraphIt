package org.opengraph.graph.edge.domain;

import org.opengraph.graph.edge.schema.EdgeType;

/**
 * The internal representation of an edge.
 *
 * @author jon
 *
 */
public class EdgePrimitive {

    private final EdgeId edgeId;
    private final int startNodeId;
    private final int endNodeId;
    private final float weight;

    public EdgePrimitive(EdgeId edgeId, int startNodeId, int endNodeId, float weight) {
        this.edgeId = edgeId;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.weight = weight;
    }

    public int getId() {
        return edgeId.getIndex();
    }

    public EdgeType getEdgeType() {
        return edgeId.getEdgeType();
    }

    public EdgeId getEdgeId() {
        return edgeId;
    }

    public int getStartNodeId() {
        return startNodeId;
    }

    public int getEndNodeId() {
        return endNodeId;
    }

    public float getWeight() {
        return weight;
    }

    public EdgePrimitive reverse() {
        return new EdgePrimitive(edgeId, endNodeId, startNodeId, weight);
    }

    @Override
    public int hashCode() {
        return edgeId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EdgePrimitive other = (EdgePrimitive) obj;
        return edgeId.equals(other.edgeId);
    }

    @Override
    public String toString() {
        return "InternalEdge [edgeId=" + edgeId + ", startNodeId=" + startNodeId + ", endNodeId="
            + endNodeId + ", weight=" + weight + "]";
    }

}
