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

package org.jon.ivmark.graphit.recommendation;

import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NodeToItemTransformerTest {

    @Test
    public void testTransform() {
        Node node = new Node(1, new NodeId(new NodeType("test"), "1"), new HashMapProperties());
        Item item = new NodeToItemTransformer().apply(node);
        assertNotNull(item);
        assertEquals("1", item.getItemId());
        assertEquals(new HashMap<String, Object>(), item.getProperties());
    }
}
