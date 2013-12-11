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

package org.jon.ivmark.graphit.recommendation.repository;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.core.properties.repository.PropertiesRepository;
import org.jon.ivmark.graphit.recommendation.GraphConstants;
import org.jon.ivmark.graphit.recommendation.Item;

public class ItemNodeRepository implements PropertiesRepository<NodeId> {

    private final ItemRepository items;

    public ItemNodeRepository(ItemRepository items) {
        this.items = items;
    }

    @Override
    public Properties getProperties(NodeId nodeId) {
        Preconditions.checkArgument(nodeId.getNodeType().equals(GraphConstants.ITEM));
        Item item = items.get(nodeId.getId());
        if (item == null) {
            return null;
        }
        return new HashMapProperties(item.getProperties());
    }

    @Override
    public void saveProperties(NodeId id, Properties properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties removeProperties(NodeId id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(NodeId id, String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeProperty(NodeId id, String key) {
        throw new UnsupportedOperationException();
    }
}
