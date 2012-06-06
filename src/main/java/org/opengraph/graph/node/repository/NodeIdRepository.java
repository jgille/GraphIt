package org.opengraph.graph.node.repository;

import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.repository.GraphRepository;

public interface NodeIdRepository extends GraphRepository {

    int getNodeIndex(NodeId nodeId);

    NodeId getNodeId(int nodeIndex);

    int insert(NodeId nodeId);

    void insert(int nodeIndex, NodeId nodeId);

    NodeId remove(int nodeIndex);

}
