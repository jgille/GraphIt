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

package org.graphit.graph.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;
import org.graphit.graph.schema.GraphMetadata;
import org.springframework.util.Assert;

/**
 * Exports a graph to file as json.
 *
 * @author jon
 *
 */
public final class PropertyGraphJsonUtils {

    private static final String INDEX = "_index";
    private static final String ID = "_id";
    private static final String TYPE = "_type";
    private static final String START = "_start";
    private static final String END = "_end";
    private static final String WEIGHT = "_weight";
    private static final String METADATA = "metadata";
    private static final String NAME = "name";
    private static final String NODE_TYPES = "nodetypes";
    private static final String EDGE_TYPES = "edgetypes";
    private static final String NODES = "nodes";
    private static final String EDGES = "edges";
    private static final String SORT_ORDER = "sortorder";

    private PropertyGraphJsonUtils() {

    }

    /**
     * Imports a graph from a file containg json.
     */
    public static void importJson(PropertyGraph graph, File in) throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser jsonParser = jsonFactory.createJsonParser(in);

        JsonToken current = jsonParser.nextToken();
        if (current != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException("Error: json root should be object.");
        }

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            // move from field name to field value
            current = jsonParser.nextToken();
            if (fieldName.equals(METADATA)) {
                importMetadata(graph, jsonParser);
            } else if (fieldName.equals(NODES)) {
                importNodes(graph, jsonParser, current);
            } else if (fieldName.equals(EDGES)) {
                importEdges(graph, jsonParser, current);
            } else {
                throw new IllegalArgumentException("Unexpected field name: " + fieldName);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void importMetadata(PropertyGraph graph, JsonParser jsonParser)
        throws IOException {
        // TODO: Use a dedicated domain object intead of a map
        Map<String, Object> metadata =
            jsonParser.readValueAs(new TypeReference<Map<String, Object>>() {
            });

        String name = (String) metadata.get(NAME);
        Assert.isTrue(graph.getMetadata().getGraphName().equals(name), "Unexpected graph name: "
            + name);

        List<String> nodeTypes = (List<String>) metadata.get(NODE_TYPES);
        for (String nodeType : nodeTypes) {
            graph.createNodeType(nodeType);
        }
        List<Map<String, String>> edgeTypes =
            (List<Map<String, String>>) metadata.get(EDGE_TYPES);
        for (Map<String, String> edgeType : edgeTypes) {
            graph.createEdgeType(edgeType.get(NAME),
                                 EdgeSortOrder.valueOf(edgeType.get(SORT_ORDER)));
        }
    }

    private static void importNodes(PropertyGraph graph, JsonParser jsonParser, JsonToken current)
        throws IOException {
        NodeTypes nodeTypes = graph.getMetadata().getNodeTypes();
        if (current == JsonToken.START_ARRAY) {
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                // TODO: Use a dedicated domain object intead of a map
                Map<String, Object> nodeData =
                    jsonParser.readValueAs(new TypeReference<Map<String, Object>>() {
                    });
                int index = (Integer) nodeData.remove(INDEX);
                NodeType nodeType = nodeTypes.valueOf((String) nodeData.remove(TYPE));
                NodeId nodeId = new NodeId(nodeType, (String) nodeData.remove(ID));
                Node node = graph.addNode(nodeId, index);
                if (!nodeData.isEmpty()) {
                    for (Map.Entry<String, Object> propertyEntry : nodeData.entrySet()) {
                        node.setProperty(propertyEntry.getKey(), propertyEntry.getValue());
                    }
                    graph.setNodeProperties(nodeId, node);
                }
            }
        }
    }

