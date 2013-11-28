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

package org.jon.ivmark.graphit.core.graph.edge;

/**
 * Describes the order in which outgoing and incoming edges are kept sorted for
 * a node.
 *
 * @author jon
 *
 */
public enum EdgeSortOrder {

    UNDEFINED
    {
        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
            return UNSORTED_COMPARATOR;
        }
    },
    ASCENDING_WEIGHT
    {
        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
            return new EdgeWeightComparator(edgeWeigher, false);
        }
    },
    DESCENDING_WEIGHT
    {
        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher) {
            return new EdgeWeightComparator(edgeWeigher, true);
        }
    };

    private static final EdgeIndexComparator UNSORTED_COMPARATOR =
        new UnsortedEdgeIndexComparator();

    /**
     * Gets a comparator used to sort edge indexes in an outgoing or incoming
     * edge vector for a node.
     */
    public abstract EdgeIndexComparator getEdgeComparator(EdgeWeigher edgeWeigher);
}
