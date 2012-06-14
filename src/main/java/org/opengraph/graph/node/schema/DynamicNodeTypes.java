package org.opengraph.graph.node.schema;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * A dynamic set of {@link NodeType}s backed by a {@link HashMap}.
 *
 * @author jon
 *
 */
public class DynamicNodeTypes implements NodeTypes {

    private final Map<String, NodeType> nodeTypes;

    /**
     * Creates a new instance.
     */
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

    /**
     * Adds a {@link NodeType} to the set.
     */
    public DynamicNodeTypes add(NodeType nodeType) {
        nodeTypes.put(nodeType.name(), nodeType);
        return this;
    }

}
