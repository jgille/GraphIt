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

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.PropertyGraphImpl;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.traversal.Traversable;
import org.jon.ivmark.graphit.recommendation.*;
import org.jon.ivmark.graphit.recommendation.repository.ItemNodeRepository;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;

import java.util.List;

import static org.jon.ivmark.graphit.core.graph.edge.EdgeDirection.OUTGOING;
import static org.jon.ivmark.graphit.recommendation.RecommendationGraphMetadata.getMetadata;

public class ItemBasedRecommenderImpl implements ItemBasedRecommender {

    private final PropertyGraph similarites;

    public ItemBasedRecommenderImpl(ItemRepository itemRepository,
                                    List<Similarities> similaritiesList) {
        this(createGraph(itemRepository, similaritiesList));
    }

    ItemBasedRecommenderImpl(PropertyGraph similarites) {
        this.similarites = similarites;
    }

    @Override
    public Recommendation recommend(String itemId, String similarityType) {
        Traversable<Node> similar = getSimilarItems(itemId, new EdgeType(similarityType));
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
                                             List<Similarities> similaritiesList) {
        Preconditions.checkNotNull(itemRepository);
        Preconditions.checkNotNull(similaritiesList);

        PropertyGraphImpl graph = new PropertyGraphImpl(getMetadata());
        graph.setNodePropertiesRepo(new ItemNodeRepository(itemRepository));
        for (Similarities similarities : similaritiesList) {
            addEdges(graph, similarities.getSimilarities(), new EdgeType(similarities.getSimilarityType()));
        }

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

}
