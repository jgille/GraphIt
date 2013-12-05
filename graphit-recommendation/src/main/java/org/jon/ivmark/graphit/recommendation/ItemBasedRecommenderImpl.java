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

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.PropertyGraphImpl;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.traversal.Traversable;

import java.util.ArrayList;
import java.util.Collections;

import static org.jon.ivmark.graphit.core.graph.edge.EdgeDirection.OUTGOING;
import static org.jon.ivmark.graphit.recommendation.GraphConstants.*;
import static org.jon.ivmark.graphit.recommendation.RecommendationGraphMetadata.getMetadata;

public class ItemBasedRecommenderImpl implements ItemBasedRecommender {

    private final PropertyGraph similarites;

    public ItemBasedRecommenderImpl(ItemRepository itemRepository,
                                    Iterable<Similarity> othersAlsoBought,
                                    Iterable<Similarity> othersAlsoViewed,
                                    Iterable<Similarity> othersAlsoLiked) {
        this(createGraph(itemRepository, othersAlsoBought, othersAlsoViewed, othersAlsoLiked));
    }

    ItemBasedRecommenderImpl(PropertyGraph similarites) {
        this.similarites = similarites;
    }

    @Override
    public Recommendation othersAlsoBought(String itemId) {
        Traversable<Node> similar = getSimilarItems(itemId, OTHERS_ALSO_BOUGHT);
        return new RecommendationImpl(similar);
    }

    @Override
    public Recommendation othersAlsoViewed(String itemId) {
        Traversable<Node> similar = getSimilarItems(itemId, OTHERS_ALSO_VIEWED);
        return new RecommendationImpl(similar);
    }

    @Override
    public Recommendation othersAlsoLiked(String itemId) {
        Traversable<Node> similar = getSimilarItems(itemId, OTHERS_ALSO_LIKED);
        return new RecommendationImpl(similar);
    }

    @Override
    public int numberOfItems() {
        return similarites.numberOfNodes();
    }

    private Traversable<Node> getSimilarItems(String itemId, EdgeType edgeType) {
        return similarites.getNeighbors(ItemId.withId(itemId), edgeType, OUTGOING);
    }

    private static PropertyGraph createGraph(ItemRepository itemRepository,
                                             Iterable<Similarity> othersAlsoBought,
                                             Iterable<Similarity> othersAlsoViewed,
                                             Iterable<Similarity> othersAlsoLiked) {
        Preconditions.checkNotNull(itemRepository);
        Preconditions.checkNotNull(othersAlsoBought);
        Preconditions.checkNotNull(othersAlsoViewed);
        Preconditions.checkNotNull(othersAlsoLiked);

        PropertyGraphImpl graph = new PropertyGraphImpl(getMetadata());
        graph.setNodePropertiesRepo(new ItemNodeRepository(itemRepository));
        addEdges(graph, othersAlsoBought, GraphConstants.OTHERS_ALSO_BOUGHT);
        addEdges(graph, othersAlsoLiked, GraphConstants.OTHERS_ALSO_LIKED);
        addEdges(graph, othersAlsoViewed, GraphConstants.OTHERS_ALSO_VIEWED);

        return graph;
    }

    private static void addEdges(PropertyGraph graph, Iterable<Similarity> similarities, final EdgeType edgeType) {
        for (Similarity similarity : similarities) {
            NodeId source = ItemId.withId(similarity.getSource());
            NodeId similar = ItemId.withId(similarity.getSimilar());
            addNode(graph, source);
            addNode(graph, similar);
            graph.addEdge(source, similar, edgeType, similarity.getSimilarity());
        }
    }

    private static void addNode(PropertyGraph graph, NodeId nodeId) {
        if (graph.getNode(nodeId) == null) {
            graph.addNode(nodeId);
        }
    }

    public static class Builder {
        private ItemRepository items = new InMemoryItemRepository(Collections.<Item>emptyList());
        private Iterable<Similarity> othersAlsoBought = new ArrayList<Similarity>();
        private Iterable<Similarity> othersAlsoViewed = new ArrayList<Similarity>();
        private Iterable<Similarity> othersAlsoLiked = new ArrayList<Similarity>();

        public Builder items(ItemRepository items) {
            this.items = items;
            return this;
        }

        public Builder othersAlsoBought(Iterable<Similarity> othersAlsoBought) {
            this.othersAlsoBought = othersAlsoBought;
            return this;
        }

        public Builder othersAlsoViewed(Iterable<Similarity> othersAlsoViewed) {
            this.othersAlsoViewed = othersAlsoViewed;
            return this;
        }

        public Builder othersAlsoLiked(Iterable<Similarity> othersAlsoLiked) {
            this.othersAlsoLiked = othersAlsoLiked;
            return this;
        }

        public ItemBasedRecommenderImpl build() {
            return new ItemBasedRecommenderImpl(items, othersAlsoBought, othersAlsoViewed, othersAlsoLiked);
        }
    }

}
