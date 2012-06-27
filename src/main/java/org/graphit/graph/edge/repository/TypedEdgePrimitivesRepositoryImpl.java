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

package org.graphit.graph.edge.repository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.graphit.common.procedures.Procedure;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.exception.DuplicateKeyException;
import org.graphit.graph.exception.GraphException;
import org.springframework.util.Assert;

/**
 *
 * A {@link TypedEdgePrimitivesRepository} implementation backed by primitive
 * collections.
 *
 * @author jon
 *
 */
public class TypedEdgePrimitivesRepositoryImpl extends AbstractTypedEdgePrimitivesRepository {

    private static final int DEFAULT_CAPACITY = 1000;
    private final EdgePrimitivesBuffer buffer;

    /**
     * Constructs a new repo for the given edge type with a default backing
     * buffer with default initial capacity.
     */
    public TypedEdgePrimitivesRepositoryImpl(EdgeType edgeType) {
        this(edgeType, DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new repo for the given edge type with a default backing
     * buffer with the provided initial capacity
     */
    public TypedEdgePrimitivesRepositoryImpl(EdgeType edgeType, int initialCapacity) {
        this(edgeType,
             new ShardedEdgePrimitivesBuffer(edgeType,
                                             Runtime.getRuntime().availableProcessors() * 2,
                                             initialCapacity));
    }

    /**
     * Constructs a new repo for the given edge type using a custom backing
     * buffer.
     */
    public TypedEdgePrimitivesRepositoryImpl(EdgeType edgeType, EdgePrimitivesBuffer buffer) {
        super(edgeType);
        this.buffer = buffer;
    }

    @Override
    public EdgeId addEdge(int startNodeIndex, int endNodeIndex) {
        EdgeId edgeId = generateEdgeId();
        addEdge(edgeId, startNodeIndex, endNodeIndex);
        return edgeId;
    }

    @Override
    public void addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex) {
        validate(edgeId);
        EdgePrimitive previous = getEdge(edgeId);
        if (previous != null) {
            throw new DuplicateKeyException(edgeId);
        }
        buffer.upsert(edgeId.getIndex(), startNodeIndex, endNodeIndex, 0);
        insert(new EdgePrimitive(edgeId, startNodeIndex, endNodeIndex, 0));
    }

    @Override
    public EdgeId addWeightedEdge(int startNodeIndex, int endNodeIndex, float weight) {
        EdgeId edgeId = generateEdgeId();
        addWeightedEdge(edgeId, startNodeIndex, endNodeIndex, weight);
        return edgeId;
    }

    @Override
    public void addWeightedEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight) {
        validate(edgeId);
        EdgePrimitive previous = getEdge(edgeId);
        if (previous != null) {
            throw new DuplicateKeyException(edgeId);
        }
        buffer.upsert(edgeId.getIndex(), startNodeIndex, endNodeIndex, weight);
        insert(new EdgePrimitive(edgeId, startNodeIndex, endNodeIndex, weight));
    }

    @Override
    public EdgePrimitive getEdge(EdgeId edgeId) {
        validate(edgeId);
        return buffer.get(edgeId.getIndex());
    }

    @Override
    public EdgePrimitive removeEdge(EdgeId edgeId) {
        validate(edgeId);
        EdgePrimitive edge = buffer.remove(edgeId.getIndex());
        if (edge != null) {
            delete(edge);
        }
        return edge;
    }

    @Override
    public void setEdgeWeight(EdgeId edgeId, float weight) {
        EdgePrimitive edge = getEdge(edgeId);
        Assert.notNull(edge);
        buffer.upsert(edgeId.getIndex(), edge.getStartNodeIndex(), edge.getEndNodeIndex(), weight);
        reindex(new EdgePrimitive(edgeId, edge.getStartNodeIndex(), edge.getEndNodeIndex(), weight));
    }

    @Override
    public float getEdgeWeight(int edgeIndex) {
        if (!getEdgeType().isWeighted()) {
            return 0;
        }
        EdgePrimitive edge = buffer.get(edgeIndex);
        if (edge == null) {
            return -1;
        }
        return edge.getWeight();
    }

    @Override
    public void dump(File out) throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        final JsonGenerator generator = jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);
        try {
            generator.writeStartArray();
            buffer.forEach(new Procedure<EdgePrimitive>() {

                @Override
                public boolean apply(EdgePrimitive edge) {
                    dumpEdge(generator, edge);
                    return true;
                }
            });
            generator.writeEndArray();
        } finally {
            generator.close();
        }
    }

    private void dumpEdge(JsonGenerator generator, EdgePrimitive edge) {
        // TODO: Avoid creating a map just to generate json
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("i", edge.getIndex());
        map.put("s", edge.getStartNodeIndex());
        map.put("e", edge.getEndNodeIndex());
        if (getEdgeType().isWeighted()) {
            map.put("w", edge.getWeight());
        }
        try {
            generator.writeObject(map);
        } catch (JsonProcessingException e) {
            throw new GraphException("Unable to dump edges.", e);
        } catch (IOException e) {
            throw new GraphException("Unable to dump edges.", e);
        }
    }

    @Override
    public void restore(File in) throws IOException {
        Assert.isTrue(in.exists());
        Assert.isTrue(buffer.size() == 0, "Can not restore a non empty repo.");
        ObjectMapper mapper = new ObjectMapper();
        // TODO: Use a dedicated domain object instead of a map
        List<Map<String, Object>> edges =
            mapper.readValue(in, new TypeReference<List<Map<String, Object>>>() {
            });
        EdgeType edgeType = getEdgeType();
        for (Map<String, Object> map : edges) {
            EdgeId edgeId = new EdgeId(edgeType, (Integer) map.get("i"));
            int startNodeIndex = (Integer) map.get("s");
            int endNodeIndex = (Integer) map.get("e");
            if (edgeType.isWeighted()) {
                float weight = ((Double) map.get("w")).floatValue();
                addWeightedEdge(edgeId, startNodeIndex, endNodeIndex, weight);
            } else {
                addEdge(edgeId, startNodeIndex, endNodeIndex);
            }
        }
    }

    @Override
    protected String getDataDirectory() {
        return FilenameUtils.concat(getRootDataDirectory(), "edges/primitives");
    }

    @Override
    protected String getFileName() {
        return String.format("%s.json", getEdgeType().name());
    }

    private void validate(EdgeId edgeId) {
        Assert.isTrue(getEdgeType().equals(edgeId.getEdgeType()), "Illegal edge type for: "
            + edgeId);
    }

    public int size() {
        return buffer.size();
    }
}
