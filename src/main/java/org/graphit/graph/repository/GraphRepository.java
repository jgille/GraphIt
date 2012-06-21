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

package org.graphit.graph.repository;

import java.io.File;
import java.io.IOException;

/**
 * Common interface for classes used as repositories in graph.
 *
 * @author jon
 *
 */
public interface GraphRepository {

    /**
     * Initiates the repo.
     */
    void init();

    /**
     * Shuts the repo down.
     */
    void shutdown();

    /**
     * Dumps this repo to file. Might throw an
     * {@link UnsupportedOperationException} depending on the repo
     * implementation.
     */
    void dump(File out) throws IOException;

    /**
     * Restores this repo from file. Might throw an
     * {@link UnsupportedOperationException} depending on the repo
     * implementation.
     *
     * Should never be called for a non empty repo.
     */
    void restore(File in) throws IOException;
}
