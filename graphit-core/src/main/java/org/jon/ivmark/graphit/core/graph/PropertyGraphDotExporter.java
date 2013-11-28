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

package org.jon.ivmark.graphit.core.graph;

import org.codehaus.jackson.map.ObjectMapper;
import org.jon.ivmark.graphit.core.graph.edge.Edge;
import org.jon.ivmark.graphit.core.graph.edge.EdgeId;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.edge.EdgeTypes;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.graph.node.NodeTypes;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exports a graph in Graphviz DOT language format. Please note that no edge
 * properties are included.
 *
 * @author jon
 */
public final class PropertyGraphDotExporter {

    private static final List<String> SHAPES = Arrays.asList(
            "box",
            "ellipse",
            "hexagon",
            "circle",
            "egg",
            "triangle",
            "diamond",
            "trapezium",
            "parallelogram",
            "house",
            "pentagon",
            "hexagon",
            "septagon",
            "octagon",
            "doublecircle",
            "doubleoctagon",
            "tripleoctagon",
            "invtriangle",
            "invtrapezium",
            "invhouse",
            "Mdiamond",
            "Msquare",
            "Mcircle",
            "rectangle",
            "square",
            "note",
            "tab",
            "folder",
            "box3d",
            "component"
    );

    // Maximum contrast colors according to Kenneth Kelly:
    // http://eleanormaclure.files.wordpress.com/2011/03/colour-coding.pdf
    private static final List<String> COLORS = Arrays.asList(
            "#00538A",
            "#FF8E00",
            "#C10020",
            "#232C16",
            "#007D34",
            "#53377A",
            "#FF6800",
            "#B32851",
            "#593315",
            "#93AA00",
            "#F13A13",
            "#7F180D",
            "#F6768E",
            "#CEA262",
            "#F4C800",
            "#817066",
            "#803E75",
            "#FF7A5C",
            "#FFB300",
            "#A6BDD7"
    );
    private final PropertyGraph graph;
    private final DecimalFormat df;
    private final Map<NodeType, String> nodeShapes;
    private final Map<EdgeType, String> edgeColors;

    private final ObjectMapper mapper;

    /**
     * Constructs a new exporter for the provided graph.
     */
    public PropertyGraphDotExporter(PropertyGraph graph) {
        this.graph = graph;
        this.df = new DecimalFormat("#.###");
        this.mapper = new ObjectMapper();

        NodeTypes nodeTypes = graph.getMetadata().getNodeTypes();
        int numNodeTypes = nodeTypes.size();
        this.nodeShapes = new HashMap<NodeType, String>(numNodeTypes);
        int ni = 0;
        for (NodeType nodeType : nodeTypes.elements()) {
            int i = ni++ % SHAPES.size();
            nodeShapes.put(nodeType, SHAPES.get(i));
        }

        EdgeTypes edgeTypes = graph.getMetadata().getEdgeTypes();
        int numEdgeNodeTypes = edgeTypes.size();
        this.edgeColors = new HashMap<EdgeType, String>(numEdgeNodeTypes);
        int ei = 0;
        for (EdgeType edgeType : edgeTypes.elements()) {
            int i = ei++ % COLORS.size();
            edgeColors.put(edgeType, COLORS.get(i));
        }
    }

    /**
     * Exports the graph to the provided file.
     */
    public void export(File out) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),
                    Charset.forName("UTF-8")));
            writer.write("digraph \"");
            writer.write(graph.getMetadata().getGraphName());
            writer.write("\" {");
            writer.newLine();
            exportEdges(writer);
            exportNodes(writer);
            writer.write("}");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void exportEdges(BufferedWriter writer) throws IOException {
        for (Edge edge : graph.getEdges()) {
            writeEdge(edge, writer);
        }
    }

    private void writeEdge(Edge edge, BufferedWriter writer) throws IOException {
        EdgeId edgeId = edge.getEdgeId();
        NodeId start = edge.getStartNode().getNodeId();
        NodeId end = edge.getEndNode().getNodeId();
        String label =
                String.format("_type=%s, _weight=%s", edgeId.getEdgeType().name(),
                        df.format(edge.getWeight()));
        String edgeString =
                String
                        .format("%s -> %s [label=\"%s\" color=\"%s\" fontsize=9]",
                                formatNodeId(start), formatNodeId(end),
                                label, edgeColors.get(edge.getType()));
        writer.write(edgeString);
        writer.newLine();

    }

    private void exportNodes(BufferedWriter writer) throws IOException {
        for (Node node : graph.getNodes()) {
            writeNode(node, writer);
        }
    }

    private void writeNode(Node node, BufferedWriter writer) throws IOException {
        String nid = formatNodeId(node.getNodeId());
        Map<String, Object> props = node.asPropertyMap();
        props.put("_type", node.getNodeId().getNodeType().name());
        props.put("_id", node.getNodeId().getId());
        String propsString =
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(props)
                        .replace("\n", "<br align=\"left\"/>");
        String label =
                String.format("%s [shape=\"%s\" label=<%s<br align=\"left\"/>> fontsize=9];",
                        nid,
                        nodeShapes.get(node.getNodeId().getNodeType()),
                        propsString);
        writer.write(label);
        writer.newLine();
    }

    private String formatNodeId(NodeId nodeId) {
        return String.format("\"%s:%s\"", nodeId.getNodeType().name(), nodeId.getId());
    }
}
