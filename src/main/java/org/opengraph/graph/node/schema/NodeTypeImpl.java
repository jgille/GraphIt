package org.opengraph.graph.node.schema;

public class NodeTypeImpl implements NodeType {

    private final String name;

    public NodeTypeImpl(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

}
