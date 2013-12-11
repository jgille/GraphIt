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

import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.recommendation.Item;
import org.jon.ivmark.graphit.recommendation.ItemId;
import org.jon.ivmark.graphit.recommendation.repository.ItemNodeRepository;
import org.jon.ivmark.graphit.recommendation.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemNodeRepositoryTest {

    private ItemNodeRepository repository;

    @Mock
    private ItemRepository items;

    @Before
    public void setUp() {
        this.repository = new ItemNodeRepository(items);
    }

    @Test
    public void testGetMissingProperties() {
        Properties properties = repository.getProperties(ItemId.withId("test"));
        assertThat(properties, nullValue());
    }

    @Test
    public void testGetProperties() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("1", 2);
        Item item = new Item("test", map);
        when(items.get("test")).thenReturn(item);
        Properties properties = repository.getProperties(ItemId.withId("test"));
        assertThat(properties.asPropertyMap(), is(map));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveProperties() {
        repository.saveProperties(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveProperties() {
        repository.removeProperties(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetProperty() {
        repository.setProperty(null, null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveProperty() {
       repository.removeProperty(null, null);
    }
}
