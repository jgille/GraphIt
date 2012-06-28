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

import org.graphit.graph.edge.domain.EdgeVector;

/**
 * A repo containg methods relating to querying/modifying the {@link EdgeVector}
 * (outgoing or incoming) for a node.
 * 
 * @author jon
 * 
 */
public interface EdgeVectorRepository {

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getOutgoingEdges(int startNodeIndex);

    /**
     * Gets the outgoing edges for a node.
     */
    EdgeVector getIncomingEdges(int endNodeIndex);

    /**
     * Sets the outgoing edges for a node.
     */
    void setOutgoingEdges(int startNodeIndex, EdgeVector edges);

    /**
     * Sets the outgoing edges for a node.
     */
    void setIncomingEdges(int endNodeIndex, EdgeVector edges);

}
