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

import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.repository.InMemoryItemRepository;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InMemoryItemRepositoryTest {

    @Test
    public void testGet() {
        Map<String, Object> properties = new HashMap<String, Object>();
        List<Item> items = Arrays.asList(new Item("test", properties));
        InMemoryItemRepository repository = new InMemoryItemRepository(items);
        Item item = repository.get("test");
        assertThat(item.getItemId(), is("test"));
        assertThat(item.getProperties(), is(properties));
    }

    @Test
    public void testImportJson() {
        File file = ResourceUtils.resourceFile("fixtures/items.json");
        ItemRepository repository = InMemoryItemRepository.fromJson(file);
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("A", 1);
        Item item = repository.get("test");
        assertThat(item.getItemId(), is("test"));
        assertThat(item.getProperties(), is(properties));
    }
}
