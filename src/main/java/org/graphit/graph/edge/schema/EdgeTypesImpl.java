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

package org.graphit.graph.edge.schema;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dynamic {@link EdgeTypes} implementation keeping {@link EdgeType}s in a
 * {@link HashMap}.
 *
 * @author jon
 *
 */
public class EdgeTypesImpl implements EdgeTypes {

    private final Map<String, EdgeType> edgeTypes;

    /**
     * Constructs a new instance.
     */
    public EdgeTypesImpl() {
        this.edgeTypes = new ConcurrentHashMap<String, EdgeType>();
    }

    /**
     * Ensures the existance of the given edge type.
     */
    public void ensureHasEdgeType(EdgeType edgeType) {
        if (!edgeTypes.containsKey(edgeType.name())) {
            edgeTypes.put(edgeType.name(), edgeType);
        }
    }

    @Override
    public EdgeType valueOf(String name) {
        EdgeType edgeType = edgeTypes.get(name);
        if (edgeType == null) {
            edgeType = new EdgeTypeImpl(name);
            add(edgeType);
        }
        return edgeType;
    }

    @Override
    public Collection<EdgeType> elements() {
        return edgeTypes.values();
    }

    @Override
    public int size() {
        return edgeTypes.size();
    }

    /**
     * Adds an edge type to this edge type set.
     */
    public EdgeTypesImpl add(EdgeType edgeType) {
        edgeTypes.put(edgeType.name(), edgeType);
        return this;
    }

    /**
     * Adds an unweighted edge type to this edge type set.
     */
    public EdgeTypesImpl add(String edgeTypeName) {
        return add(new EdgeTypeImpl(edgeTypeName));
    }
}
