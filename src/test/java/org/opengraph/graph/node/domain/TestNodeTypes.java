package org.opengraph.graph.node.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.opengraph.graph.node.schema.NodeType;
import org.opengraph.graph.node.schema.NodeTypes;

public class TestNodeTypes implements NodeTypes {

    private final List<NodeType> elements;

    public TestNodeTypes() {
        this.elements = new ArrayList<NodeType>();
        for (TestNodeType nt : EnumSet.allOf(TestNodeType.class)) {
            elements.add(nt);
        }
    }

    @Override
    public NodeType valueOf(String name) {
        return TestNodeType.valueOf(name);
    }

    @Override
    public Collection<NodeType> elements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public String getGraphName() {
        return "test";
    }

}
