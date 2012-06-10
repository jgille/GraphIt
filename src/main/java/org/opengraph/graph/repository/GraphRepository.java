package org.opengraph.graph.repository;

import java.io.File;
import java.io.IOException;

public interface GraphRepository {

    void init();

    void shutdown();

    void dump(File out) throws IOException;

    void restore(File in) throws IOException;
}
