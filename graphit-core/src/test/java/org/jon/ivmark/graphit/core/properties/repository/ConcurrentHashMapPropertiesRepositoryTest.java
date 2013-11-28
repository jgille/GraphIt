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

package org.jon.ivmark.graphit.core.properties.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jon.ivmark.graphit.core.graph.node.domain.NodeId;
import org.jon.ivmark.graphit.core.graph.node.schema.NodeType;
import org.jon.ivmark.graphit.core.graph.node.schema.NodeTypeImpl;
import org.jon.ivmark.graphit.core.properties.domain.Properties;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class ConcurrentHashMapPropertiesRepositoryTest {

    private static final NodeType NODE_TYPE = new NodeTypeImpl("NT");

    private NodeId createId(String id) {
        return new NodeId(NODE_TYPE, id);
    }

    @Test
    public void testSaveGetRemoveProperties() {
        NodePropertiesRepository repo = new NodePropertiesRepository(10);

        Properties props = repo.getProperties(createId("A"));
        assertTrue(props.isEmpty());
        props.setProperty("B", 1);

        repo.saveProperties(createId("A"), props);
        props = repo.getProperties(createId("A"));
        assertEquals(1, props.size());
        assertEquals(1, props.getProperty("B"));

        props = repo.removeProperties(createId("A"));
        assertEquals(1, props.size());
        assertEquals(1, props.getProperty("B"));

        props = repo.getProperties(createId("A"));
        assertTrue(props.isEmpty());

        props = repo.removeProperties(createId("A"));
        assertTrue(props.isEmpty());
    }

    @Test
    public void testSetAndRemoveProperty() {
        NodePropertiesRepository repo = new NodePropertiesRepository(10);

        Properties props = repo.getProperties(createId("A"));
        props.setProperty("B", 1);
        repo.saveProperties(createId("A"), props);

        repo.setProperty(createId("A"), "B", 2);
        repo.setProperty(createId("A"), "C", 3);

        props = repo.removeProperties(createId("A"));
        assertEquals(2, props.size());
        assertEquals(2, props.getProperty("B"));
        assertEquals(3, props.getProperty("C"));

        repo.setProperty(createId("A"), "B", 4);
        props = repo.removeProperties(createId("A"));
        assertEquals(1, props.size());
        assertEquals(4, props.getProperty("B"));
    }
}
