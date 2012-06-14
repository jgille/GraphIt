package org.opengraph.graph.schema;

import java.util.Collection;

/**
 * Describes a set of named graphs.
 *
 * @author jon
 *
 */
public interface GraphSchema {

    /**
     * Gets metadata for all graphs.
     */
    Collection<GraphMetadata> getGraphMetadata();

    /**
     * Gets metadata for a names graph.
     */
    GraphMetadata getGraphMetadata(String graphName);

}
