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

package org.graphit.performance;

import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.traversal.EdgeDirection;
import org.springframework.util.Assert;

/**
 * @author jon
 *
 */
public abstract class GraphTraversalMethod extends AbstractGraphMethod<Integer> {

    public GraphTraversalMethod(PropertyGraph graph, GraphLoadTestStats stats) {
        super(graph, stats);
    }

    private NodeId nodeId;
    private EdgeType edgeType;
    private EdgeDirection edgeDirection;

    @Override
    public void init(String[] params) {
        Assert.isTrue(params.length == 4);
        PropertyGraph graph = getGraph();
        int i = 0;
        NodeType nodeType = graph.getNodeType(params[i++]);
        String id = params[i++];
        this.nodeId = new NodeId(nodeType, id);
        this.edgeType = graph.getEdgeType(params[i++]);
        this.edgeDirection = EdgeDirection.valueOf(params[i]);
    }

    @Override
    public Integer call() {
        return invokeMethod(nodeId, edgeType, edgeDirection);
    }

    protected abstract Integer invokeMethod(NodeId nodeId, EdgeType edgeType,
                                            EdgeDirection edgeDirection);
}
