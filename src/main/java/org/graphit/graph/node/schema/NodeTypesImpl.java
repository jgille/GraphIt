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

package org.graphit.graph.node.schema;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

/**
 * A dynamic set of {@link NodeType}s backed by a {@link HashMap}.
 *
 * @author jon
 *
 */
public class NodeTypesImpl implements NodeTypes {

    private final Map<String, NodeType> nodeTypes;

    /**
     * Creates a new instance.
     */
    public NodeTypesImpl() {
        this.nodeTypes = new ConcurrentHashMap<String, NodeType>();
    }

    /**
     * Ensures the existance of the given node type.
     */
    public void ensureHasNodeType(String nodeTypeName) {
        if (!nodeTypes.containsKey(nodeTypeName)) {
            nodeTypes.put(nodeTypeName, new NodeTypeImpl(nodeTypeName));
        }
    }

    @Override
    public NodeType valueOf(String name) {
        NodeType nodeType = nodeTypes.get(name);
        Assert.notNull(nodeType, "No node type named '" + name + "'.");
        return nodeType;
    }

    @Override
    public Collection<NodeType> elements() {
        return nodeTypes.values();
    }

    @Override
    public int size() {
        return nodeTypes.size();
    }

    /**
     * Adds a {@link NodeType} to the set.
     */
    public NodeTypesImpl add(NodeType nodeType) {
        nodeTypes.put(nodeType.name(), nodeType);
        return this;
    }

    /**
     * Adds a {@link NodeType} to the set.
     */
    public NodeTypesImpl add(String nodeTypeName) {
        NodeType nodeType = new NodeTypeImpl(nodeTypeName);
        nodeTypes.put(nodeType.name(), nodeType);
        return this;
    }

    @Override
    public boolean contains(NodeType nodeType) {
        return nodeTypes.containsKey(nodeType.name())
            && nodeType.equals(nodeTypes.get(nodeType.name()));
    }

}
