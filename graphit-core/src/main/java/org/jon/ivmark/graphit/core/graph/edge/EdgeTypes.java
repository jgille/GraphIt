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

package org.jon.ivmark.graphit.core.graph.edge;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.properties.DynamicEnumerationSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Dynamic {@link EdgeTypes} implementation keeping {@link EdgeType}s in a
 * {@link HashMap}.
 *
 * @author jon
 */
public final class EdgeTypes implements DynamicEnumerationSet<EdgeType> {

    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    private final Map<String, EdgeType> edgeTypes;

    /**
     * Constructs a new instance.
     */
    public EdgeTypes() {
        this.edgeTypes = Collections.synchronizedMap(new HashMap<String, EdgeType>());
    }

    @Override
    public EdgeType valueOf(String name) {
        Preconditions.checkArgument(edgeTypes.containsKey(name), "No such edge type: " + name);
        return edgeTypes.get(name);
    }

    @Override
    public Collection<EdgeType> elements() {
        return edgeTypes.values();
    }

    @Override
    public int size() {
        return edgeTypes.size();
    }

    @Override
    public void add(EdgeType edgeType) {
        String edgeTypeName = edgeType.name();
        Preconditions.checkArgument(VALID_NAME_PATTERN.matcher(edgeTypeName).matches(),
                "Invalid edge type name");
        Preconditions.checkArgument(!edgeTypes.containsKey(edgeType.name()));
        edgeTypes.put(edgeType.name(), edgeType);
    }

    @Override
    public void add(String edgeTypeName) {
        add(new EdgeTypeImpl(edgeTypeName));
    }

    @Override
    public String toString() {
        return "EdgeTypes [edgeTypes=" + edgeTypes + "]";
    }

    @Override
    public EdgeType getOrAdd(String edgeTypeName) {
        EdgeType edgeType = edgeTypes.get(edgeTypeName);
        if (edgeType == null) {
            edgeType = new EdgeTypeImpl(edgeTypeName);
            add(edgeType);
        }
        return edgeType;
    }
}
