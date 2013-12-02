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
import org.jon.ivmark.graphit.core.graph.traversal.Traversable;
import org.jon.ivmark.graphit.core.properties.Properties;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RecommendationImpl implements Recommendation {

    private final Traversable<Node> recommendation;

    public RecommendationImpl(Traversable<Node> recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public Recommendation filter(final Predicate<Properties> filter) {
        return new RecommendationImpl(recommendation.filter(new RecommendationPropertiesFilter(filter)));
    }

    @Override
    public Recommendation discard(final Set<String> itemIds) {
        return new RecommendationImpl(recommendation.filter(new DiscardItemsFilter(itemIds)));
    }

    @Override
    public Recommendation limit(int maxNumberOfItems) {
        return new RecommendationImpl(recommendation.head(maxNumberOfItems));
    }

    @Override
    public List<Item> get() {
        return items().asList();
    }

    @Override
    public Iterator<Item> iterator() {
        return items().iterator();
    }

    private Traversable<Item> items() {
        return recommendation.transform(new NodeToItemTransformer());
    }
}
