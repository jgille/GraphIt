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

package org.graphit.common.enumeration;

import java.util.Collection;

/**
 * Used to mimic an {@link EnumSet} with dynamic content.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the elements in this set.
 */
public interface DynamicEnumerationSet<T extends DynamicEnumerableElement> {

    /**
     * Gets the element with the provided name.
     */
    T valueOf(String name);

    /**
     * Gets all elements in this set.
     */
    Collection<T> elements();

    /**
     * Gets the size (number of elements) of this set.
     */
    int size();

    /**
     * Adds an element to the set.
     */
    void add(T element);

    /**
     * Adds a named element of default type to the set.
     */
    void add(String element);

    /**
     * Gets the element with the given name, or creates it if no such element
     * exists.
     */
    T getOrAdd(String edgeTypeName);

}
