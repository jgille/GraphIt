package org.graphit.graph.schema;

import org.graphit.graph.edge.domain.TestEdgeTypes;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.domain.TestNodeTypes;
import org.graphit.graph.node.schema.NodeTypes;
import org.graphit.graph.schema.GraphMetadata;

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
