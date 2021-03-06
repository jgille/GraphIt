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

package org.jon.ivmark.graphit.core.graph.node.repository;

import org.jon.ivmark.graphit.core.graph.node.NodeId;

/**
 * A repo containing a mapping between node index and {@link NodeId}.
 * 
 * @author jon
 * 
 */
public interface NodeIdRepository {

    /**
     * Gets the index of a node given it's id.
     */
    int getNodeIndex(NodeId nodeId);

    /**
     * Gets the if of a node given it's index.
     */
    NodeId getNodeId(int nodeIndex);

    /**
     * Inserts a new node.
     * 
     * @return The index of the new node.
     */
    int insert(NodeId nodeId);

    /**
     * Inserts a node with a defined index. Will throw an exception if the index
     * if used or out of range. This method should normally not be used, it's
     * primary use case is for restoring nodes from file.
     */
    void insert(int nodeIndex, NodeId nodeId);

    /**
     * Removes a node given it's index.
     * 
     * @return The id removed node id.
     */
    NodeId remove(int nodeIndex);

    /**
     * Gets the number of nodes in this repo.
     */
    int size();

    /**
     * Returns all nodes currently in this repo.
     */
    Iterable<NodeId> getNodes();
}
