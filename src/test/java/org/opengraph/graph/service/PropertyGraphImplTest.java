package org.opengraph.graph.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.opengraph.graph.edge.util.TestEdgeType.BOUGHT;
import static org.opengraph.graph.edge.util.TestEdgeType.SIMILAR;
import static org.opengraph.graph.edge.util.TestEdgeType.VIEWED;
import static org.opengraph.graph.node.domain.TestNodeType.PRODUCT;
import static org.opengraph.graph.node.domain.TestNodeType.USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.opengraph.graph.blueprints.BlueprintsGraphImpl;
import org.opengraph.graph.edge.domain.Edge;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.node.domain.Node;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.node.schema.NodeType;
import org.opengraph.graph.schema.TestGraphMetadata;
import org.opengraph.graph.service.PropertyGraph;
import org.opengraph.graph.service.PropertyGraphImpl;
import org.opengraph.graph.traversal.EdgeDirection;
import org.springframework.core.convert.converter.Converter;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class PropertyGraphImplTest {

    @Test
    public void testAddSingleEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());

        NodeId u1 = new NodeId(USER, "u1");
        graph.addNode(u1);

        NodeId p1 = new NodeId(PRODUCT, "p1");
        graph.addNode(p1);

        Edge edge = graph.addEdge(u1, p1, BOUGHT);
        assertThat(edge.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge.getEndNode().getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testAddDuplicateNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());

        NodeId u1 = new NodeId(USER, "u1");
        graph.addNode(u1);

        boolean exception = false;
        try {
            graph.addNode(u1);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertThat(exception, Matchers.is(true));
    }

    @Test
    public void testAddEdgeWithNonExistingStartNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());

        NodeId u1 = new NodeId(USER, "u1");

        NodeId p1 = new NodeId(PRODUCT, "p1");
        graph.addNode(p1);

        boolean exception = false;
        try {
            graph.addEdge(u1, p1, BOUGHT);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertThat(exception, Matchers.is(true));
    }

    @Test
    public void testAddEdgeWithNonExistingEndNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());

        NodeId u1 = new NodeId(USER, "u1");
        graph.addNode(u1);

        NodeId p1 = new NodeId(PRODUCT, "p1");

        boolean exception = false;
        try {
            graph.addEdge(u1, p1, BOUGHT);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertThat(exception, Matchers.is(true));
    }

    @Test
    public void testGetExistingNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u1 = new NodeId(USER, "u1");
        Node node = graph.getNode(u1);

        assertThat(node, Matchers.notNullValue());
        assertThat(node.getNodeId(), Matchers.is(u1));

        assertThat(graph.getNode(node.getIndex()), Matchers.is(node));
    }

    @Test
    public void testGetNonExistingNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u2 = new NodeId(USER, "u2");
        Node node = graph.getNode(u2);

        assertThat(node, Matchers.nullValue());
    }

    @Test
    public void testGetExistingEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());

        NodeId u1 = new NodeId(USER, "u1");
        graph.addNode(u1);

        NodeId u2 = new NodeId(USER, "u2");
        graph.addNode(u2);

        NodeId p1 = new NodeId(PRODUCT, "p1");
        graph.addNode(p1);

        NodeId p2 = new NodeId(PRODUCT, "p2");
        graph.addNode(p2);

        NodeId p3 = new NodeId(PRODUCT, "p3");
        graph.addNode(p3);

        graph.addEdge(u1, p1, BOUGHT);
        Edge e2 = graph.addEdge(u1, p2, VIEWED);
        graph.addEdge(u2, p3, VIEWED);

        Edge edge = graph.getEdge(e2.getEdgeId());
        assertThat(edge.getType(), Matchers.is((EdgeType) VIEWED));

        assertThat(edge.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge.getEndNode().getNodeId(), Matchers.is(p2));
    }

    @Test
    public void testGetNonExistingEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());

        NodeId u1 = new NodeId(USER, "u1");
        graph.addNode(u1);

        NodeId u2 = new NodeId(USER, "u2");
        graph.addNode(u2);

        NodeId p1 = new NodeId(PRODUCT, "p1");
        graph.addNode(p1);

        NodeId p2 = new NodeId(PRODUCT, "p2");
        graph.addNode(p2);

        graph.addEdge(u1, p1, BOUGHT);
        graph.addEdge(u1, p2, VIEWED);

        Edge edge1 = graph.getEdge(new EdgeId(BOUGHT, Integer.MAX_VALUE));
        assertThat(edge1, Matchers.nullValue());

        Edge edge2 = graph.getEdge(new EdgeId(SIMILAR, 0));
        assertThat(edge2, Matchers.nullValue());
    }

    @Test
    public void testIterateOutgoingEdgesForNonExistingNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u2 = new NodeId(USER, "u2");
        List<Edge> edges =
            asList(graph.getEdges(u2, BOUGHT, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(0));
    }

    @Test
    public void testIterateIncomingEdgesForNonExistingNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u2 = new NodeId(USER, "u2");
        List<Edge> edges =
            asList(graph.getEdges(u2, BOUGHT, EdgeDirection.INCOMING));
        assertThat(edges.size(), Matchers.is(0));
    }

    @Test
    public void testIterateSingleOutgoingEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Edge> edges =
            asList(graph.getEdges(u1, BOUGHT, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(1));
        Edge edge = edges.get(0);

        assertThat(edge.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge.getEndNode().getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testIterateSingleIncomingEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Edge> edges =
            asList(graph.getEdges(p1, BOUGHT, EdgeDirection.INCOMING));
        assertThat(edges.size(), Matchers.is(1));
        Edge edge = edges.get(0);

        assertThat(edge.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge.getEndNode().getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testMultipleUnsortedOutgoingEdges() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1", "p2")
            .buy("u1", "p1", "p2");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        NodeId p2 = new NodeId(PRODUCT, "p2");

        List<Edge> edges =
            asList(graph.getEdges(u1, BOUGHT, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(2));
        Edge edge1 = edges.get(0);

        assertThat(edge1.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge1.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge1.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge1.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge1.getEndNode().getNodeId(), Matchers.is(p1));

        Edge edge2 = edges.get(1);

        assertThat(edge2.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge2.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge2.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge2.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge2.getEndNode().getNodeId(), Matchers.is(p2));
    }

    @Test
    public void testMultipleUnsortedIncomingEdges() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1", "u2").addProducts("p1", "p2")
            .buy("u1", "p1", "p2").buy("u2", "p1");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId u2 = new NodeId(USER, "u2");
        NodeId p1 = new NodeId(PRODUCT, "p1");

        List<Edge> edges =
            asList(graph.getEdges(p1, BOUGHT, EdgeDirection.INCOMING));
        assertThat(edges.size(), Matchers.is(2));
        Edge edge1 = edges.get(0);

        assertThat(edge1.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge1.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge1.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge1.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge1.getEndNode().getNodeId(), Matchers.is(p1));

        Edge edge2 = edges.get(1);

        assertThat(edge2.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge2.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge2.getStartNode().getNodeId(), Matchers.is(u2));

        assertThat(edge2.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge2.getEndNode().getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testMultipleSortedOutgoingEdges() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3", "p4")
            .similar("p1", "p2", 1).similar("p1", "p3", 3).similar("p1", "p4", 2);

        NodeId p1 = new NodeId(PRODUCT, "p1");
        NodeId p2 = new NodeId(PRODUCT, "p2");
        NodeId p3 = new NodeId(PRODUCT, "p3");
        NodeId p4 = new NodeId(PRODUCT, "p4");

        List<Edge> edges =
            asList(graph.getEdges(p1, SIMILAR, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(3));
        Edge edge1 = edges.get(0);

        assertThat(edge1.getType(), Matchers.is((EdgeType) SIMILAR));

        assertThat(edge1.getStartNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge1.getStartNode().getNodeId(), Matchers.is(p1));

        assertThat(edge1.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge1.getEndNode().getNodeId(), Matchers.is(p3));

        Edge edge2 = edges.get(1);

        assertThat(edge2.getType(), Matchers.is((EdgeType) SIMILAR));

        assertThat(edge2.getStartNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge2.getStartNode().getNodeId(), Matchers.is(p1));

        assertThat(edge2.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge2.getEndNode().getNodeId(), Matchers.is(p4));

        Edge edge3 = edges.get(2);

        assertThat(edge3.getType(), Matchers.is((EdgeType) SIMILAR));

        assertThat(edge3.getStartNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge3.getStartNode().getNodeId(), Matchers.is(p1));

        assertThat(edge3.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge3.getEndNode().getNodeId(), Matchers.is(p2));
    }

    @Test
    public void testMultipleSortedIncomingEdges() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3", "p4")
            .similar("p1", "p2", 1).similar("p3", "p2", 3).similar("p4", "p2", 2);

        NodeId p1 = new NodeId(PRODUCT, "p1");
        NodeId p2 = new NodeId(PRODUCT, "p2");
        NodeId p3 = new NodeId(PRODUCT, "p3");
        NodeId p4 = new NodeId(PRODUCT, "p4");

        List<Edge> edges =
            asList(graph.getEdges(p2, SIMILAR, EdgeDirection.INCOMING));
        assertThat(edges.size(), Matchers.is(3));
        Edge edge1 = edges.get(0);

        assertThat(edge1.getType(), Matchers.is((EdgeType) SIMILAR));

        assertThat(edge1.getStartNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge1.getStartNode().getNodeId(), Matchers.is(p3));

        assertThat(edge1.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge1.getEndNode().getNodeId(), Matchers.is(p2));

        Edge edge2 = edges.get(1);

        assertThat(edge2.getType(), Matchers.is((EdgeType) SIMILAR));

        assertThat(edge2.getStartNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge2.getStartNode().getNodeId(), Matchers.is(p4));

        assertThat(edge2.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge2.getEndNode().getNodeId(), Matchers.is(p2));

        Edge edge3 = edges.get(2);

        assertThat(edge3.getType(), Matchers.is((EdgeType) SIMILAR));

        assertThat(edge3.getStartNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge3.getStartNode().getNodeId(), Matchers.is(p1));

        assertThat(edge3.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge3.getEndNode().getNodeId(), Matchers.is(p2));
    }

    @Test
    public void testMultipleOutgoingEdgeTypes() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1", "p2").buy("u1", "p1")
            .view("u1", "p2");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Edge> edges =
            asList(graph.getEdges(u1, BOUGHT, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(1));
        Edge edge = edges.get(0);

        assertThat(edge.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge.getEndNode().getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testMultipleIncomingEdgeTypes() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1", "p2").buy("u1", "p1")
            .view("u1", "p2");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Edge> edges =
            asList(graph.getEdges(p1, BOUGHT, EdgeDirection.INCOMING));
        assertThat(edges.size(), Matchers.is(1));
        Edge edge = edges.get(0);

        assertThat(edge.getType(), Matchers.is((EdgeType) BOUGHT));

        assertThat(edge.getStartNode().getType(), Matchers.is((NodeType) USER));
        assertThat(edge.getStartNode().getNodeId(), Matchers.is(u1));

        assertThat(edge.getEndNode().getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(edge.getEndNode().getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testIterateSingleOutgoingConvertedEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        Converter<Edge, Node> converter = new Converter<Edge, Node>() {

            @Override
            public Node convert(Edge edge) {
                return edge.getEndNode();
            }
        };

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Node> neighbors =
            asList(graph.getAndConvertEdges(u1, BOUGHT, EdgeDirection.OUTGOING, converter));
        assertThat(neighbors.size(), Matchers.is(1));
        Node node = neighbors.get(0);

        assertThat(node.getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(node.getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testIterateSingleOutgoingNeighbor() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");
        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Node> neighbors =
            asList(graph.getNeighbors(u1, BOUGHT, EdgeDirection.OUTGOING));
        assertThat(neighbors.size(), Matchers.is(1));
        Node node = neighbors.get(0);

        assertThat(node.getType(), Matchers.is((NodeType) PRODUCT));
        assertThat(node.getNodeId(), Matchers.is(p1));
    }

    @Test
    public void testIterateSingleOutgoingConvertedNeighbor() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        Converter<Node, String> converter = new Converter<Node, String>() {

            @Override
            public String convert(Node node) {
                return node.getNodeId().getId();
            }
        };

        NodeId u1 = new NodeId(USER, "u1");
        List<String> neighbors =
            asList(graph.getAndConvertNeighbors(u1, BOUGHT, EdgeDirection.OUTGOING, converter));
        assertThat(neighbors.size(), Matchers.is(1));
        String nodeId = neighbors.get(0);

        assertThat(nodeId, Matchers.is("p1"));
    }

    @Test
    public void testIterateSingleIncomingConvertedEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        Converter<Edge, Node> converter = new Converter<Edge, Node>() {

            @Override
            public Node convert(Edge edge) {
                return edge.getStartNode();
            }
        };

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Node> neighbors =
            asList(graph.getAndConvertEdges(p1, BOUGHT, EdgeDirection.INCOMING, converter));
        assertThat(neighbors.size(), Matchers.is(1));
        Node node = neighbors.get(0);

        assertThat(node.getType(), Matchers.is((NodeType) USER));
        assertThat(node.getNodeId(), Matchers.is(u1));
    }

    @Test
    public void testIterateSingleIncomingNeighbor() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<Node> neighbors =
            asList(graph.getNeighbors(p1, BOUGHT, EdgeDirection.INCOMING));
        assertThat(neighbors.size(), Matchers.is(1));
        Node node = neighbors.get(0);

        assertThat(node.getType(), Matchers.is((NodeType) USER));
        assertThat(node.getNodeId(), Matchers.is(u1));
    }

    @Test
    public void testIterateSingleIncomingConvertedNeighbor() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1").buy("u1", "p1");

        Converter<Node, String> converter = new Converter<Node, String>() {

            @Override
            public String convert(Node node) {
                return node.getNodeId().getId();
            }
        };

        NodeId p1 = new NodeId(PRODUCT, "p1");
        List<String> neighbors =
            asList(graph.getAndConvertNeighbors(p1, BOUGHT, EdgeDirection.INCOMING, converter));
        assertThat(neighbors.size(), Matchers.is(1));
        String nodeId = neighbors.get(0);

        assertThat(nodeId, Matchers.is("u1"));
    }

    @Test
    public void testRemoveUnsortedEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3").addUsers("u1", "u2")
            .buy("u1", "p1", "p2", "p3").buy("u2", "p3");

        List<Edge> edges =
            asList(graph.getEdges(new NodeId(USER, "u1"), BOUGHT, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(3));

        Edge e1 = edges.get(0);
        Edge e2 = edges.get(1);
        Edge e3 = edges.get(2);

        graph.removeEdge(e2.getEdgeId());
        List<Edge> modifiedEdges =
            asList(graph.getEdges(new NodeId(USER, "u1"), BOUGHT, EdgeDirection.OUTGOING));
        assertThat(modifiedEdges.size(), Matchers.is(2));
        assertThat(modifiedEdges, Matchers.is(Arrays.asList(e1, e3)));
    }

    @Test
    public void testRemoveSortedEdge() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3", "p4")
            .similar("p1", "p2", 2).similar("p1", "p3", 3).similar("p1", "p4", 1);

        List<Edge> edges =
            asList(graph.getEdges(new NodeId(PRODUCT, "p1"), SIMILAR, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(3));

        Edge e1 = edges.get(0);
        Edge e2 = edges.get(1);
        Edge e3 = edges.get(2);

        graph.removeEdge(e2.getEdgeId());
        List<Edge> modifiedEdges =
            asList(graph.getEdges(new NodeId(PRODUCT, "p1"), SIMILAR, EdgeDirection.OUTGOING));
        assertThat(modifiedEdges.size(), Matchers.is(2));
        assertThat(modifiedEdges, Matchers.is(Arrays.asList(e1, e3)));
    }

    @Test
    public void testSetEdgeWeightForWeightedEdgeType() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3", "p4")
            .similar("p1", "p2", 2).similar("p1", "p3", 3).similar("p1", "p4", 1);

        List<Edge> edges =
            asList(graph.getEdges(new NodeId(PRODUCT, "p1"), SIMILAR, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(3));

        Edge e1 = edges.get(0);
        Edge e2 = edges.get(1);
        Edge e3 = edges.get(2);

        graph.setEdgeWeight(e2.getEdgeId(), 5);
        List<Edge> modifiedEdges =
            asList(graph.getEdges(new NodeId(PRODUCT, "p1"), SIMILAR, EdgeDirection.OUTGOING));
        assertThat(modifiedEdges.size(), Matchers.is(3));
        assertThat(modifiedEdges, Matchers.is(Arrays.asList(e2, e1, e3)));
    }

    @Test
    public void testSetEdgeWeightForUnweightedEdgeType() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3").addUsers("u1", "u2")
            .buy("u1", "p1", "p2", "p3").buy("u2", "p3");

        List<Edge> edges =
            asList(graph.getEdges(new NodeId(USER, "u1"), BOUGHT, EdgeDirection.OUTGOING));
        assertThat(edges.size(), Matchers.is(3));

        Edge e2 = edges.get(1);

        boolean exception = false;
        try {
            graph.setEdgeWeight(e2.getEdgeId(), 5);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertThat(exception, Matchers.is(true));
    }

    @Test
    public void testAsBlueprintsGraph() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addUsers("u1").addProducts("p1", "p2", "p3").buy("u1", "p1", "p3")
            .view("u1", "p2");
        Graph blueprintsGraph = new BlueprintsGraphImpl(graph);
        assertThat(blueprintsGraph, Matchers.notNullValue());
        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");
        NodeId p3 = new NodeId(PRODUCT, "p3");

        Vertex v1 = blueprintsGraph.getVertex(u1);
        assertThat(v1, Matchers.notNullValue());
        assertThat(v1.getId(), Matchers.is((Object) u1));

        List<com.tinkerpop.blueprints.Edge> edges =
            asList(v1.getEdges(Direction.OUT, BOUGHT.name()));
        assertThat(edges.size(), Matchers.is(2));

        com.tinkerpop.blueprints.Edge e1 = edges.get(0);
        assertThat(e1.getVertex(Direction.OUT).getId(), Matchers.is((Object) u1));
        assertThat(e1.getVertex(Direction.IN).getId(), Matchers.is((Object) p1));

        com.tinkerpop.blueprints.Edge e2 = edges.get(1);
        assertThat(e2.getVertex(Direction.OUT).getId(), Matchers.is((Object) u1));
        assertThat(e2.getVertex(Direction.IN).getId(), Matchers.is((Object) p3));
    }

    @Test
    public void testRemoveNode() {
        PropertyGraph graph = new PropertyGraphImpl(new TestGraphMetadata());
        new GraphBuilder(graph).addProducts("p1", "p2", "p3").addUsers("u1", "u2")
            .buy("u1", "p1", "p2", "p3").buy("u2", "p3").view("u1", "p2").similar("p1", "p2", 10);

        NodeId p2 = new NodeId(PRODUCT, "p2");
        graph.removeNode(p2);

        NodeId u1 = new NodeId(USER, "u1");
        NodeId p1 = new NodeId(PRODUCT, "p1");

        List<Node> u1Bought = asList(graph.getNeighbors(u1, BOUGHT, EdgeDirection.OUTGOING));
        assertThat(u1Bought.size(), Matchers.is(2));
        assertThat(u1Bought.get(0).getNodeId().getId(), Matchers.is("p1"));
        assertThat(u1Bought.get(1).getNodeId().getId(), Matchers.is("p3"));

        List<Node> u1Viewed = asList(graph.getNeighbors(u1, VIEWED, EdgeDirection.OUTGOING));
        assertThat(u1Viewed.size(), Matchers.is(0));

        List<Node> p1Similar = asList(graph.getNeighbors(p1, SIMILAR, EdgeDirection.OUTGOING));
        assertThat(p1Similar.size(), Matchers.is(0));

        Node node = graph.getNode(p2);
        assertThat(node, Matchers.nullValue());
    }

    private <E> List<E> asList(Iterable<E> it) {
        List<E> list = new ArrayList<E>();
        for (E value : it) {
            list.add(value);
        }
        return list;
    }

    private static class GraphBuilder {

        private final PropertyGraph graph;

        public GraphBuilder(PropertyGraph graph) {
            this.graph = graph;
        }

        public GraphBuilder addUsers(String... userIds) {
            for (String userId : userIds) {
                graph.addNode(new NodeId(USER, userId));
            }
            return this;
        }

        public GraphBuilder addProducts(String... productIds) {
            for (String productId : productIds) {
                graph.addNode(new NodeId(PRODUCT, productId));
            }
            return this;
        }

        public GraphBuilder buy(String userId, String... productIds) {
            for (String productId : productIds) {
                graph.addEdge(new NodeId(USER, userId), new NodeId(PRODUCT, productId), BOUGHT);
            }
            return this;
        }

        public GraphBuilder view(String userId, String... productIds) {
            for (String productId : productIds) {
                graph.addEdge(new NodeId(USER, userId), new NodeId(PRODUCT, productId), VIEWED);
            }
            return this;
        }

        public GraphBuilder similar(String p1, String p2, float weight) {
            graph.addEdge(new NodeId(PRODUCT, p1), new NodeId(PRODUCT, p2), SIMILAR,
                          weight);
            return this;
        }
    }
}
