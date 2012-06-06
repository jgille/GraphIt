package org.opengraph.graph.edge.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.schema.EdgeTypes;
import org.opengraph.graph.edge.util.TestEdgeType;

public class TestEdgeTypes implements EdgeTypes {

    private final List<EdgeType> elements;

    public TestEdgeTypes() {
        this.elements = new ArrayList<EdgeType>();
        for (TestEdgeType et : EnumSet.allOf(TestEdgeType.class)) {
            elements.add(et);
        }
    }

    @Override
    public EdgeType valueOf(String name) {
        return TestEdgeType.valueOf(name);
    }

    @Override
    public Collection<EdgeType> elements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public int size() {
        return elements.size();
    }

}
