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

package org.jon.ivmark.graphit.core.graph.node.repository;

/**
 * A list like interface that does not allow duplicates and provides a fast
 * indexOf look-up.
 *
 * @author jon
 *
 */
public interface MappedList<E> {

    /**
     * Gets the element at the provided index. Returns null if the index is out
     * of bounds.
     */
    E get(int index);

    /**
     * Adds an element an returns the index of this new element.
     */
    int add(E element);

    /**
     * Sets the element at the provided index, throwing an exception if the
     * element already exists. Might result in the list being expanded.
     */
    void insert(int index, E element);

    /**
     * Sets the element at the provided index. Might result in the list being
     * expanded.
     */
    void set(int index, E element);

    /**
     * Removes the element at the provided index.
     */
    E remove(int index);

    /**
     * Returns the index of an element, or -1 if it does not exist.
     */
    int indexOf(E element);

    /**
     * Returns this instance as an Iterable. Note that iteration order is not
     * guaranteed to be the same as insertion order.
     */
    Iterable<E> iterable();
}
