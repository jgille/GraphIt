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

import org.graphit.properties.domain.MapProperties;
import org.graphit.properties.domain.Properties;

/**
 * A {@link PropertiesRepository} that will always return empty
 * {@link Properties} instances.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the id of each entry in this repo.
 */
public class AlwaysEmptyPropertiesRepository<T> implements PropertiesRepository<T> {

    @Override
    public Properties getProperties(T id) {
        return new MapProperties();
    }

    @Override
    public void saveProperties(T id, Properties properties) {
    }

    @Override
    public Properties removeProperties(T id) {
        return new MapProperties();
    }

    @Override
    public void setProperty(T id, String key, Object value) {
    }

    @Override
    public void removeProperty(T id, String key) {
    }

}
