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

/**
 * Base {@link GraphRepository} class.
 *
 * @author jon
 *
 */
public abstract class AbstractGraphRepository implements GraphRepository {

    private String dataDirectory;

    /**
     * Gets the data directory used by this repo, e.g.
     * /usr/local/opengraph/data/somegraph/edges/primitives.
     */
    protected abstract String getDataDirectory();

    /**
     * Gets the name of the file in which this repo stores it's data. e.g.
     * nodes.json.
     */
    protected abstract String getFileName();

    /**
     * Gets the root directory for the graph, under which this repo might store
     * data.
     */

    protected String getRootDataDirectory() {
        return dataDirectory;
    }

    /**
     * Sets the root directory for the graph, under which this repo might store
     * data.
     */
    public void setRootDataDirectory(String directory) {
        this.dataDirectory = directory;
    }


}
