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
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.recommendation.*;
import org.jon.ivmark.graphit.recommendation.repository.InMemoryItemRepository;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.jon.ivmark.graphit.recommendation.service.ItemBasedRecommender;
import org.jon.ivmark.graphit.recommendation.service.ItemBasedRecommenderImpl;
import org.jon.ivmark.graphit.test.categories.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jon.ivmark.graphit.core.properties.filter.PropertiesFilterBuilder.where;
import static org.jon.ivmark.graphit.recommendation.GraphConstants.OTHERS_ALSO_BOUGHT;
import static org.jon.ivmark.graphit.recommendation.GraphConstants.OTHERS_ALSO_LIKED;
import static org.jon.ivmark.graphit.recommendation.GraphConstants.OTHERS_ALSO_VIEWED;

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
        List<Item> items = new ArrayList<Item>();
        items.add(item(N1, buildProperties(
                "Name", "1",
                "Price", 10,
                "OnSale", false,
                "Categories", Arrays.asList("Action", "11+"))));
        items.add(item(N2, buildProperties(
                "Name", "2",
                "Price", 20,
                "OnSale", true,
                "Categories", Arrays.asList("Action", "11+"))));
        items.add(item(N3, buildProperties(
                "Name", "3",
                "Price", 30,
                "OnSale", false,
                "Categories", Arrays.asList("Action", "11+"))));
        items.add(item(N4, buildProperties(
                "Name", "4",
                "Price", 40,
                "OnSale", true,
                "Categories", Arrays.asList("Action", "11+"))));
        items.add(item(N5, buildProperties(
                "Name", "5",
                "Price", 50,
                "OnSale", false,
                "Categories", Arrays.asList("Action", "11+"))));
        items.add(item(N6, buildProperties(
                "Name", "6",
                "Price", 60,
                "OnSale", true,
                "Categories", Arrays.asList("Action", "11+"))));

        ItemRepository itemRepository = new InMemoryItemRepository(items);

        List<Similarity> othersAlsoBought = new ArrayList<Similarity>();
        List<Similarity> othersAlsoViewed = new ArrayList<Similarity>();
        List<Similarity> othersAlsoLiked = new ArrayList<Similarity>();

        similarity(othersAlsoBought, N1, N2, 1f);
        similarity(othersAlsoBought, N1, N3, 2f);
        similarity(othersAlsoBought, N1, N4, 0.5f);
        similarity(othersAlsoBought, N4, N5, 0.5f);
        similarity(othersAlsoBought, N4, N6, 1.5f);

        similarity(othersAlsoLiked, N1, N2, 1f);
        similarity(othersAlsoViewed, N1, N3, 2f);

        List<Similarities> allSimilarities = Arrays.asList(
                new Similarities(GraphConstants.OTHERS_ALSO_VIEWED.name(), othersAlsoViewed),
                new Similarities(OTHERS_ALSO_BOUGHT.name(), othersAlsoBought),
                new Similarities(GraphConstants.OTHERS_ALSO_LIKED.name(), othersAlsoLiked)
                );

        this.recommender = new ItemBasedRecommenderImpl(itemRepository, allSimilarities);
    }

    private void similarity(List<Similarity> similarities, NodeId source, NodeId similar, float weight) {
        similarities.add(new Similarity(source.getId(), similar.getId(), weight));
    }

    private Item item(NodeId nodeId, Map<String, Object> properties) {
        return new Item(nodeId.getId(), properties);
    }

    private Map<String, Object> buildProperties(Object... keyValues) {
        Preconditions.checkArgument(keyValues.length % 2 == 0);
        Map<String, Object> properties = new HashMap<String, Object>();
        for (int i = 0; i <= keyValues.length - 1; i += 2) {
            String key = (String) keyValues[i];
            Object value = keyValues[i + 1];
            properties.put(key, value);
        }
        return properties;
    }

    @Test
    public void testSimpleRecommendation() {
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_BOUGHT.name());
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(3));
        assertThat(items.get(0).getItemId(), is(N3.getId()));
        assertThat(items.get(1).getItemId(), is(N2.getId()));
        assertThat(items.get(2).getItemId(), is(N4.getId()));
    }

    @Test
    public void testOthersAlsoLiked() {
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_LIKED.name());
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getItemId(), is(N2.getId()));
    }

    @Test
    public void testOthersAlsoViewed() {
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_VIEWED.name());
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getItemId(), is(N3.getId()));
    }

    @Test
    public void testLimitedRecommendation() {
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_BOUGHT.name()).limit(2);
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
        assertThat(items.get(0).getItemId(), is(N3.getId()));
        assertThat(items.get(1).getItemId(), is(N2.getId()));
    }

    @Test
    public void testFilteredRecommendation() {
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_BOUGHT.name())
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
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_BOUGHT.name()).discard(discarded);
        assertThat(recommendation, notNullValue());
        List<Item> items = recommendation.get();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getItemId(), is(N4.getId()));
    }

    @Test
    public void testCombination() {
        Set<String> discarded = new HashSet<String>(Arrays.asList(N2.getId()));
        Recommendation recommendation = recommender.recommend(N1.getId(), OTHERS_ALSO_BOUGHT.name())
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
