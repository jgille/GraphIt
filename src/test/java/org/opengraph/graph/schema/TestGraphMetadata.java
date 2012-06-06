package org.opengraph.graph.schema;

import org.opengraph.graph.edge.domain.TestEdgeTypes;
import org.opengraph.graph.edge.schema.EdgeTypes;
import org.opengraph.graph.node.domain.TestNodeTypes;
import org.opengraph.graph.node.schema.NodeTypes;
import org.opengraph.graph.schema.GraphMetadata;

public class TestGraphMetadata implements GraphMetadata {

    private final NodeTypes nodeTypes = new TestNodeTypes();
    private final EdgeTypes edgeTypes = new TestEdgeTypes();

    @Override
    public String getGraphName() {
        return "test";
    }

    @Override
    public NodeTypes getNodeTypes() {
        return nodeTypes;
    }

    @Override
    public EdgeTypes getEdgeTypes() {
        return edgeTypes;
    }

}
