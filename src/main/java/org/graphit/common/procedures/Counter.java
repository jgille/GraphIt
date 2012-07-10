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

package org.graphit.common.procedures;


/**
 *
 * Interface for counting the number of times distinct elements occur in an
 * collection of elements.
 *
 * @author jon
 *
 */
public interface Counter<E> {

    /**
     * Gets the number of times this element occurred.
     */
    int count(E element);

    /**
     * Adds an element.
     */
    void add(E element);

    /**
     * Gets an iterable of all counted elements.
     */
    Iterable<CountedElement<E>> iterable(CountSortOrder sortOrder);
}
