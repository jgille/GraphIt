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

import com.google.common.base.Predicate;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationPropertiesFilterTest {

    @Mock
    private Predicate<Properties> filter;

    @Test
    public void testApply() {
        Properties properties = new HashMapProperties();
        Node node = new Node(1, new NodeId(new NodeType("test"), "1"), properties);
        when(filter.apply(node)).thenReturn(true);
        assertTrue(new RecommendationPropertiesFilter(filter).apply(node));
        verify(filter).apply(node);
    }

}
