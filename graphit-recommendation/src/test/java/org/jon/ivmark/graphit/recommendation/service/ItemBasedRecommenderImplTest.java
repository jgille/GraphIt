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

package org.jon.ivmark.graphit.recommendation.service;

import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.traversal.Traversable;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.ItemId;
import org.jon.ivmark.graphit.recommendation.Recommendation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jon.ivmark.graphit.core.graph.edge.EdgeDirection.OUTGOING;
import static org.jon.ivmark.graphit.recommendation.GraphConstants.OTHERS_ALSO_BOUGHT;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemBasedRecommenderImplTest {

    @Mock
    private PropertyGraph similarities;

    private ItemBasedRecommender recommender;

    private final Node node = new Node(1, ItemId.withId("test"), new HashMapProperties());
    private final Traversable<Node> similarNodes = new Traversable<Node>(node);

    @Before
    public void setUp() throws Exception {
        this.recommender = new ItemBasedRecommenderImpl(similarities);
    }

    @Test
    public void testRecommend() {
        NodeId itemId = node.getNodeId();
        when(similarities.getNeighbors(itemId, OTHERS_ALSO_BOUGHT, OUTGOING)).thenReturn(similarNodes);

        Recommendation recommendation = recommender.recommend(itemId.getId(), OTHERS_ALSO_BOUGHT.name());
        checkRecommendation(recommendation);
    }

    private void checkRecommendation(Recommendation recommendation) {
        assertThat(recommendation, notNullValue());
        List<Item> recommendedItems = recommendation.get();
        assertThat(recommendedItems.size(), is(1));
        Item item = recommendedItems.get(0);
        assertThat(item.getItemId(), is(node.getNodeId().getId()));
        assertThat(item.getProperties(), is(node.asPropertyMap()));
    }
}
