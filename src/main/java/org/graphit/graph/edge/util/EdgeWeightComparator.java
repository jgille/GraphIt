package org.graphit.graph.edge.util;

/**
 * An {@link EdgeIndexComparator} that sorts edges on edge weight.
 *
 * @author jon
 *
 */
public class EdgeWeightComparator implements EdgeIndexComparator {

    private final EdgeWeigher weigher;
    private final boolean descending;

    /**
     * Creates an ascending comparator, using the provided weigher to get edge
     * weights.
     */
    public EdgeWeightComparator(EdgeWeigher weigher) {
        this(weigher, false);
    }

    /**
     * Creates an ascending or descending comparator, using the provided weigher
     * to get edge weights.
     */
    public EdgeWeightComparator(EdgeWeigher weigher, boolean descending) {
        this.weigher = weigher;
        this.descending = descending;
    }

    @Override
    public int compare(Integer e1, Integer e2) {
        float w1 = weigher.getEdgeWeight(e1);
        float w2 = weigher.getEdgeWeight(e2);
        int c = ((Float) w1).compareTo(w2);
        if (descending) {
            return -c;
        }
        return c;
    }

    @Override
    public boolean isSorted() {
        return true;
    }

}
