package org.opengraph.graph.edge.schema;

import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.edge.util.EdgeWeigher;
import org.opengraph.graph.edge.util.UnsortedEdgeIndexComparator;

public class EdgeTypeImpl implements EdgeType {

    private final String name;
    private final boolean isWeighted;
    private EdgeIndexComparator edgeComparator = new UnsortedEdgeIndexComparator();

    public EdgeTypeImpl(String name) {
        this(name, false);
    }

    public EdgeTypeImpl(String name, boolean isWeighted) {
        this.name = name;
        this.isWeighted = isWeighted;
    }

    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isWeighted() {
        return isWeighted;
    }

    @Override
    public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
        return edgeComparator;
    }

    @Override
    public String toString() {
        return "EdgeTypeImpl [name=" + name + ", isWeighted=" + isWeighted + ", edgeComparator="
            + edgeComparator + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        EdgeTypeImpl other = (EdgeTypeImpl) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
