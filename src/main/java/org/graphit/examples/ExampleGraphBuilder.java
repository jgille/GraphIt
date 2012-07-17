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

package org.graphit.examples;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypeImpl;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.graphit.graph.schema.GraphMetadata;
import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.service.PropertyGraphDotExporter;
import org.graphit.graph.service.PropertyGraphImpl;
import org.graphit.graph.service.PropertyGraphJsonUtils;

/**
 * Some examples using tinkerpop.
 *
 * @author jon
 *
 */
public class ExampleGraphBuilder {

    private static final NodeType USER = new NodeTypeImpl("User");
    private static final NodeType TRACK = new NodeTypeImpl("Track");
    private static final EdgeType BOUGHT = new EdgeTypeImpl("Bought");
    private static final EdgeType LISTENED_TO = new EdgeTypeImpl("ListenedTo");
    private static final EdgeType SIMILAR = new EdgeTypeImpl("Similar",
                                                             EdgeSortOrder.DESCENDING_WEIGHT);

    private PropertyGraph createGraph() {
        PropertyGraph graph = new PropertyGraphImpl();
        graph.setGraphName("Music store");
        GraphMetadata metadata = graph.getMetadata();
        metadata.addNodeType(USER);
        metadata.addNodeType(TRACK);
        metadata.addEdgeType(BOUGHT);
        metadata.addEdgeType(LISTENED_TO);
        metadata.addEdgeType(SIMILAR);

        Node john = createUser(graph, "john.doe", "John Doe");
        Node sam = createUser(graph, "sam98", "Samantha Fox");
        Node mary = createUser(graph, "mary_j", "Mary J. Blige");

        Node karmin = createTrack(graph, UUID.randomUUID().toString(),
                                  Arrays.asList("Karmin"), "Crash you party", 0.99);
        Node theStreets =
            createTrack(graph, UUID.randomUUID().toString(),
                        Arrays.asList("The Streets"), "Blinded by the lights (Nero remix)", 1d);
        Node rihanna = createTrack(graph, UUID.randomUUID().toString(),
                                   Arrays.asList("Rihanna", "Calvin Harris"), "We found love", 2.2);
        Node cher = createTrack(graph, UUID.randomUUID().toString(),
                                Arrays.asList("Cher", "Mike Posner"), "With Ur Love", 1.9);
        Node skepta = createTrack(graph, UUID.randomUUID().toString(),
                                  Arrays.asList("Skepta"), "Hold On", 1.25);

        graph.addEdge(john.getNodeId(), karmin.getNodeId(), BOUGHT);
        graph.addEdge(john.getNodeId(), theStreets.getNodeId(), BOUGHT);
        graph.addEdge(sam.getNodeId(), theStreets.getNodeId(), BOUGHT);
        graph.addEdge(sam.getNodeId(), rihanna.getNodeId(), BOUGHT);
        graph.addEdge(sam.getNodeId(), cher.getNodeId(), BOUGHT);
        graph.addEdge(mary.getNodeId(), theStreets.getNodeId(), BOUGHT);
        graph.addEdge(mary.getNodeId(), cher.getNodeId(), BOUGHT);

        graph.addEdge(john.getNodeId(), karmin.getNodeId(), LISTENED_TO);
        graph.addEdge(john.getNodeId(), cher.getNodeId(), LISTENED_TO);
        graph.addEdge(sam.getNodeId(), karmin.getNodeId(), LISTENED_TO);
        graph.addEdge(sam.getNodeId(), theStreets.getNodeId(), LISTENED_TO);
        graph.addEdge(mary.getNodeId(), skepta.getNodeId(), LISTENED_TO);

        graph.addEdge(karmin.getNodeId(), theStreets.getNodeId(), SIMILAR, 1);
        graph.addEdge(theStreets.getNodeId(), karmin.getNodeId(), SIMILAR, 1);
        graph.addEdge(theStreets.getNodeId(), cher.getNodeId(), SIMILAR, 2);
        graph.addEdge(cher.getNodeId(), theStreets.getNodeId(), SIMILAR, 2);
        graph.addEdge(theStreets.getNodeId(), rihanna.getNodeId(), SIMILAR, 1);
        graph.addEdge(rihanna.getNodeId(), theStreets.getNodeId(), SIMILAR, 1);
        graph.addEdge(rihanna.getNodeId(), cher.getNodeId(), SIMILAR, 1);
        graph.addEdge(cher.getNodeId(), rihanna.getNodeId(), SIMILAR, 1);

        return graph;
    }

    private Node createUser(PropertyGraph graph, String userId, String userName) {
        Node user = graph.addNode(new NodeId(USER, userId));
        user.setProperty("Name", userName);
        graph.setNodeProperties(user.getNodeId(), user);
        return user;
    }

    private Node createTrack(PropertyGraph graph, String trackId,
                             List<String> artists, String title, double price) {
        Node track = graph.addNode(new NodeId(TRACK, trackId));
        track.setProperty("Artists", artists);
        track.setProperty("Title", title);
        track.setProperty("Price", price);
        graph.setNodeProperties(track.getNodeId(), track);
        return track;
    }

    /**
     * Main method. The graph will exported to file (as json) using the first
     * argument as file name, if such an argument is present. It will be
     * exported in graphviz dot format to a file with the name of argument 2, if
     * such an argument is present.
     */
    public static void main(String[] args) throws IOException {
        PropertyGraph graph = new ExampleGraphBuilder().createGraph();
        if (args.length > 0) {
            PropertyGraphJsonUtils.exportJson(graph, new File(args[0]), true, true);
        }
        if (args.length > 1) {
            new PropertyGraphDotExporter(graph).export(new File(args[1]));
        }
    }
}
