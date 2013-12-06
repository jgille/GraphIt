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

package org.jon.ivmark.graphit.core.graph.edge;

import com.google.common.base.Predicates;
import org.jon.ivmark.graphit.core.graph.edge.Edge;
import org.jon.ivmark.graphit.core.graph.edge.EdgeFilter;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.jon.ivmark.graphit.core.graph.edge.TestEdgeTypes.BOUGHT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EdgeFilterTest {

    @Mock
    private Edge edge;

    @Test
    public void assertThatEverythingIsAcceptedIfNoFilters() {
        assertTrue(new EdgeFilter().apply(edge));
    }

    @Test
    public void testWithNodetypeFilterThatRejects() {
        assertFalse(new EdgeFilter().withEdgeTypes(BOUGHT).apply(edge));
    }

    @Test
    public void testWithNodetypeFilterThatAccepts() {
        when(edge.getType()).thenReturn(BOUGHT);
        assertTrue(new EdgeFilter().withEdgeTypes(BOUGHT).apply(edge));
    }

    @Test
    public void testWithNodetypeFilterThatRejectsAndPropFilterThatAccepts() {
        assertFalse(new EdgeFilter().withEdgeTypes(BOUGHT).
                filterOnProperties(Predicates.<Properties>alwaysTrue()).apply(edge));
    }

    @Test
    public void testWithNodetypeFilterThatRejectsAndPropFilterThatRejects() {
        assertFalse(new EdgeFilter().withEdgeTypes(BOUGHT)
                .filterOnProperties(Predicates.<Properties>alwaysFalse()).apply(edge));
    }

    @Test
    public void testWithNodetypeFilterThatAcceptsAndPropFilterThatAccepts() {
        when(edge.getType()).thenReturn(BOUGHT);
        assertTrue(new EdgeFilter().withEdgeTypes(BOUGHT)
                .filterOnProperties(Predicates.<Properties>alwaysTrue()).apply(edge));
    }

    @Test
    public void testWithNodetypeFilterThatAcceptsAndPropFilterThatRejects() {
        assertFalse(new EdgeFilter().withEdgeTypes(BOUGHT)
                .filterOnProperties(Predicates.<Properties>alwaysTrue()).apply(edge));
    }
}