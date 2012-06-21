package org.graphit.graph.node.repository;

import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.repository.GraphRepository;

/**
 * A repo containing a mapping between node index and {@link NodeId}.
 *
 * @author jon
 *
 */
public interface NodeIdRepository extends GraphRepository {

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

}
