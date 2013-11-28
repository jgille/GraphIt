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

/**
 * @author jon
 *
 */
public class GraphLoadTestStats {

    private long t0;
    private int nofGetEdges;
    private int nofGetNeighbors;
    private int nofTraversedEdges;
    private int nofAddEdges;
    private int nofRemoveEdges;
    private int nofErrors;

    public void start() {
        this.t0 = System.currentTimeMillis();
    }

    public void finish() {
        finish("");
    }

    public void finish(String header) {
        long t1 = System.currentTimeMillis();
        logResult(header, t1 - t0);
    }

    private void logResult(String header, long elapsedMillis) {
        String message =
            String.format("%s\n" +
                "Nof getEdges: %d\n" +
                "Nof getNeighbors: %d\n" +
                "Nof traversed edges: %d\n" +
                "Nof added edges: %d\n" +
                "Nof removed edges: %d\n" +
                "Nof ops: %d\n" +
                "Ops per second: %d\n" +
                "Traversed edges per second: %d\n" +
                "Elapsed time: %.2f s\n",
                          header,
                          nofGetEdges, nofGetNeighbors, nofTraversedEdges, nofAddEdges,
                          nofRemoveEdges,
                          nofGetEdges + nofGetNeighbors + nofAddEdges + nofRemoveEdges,
                          Math.round((1000d * (nofGetEdges + nofGetNeighbors +
                              nofAddEdges + nofRemoveEdges))
                              / elapsedMillis),
                          Math.round((1000d * nofTraversedEdges) / elapsedMillis),
                          elapsedMillis / 1000d
                );
        System.out.println(message);
    }

    public synchronized void logGetEdges(int nofEdges) {
        nofGetEdges++;
        nofTraversedEdges += nofEdges;
    }

    public synchronized void logGetNeighbors(int nofNeighbors) {
        nofGetNeighbors++;
        nofTraversedEdges += nofNeighbors;
    }

    public synchronized void logAddEdge() {
        nofAddEdges++;
    }

    public synchronized void logRemoveEdge() {
        nofRemoveEdges++;
    }

    public synchronized void logError() {
        nofErrors++;
    }

    public synchronized int getNofErrors() {
        return nofErrors;
    }

}
