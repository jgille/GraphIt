/*
 * Copyright 2012 Jon Ivmark
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        int c = ((Float) weigher.getEdgeWeight(e1)).compareTo(weigher.getEdgeWeight(e2));
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
