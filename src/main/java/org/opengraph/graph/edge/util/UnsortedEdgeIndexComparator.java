package org.opengraph.graph.edge.util;

public class UnsortedEdgeIndexComparator implements EdgeIndexComparator {

    @Override
    public int compare(Integer o1, Integer o2) {
        return -1;
    }

    @Override
    public boolean isSorted() {
        return false;
    }

}
