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

package org.jon.ivmark.graphit.core.graph.traversal;

import com.google.common.base.Predicate;
import org.jon.ivmark.graphit.core.graph.edge.Edge;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A filter on edge type.
 *
 * @author jon
 * 
 */
public class EdgeTypeFilter implements Predicate<Edge> {

    private final Set<EdgeType> edgeTypes;

    /**
     * Creates a new filter.
     */
    public EdgeTypeFilter(EdgeType... edgeTypes) {
        this.edgeTypes = new HashSet<EdgeType>(Arrays.asList(edgeTypes));
    }

    @Override
    public boolean apply(Edge edge) {
        return edgeTypes.contains(edge.getType());
    }

}
