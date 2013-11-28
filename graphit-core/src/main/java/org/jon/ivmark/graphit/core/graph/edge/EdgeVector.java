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

import org.apache.mahout.math.function.IntProcedure;

import java.util.List;

/**
 * A vector of edges originating from a node.
 *
 * All implementations of this class are immutable, and all modifying operations
 * returns a new instance in a copy on write fashion.
 *
 * @author jon
 */
public interface EdgeVector {

    /**
     * Gets the unique id of the root node for the edges in this vector.
     */
    int getRootNode();

    /**
     * Gets the edge type for the edges in this vector.
     */
    EdgeType getEdgeType();

    /**
     * Gets the direction of the edges (incoming/outgoing).
     */
    EdgeDirection getEdgeDirection();

    /**
     * Adds an edge to this vector.
     *
     * @param edgeId
     *            The unique id for the new edge.
     */
    EdgeVector add(int edgeId);

    /**
     * Removes an edge.
     *
     * @param edgeId
     *            The unique id for the edge that should be removed.
     */
    EdgeVector remove(int edgeId);

    /**
     * Reindex an edge in this vector.
     *
     * @param edgeId
     *            The unique id for the edge that should be removed.
     */
    EdgeVector reindex(int edgeId);

    /**
     * Applies the procedure for each edge id in this vector. Stops once a
     * procedure call returns false.
     *
     * @param procedure
     *            The procedure to apply for each edge id.
     */
    void forEachEdgeId(IntProcedure procedure);

    /**
     * Gets all edges as a list.
     */
    List<Integer> asList();

    /**
     * Gets the number of edges.
     */
    int size();

    /**
     * Returns true if this vector contains no edges, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the ids of the edges in this vector.
     */
    Iterable<EdgeId> iterable();

}
