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

package org.jon.ivmark.graphit.core.graph.traversal;

import com.google.common.base.Predicates;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.jon.ivmark.graphit.core.graph.node.TestNodeType.PRODUCT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeFilterTest {

    @Mock
    private Node node;

    @Test
    public void assertThatEverythingIsAcceptedIfNoFilters() {
        assertTrue(new NodeFilter().apply(node));
    }

    @Test
    public void testWithNodetypeFilterThatRejects() {
        assertFalse(new NodeFilter().withNodeTypes(PRODUCT).apply(node));
    }

    @Test
    public void testWithNodetypeFilterThatAccepts() {
        when(node.getType()).thenReturn(PRODUCT);
        assertTrue(new NodeFilter().withNodeTypes(PRODUCT).apply(node));
    }

    @Test
    public void testWithNodetypeFilterThatRejectsAndPropFilterThatAccepts() {
        assertFalse(new NodeFilter().withNodeTypes(PRODUCT).
                filterOnProperties(Predicates.<Properties>alwaysTrue()).apply(node));
    }

    @Test
    public void testWithNodetypeFilterThatRejectsAndPropFilterThatRejects() {
        assertFalse(new NodeFilter().withNodeTypes(PRODUCT)
                .filterOnProperties(Predicates.<Properties>alwaysFalse()).apply(node));
    }

    @Test
    public void testWithNodetypeFilterThatAcceptsAndPropFilterThatAccepts() {
        when(node.getType()).thenReturn(PRODUCT);
        assertTrue(new NodeFilter().withNodeTypes(PRODUCT)
                .filterOnProperties(Predicates.<Properties>alwaysTrue()).apply(node));
    }

    @Test
    public void testWithNodetypeFilterThatAcceptsAndPropFilterThatRejects() {
        assertFalse(new NodeFilter().withNodeTypes(PRODUCT)
                .filterOnProperties(Predicates.<Properties>alwaysTrue()).apply(node));
    }
}