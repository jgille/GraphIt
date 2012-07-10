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

package org.graphit.graph.node.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.graphit.graph.exception.DuplicateKeyException;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class NodeIdRepositoryImplTest {

    private static final NodeType USER = new NodeTypeImpl("user");
    private static final NodeType PRODUCT = new NodeTypeImpl("product");

    private NodeIdRepositoryImpl repo;

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    @Before
    public void setupRepo() {
        this.repo = new NodeIdRepositoryImpl();
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

    private NodeId newUser(String id) {
        return new NodeId(USER, id);
    }

    private NodeId newProduct(String id) {
        return new NodeId(PRODUCT, id);
    }
}
