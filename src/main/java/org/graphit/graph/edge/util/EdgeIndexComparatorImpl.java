package org.graphit.graph.edge.util;

/**
 * An {@link EdgeIndexComparator} that compares the actual indexes.
 *
 * @author jon
 *
 */
public class EdgeIndexComparatorImpl implements EdgeIndexComparator {

    private final boolean descending;

    /**
     * Creates an ascending comparator.
     */
    public EdgeIndexComparatorImpl() {
        this(false);
    }

    /**
     * Creates an ascending or descending comparator.
     */
    public EdgeIndexComparatorImpl(boolean descending) {
        this.descending = descending;
    }

    @Override
    public int compare(Integer e1, Integer e2) {
        int c = e1.compareTo(e2);
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
