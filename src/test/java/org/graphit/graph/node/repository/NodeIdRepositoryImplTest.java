package org.graphit.graph.node.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.JsonParseException;
import org.graphit.graph.exception.DuplicateKeyException;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.repository.NodeIdRepositoryImpl;
import org.graphit.graph.node.schema.DynamicNodeTypes;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.ClassPathResource;

public class NodeIdRepositoryImplTest {

    private static final NodeType USER = new NodeTypeImpl("user");
    private static final NodeType PRODUCT = new NodeTypeImpl("product");

    private NodeIdRepositoryImpl repo;

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    @Before
    public void setupRepo() {
        DynamicNodeTypes nodeTypes = new DynamicNodeTypes();
        nodeTypes.add(PRODUCT).add(USER);
        this.repo = new NodeIdRepositoryImpl(nodeTypes);
    }

    @Test
    public void testInsertAndGetNodeIndex() {
        NodeId user = newUser("u1");
        int u = repo.insert(user);
        assertEquals(u, repo.getNodeIndex(user));

        NodeId product = newProduct("p1");
        int p = repo.insert(product);
        assertEquals(p, repo.getNodeIndex(product));
    }

    @Test
    public void testInsertAndGetNodeId() {
        NodeId user = newUser("u1");
        int u = repo.insert(user);
        assertEquals(user, repo.getNodeId(u));

        NodeId product = newProduct("p1");
        int p = repo.insert(product);
        assertEquals(product, repo.getNodeId(p));
    }

    @Test
    public void testInsertIndexed() {
        NodeId user = newUser("u1");
        repo.insert(997, user);
        assertEquals(user, repo.getNodeId(997));
    }

    @Test
    public void testInsertDuplicateKey() {
        NodeId u1 = newUser("u1");
        NodeId u2 = newUser("u2");
        repo.insert(0, u1);
        boolean exception = false;
        try {
            repo.insert(0, u2);
        } catch (DuplicateKeyException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testInsertNegtiveIndexed() {
        NodeId user = newUser("u1");
        boolean exception = false;
        try {
            repo.insert(-1, user);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testGetNonExistingNodeId() {
        int index = repo.getNodeIndex(newUser("u1"));
        assertEquals(index, -1);
    }

    @Test
    public void testGetNonExistingNodeIndex() {
        assertNull(repo.getNodeId(0));
    }

    @Test
    public void testGetNodeIdIllegal() {
        boolean exception = false;
        try {
            repo.getNodeId(-1);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testRemoveExistingNode() {
        NodeId user = newUser("u1");
        int u = repo.insert(user);
        assertEquals(u, repo.getNodeIndex(user));
        assertEquals(user, repo.remove(u));
        assertNull(repo.getNodeId(u));
    }

    @Test
    public void testRemoveNonExistingNode() {
        assertNull(repo.remove(0));
    }

    @Test
    public void testRemoveNodeIdIllegal() {
        boolean exception = false;
        try {
            repo.remove(-1);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testDump() throws IOException {
        NodeId u1 = newUser("u1");
        NodeId u2 = newUser("u2");
        NodeId p1 = newProduct("p1");
        NodeId p2 = newProduct("p2");
        NodeId p3 = newProduct("p3");

        repo.insert(u1);
        repo.insert(u2);
        repo.insert(p1);
        repo.insert(p2);
        repo.insert(p3);

        File file = out.newFile("testDump");
        repo.dump(file);

        String json = FileUtils.readFileToString(file);

        String expected =
            FileUtils
                .readFileToString(new ClassPathResource("/fixtures/nodes/primitives/expected.json")
                                           .getFile())
                .trim();

        assertEquals(expected, json);
    }

    @Test
    public void testRestore() throws IOException {
        File file = new ClassPathResource("/fixtures/nodes/primitives/expected.json").getFile();
        repo.restore(file);

        NodeId u1 = newUser("u1");
        NodeId u2 = newUser("u2");
        NodeId p1 = newProduct("p1");
        NodeId p2 = newProduct("p2");
        NodeId p3 = newProduct("p3");

        List<NodeId> nodes = repo.getNodeIds();
        List<NodeId> exptected = Arrays.asList(u1, u2, p1, p2, p3);
        assertEquals(exptected, nodes);
        int i = 0;
        for (NodeId node : nodes) {
            assertEquals(i++, repo.getNodeIndex(node));
        }
    }

    @Test
    public void testRestoreBroken() throws IOException {
        File file = new ClassPathResource("/fixtures/nodes/primitives/broken_nodes.json").getFile();
        boolean exception = false;
        try {
            repo.restore(file);
        } catch (JsonParseException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testInitWithoutData() {
        assertFalse(repo.isInited());
        repo.init();
        assertTrue(repo.isInited());
        List<NodeId> nodes = repo.getNodeIds();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testInitWithData() throws IOException {
        repo.setRootDataDirectory(new ClassPathResource("/fixtures/").getFile().getAbsolutePath());
        assertFalse(repo.isInited());
        repo.init();
        assertTrue(repo.isInited());

        NodeId u1 = newUser("u1");
        NodeId u2 = newUser("u2");
        NodeId p1 = newProduct("p1");
        NodeId p2 = newProduct("p2");
        NodeId p3 = newProduct("p3");

        List<NodeId> nodes = repo.getNodeIds();
        List<NodeId> exptected = Arrays.asList(u1, u2, p1, p2, p3);
        assertEquals(exptected, nodes);
        int i = 0;
        for (NodeId node : nodes) {
            assertEquals(i++, repo.getNodeIndex(node));
        }
    }

    @Test
    public void testShutdownWithoutDataDir() {
        NodeId u1 = newUser("u1");
        NodeId u2 = newUser("u2");
        NodeId p1 = newProduct("p1");
        NodeId p2 = newProduct("p2");
        NodeId p3 = newProduct("p3");

        repo.insert(u1);
        repo.insert(u2);
        repo.insert(p1);
        repo.insert(p2);
        repo.insert(p3);

        assertFalse(repo.isShutdown());
        repo.shutdown();
        assertTrue(repo.isShutdown());
    }

    @Test
    public void testShutdownWithDataDir() throws IOException {
        repo.setRootDataDirectory(out.getRoot().getAbsolutePath());

        NodeId u1 = newUser("u1");
        NodeId u2 = newUser("u2");
        NodeId p1 = newProduct("p1");
        NodeId p2 = newProduct("p2");
        NodeId p3 = newProduct("p3");

        repo.insert(u1);
        repo.insert(u2);
        repo.insert(p1);
        repo.insert(p2);
        repo.insert(p3);

        assertFalse(repo.isShutdown());
        repo.shutdown();
        assertTrue(repo.isShutdown());

        String outFile =
            FilenameUtils.concat(out.getRoot().getAbsolutePath(), "nodes/primitives/nodes.json");
        String json = FileUtils.readFileToString(new File(outFile));

        String expected =
            FileUtils
                .readFileToString(new ClassPathResource("/fixtures/nodes/primitives/expected.json")
                    .getFile())
                .trim();

        assertEquals(expected, json);

    }

    private NodeId newUser(String id) {
        return new NodeId(USER, id);
    }

    private NodeId newProduct(String id) {
        return new NodeId(PRODUCT, id);
    }
}
