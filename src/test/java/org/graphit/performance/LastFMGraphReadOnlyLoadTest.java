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

import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.utils.LastFMGraph;
import org.graphit.io.util.ResourceUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author jon
 *
 */
public class LastFMGraphReadOnlyLoadTest {

    private final int nofThreads = Runtime.getRuntime().availableProcessors() + 1;

    private static PropertyGraph graph;

    @BeforeClass
    public static void loadGraph() throws IOException {
        if (graph == null) {
            graph = LastFMGraph.load();
        }
    }

    @Test
    public void testOneGetEdges() throws IOException, InterruptedException, ExecutionException {
        LoadTester.runTest(graph, nofThreads, 1, getScenario("OneGetEdges.csv"));
    }

    @Test
    public void testCycleOneGetEdges() throws IOException, InterruptedException, ExecutionException {
        LoadTester.runTest(graph, nofThreads, 5000, getScenario("OneGetEdges.csv"));
    }

    @Test
    public void testGetEdgesForAllNodes() throws IOException, InterruptedException,
        ExecutionException {
        LoadTester.runTest(graph, nofThreads, 1, getScenario("getEdgesForAllNodes.csv"));
    }

    @Test
    public void testCycleGetEdgesForAllNodes() throws IOException, InterruptedException,
        ExecutionException {
        LoadTester.runTest(graph, nofThreads, 20, getScenario("getEdgesForAllNodes.csv"));
    }

    private File getScenario(String fileName) throws IOException {
        File scenarioDir = ResourceUtils.resourceFile("performance/scenarios");
        return new File(scenarioDir, fileName);
    }

    public static void main(String[] args) throws IOException, InterruptedException,
    ExecutionException {
        loadGraph();
        LastFMGraphReadOnlyLoadTest test = new LastFMGraphReadOnlyLoadTest();
        test.testCycleGetEdgesForAllNodes();
    }
}
