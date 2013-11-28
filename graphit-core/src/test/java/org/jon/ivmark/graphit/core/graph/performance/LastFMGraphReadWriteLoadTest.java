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

package org.jon.ivmark.graphit.core.graph.performance;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.edge.Edge;
import org.jon.ivmark.graphit.core.graph.edge.EdgeDirection;
import org.jon.ivmark.graphit.core.graph.lastfm.LastFMGraph;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.traversal.EdgeTypeFilter;
import org.jon.ivmark.graphit.core.graph.traversal.NodeTypeFilter;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.test.categories.LoadTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Category(LoadTest.class)
public class LastFMGraphReadWriteLoadTest {

    private final int nofThreads = Runtime.getRuntime().availableProcessors() + 1;

    private PropertyGraph graph;

    @Before
    public void loadGraph() throws IOException {
        this.graph = LastFMGraph.load();
    }

    @Test
    public void testReadOnly() throws IOException, InterruptedException,
        ExecutionException {
        LoadTester.runTest(graph, nofThreads, 1, getScenario("readOnlyScenario.csv"));
    }

    @Test
    public void testReadMostly() throws IOException, InterruptedException,
        ExecutionException {
        LoadTester.runTest(graph, nofThreads, 1, getScenario("readMostlyScenario.csv"));
    }

    @Test
    public void testWriteMostly() throws IOException, InterruptedException,
        ExecutionException {
        LoadTester.runTest(graph, nofThreads, 1, getScenario("writeMostlyScenario.csv"));
    }

    @Test
    public void testReadWrite() throws IOException, InterruptedException,
        ExecutionException {
        LoadTester.runTest(graph, nofThreads, 1, getScenario("readWriteScenario.csv"));
    }

    private static void createReadOnlyScenario(PropertyGraph graph) throws IOException {
        createScenario(graph, "/tmp/readOnlyScenario.csv", 100, 0, 0);
    }

    private static void createReadMostlyScenario(PropertyGraph graph) throws IOException {
        createScenario(graph, "/tmp/readMostlyScenario.csv", 90, 5, 5);
    }

    private static void createWriteMostlyScenario(PropertyGraph graph) throws IOException {
        createScenario(graph, "/tmp/writeMostlyScenario.csv", 20, 40, 40);
    }

    private static void createReadWriteScenario(PropertyGraph graph) throws IOException {
        createScenario(graph, "/tmp/readWriteScenario.csv", 50, 25, 25);
    }

    private static void createScenario(PropertyGraph graph,
                                       String fileName, int readPercentage,
                                       int removePercentage, int addPercentage) throws IOException {
        Preconditions.checkArgument(readPercentage + removePercentage + addPercentage == 100);
        Random random = new Random(1234);
        List<Edge> edges =
            graph.getEdges().filter(new EdgeTypeFilter(LastFMGraph.LISTENED_TO)).asList();
        Collections.shuffle(edges, random);
        List<Node> nodes = graph.getNodes().asList();
        Collections.shuffle(nodes, random);
        List<String[]> rows = new ArrayList<String[]>();

        List<Node> users = graph.getNodes().filter(new NodeTypeFilter(LastFMGraph.USER)).asList();
        List<Node> artists =
            graph.getNodes().filter(new NodeTypeFilter(LastFMGraph.ARTIST)).asList();
        int i = 0;
        for (Edge edge : edges) {
            Node node = edge.getStartNode();
            int j = i % 100;
            if (j < readPercentage) {
                EdgeDirection edgeDirection = node.getType().equals(LastFMGraph.USER) ?
                    EdgeDirection.OUTGOING : EdgeDirection.INCOMING;
                rows.add(new String[] { "GET_EDGES", node.getType().name(),
                        node.getNodeId().getId(), LastFMGraph.LISTENED_TO.name(),
                        edgeDirection.name()
                });
            } else if (j < readPercentage + removePercentage) {
                // Remove random edge
                rows.add(new String[] { "REMOVE_EDGE", edge.getType().name(),
                        edge.getIndex() + "" });
            } else {
                // Add random edge
                Node user = users.get(Math.abs(random.nextInt()) % users.size());
                Node artist = artists.get(Math.abs(random.nextInt()) % artists.size());
                rows.add(new String[] { "ADD_EDGE",
                        LastFMGraph.USER.name(), user.getNodeId().getId(),
                        LastFMGraph.ARTIST.name(), artist.getNodeId().getId(),
                        LastFMGraph.LISTENED_TO.name(), "100" });
            }
            i++;
        }
        Collections.shuffle(rows, random);
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(fileName));
            writer.writeAll(rows);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    private File getScenario(String fileName) throws IOException {
        File scearioDir = ResourceUtils.resourceFile("performance/scenarios");
        return new File(scearioDir, fileName);
    }

    public static void main(String[] args) throws IOException {
        PropertyGraph graph = LastFMGraph.load();
        createReadMostlyScenario(graph);
        createWriteMostlyScenario(graph);
        createReadWriteScenario(graph);
        createReadOnlyScenario(graph);
    }

}
