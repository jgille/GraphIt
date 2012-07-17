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
 * Interface for reducing iterables to a final result in a MapReduce operation.
 * 
 * @author jon
 * 
 */
public interface Reducer<E, T> {

    /**
     * Computes the final result from a mapped iterable.
     */
    T reduce(Iterable<E> input);
}
