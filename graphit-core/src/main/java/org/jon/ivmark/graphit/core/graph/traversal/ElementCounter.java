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

/**
 * A {@link Reducer} that counts the number of times each distinct element
 * occurs in an iterable.
 * 
 * @author jon
 * 
 */
public class ElementCounter<E> implements Reducer<E, Counter<E>> {

    @Override
    public Counter<E> reduce(Iterable<E> input) {
        Counter<E> counter = new Counter<E>();
        for (E element : input) {
            counter.add(element);
        }
        return counter;
    }
}
