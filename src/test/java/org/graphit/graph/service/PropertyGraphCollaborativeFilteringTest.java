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

package org.graphit.graph.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.graphit.common.procedures.ConcatMapper;
import org.graphit.common.procedures.CountSortOrder;
import org.graphit.common.procedures.CountedElement;
import org.graphit.common.procedures.Counter;
import org.graphit.common.procedures.CounterReducer;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.traversal.EdgeDirection;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author jon
 * 
 */
public class PropertyGraphCollaborativeFilteringTest {

    @Test
    public void testCollaborativeFiltering() {
        PropertyGraph graph = new PropertyGraphImpl("collaborativeFiltering");

        NodeType user = graph.getOrCreateNodeType("user");
        NodeType item = graph.getOrCreateNodeType("item");

        Node u1 = graph.addNode(new NodeId(user, "u1"));
        Node u2 = graph.addNode(new NodeId(user, "u2"));
        Node u3 = graph.addNode(new NodeId(user, "u3"));

        Node p1 = graph.addNode(new NodeId(item, "p1"));
        Node p2 = graph.addNode(new NodeId(item, "p2"));
        Node p3 = graph.addNode(new NodeId(item, "p3"));

        final EdgeType bought = graph.getOrCreateEdgeType("bought");

        graph.addEdge(u1.getNodeId(), p1.getNodeId(), bought);
        graph.addEdge(u2.getNodeId(), p1.getNodeId(), bought);
        graph.addEdge(u3.getNodeId(), p1.getNodeId(), bought);

        graph.addEdge(u2.getNodeId(), p2.getNodeId(), bought);
        graph.addEdge(u3.getNodeId(), p2.getNodeId(), bought);

        graph.addEdge(u3.getNodeId(), p3.getNodeId(), bought);

        List<String> r1 = asList(collaborativeFilter(graph, p1.getNodeId(), bought));
        assertEquals(Arrays.asList("p2", "p3"), r1);

        List<String> r2 = asList(collaborativeFilter(graph, p2.getNodeId(), bought));
        assertEquals(Arrays.asList("p1", "p3"), r2);
    }

    private Counter<Node> collaborativeFilter(final PropertyGraph graph, final NodeId start,
                                              final EdgeType edgeType) {
        return graph.getNeighbors(start, edgeType, EdgeDirection.INCOMING)
            .transform(new Function<Node, Iterable<Node>>() {

                @Override
                public Iterable<Node> apply(Node node) {
                    return graph.getNeighbors(node.getNodeId(), edgeType, EdgeDirection.OUTGOING)
                        .filter(new Predicate<Node>() {

                            @Override
                            public boolean apply(Node input) {
                                return !input.getNodeId().equals(start);
                            }
                        });
                }
            })
            .mapReduce(new ConcatMapper<Node>(),
                       new CounterReducer<Node>());

    }

    private List<String> asList(Counter<Node> counter) {
        List<String> res = new ArrayList<String>();
        for (CountedElement<Node> element : counter.iterable(CountSortOrder.DESCENDING_COUNT)) {
            res.add(element.getElement().getNodeId().getId());
        }
        return res;
    }
}
