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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraph;

import com.google.common.collect.Lists;

/**
 * @author jon
 *
 */
public class LoadTester {

    static void runTest(PropertyGraph graph, final int nofThreads, final int nofCycles, File csv)
        throws IOException,
        InterruptedException, ExecutionException {
        String header = String.format("\n%s - %d cycles", csv.getName(), nofCycles);
        final GraphLoadTestStats stats = new GraphLoadTestStats();
        List<GraphMethod<?>> methods = GraphMethodFactory.parseCsv(graph, stats, csv);
        final List<List<GraphMethod<?>>> partitions =
            Lists.partition(methods, (int) Math.ceil(1d * methods.size() / nofThreads));

        ExecutorService service = Executors.newFixedThreadPool(nofThreads);
        stats.start();
        for (int c = 0; c < nofCycles; c++) {
            service.submit(new Runnable() {

                @Override
                public void run() {
                    for (int j = 0; j < nofThreads; j++) {
                        List<GraphMethod<?>> partition = partitions.get(j);
                        for (GraphMethod<?> method : partition) {
                            try {
                                method.invoke();
                            } catch (Exception e) {
                                stats.logError();
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
        service.shutdown();
        service.awaitTermination(30, TimeUnit.MINUTES);

        stats.finish(header);
        assertEquals(0, stats.getNofErrors());
    }

}
