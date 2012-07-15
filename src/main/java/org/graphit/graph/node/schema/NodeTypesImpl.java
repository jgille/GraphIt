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
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

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
        this.nodeTypes = Collections.synchronizedMap(new TreeMap<String, NodeType>());
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
        Assert.isTrue(nodeTypes.containsKey(name), "No such edge type: " + name);
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
        Assert.isTrue(!nodeTypes.containsKey(nodeType.name()));
        nodeTypes.put(nodeType.name(), nodeType);
    }

    @Override
    public void add(String nodeTypeName) {
        NodeType nodeType = new NodeTypeImpl(nodeTypeName);
        nodeTypes.put(nodeType.name(), nodeType);
    }

    @Override
    public String toString() {
        return "NodeTypesImpl [nodeTypes=" + nodeTypes + "]";
    }

    @Override
    public NodeType getOrAdd(String nodeTypeName) {
        NodeType nodeType = nodeTypes.get(nodeTypeName);
        if (nodeType == null) {
            nodeType = new NodeTypeImpl(nodeTypeName);
            nodeTypes.put(nodeTypeName, nodeType);
        }
        return nodeType;
    }
}
