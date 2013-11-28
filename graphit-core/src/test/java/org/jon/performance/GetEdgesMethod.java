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

import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeType;
import org.jon.ivmark.graphit.core.graph.node.domain.NodeId;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.traversal.EdgeDirection;

/**
 * @author jon
 *
 */
public class GetEdgesMethod extends GraphTraversalMethod {

    public GetEdgesMethod(PropertyGraph graph, GraphLoadTestStats stats) {
        super(graph, stats);
    }

    @Override
    protected Integer
        invokeMethod(NodeId nodeId, EdgeType edgeType, EdgeDirection edgeDirection) {
        int count = getGraph().getEdges(nodeId, edgeType, edgeDirection).size();
        getStats().logGetEdges(count);
        return count;
    }

}
