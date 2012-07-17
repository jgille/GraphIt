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

import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;

/**
 * 
 * A buffer of edge primitives.
 * 
 * @author jon
 * 
 */
public interface EdgePrimitivesBuffer {

    /**
     * Gets the size of this buffer
     * 
     * @return
     */
    int size();

    /**
     * Adds an edge primitive to the buffer at a given index.
     */
    void upsert(int index, int startNode, int endNode, float weight);

    /**
     * Gets an edge primitive.
     */
    EdgePrimitive get(int index);

    /**
     * Gets and removes an edge primitive.
     */
    EdgePrimitive remove(int index);

    /**
     * Gets the type of the edges in this buffer.
     */
    EdgeType getEdgeType();

}