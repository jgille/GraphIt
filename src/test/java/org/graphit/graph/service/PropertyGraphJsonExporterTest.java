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

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.properties.domain.PropertiesBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author jon
 *
 */
public class PropertyGraphJsonExporterTest {

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    private PropertyGraph graph;

    @Before
    public void setupGraph() {
        this.graph = new PropertyGraphImpl("testExport");

        NodeType user = graph.createNodeType("user");
        NodeType item = graph.createNodeType("item");

        EdgeType bought = graph.createEdgeType("bought");
        EdgeType recommended = graph.createEdgeType("recommended", EdgeSortOrder.DESCENDING_WEIGHT);

        NodeId u1 = graph.addNode(new NodeId(user, "u1")).getNodeId();
        graph
            .setNodeProperties(u1, PropertiesBuilder.start().set("name", "U1").set("age", 1).get());

        NodeId u2 = graph.addNode(new NodeId(user, "u2")).getNodeId();
        graph
            .setNodeProperties(u2, PropertiesBuilder.start().set("name", "U2").set("age", 2).get());

        NodeId i1 = graph.addNode(new NodeId(item, "i1")).getNodeId();
        graph.setNodeProperties(i1, PropertiesBuilder.start()
            .set("title", "i1").set("price", 100f).get());

        NodeId i2 = graph.addNode(new NodeId(item, "i2")).getNodeId();
        graph.setNodeProperties(i2, PropertiesBuilder.start()
            .set("title", "i2").set("price", 200f).get());

        NodeId i3 = graph.addNode(new NodeId(item, "i3")).getNodeId();
        graph.setNodeProperties(i3, PropertiesBuilder.start()
            .set("title", "i3").set("price", 300f).get());

        EdgeId b1 = graph.addEdge(u1, i1, bought).getEdgeId();
        graph.setEdgeProperties(b1, PropertiesBuilder.start().set("aliases", Arrays.asList("b1"))
            .get());

        EdgeId b2 = graph.addEdge(u1, i2, bought).getEdgeId();
        graph.setEdgeProperties(b2, PropertiesBuilder.start().set("aliases", Arrays.asList("b2"))
            .get());

        EdgeId b3 = graph.addEdge(u2, i2, bought).getEdgeId();
        graph.setEdgeProperties(b3, PropertiesBuilder.start().set("aliases", Arrays.asList("b3"))
            .get());

        EdgeId b4 = graph.addEdge(u2, i3, bought).getEdgeId();
        graph.setEdgeProperties(b4, PropertiesBuilder.start().set("aliases", Arrays.asList("b4"))
            .get());


        graph.addEdge(i1, i2, recommended, 15).getEdgeId();
        graph.addEdge(i1, i3, recommended, 25).getEdgeId();
        graph.addEdge(i2, i3, recommended, 20).getEdgeId();
    }

    @Test
    public void testExportJsonNoProperties() throws IOException {
        File file = out.newFile();
        graph.exportJson(file, false, false);

        String fileContent = FileUtils.readFileToString(file);
        String expected = getExpected("exportedGraphNoProperties.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testExportJson() throws IOException {
        File file = out.newFile();
        graph.exportJson(file, true, true);

        String fileContent = FileUtils.readFileToString(file);
        String expected = getExpected("exportedGraph.json");
        assertEquals(expected, fileContent);
    }

    private String getExpected(String fileName) throws IOException {
        String path = String.format("/fixtures/%s", fileName);
        Resource resource = new ClassPathResource(path);
        File file = resource.getFile();
        return FileUtils.readFileToString(file);
    }
}
