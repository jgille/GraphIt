package org.opengraph.graph.traversal;

import org.opengraph.graph.edge.domain.EdgeDirection;
import org.opengraph.graph.edge.schema.EdgeType;

public class TraversalEdgeDescriptor {

    private final EdgeType type;
    private final EdgeDirection direction;

    public TraversalEdgeDescriptor(EdgeType type, EdgeDirection driection) {
        this.type = type;
        this.direction = driection;
    }

    public EdgeType getType() {
        return type;
    }

    public EdgeDirection getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "EdgeDescription [type=" + type + ", direction=" + direction + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
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
        TraversalEdgeDescriptor other = (TraversalEdgeDescriptor) obj;
        if (direction != other.direction) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }


}
