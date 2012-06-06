package org.opengraph.graph.node.repository;

import org.opengraph.graph.node.domain.NodeId;

public interface NodeIdRepository {

    int getNodeIndex(NodeId nodeId);

    NodeId getNodeId(int nodeIndex);

    int insert(NodeId nodeId);

    void insert(int nodeIndex, NodeId nodeId);

    NodeId remove(int nodeIndex);

}
