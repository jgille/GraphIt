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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.utils.LastFMGraph;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author jon
 *
 */
public class LastFMGraphReadOnlyLoadTest {

    private final int nofThreads = Runtime.getRuntime().availableProcessors() * 2;

    private static PropertyGraph graph;

    @BeforeClass
    public static void loadGraph() throws IOException {
        if (graph == null) {
            graph = LastFMGraph.load();
        }
    }

    @Test(timeout = 100)
    public void testOneGetEdges() throws IOException, InterruptedException, ExecutionException {
        runTest(nofThreads, 1, getScenario("OneGetEdges.csv"));
    }

    @Test(timeout = 60000)
    public void testCycleOneGetEdges() throws IOException, InterruptedException, ExecutionException {
        runTest(nofThreads, 50000, getScenario("OneGetEdges.csv"));
    }

    @Test(timeout = 2000)
    public void testGetEdgesForAllNodes() throws IOException, InterruptedException,
        ExecutionException {
        runTest(nofThreads, 1, getScenario("getEdgesForAllNodes.csv"));
    }

    @Test(timeout = 90000)
    public void testCycleGetEdgesForAllNodes() throws IOException, InterruptedException,
        ExecutionException {
        runTest(nofThreads, 100, getScenario("getEdgesForAllNodes.csv"));
    }

    private void runTest(int nofThreads, int nofCycles, File csv) throws IOException,
        InterruptedException, ExecutionException {
        String header = String.format("\n%s - %d cycles", csv.getName(), nofCycles);
        GraphLoadTestStats stats = new GraphLoadTestStats();
        List<GraphMethod<?>> methods = GraphMethodFactory.parseCsv(graph, stats, csv);
        ExecutorService service = Executors.newFixedThreadPool(nofThreads);
        stats.start();
        int j = 0;
        for (int i = 0; i < nofCycles; i++) {
            for (GraphMethod<?> method : methods) {
                Future<?> f = service.submit(method);
                if (j++ % nofThreads == 0) {
                    f.get();
                }
            }
        }
        service.shutdown();
        service.awaitTermination(30, TimeUnit.MINUTES);
        stats.finish(header);
    }

    private File getScenario(String fileName) throws IOException {
        File scearioDir = new ClassPathResource("performance/scenarios").getFile();
        return new File(scearioDir, fileName);
    }

    public static void main(String[] args) throws IOException, InterruptedException,
        ExecutionException {
        loadGraph();
        LastFMGraphReadOnlyLoadTest test = new LastFMGraphReadOnlyLoadTest();
        test.testCycleGetEdgesForAllNodes();
    }
}
