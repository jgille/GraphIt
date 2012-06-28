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

package org.graphit.graph.edge.repository;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.repository.GraphRepository;

/**
 * A repo containing methods related to storing {@link EdgePrimitive}s.
 *
 * @author jon
 *
 */
public interface EdgePrimitivesRepository extends GraphRepository {

    /**
     * Adds an edge an returns the id of the new edge.
     */
    EdgeId addEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType);

    /**
     * Adds a weighted edge an returns the id of the new edge.
     */
    EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, EdgeType edgeType, float weight);

    /**
     * Gets the edge with the provided id.
     */
    EdgePrimitive getEdge(EdgeId edgeId);

    /**
     * Removes the edge with the provided id.
     */
    EdgePrimitive removeEdge(EdgeId edgeId);

    /**
     * Updates the weight for an edge.
     */
    void setEdgeWeight(EdgeId edge, float weight);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getOutgoingEdges(int startNodeIndex, EdgeType edgeType);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getIncomingEdges(int endNodeIndex, EdgeType edgeType);
}