    private static void importEdges(PropertyGraph graph, JsonParser jsonParser, JsonToken current)
        throws IOException {
        EdgeTypes edgeTypes = graph.getMetadata().getEdgeTypes();
        if (current == JsonToken.START_ARRAY) {
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                // TODO: Use a dedicated domain object intead of a map
                Map<String, Object> edgeData =
                    jsonParser.readValueAs(new TypeReference<Map<String, Object>>() {
                    });

                int index = (Integer) edgeData.remove(INDEX);
                EdgeType edgeType = edgeTypes.valueOf((String) edgeData.remove(TYPE));
                EdgeId edgeId = new EdgeId(edgeType, index);
                int startNodeIndex = (Integer) edgeData.remove(START);
                int endNodeIndex = (Integer) edgeData.remove(END);
                Double weight = (Double) edgeData.remove(WEIGHT);
                Edge edge =
                    graph.addEdge(edgeId, startNodeIndex, endNodeIndex, weight.floatValue());
                if (!edgeData.isEmpty()) {
                    for (Map.Entry<String, Object> propertyEntry : edgeData.entrySet()) {
                        edge.setProperty(propertyEntry.getKey(), propertyEntry.getValue());
                    }
                    graph.setEdgeProperties(edgeId, edge);
                }
            }
        }
    }

    /**
     * Exports a graph to file as json.
     */
    public static void exportJson(PropertyGraph graph, File out, boolean includeNodeProperties,
                                  boolean includeEdgeProperties)
        throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonGenerator generator = jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);

        try {
            generator.writeStartObject();
            writeMetadata(graph, generator);
            writeNodes(graph, generator, includeNodeProperties);
            writeEdges(graph, generator, includeEdgeProperties);

            generator.writeEndObject();
        } finally {
            generator.close();
        }
    }

    private static void writeMetadata(PropertyGraph graph, JsonGenerator generator)
        throws IOException {
        GraphMetadata metadata = graph.getMetadata();
        generator.writeFieldName(METADATA);
        generator.writeStartObject();
        generator.writeStringField(NAME, metadata.getGraphName());

        generator.writeFieldName(NODE_TYPES);
        generator.writeStartArray();
        for (NodeType nodeType : metadata.getNodeTypes().elements()) {
            generator.writeString(nodeType.name());
        }
        generator.writeEndArray();

        generator.writeFieldName(EDGE_TYPES);
        generator.writeStartArray();
        for (EdgeType edgeType : metadata.getEdgeTypes().elements()) {
            // TODO: Use a dedicated domain object intead of a map
            Map<String, String> map = new HashMap<String, String>();
            map.put(NAME, edgeType.name());
            map.put(SORT_ORDER, edgeType.getSortOrder().name());
            generator.writeObject(map);
        }
        generator.writeEndArray();

        generator.writeEndObject();
    }

    private static void writeNodes(PropertyGraph graph, JsonGenerator generator,
                            boolean includeProperties) throws IOException {
        generator.writeFieldName(NODES);
        generator.writeStartArray();
        for (Node node : graph.getNodes()) {
            // TODO: Use a dedicated domain object intead of a map
            Map<String, Object> map =
                includeProperties ? node.asPropertyMap() : new HashMap<String, Object>();
            map.put(INDEX, node.getIndex());
            map.put(TYPE, node.getType().name());
            map.put(ID, node.getNodeId().getId());
                generator.writeObject(map);
        }
        generator.writeEndArray();
    }

    private static void writeEdges(PropertyGraph graph, JsonGenerator generator,
                                   boolean includeProperties) throws IOException {
        generator.writeFieldName(EDGES);
        generator.writeStartArray();
        for (Edge edge : graph.getEdges()) {
            // TODO: Use a dedicated domain object intead of a map
            Map<String, Object> map =
                includeProperties ? edge.asPropertyMap() : new HashMap<String, Object>();
            map.put(INDEX, edge.getIndex());
            map.put(TYPE, edge.getType().name());
            map.put(START, edge.getStartNode().getIndex());
            map.put(END, edge.getEndNode().getIndex());
            map.put(WEIGHT, edge.getWeight());
            generator.writeObject(map);
        }
        generator.writeEndArray();
    }
}
