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
