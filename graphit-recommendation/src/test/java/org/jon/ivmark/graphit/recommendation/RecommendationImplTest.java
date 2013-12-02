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
import com.google.common.base.Predicates;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.graph.traversal.Traversable;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationImplTest {

    private static final NodeType ITEM = new NodeType("item");

    @Mock
    private Traversable<Node> traversable;

    @Mock
    private Traversable<Item> itemTraversable;

    @Before
    public void setUp() {
        when(traversable.transform(any(NodeToItemTransformer.class))).thenReturn(itemTraversable);
    }

    @Test
    public void testGet() {
        Map<String, Object> properties = new HashMap<String, Object>();
        List<Item> result = Arrays.asList(new Item("1", properties));

        when(itemTraversable.asList()).thenReturn(result);

        Recommendation recommendation = new RecommendationImpl(traversable);
        List<Item> items = recommendation.get();
        assertThat(items, is(result));
    }

    @Test
    public void testFilter() {
        when(traversable.filter(any(RecommendationPropertiesFilter.class))).thenReturn(traversable);

        Recommendation recommendation = new RecommendationImpl(traversable);
        Predicate<Properties> filter = Predicates.alwaysTrue();
        recommendation.filter(filter);

        verify(traversable).filter(any(RecommendationPropertiesFilter.class));
    }

    @Test
    public void testDiscard() {
        when(traversable.filter(any(DiscardItemsFilter.class))).thenReturn(traversable);

        Recommendation recommendation = new RecommendationImpl(traversable);
        recommendation.discard(new HashSet<String>());

        verify(traversable).filter(any(DiscardItemsFilter.class));
    }

    @Test
    public void testLimit() {
        when(traversable.head(5)).thenReturn(traversable);

        Recommendation recommendation = new RecommendationImpl(traversable);
        recommendation.limit(5);

        verify(traversable).head(5);
    }

    @Test
    public void testIterator() {
        Map<String, Object> properties = new HashMap<String, Object>();
        List<Item> result = Arrays.asList(new Item("1", properties));
        Iterator<Item> iterator = result.iterator();

        when(itemTraversable.iterator()).thenReturn(iterator);

        Recommendation recommendation = new RecommendationImpl(traversable);
        Iterator<Item> itemIterator = recommendation.iterator();
        assertThat(itemIterator, is(iterator));
    }
}
