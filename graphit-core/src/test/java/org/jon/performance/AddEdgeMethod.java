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

package org.jon.performance;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.edge.domain.Edge;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeType;
import org.jon.ivmark.graphit.core.graph.node.domain.NodeId;
import org.jon.ivmark.graphit.core.graph.node.schema.NodeType;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraph;

/**
 * @author jon
 *
 */
public class AddEdgeMethod extends AbstractGraphMethod<Edge> {

    public AddEdgeMethod(PropertyGraph graph, GraphLoadTestStats stats) {
        super(graph, stats);
    }

    private NodeId startNode;
    private NodeId endNode;
    private EdgeType edgeType;
    private float weight;

    @Override
    public void init(String[] params) {
        Preconditions.checkArgument(params.length == 6);
        PropertyGraph graph = getGraph();
        int i = 0;
        NodeType fromType = graph.getNodeType(params[i++]);
        String fromId = params[i++];
        NodeType toType = graph.getNodeType(params[i++]);
        String toId = params[i++];
        this.startNode = new NodeId(fromType, fromId);
        this.endNode = new NodeId(toType, toId);
        this.edgeType = graph.getEdgeType(params[i++]);
        this.weight = Float.valueOf(params[i++]);

    }

    @Override
    public Edge invoke() {
        getStats().logAddEdge();
        return getGraph().addEdge(startNode, endNode, edgeType, weight);
    }

}
