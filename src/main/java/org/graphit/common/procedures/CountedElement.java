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
 * Domain object for counting the number of times an element occurs in a
 * collection.
 * 
 * @author jon
 * 
 */
public class CountedElement<E> {

    private final E element;
    private final int count;

    /**
     * Constructs a new element.
     */
    public CountedElement(E element, int count) {
        this.element = element;
        this.count = count;
    }

    /**
     * Gets the counted element.
     * 
     */
    public E getElement() {
        return element;
    }

    /**
     * Gets the number of times the element occurred.
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "CountedElement [element=" + element + ", count=" + count + "]";
    }
}
