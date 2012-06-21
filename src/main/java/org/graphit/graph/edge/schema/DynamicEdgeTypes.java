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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Dynamic {@link EdgeTypes} implementation keeping {@link EdgeType}s in a
 * {@link HashMap}.
 *
 * @author jon
 *
 */
public class DynamicEdgeTypes implements EdgeTypes {

    private final Map<String, EdgeType> edgeTypes;

    /**
     * Constructs a new instance.
     */
    public DynamicEdgeTypes() {
        this.edgeTypes = Collections.synchronizedMap(new LinkedHashMap<String, EdgeType>());
    }

    @Override
    public EdgeType valueOf(String name) {
        EdgeType edgeType = edgeTypes.get(name);
        Assert.notNull(edgeType);
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
    public DynamicEdgeTypes add(EdgeType edgeType) {
        edgeTypes.put(edgeType.name(), edgeType);
        return this;
    }

}
