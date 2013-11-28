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
 * A factory creating {@link EnumMapProperties} instances.
 *
 * @author jon
 * 
 */
public class EnumMapPropertiesFactory<E extends Enum<E>> implements PropertiesFactory {

    private final Class<E> enumClass;

    /**
     * Creates a new factory.
     */
    public EnumMapPropertiesFactory(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Properties createEmptyProperties() {
        return new EnumMapProperties<E>(enumClass);
    }

}
