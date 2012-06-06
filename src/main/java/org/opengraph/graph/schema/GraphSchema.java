package org.opengraph.graph.schema;

import java.util.Collection;

public interface GraphSchema {

    Collection<GraphMetadata> getGraphMetadata();

    GraphMetadata getGraphMetadata(String graphName);

}
