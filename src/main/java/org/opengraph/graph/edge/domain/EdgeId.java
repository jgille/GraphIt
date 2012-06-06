package org.opengraph.graph.edge.domain;

import org.opengraph.graph.edge.schema.EdgeType;
import org.springframework.util.Assert;

public class EdgeId {

    private final EdgeType edgeType;
    private final int index;

    public EdgeId(EdgeType edgeType, int index) {
        Assert.isTrue(index >= 0, "Index must not be negative.");
        this.edgeType = edgeType;
        this.index = index;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "EdgeId [edgeType=" + edgeType + ", index=" + index + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + edgeType.hashCode();
        result = prime * result + index;
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
        EdgeId other = (EdgeId) obj;
        return index == other.index && edgeType.equals(other.edgeType);
    }

}
