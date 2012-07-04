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

package org.graphit.properties.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.graphit.properties.domain.HashMapPropertiesFactory;
import org.graphit.properties.domain.Properties;
import org.graphit.properties.domain.PropertiesFactory;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class ConcurrentHashMapPropertiesRepositoryTest {

    @Test
    public void testSetGetPropertiesFactory() {
        ConcurrentHashMapPropertiesRepository<String> repo =
            new ConcurrentHashMapPropertiesRepository<String>(10);
        assertThat(repo.getPropertiesFactory(), Matchers.instanceOf(HashMapPropertiesFactory.class));

        PropertiesFactory pf = new PropertiesFactory() {

            @Override
            public Properties createEmptyProperties() {
                return null;
            }
        };
        repo.setPropertiesFactory(pf);
        assertEquals(pf, repo.getPropertiesFactory());
    }

    @Test
    public void testSaveGetRemoveProperties() {
        ConcurrentHashMapPropertiesRepository<String> repo =
            new ConcurrentHashMapPropertiesRepository<String>(10);

        Properties props = repo.getProperties("A");
        assertTrue(props.isEmpty());
        props.setProperty("B", 1);

        repo.saveProperties("A", props);
        props = repo.getProperties("A");
        assertEquals(1, props.size());
        assertEquals(1, props.getProperty("B"));

        props = repo.removeProperties("A");
        assertEquals(1, props.size());
        assertEquals(1, props.getProperty("B"));

        props = repo.getProperties("A");
        assertTrue(props.isEmpty());

        props = repo.removeProperties("A");
        assertTrue(props.isEmpty());
    }

    @Test
    public void testSetAndRemoveProperty() {
        ConcurrentHashMapPropertiesRepository<String> repo =
            new ConcurrentHashMapPropertiesRepository<String>(10);

        Properties props = repo.getProperties("A");
        props.setProperty("B", 1);
        repo.saveProperties("A", props);

        repo.setProperty("A", "B", 2);
        repo.setProperty("A", "C", 3);

        props = repo.removeProperties("A");
        assertEquals(2, props.size());
        assertEquals(2, props.getProperty("B"));
        assertEquals(3, props.getProperty("C"));

        repo.setProperty("A", "B", 4);
        props = repo.removeProperties("A");
        assertEquals(1, props.size());
        assertEquals(4, props.getProperty("B"));

    }
}
