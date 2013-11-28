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

import org.jon.ivmark.graphit.core.graph.service.PropertyGraph;

/**
 * @author jon
 *
 */
public abstract class AbstractGraphMethod<V> implements GraphMethod<V> {

    private final PropertyGraph graph;
    private final GraphLoadTestStats stats;

    public AbstractGraphMethod(PropertyGraph graph, GraphLoadTestStats stats) {
        this.graph = graph;
        this.stats = stats;
    }

    protected PropertyGraph getGraph() {
        return graph;
    }

    public GraphLoadTestStats getStats() {
        return stats;
    }

}
