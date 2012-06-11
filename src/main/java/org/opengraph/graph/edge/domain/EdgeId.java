package org.opengraph.graph.edge.domain;

import org.opengraph.graph.edge.schema.EdgeType;
import org.springframework.util.Assert;

/**
 * A unique identifier for an edge in a graph. Should normally never be
 * constructed manually.
 *
 * @author jon
 *
 */
public class EdgeId {

    private final EdgeType edgeType;
    private final int index;

    /**
     * Constructs a new edge id.
     * 
     * @param edgeType
     *            The type of the edge.
     * @param index
     *            The index of the edge.
     */
    public EdgeId(EdgeType edgeType, int index) {
        Assert.isTrue(index >= 0, "Index must not be negative.");
        this.edgeType = edgeType;
        this.index = index;
    }

    /**
     * Gets the type of the edge.
     */
    public EdgeType getEdgeType() {
        return edgeType;
    }

    /**
     * Gets the index of the edge.
     */
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
