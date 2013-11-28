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

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link Procedure} that collects processed elements in a list.
 * 
 * @author jon
 * 
 * @param <T>
 *            The generic type of the processed elements.
 */
public class Collector<T> implements Procedure<T> {

    private final Predicate<T> predicate;
    private final List<T> elements = new ArrayList<T>();

    /**
     * Creates a new procedure.
     */
    public Collector() {
        this(new Predicate<T>() {

            @Override
            public boolean apply(T element) {
                return true;
            }
        });
    }

    /**
     * Creates a new filtered procedure.
     * 
     * @param predicate
     *            A predicate used to decide whether or not to collect an
     *            element.
     */
    public Collector(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean apply(T element) {
        if (!predicate.apply(element)) {
            return false;
        }
        elements.add(element);
        return true;
    }

    /**
     * Gets all collected elements.
     * 
     */
    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
