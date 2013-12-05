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

package org.jon.ivmark.graphit.recommendation.lastfm;

import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.recommendation.*;
import org.jon.ivmark.graphit.test.categories.LoadTest;
import org.jon.ivmark.graphit.test.categories.SlowTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Category(LoadTest.class)
public class LastFmTests {

    @Test
    public void testImportArtists() {
        File file = ResourceUtils.resourceFile("lastfm/artists.json");
        long t0 = System.currentTimeMillis();
        InMemoryItemRepository repository = InMemoryItemRepository.fromJson(file);
        int size = repository.size();
        assertThat(size, is(17632));
        long t1 = System.currentTimeMillis();
        System.out.println(String.format("Imported artists in %d ms", (t1 - t0)));
    }

    @Test
    public void testImportSimilarities() {
        File file = ResourceUtils.resourceFile("lastfm/mahout/log-likelyhood-similarities.csv");
        long t0 = System.currentTimeMillis();
        List<Similarity> similarities = SimilarityImporter.importSimilarities(file, '\t');
        assertThat(similarities, notNullValue());
        assertThat(similarities.size(), is(1319978));
        long t1 = System.currentTimeMillis();
        System.out.println(String.format("Imported similarities in %d ms", (t1 - t0)));
    }

    @Test
    public void testItemBasedRecommendations() {
        File artists = ResourceUtils.resourceFile("lastfm/artists.json");
        File similarities = ResourceUtils.resourceFile("lastfm/mahout/log-likelyhood-similarities.csv");
        InMemoryItemRepository repository = InMemoryItemRepository.fromJson(artists);
        List<Similarity> othersAlsoLiked = SimilarityImporter.importSimilarities(similarities, '\t');

        long t0 = System.currentTimeMillis();
        ItemBasedRecommenderImpl recommender =
                new ItemBasedRecommenderImpl.Builder().items(repository).othersAlsoLiked(othersAlsoLiked).build();
        long t1 = System.currentTimeMillis();
        int numberOfItems = recommender.numberOfItems();

        System.out.println(String.format("Created recommender with %d items in %d ms", numberOfItems, (t1 - t0)));

        String coldplay = "65";

        long t2 = System.currentTimeMillis();
        List<Item> items = recommender.othersAlsoLiked(coldplay).get();
        long t3 = System.currentTimeMillis();
        assertThat(items, notNullValue());
        assertThat(items.size(), is(4568));
        System.out.println(String.format("Recommended %d items in %d ms", items.size(), (t3 - t2)));

        long t4 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            recommender.othersAlsoLiked(coldplay).limit(10).get();
        }
        long t5 = System.currentTimeMillis();
        System.out.println(String.format("1000 recommendations in %d ms", (t5 - t4)));

    }
}
