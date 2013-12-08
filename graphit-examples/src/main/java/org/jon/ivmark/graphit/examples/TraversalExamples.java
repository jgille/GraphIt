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

package org.jon.ivmark.graphit.examples;

import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.node.NodeFilter;
import org.jon.ivmark.graphit.core.graph.node.NodeTypeFilter;
import org.jon.ivmark.graphit.core.graph.traversal.PropertyRetriever;

import java.util.List;
import java.util.regex.Pattern;

import static org.jon.ivmark.graphit.core.graph.edge.EdgeDirection.INCOMING;
import static org.jon.ivmark.graphit.core.graph.edge.EdgeDirection.OUTGOING;
import static org.jon.ivmark.graphit.core.properties.filter.PropertiesFilterBuilder.where;
import static org.jon.ivmark.graphit.examples.ExampleConstants.*;
import static org.jon.ivmark.graphit.examples.ExampleGraphBuilder.JOHN;
import static org.jon.ivmark.graphit.examples.ExampleGraphBuilder.RIHANNA;

public class TraversalExamples {

    public final PropertyGraph graph;

    public TraversalExamples() {
        this.graph = ExampleGraphBuilder.createGraph();
    }

    public List<Node> getAllNodes() {
        return graph.getNodes().asList();
    }

    public List<Node> getUsers() {
        return graph.getNodes().filter(new NodeTypeFilter(USER)).asList();
    }

    public List<String> getUserNames() {
        return graph.getNodes().filter(new NodeTypeFilter(USER))
                .transform(new PropertyRetriever<Node, String>("Name"))
                .asList();
    }

    public List<Node> getPurchases(String userId) {
        NodeId user = new NodeId(USER, userId);
        return graph.getNeighbors(user, BOUGHT, OUTGOING).asList();
    }

    public List<Node> getPurchasesWithLimit(String userId, int limit) {
        NodeId user = new NodeId(USER, userId);
        return graph.getNeighbors(user, BOUGHT, OUTGOING).head(limit).asList();
    }

    public List<Node> getCheapPurchases(String userId) {
        NodeId user = new NodeId(USER, userId);
        return graph.getNeighbors(user, BOUGHT, OUTGOING)
                .filter(new NodeFilter()
                        .withNodeTypes(TRACK)
                        .filterOnProperties(where("Price").lessThan(1d)
                                .build()))
                .asList();
    }

    public List<Node> getMediumPricedPurchasesWithALongTitle(String userId) {
        NodeId user = new NodeId(USER, userId);
        return graph.getNeighbors(user, BOUGHT, OUTGOING)
                .filter(new NodeFilter()
                        .withNodeTypes(TRACK)
                        .filterOnProperties(
                                where("Price").greaterThan(0.5d)
                                        .and("Price").lessThan(4d)
                                        .and("Title").matches(Pattern.compile(".{12,}"))
                                        .build()))
                .asList();
    }

    public List<Node> getBuyers(String trackId) {
        NodeId track = new NodeId(TRACK, trackId);
        return graph.getNeighbors(track, BOUGHT, INCOMING).asList();
    }

    public static void main(String[] args) {
        TraversalExamples examples = new TraversalExamples();

        System.out.println("Get all nodes");
        System.out.println(examples.getAllNodes());

        System.out.println("Get all users");
        System.out.println(examples.getUsers());

        System.out.println("Get all user names");
        System.out.println(examples.getUserNames());

        System.out.println("Cheap purchases");
        System.out.println(examples.getCheapPurchases(JOHN));

        System.out.println("Medium purchases");
        System.out.println(examples.getMediumPricedPurchasesWithALongTitle(JOHN));

        System.out.println("Get buyers");
        System.out.println(examples.getBuyers(RIHANNA));

        System.out.println("Get purchases");
        System.out.println(examples.getPurchases(JOHN));

        System.out.println("Get purchases with limit");
        System.out.println(examples.getPurchasesWithLimit(JOHN, 1));
    }
}
