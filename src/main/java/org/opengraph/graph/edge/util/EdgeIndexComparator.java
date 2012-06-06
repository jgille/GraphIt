package org.opengraph.graph.edge.util;

import java.util.Comparator;

public interface EdgeIndexComparator extends Comparator<Integer> {

    boolean isSorted();
}
