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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jon.ivmark.graphit.recommendation.Item;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryItemRepository implements ItemRepository {

    private final Map<String, Item> items;

    public InMemoryItemRepository(List<Item> items) {
        this.items = new HashMap<String, Item>(items.size());
        for (Item item : items) {
            this.items.put(item.getItemId(), item);
        }
    }

    @Override
    public Item get(String itemId) {
        return items.get(itemId);
    }

    public int size() {
        return items.size();
    }

    public static InMemoryItemRepository fromJson(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Item> items = objectMapper.readValue(file, new TypeReference<List<Item>>() {});
            return new InMemoryItemRepository(items);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file", e);
        }
    }
}
