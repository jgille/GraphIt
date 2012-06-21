package org.graphit.graph.edge.util;

/**
 * An {@link EdgeIndexComparator} to use when edges are not to be kept sorted.
 * 
 * @author jon
 * 
 */
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
