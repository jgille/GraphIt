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

import org.apache.commons.io.FileUtils;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.io.util.ResourceUtils;
import org.graphit.properties.domain.PropertiesBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author jon
 * 
 */
public class PropertyGraphImplJsonTest {

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    private PropertyGraphImpl graph;

    @Before
    public void setupGraph() {
        this.graph = new PropertyGraphImpl("test");

        NodeType user = graph.createNodeType("user");
        NodeType item = graph.createNodeType("item");

        EdgeType bought = graph.createEdgeType("bought");
        EdgeType recommended = graph.createEdgeType("recommended", EdgeSortOrder.DESCENDING_WEIGHT);

        NodeId u1 = graph.addNode(new NodeId(user, "u1")).getNodeId();
        graph
            .setNodeProperties(u1, PropertiesBuilder.start().set("name", "U1").set("age", 1).build());

        NodeId u2 = graph.addNode(new NodeId(user, "u2")).getNodeId();
        graph
            .setNodeProperties(u2, PropertiesBuilder.start().set("name", "U2").set("age", 2).build());

        NodeId i1 = graph.addNode(new NodeId(item, "i1")).getNodeId();
        graph.setNodeProperties(i1, PropertiesBuilder.start()
            .set("title", "i1").set("price", 100f).build());

        NodeId i2 = graph.addNode(new NodeId(item, "i2")).getNodeId();
        graph.setNodeProperties(i2, PropertiesBuilder.start()
            .set("title", "i2").set("price", 200f).build());

        NodeId i3 = graph.addNode(new NodeId(item, "i3")).getNodeId();
        graph.setNodeProperties(i3, PropertiesBuilder.start()
            .set("title", "i3").set("price", 300f).build());

        EdgeId b1 = graph.addEdge(u1, i1, bought).getEdgeId();
        graph.setEdgeProperties(b1, PropertiesBuilder.start().set("aliases", Arrays.asList("b1"))
            .build());

        EdgeId b2 = graph.addEdge(u1, i2, bought).getEdgeId();
        graph.setEdgeProperties(b2, PropertiesBuilder.start().set("aliases", Arrays.asList("b2"))
            .build());

        EdgeId b3 = graph.addEdge(u2, i2, bought).getEdgeId();
        graph.setEdgeProperties(b3, PropertiesBuilder.start().set("aliases", Arrays.asList("b3"))
            .build());

        EdgeId b4 = graph.addEdge(u2, i3, bought).getEdgeId();
        graph.setEdgeProperties(b4, PropertiesBuilder.start().set("aliases", Arrays.asList("b4"))
            .build());

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

    @Test
    public void testImportExportJsonNoProperties() throws IOException {
        File file = out.newFile();
        PropertyGraph importedGraph = new PropertyGraphImpl("test");
        File in = getResourceFile("exportedGraphNoProperties.json");
        importedGraph.importJson(in);

        importedGraph.exportJson(file, false, false);

        String fileContent = FileUtils.readFileToString(file);
        String expected = getExpected("exportedGraphNoProperties.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testImportExportJson() throws IOException {
        File file = out.newFile();
        PropertyGraph importedGraph = new PropertyGraphImpl("test");
        File in = getResourceFile("exportedGraph.json");
        importedGraph.importJson(in);

        importedGraph.exportJson(file, true, true);

        String fileContent = FileUtils.readFileToString(file);
        String expected = getExpected("exportedGraph.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testShutdownNoDataDir() throws IOException {
        graph.shutdown(); // Just make sure no exception is thrown
    }

    @Test
    public void testShutdownWithDataDir() throws IOException {
        File dir = out.newFolder();
        graph.setDataDir(dir.getAbsolutePath());
        graph.shutdown();

        String fileContent = FileUtils.readFileToString(new File(dir, "g-test.json"));
        String expected = getExpected("exportedGraph.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testShutdownEmptyGraphWithDataDir() throws IOException {
        PropertyGraphImpl emptyGraph = new PropertyGraphImpl("test");
        File dir = out.newFolder();
        emptyGraph.setDataDir(dir.getAbsolutePath());
        emptyGraph.shutdown();

        String fileContent = FileUtils.readFileToString(new File(dir, "g-test.json"));
        String expected = getExpected("emptyGraph.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testShutdownWithDataDirNoProperties() throws IOException {
        File dir = out.newFolder();
        graph.setDataDir(dir.getAbsolutePath());
        graph.setShouldPersistNodeProperties(false);
        graph.setShouldPersistEdgeProperties(false);
        graph.shutdown();

        String fileContent = FileUtils.readFileToString(new File(dir, "g-test.json"));
        String expected = getExpected("exportedGraphNoProperties.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testInitNoDataDir() {
        PropertyGraphImpl emptyGraph = new PropertyGraphImpl("test");
        emptyGraph.init();
        assertTrue(emptyGraph.getNodes().asList().isEmpty());
        assertTrue(emptyGraph.getEdges().asList().isEmpty());
    }

    @Test
    public void testInitNoDataFile() throws IOException {
        PropertyGraphImpl emptyGraph = new PropertyGraphImpl("test");
        File dir = out.newFolder();
        emptyGraph.setDataDir(dir.getAbsolutePath());
        emptyGraph.init();
        assertTrue(emptyGraph.getNodes().asList().isEmpty());
        assertTrue(emptyGraph.getEdges().asList().isEmpty());
    }

    @Test
    public void testInitAndExport() throws IOException {
        PropertyGraphImpl g = new PropertyGraphImpl("test");
        File dir = out.newFolder();

        File resourceFile = getResourceFile("exportedGraph.json");
        File graphFile = new File(dir, "g-test.json");
        FileUtils.copyFile(resourceFile, graphFile);

        g.setDataDir(dir.getAbsolutePath());
        g.init();

        // Should have been renamed now
        assertFalse(graphFile.exists());

        File versionsDir = new File(dir, "versions");
        assertTrue(versionsDir.exists());
        String[] files = versionsDir.list();
        assertEquals(1, files.length);
        String vFileName = files[0];
        assertTrue(vFileName.matches("g-test.\\d*.json"));
        String expected = getExpected("exportedGraph.json");

        assertEquals(expected, FileUtils.readFileToString(new File(versionsDir, vFileName)));

        File file = out.newFile();
        g.exportJson(file, true, true);

        String fileContent = FileUtils.readFileToString(file);
        assertEquals(expected, fileContent);
    }

    @Test
    public void testInitAndExportWitoutProperties() throws IOException {
        PropertyGraphImpl g = new PropertyGraphImpl("test");
        File dir = out.newFolder();

        File resourceFile = getResourceFile("exportedGraphNoProperties.json");
        File graphFile = new File(dir, "g-test.json");
        FileUtils.copyFile(resourceFile, graphFile);

        g.setDataDir(dir.getAbsolutePath());
        g.init();

        File file = out.newFile();
        g.exportJson(file, true, true);

        String fileContent = FileUtils.readFileToString(file);
        String expected = getExpected("exportedGraphNoProperties.json");
        assertEquals(expected, fileContent);
    }

    @Test
    public void testInitNonEmptyGraph() {
        boolean exception = false;
        try {
            graph.init();
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    private String getExpected(String fileName) throws IOException {
        return FileUtils.readFileToString(getResourceFile(fileName));
    }

    private File getResourceFile(String fileName) {
        String path = String.format("fixtures/%s", fileName);
        return ResourceUtils.resourceFile(path);
    }

}
