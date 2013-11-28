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

import com.google.common.base.Preconditions;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.service.PropertyGraph;

import java.util.Arrays;

/**
 * @author jon
 *
 */
public class RemoveEdgeMethod extends AbstractGraphMethod<Edge> {

    public RemoveEdgeMethod(PropertyGraph graph, GraphLoadTestStats stats) {
        super(graph, stats);
    }

    private EdgeId edgeId;

    @Override
    public void init(String[] params) {
        Preconditions.checkArgument(params.length == 2, "Illegal params: " + Arrays.toString(params));
        PropertyGraph graph = getGraph();
        int i = 0;
        EdgeType edgeType = graph.getEdgeType(params[i++]);
        int id = Integer.parseInt(params[i++]);
        this.edgeId = new EdgeId(edgeType, id);
    }

    @Override
    public Edge invoke() {
        getStats().logRemoveEdge();
        Edge edge = getGraph().removeEdge(edgeId);
        return edge;
    }

}
