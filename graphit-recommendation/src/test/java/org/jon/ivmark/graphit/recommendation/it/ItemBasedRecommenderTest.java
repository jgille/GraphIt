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

package org.jon.ivmark.graphit.recommendation.it;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.PropertyGraphImpl;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.node.repository.NodePropertiesRepository;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.recommendation.*;
import org.jon.ivmark.graphit.test.categories.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jon.ivmark.graphit.core.graph.traversal.PropertiesFilterBuilder.where;
import static org.jon.ivmark.graphit.recommendation.GraphConstants.OTHERS_ALSO_BOUGHT;
import static org.jon.ivmark.graphit.recommendation.RecommendationGraphMetadata.getMetadata;

@Category(IntegrationTest.class)
public class ItemBasedRecommenderTest {

    private static final NodeId N1 = ItemId.withId("1");
    private static final NodeId N2 = ItemId.withId("2");
    private static final NodeId N3 = ItemId.withId("3");
    private static final NodeId N4 = ItemId.withId("4");
    private static final NodeId N5 = ItemId.withId("5");
    private static final NodeId N6 = ItemId.withId("6");

    private ItemBasedRecommender recommender;

    @Before
    public void setUp() {
        PropertyGraphImpl graph = new PropertyGraphImpl(getMetadata());
        graph.setNodePropertiesRepo(new NodePropertiesRepository(10));

        addNodes(graph, Arrays.asList(N1, N2, N3, N4, N5, N6));

        graph.setNodeProperties(N1, buildProperties(
                "Name", "1",
                "Price", 10,
                "OnSale", false,
                "Categories", Arrays.asList("Action", "11+")));
        graph.setNodeProperties(N2, buildProperties(
                "Name", "2",
                "Price", 20,
                "OnSale", true,
                "Categories", Arrays.asList("Action", "11+")));
        graph.setNodeProperties(N3, buildProperties(
                "Name", "3",
                "Price", 30,
                "OnSale", false,
                "Categories", Arrays.asList("Action", "11+")));
        graph.setNodeProperties(N4, buildProperties(
                "Name", "4",
                "Price", 40,
                "OnSale", true,
                "Categories", Arrays.asList("Action", "11+")));
        graph.setNodeProperties(N5, buildProperties(
                "Name", "5",
                "Price", 50,
                "OnSale", false,
                "Categories", Arrays.asList("Action", "11+")));
        graph.setNodeProperties(N6, buildProperties(
                "Name", "6",
                "Price", 60,
                "OnSale", true,
                "Categories", Arrays.asList("Action", "11+")));

        graph.addEdge(N1, N2, OTHERS_ALSO_BOUGHT, 1f);
        graph.addEdge(N1, N3, OTHERS_ALSO_BOUGHT, 2f);
        graph.addEdge(N1, N4, OTHERS_ALSO_BOUGHT, 0.5f);
        graph.addEdge(N4, N5, OTHERS_ALSO_BOUGHT, 0.5f);
        graph.addEdge(N4, N6, OTHERS_ALSO_BOUGHT, 1.5f);

        this.recommender = new ItemBasedRecommenderImpl(graph);
    }

    private void addNodes(PropertyGraph graph, List<NodeId> nodeIds) {
        for (NodeId nodeId : nodeIds) {
            graph.addNode(nodeId);
        }
    }

    private Properties buildProperties(Object... keyValues) {
        Preconditions.checkArgument(keyValues.length % 2 == 0);
        Properties properties = new HashMapProperties();
        for (int i = 0; i <= keyValues.length - 1; i += 2) {
            String key = (String) keyValues[i];
            Object value = keyValues[i + 1];
            properties.setProperty(key, value);
        }
        return properties;
    }

    @Test
    public void testSimpleRecommendation() {
        Recommendation recommendation = recommender.othersAlsoBought(N1.getId());
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(3));
        assertThat(items.get(0).getItemId(), is(N3.getId()));
        assertThat(items.get(1).getItemId(), is(N2.getId()));
        assertThat(items.get(2).getItemId(), is(N4.getId()));
    }

    @Test
    public void testLimitedRecommendation() {
        Recommendation recommendation = recommender.othersAlsoBought(N1.getId()).limit(2);
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
        assertThat(items.get(0).getItemId(), is(N3.getId()));
        assertThat(items.get(1).getItemId(), is(N2.getId()));
    }

    @Test
    public void testFilteredRecommendation() {
        Recommendation recommendation = recommender.othersAlsoBought(N1.getId())
                .filter(where("Price").greaterThan(20)
                        .and("OnSale").equalTo(true)
                        .build());
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getItemId(), is(N4.getId()));
    }

    @Test
    public void testRecommendationWithDiscardedItems() {
        Set<String> discarded = new HashSet<String>(Arrays.asList(N2.getId(), N3.getId()));
        Recommendation recommendation = recommender.othersAlsoBought(N1.getId()).discard(discarded);
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getItemId(), is(N4.getId()));
    }

    @Test
    public void testCombination() {
        Set<String> discarded = new HashSet<String>(Arrays.asList(N2.getId()));
        Recommendation recommendation = recommender.othersAlsoBought(N1.getId())
                .discard(discarded)
                .filter(where("Price").lessThan(40)
                        .build())
                .limit(1);
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getItemId(), is(N3.getId()));
    }
}
