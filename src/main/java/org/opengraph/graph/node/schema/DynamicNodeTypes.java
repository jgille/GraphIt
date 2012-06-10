package org.opengraph.graph.node.schema;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class DynamicNodeTypes implements NodeTypes {

    private final Map<String, NodeType> nodeTypes;

    public DynamicNodeTypes() {
        this.nodeTypes = Collections.synchronizedMap(new LinkedHashMap<String, NodeType>());
    }

    @Override
    public NodeType valueOf(String name) {
        NodeType nodeType = nodeTypes.get(name);
        Assert.notNull(nodeType);
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

    public DynamicNodeTypes add(NodeType nodeType) {
        nodeTypes.put(nodeType.name(), nodeType);
        return this;
    }

}
