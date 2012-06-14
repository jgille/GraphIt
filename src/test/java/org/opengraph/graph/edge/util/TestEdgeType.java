package org.opengraph.graph.edge.util;

import org.opengraph.graph.edge.schema.EdgeType;

public enum TestEdgeType implements EdgeType {

    SIMILAR {
        @Override
        public boolean isWeighted() {
            return true;
        }

        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
            return new EdgeWeightComparator(edgeWeigher, true);
        }
    },
    BOUGHT {

        @Override
        public boolean isWeighted() {
            return false;
        }

        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
            return new EdgeIndexComparatorImpl();
        }
    },
    VIEWED {

        @Override
        public boolean isWeighted() {
            return false;
        }

        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
            return new EdgeIndexComparatorImpl();
        }
    };
    ;
}
