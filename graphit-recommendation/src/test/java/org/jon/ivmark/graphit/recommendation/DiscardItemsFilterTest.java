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
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DiscardItemsFilterTest {

    private DiscardItemsFilter filter;

    @Before
    public void setUp() {
        Set<String> items = new HashSet<String>();
        items.add("1");
        items.add("2");
        this.filter = new DiscardItemsFilter(items);
    }

    @Test
    public void testFilterNull() {
        assertThat(filter.apply(null), is(false));
    }

    @Test
    public void testDiscard() {
        Node node = new Node(1, new NodeId(new NodeType("test"), "1"), new HashMapProperties());
        assertThat(filter.apply(node), is(false));
    }

    @Test
    public void testDontDiscard() {
        Node node = new Node(1, new NodeId(new NodeType("test"), "3"), new HashMapProperties());
        assertThat(filter.apply(node), is(true));
    }
}
