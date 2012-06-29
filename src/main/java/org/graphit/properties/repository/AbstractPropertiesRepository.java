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

import java.io.File;
import java.io.IOException;

/**
 * Base implementation of a properties repo.
 *
 * @author jon
 *
 */
public abstract class AbstractPropertiesRepository<T> implements PropertiesRepository<T> {

    @Override
    public void init() {
        // Ignored
    }

    @Override
    public void shutdown() {
        // Ignored
    }

    @Override
    public void dump(File out) throws IOException {
        // Ignored
    }

    @Override
    public void restore(File in) throws IOException {
        // Ignored
    }
}
