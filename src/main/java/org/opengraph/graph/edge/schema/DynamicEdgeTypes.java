package org.opengraph.graph.edge.schema;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class DynamicEdgeTypes implements EdgeTypes {

    private final Map<String, EdgeType> edgeTypes;

    public DynamicEdgeTypes() {
        this.edgeTypes = Collections.synchronizedMap(new LinkedHashMap<String, EdgeType>());
    }

    @Override
    public EdgeType valueOf(String name) {
        EdgeType edgeType = edgeTypes.get(name);
        Assert.notNull(edgeType);
        return edgeType;
    }

    @Override
    public Collection<EdgeType> elements() {
        return edgeTypes.values();
    }

    @Override
    public int size() {
        return edgeTypes.size();
    }

    public DynamicEdgeTypes add(EdgeType edgeType) {
        edgeTypes.put(edgeType.name(), edgeType);
        return this;
    }

}
