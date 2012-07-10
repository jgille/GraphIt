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
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.schema.GraphMetadata;

/**
 * Exports a graph to file as json.
 *
 * @author jon
 *
 */
public class PropertyGraphJsonExporter {

    /**
     * Exports a graph to file as json.
     */
    public void exportJson(PropertyGraph graph, File out, boolean includeProperties)
        throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonGenerator generator = jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);

        try {
            generator.writeStartObject();
            writeMetadata(graph, generator);
            writeNodes(graph, generator, includeProperties);
            writeEdges(graph, generator, includeProperties);

            generator.writeEndObject();
        } finally {
            generator.close();
        }
    }

    private void writeMetadata(PropertyGraph graph, JsonGenerator generator) throws IOException {
        GraphMetadata metadata = graph.getMetadata();
        generator.writeFieldName("metadata");
        generator.writeStartObject();
        generator.writeStringField("name", metadata.getGraphName());

        generator.writeFieldName("nodetypes");
        generator.writeStartArray();
        for (NodeType nodeType : metadata.getNodeTypes().elements()) {
            generator.writeString(nodeType.name());
        }
        generator.writeEndArray();

        generator.writeFieldName("edgeTypes");
        generator.writeStartArray();
        for (EdgeType edgeType : metadata.getEdgeTypes().elements()) {
            // TODO: Use a dedicated domain object intead of a map
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", edgeType.name());
            map.put("sortorder", edgeType.getSortOrder().name());
            generator.writeObject(map);
        }
        generator.writeEndArray();

        generator.writeEndObject();
    }

    private void writeNodes(PropertyGraph graph, JsonGenerator generator,
                            boolean includeProperties) throws IOException {
        generator.writeFieldName("nodes");
        generator.writeStartArray();
        for (Node node : graph.getNodes()) {
            // TODO: Use a dedicated domain object intead of a map
            Map<String, Object> map =
                includeProperties ? node.asPropertyMap() : new HashMap<String, Object>();
                map.put("_index", node.getIndex());
                map.put("_type", node.getType().name());
                map.put("_id", node.getNodeId().getId());
                generator.writeObject(map);
        }
        generator.writeEndArray();
    }

    private void writeEdges(PropertyGraph graph, JsonGenerator generator,
                            boolean includeProperties) throws IOException {
        generator.writeFieldName("edges");
        generator.writeStartArray();
        for (Edge edge : graph.getEdges()) {
            // TODO: Use a dedicated domain object intead of a map
            Map<String, Object> map =
                includeProperties ? edge.asPropertyMap() : new HashMap<String, Object>();
            map.put("_index", edge.getIndex());
            map.put("_type", edge.getType().name());
            map.put("_start", edge.getStartNode().getIndex());
            map.put("_end", edge.getEndNode().getIndex());
            map.put("_weight", edge.getWeight());
            generator.writeObject(map);
        }
        generator.writeEndArray();
    }
}
