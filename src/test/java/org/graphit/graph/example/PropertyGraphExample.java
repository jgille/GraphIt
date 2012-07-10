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

package org.graphit.graph.example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.graphit.common.procedures.ConcatMapper;
import org.graphit.common.procedures.CountSortOrder;
import org.graphit.common.procedures.CountedElement;
import org.graphit.common.procedures.Counter;
import org.graphit.common.procedures.CounterReducer;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.service.PropertyGraphImpl;
import org.graphit.graph.traversal.EdgeDirection;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Simple example of using a {@link PropertyGraph}.
 *
 * @author jon
 *
 */
public class PropertyGraphExample {

    /**
     * Main example.
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        final PropertyGraph graph = new PropertyGraphImpl("store");

        // Create some metadata
        NodeType user = graph.getOrCreateNodeType("user");
        NodeType item = graph.getOrCreateNodeType("item");

        final EdgeType bought = graph.getOrCreateEdgeType("bought");
        EdgeType viewed = graph.getOrCreateEdgeType("viewed");

        // Add some nodes, with properties
        Node u1 = graph.addNode(new NodeId(user, "u1"));
        u1.setProperty("name", "John Doe");
        graph.setNodeProperties(u1.getNodeId(), u1);

        Node i1 = graph.addNode(new NodeId(item, "i1"));
        i1.setProperty("price", 200.0);
        i1.setProperty("name", "Playstation II");
        graph.setNodeProperties(i1.getNodeId(), i1);

        final Node i2 = graph.addNode(new NodeId(item, "i2"));
        i2.setProperty("price", 899.90);
        i2.setProperty("name", "MacBook Pro");
        i2.setProperty("categories",
                       Arrays.asList("Rather expensive computer", "Laptop"));
        graph.setNodeProperties(i2.getNodeId(), i2);

        // Add some edges, that can also have properties
        Edge v1 = graph.addEdge(u1.getNodeId(), i1.getNodeId(), viewed);
        v1.setProperty("date", new Date());
        graph.setEdgeProperties(v1.getEdgeId(), v1);

        Edge b1 = graph.addEdge(u1.getNodeId(), i2.getNodeId(), bought);
        b1.setProperty("date", new Date());
        graph.setEdgeProperties(b1.getEdgeId(), b1);

        // Get all items that user u1 has viewed
        List<Node> viewedItems =
            graph.getNeighbors(u1.getNodeId(), viewed,
                               EdgeDirection.OUTGOING).asList();

        // Get the first item that user u1 has viewed, transformed to just the
        // product name
        List<String> viewedItemName =
            graph.getNeighbors(u1.getNodeId(), viewed, EdgeDirection.OUTGOING)
                .head(1)
                .transform(new Function<Node, String>() {

                    @Override
                    public String apply(Node node) {
                        return (String) node.getProperty("name");
                    }
                })
                .asList();

        // Get all purchases of item i1 that took place within the last hour
        final long cutOff = System.currentTimeMillis() - 60 * 60 * 1000l;
        Iterable<Edge> purchases =
            graph.getEdges(i1.getNodeId(), bought, EdgeDirection.INCOMING)
                .filter(new Predicate<Edge>() {

                    @Override
                    public boolean apply(Edge purchase) {
                        Date when = (Date) purchase.getProperty("date");
                        return when != null && when.getTime() > cutOff;
                    }
                });
        // Note that graph iteration is done lazily, so no purchases have been
        // retrieved yet
        // (they will be once you start iterating them/calling toList etc).

        // Get an item recommendation using collaborative filtering
        Counter<Node> purchasedItems =
            // Get all users that bough i2
            graph.getNeighbors(i2.getNodeId(), bought, EdgeDirection.INCOMING)
                // Get all the items these users bought
                .transform(new Function<Node, Iterable<Node>>() {

                    @Override
                    public Iterable<Node> apply(Node node) {
                        return graph.getNeighbors(node.getNodeId(), bought,
                                                  EdgeDirection.OUTGOING)
                            .filter(new Predicate<Node>() {

                                @Override
                                public boolean apply(Node input) {
                                    return !input.getNodeId().equals(i2.getNodeId());
                                }
                            });
                    }
                })
                // Concatenate these and make a frequency count of all items
                .mapReduce(new ConcatMapper<Node>(),
                           new CounterReducer<Node>());
        // Get the recommendation
        Iterable<CountedElement<Node>> recommendedItems =
            purchasedItems.iterable(CountSortOrder.DESCENDING_COUNT);
    }
}
