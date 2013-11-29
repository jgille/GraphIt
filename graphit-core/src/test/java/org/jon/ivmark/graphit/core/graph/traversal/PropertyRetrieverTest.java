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

package org.jon.ivmark.graphit.core.graph.traversal;

import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertyRetrieverTest {

    @Mock
    private Properties properties;

    @Test
    public void assertThatWeCanGetProperty() {
        String key = "test";
        String value = "Hi";
        when(properties.getProperty(key)).thenReturn(value);
        String property = new PropertyRetriever<Properties, String>(key).apply(properties);
        assertEquals(value, property);
    }

    @Test
    public void assertThatNullIsReturnedIfMissing() {
        String key = "test";
        String property = new PropertyRetriever<Properties, String>(key).apply(properties);
        assertNull(property);
    }

    @Test(expected = ClassCastException.class)
    public void testIllegalType() {
        String key = "test";
        String value = "Hi";
        when(properties.getProperty(key)).thenReturn(value);
        @SuppressWarnings("unused")
        Float iAmReallyAString = new PropertyRetriever<Properties, Float>(key).apply(properties);
    }
}
