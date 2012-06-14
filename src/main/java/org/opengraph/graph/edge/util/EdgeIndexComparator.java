package org.opengraph.graph.edge.util;

import java.util.Comparator;

/**
 * A {@link Comparator} used to keep outgoing and incoming edges for a node
 * sorted.
 *
 * @author jon
 *
 */
public interface EdgeIndexComparator extends Comparator<Integer> {

    /**
     * If false, the edges won't be sorted.
     */
    boolean isSorted();
}
