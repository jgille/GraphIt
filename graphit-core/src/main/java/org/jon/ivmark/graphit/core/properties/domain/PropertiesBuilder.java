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

package org.jon.ivmark.graphit.core.properties.domain;

/**
 * 
 * Convenience methods for building {@link Properties} instances.
 * 
 * @author jon
 * 
 */
public final class PropertiesBuilder {

    private final Properties properties;

    /**
     * Creates a new builder.
     */
    public static PropertiesBuilder start(PropertiesFactory factory) {
        return new PropertiesBuilder(factory.createEmptyProperties());
    }

    /**
     * Creates a new builder.
     */
    public static PropertiesBuilder start() {
        return start(new HashMapPropertiesFactory());
    }

    private PropertiesBuilder(Properties initialProperties) {
        this.properties = initialProperties;
    }

    /**
     * Sets a property.
     */
    public PropertiesBuilder set(String key, Object value) {
        properties.setProperty(key, value);
        return this;
    }

    /**
     * Gets the built properties.
     */
    public Properties build() {
        return properties;
    }
}
