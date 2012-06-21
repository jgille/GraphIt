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
