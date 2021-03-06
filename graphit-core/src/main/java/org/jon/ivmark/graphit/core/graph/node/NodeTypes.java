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

package org.jon.ivmark.graphit.core.graph.node;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.properties.DynamicEnumerationSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A dynamic set of {@link NodeType}s backed by a {@link HashMap}.
 *
 * @author jon
 */
public final class NodeTypes implements DynamicEnumerationSet<NodeType> {

    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    private final Map<String, NodeType> nodeTypes;

    /**
     * Creates a new instance.
     */
    public NodeTypes() {
        this.nodeTypes = Collections.synchronizedMap(new HashMap<String, NodeType>());
    }

    /**
     * Ensures the existance of the given node type.
     */
    public NodeType getOrAdd(String nodeTypeName) {
        if (!nodeTypes.containsKey(nodeTypeName)) {
            NodeType nodeType = new NodeType(nodeTypeName);
            add(nodeType);
        }
        return nodeTypes.get(nodeTypeName);
    }

    @Override
    public NodeType valueOf(String name) {
        Preconditions.checkArgument(nodeTypes.containsKey(name), "No such node type: " + name);
        return nodeTypes.get(name);
    }

    @Override
    public Collection<NodeType> elements() {
        return nodeTypes.values();
    }

    @Override
    public int size() {
        return nodeTypes.size();
    }

    @Override
    public void add(NodeType nodeType) {
        String nodeTypeName = nodeType.name();
        Preconditions.checkArgument(VALID_NAME_PATTERN.matcher(nodeTypeName).matches(),
                "Invalid node type name");
        Preconditions.checkArgument(!nodeTypes.containsKey(nodeTypeName));

        nodeTypes.put(nodeTypeName, nodeType);
    }

    @Override
    public void add(String nodeTypeName) {
        NodeType nodeType = new NodeType(nodeTypeName);
        add(nodeType);
    }

    @Override
    public String toString() {
        return "NodeTypes [nodeTypes=" + nodeTypes + "]";
    }
}
