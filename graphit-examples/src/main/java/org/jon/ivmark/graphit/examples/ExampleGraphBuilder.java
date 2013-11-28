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

import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.GraphMetadata;
import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.PropertyGraphDotExporter;
import org.jon.ivmark.graphit.core.graph.PropertyGraphImpl;
import org.jon.ivmark.graphit.core.graph.PropertyGraphJsonUtils;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.jon.ivmark.graphit.examples.ExampleConstants.*;

/**
 * A class that generates the example graph.
 *
 * @author jon
 *
 */
public class ExampleGraphBuilder {

    private PropertyGraph createGraph() {
        GraphMetadata metadata = new GraphMetadata("Music store");
        metadata.addNodeType(USER)
            .addNodeType(TRACK)
            .addEdgeType(BOUGHT)
            .addEdgeType(LISTENED_TO)
            .addEdgeType(SIMILAR);

        PropertyGraph graph = new PropertyGraphImpl(metadata);

        NodeId john = createUser(graph, "john.doe", "John Doe");
        NodeId sam = createUser(graph, "sam98", "Samantha Fox");
        NodeId mary = createUser(graph, "mary_j", "Mary J. Blige");

        NodeId karmin = createTrack(graph, UUID.randomUUID().toString(),
                                    Arrays.asList("Karmin"), "Crash you party", 0.99);
        NodeId theStreets =
            createTrack(graph, UUID.randomUUID().toString(),
                        Arrays.asList("The Streets"), "Blinded by the lights (Nero remix)", 1d);
        NodeId rihanna =
            createTrack(graph, UUID.randomUUID().toString(),
                        Arrays.asList("Rihanna", "Calvin Harris"), "We found love", 2.2);
        NodeId cher = createTrack(graph, UUID.randomUUID().toString(),
                                  Arrays.asList("Cher", "Mike Posner"), "With Ur Love", 1.9);
        NodeId skepta = createTrack(graph, UUID.randomUUID().toString(),
                                    Arrays.asList("Skepta"), "Hold On", 1.25);

        graph.addEdge(john, karmin, BOUGHT);
        graph.addEdge(john, theStreets, BOUGHT);
        graph.addEdge(sam, theStreets, BOUGHT);
        graph.addEdge(sam, rihanna, BOUGHT);
        graph.addEdge(sam, cher, BOUGHT);
        graph.addEdge(mary, theStreets, BOUGHT);
        graph.addEdge(mary, cher, BOUGHT);

        graph.addEdge(john, karmin, LISTENED_TO);
        graph.addEdge(john, cher, LISTENED_TO);
        graph.addEdge(sam, karmin, LISTENED_TO);
        graph.addEdge(sam, theStreets, LISTENED_TO);
        graph.addEdge(mary, skepta, LISTENED_TO);

        graph.addEdge(karmin, theStreets, SIMILAR, 1);
        graph.addEdge(theStreets, karmin, SIMILAR, 1);
        graph.addEdge(theStreets, cher, SIMILAR, 2);
        graph.addEdge(cher, theStreets, SIMILAR, 2);
        graph.addEdge(theStreets, rihanna, SIMILAR, 1);
        graph.addEdge(rihanna, theStreets, SIMILAR, 1);
        graph.addEdge(rihanna, cher, SIMILAR, 1);
        graph.addEdge(cher, rihanna, SIMILAR, 1);

        return graph;
    }

    private NodeId createUser(PropertyGraph graph, String userId, String userName) {
        Node user = graph.addNode(new NodeId(USER, userId));
        Properties properties = new HashMapProperties(1);
        properties.setProperty("Name", userName);
        graph.setNodeProperties(user.getNodeId(), properties);
        return user.getNodeId();
    }

    private NodeId createTrack(PropertyGraph graph, String trackId,
                               List<String> artists, String title, double price) {
        Node track = graph.addNode(new NodeId(TRACK, trackId));
        Properties properties = new HashMapProperties(3);
        properties.setProperty("Artists", artists);
        properties.setProperty("Title", title);
        properties.setProperty("Price", price);
        graph.setNodeProperties(track.getNodeId(), properties);
        return track.getNodeId();
    }

    /**
     * Main method. The graph will exported to file (as json) using the first
     * argument as file name, if such an argument is present. It will also be
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
