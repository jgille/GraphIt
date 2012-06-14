package org.opengraph.graph.node.schema;

/**
 * Standar {@link NodeType} implementation.
 * 
 * @author jon
 * 
 */
public class NodeTypeImpl implements NodeType {

    private final String name;

    /**
     * Creates a new instance.
     */
    public NodeTypeImpl(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

}
