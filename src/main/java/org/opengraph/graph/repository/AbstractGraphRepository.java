package org.opengraph.graph.repository;

/**
 * Base {@link GraphRepository} class.
 *
 * @author jon
 *
 */
public abstract class AbstractGraphRepository implements GraphRepository {

    private String baseDirectory;

    /**
     * Gets the data directory used by this repo, e.g.
     * /usr/local/opengraph/data/somegraph/edges/primitives.
     */
    protected abstract String getDirectory();

    /**
     * Gets the name of the file in which this repo stores it's data. e.g.
     * nodes.json.
     */
    protected abstract String getFileName();

    protected String getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * Sets the base directory for the graph, under which this repo might store
     * data.
     */
    public void setBaseDirectory(String directory) {
        this.baseDirectory = directory;
    }


}
