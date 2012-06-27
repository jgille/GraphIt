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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.TestEdgeType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author jon
 *
 */
public class TypedEdgePrimitivesRepositoryImplTest extends
    AbstractTypedEdgePrimitivesRepositoryTest {

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    @Override
    protected TypedEdgePrimitivesRepository createRepo(EdgeType edgeType, int initialCapacity) {
        return new TypedEdgePrimitivesRepositoryImpl(edgeType, initialCapacity);
    }

    @Test
    public void testDumpWeighted() throws IOException {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        TypedEdgePrimitivesRepository repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        for (int i = 1; i <= 20; i += 3) {
            repo.addWeightedEdge(new EdgeId(edgeType, i), i * 2, i * 3, 1f * i);
        }

        File file = out.newFile("testDump");
        repo.dump(file);

        String json = FileUtils.readFileToString(file);
        assertNotNull(json);

        Resource resource =
            new ClassPathResource("/fixtures/edges/primitives/expectedWeighted.json");
        String expected =
            FileUtils.readFileToString(resource.getFile()).trim();

        assertEquals(expected, json);

    }

    @Test
    public void testDumpUnweighted() throws IOException {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        TypedEdgePrimitivesRepository repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        for (int i = 1; i <= 20; i += 3) {
            repo.addEdge(new EdgeId(edgeType, i), i * 2, i * 3);
        }

        File file = out.newFile("testDump");
        repo.dump(file);

        String json = FileUtils.readFileToString(file);
        assertNotNull(json);

        Resource resource =
            new ClassPathResource("/fixtures/edges/primitives/expectedUnweighted.json");
        String expected =
            FileUtils.readFileToString(resource.getFile()).trim();

        assertEquals(expected, json);

    }

    @Test
    public void testRestoreWeighted() throws IOException {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        TypedEdgePrimitivesRepositoryImpl repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        File in = new ClassPathResource("/fixtures/edges/primitives/weighted.json").getFile();
        repo.restore(in);

        assertEquals(7, repo.size());

        for (int i = 1; i <= 20; i += 3) {
            EdgePrimitive edge = repo.getEdge(new EdgeId(edgeType, i));
            assertNotNull(edge);
            assertEquals(i, edge.getIndex());
            assertEquals(i * 2, edge.getStartNodeIndex());
            assertEquals(i * 3, edge.getEndNodeIndex());
            assertEquals(i * 1f, edge.getWeight(), 0.00001f);
        }
    }

    @Test
    public void testRestoreUnweighted() throws IOException {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        TypedEdgePrimitivesRepositoryImpl repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        File in = new ClassPathResource("/fixtures/edges/primitives/unweighted.json").getFile();
        repo.restore(in);

        assertEquals(7, repo.size());

        for (int i = 1; i <= 20; i += 3) {
            EdgePrimitive edge = repo.getEdge(new EdgeId(edgeType, i));
            assertNotNull(edge);
            assertEquals(i, edge.getIndex());
            assertEquals(i * 2, edge.getStartNodeIndex());
            assertEquals(i * 3, edge.getEndNodeIndex());
            assertEquals(0f, edge.getWeight(), 0.00001f);
        }
    }

    @Test
    public void testInitWeightedWithDataDir() throws IOException {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        TypedEdgePrimitivesRepositoryImpl repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        repo.setRootDataDirectory(new ClassPathResource("/fixtures/").getFile().getAbsolutePath());
        repo.init();

        assertEquals(7, repo.size());

        for (int i = 1; i <= 20; i += 3) {
            EdgePrimitive edge = repo.getEdge(new EdgeId(edgeType, i));
            assertNotNull(edge);
            assertEquals(i, edge.getIndex());
            assertEquals(i * 2, edge.getStartNodeIndex());
            assertEquals(i * 3, edge.getEndNodeIndex());
            assertEquals(i * 1f, edge.getWeight(), 0.00001f);
        }
    }

    @Test
    public void testInitWeightedWithoutDataDir() {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        TypedEdgePrimitivesRepositoryImpl repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        repo.init();
        assertEquals(0, repo.size());
    }

    @Test
    public void testInitUnweightedWithDataDir() throws IOException {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        TypedEdgePrimitivesRepositoryImpl repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        repo.setRootDataDirectory(new ClassPathResource("/fixtures/").getFile().getAbsolutePath());
        repo.init();

        assertEquals(7, repo.size());

        for (int i = 1; i <= 20; i += 3) {
            EdgePrimitive edge = repo.getEdge(new EdgeId(edgeType, i));
            assertNotNull(edge);
            assertEquals(i, edge.getIndex());
            assertEquals(i * 2, edge.getStartNodeIndex());
            assertEquals(i * 3, edge.getEndNodeIndex());
            assertEquals(0f, edge.getWeight(), 0.00001f);
        }
    }

    @Test
    public void testInitUnweightedWithoutDataDir() {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        TypedEdgePrimitivesRepositoryImpl repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        repo.init();
        assertEquals(0, repo.size());
    }

    @Test
    public void testShutdownWeightedWithDataDir() throws IOException {
        EdgeType edgeType = TestEdgeType.SIMILAR;
        TypedEdgePrimitivesRepository repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        File dir = out.newFolder();
        repo.setRootDataDirectory(dir.getAbsolutePath());
        for (int i = 1; i <= 20; i += 3) {
            repo.addWeightedEdge(new EdgeId(edgeType, i), i * 2, i * 3, 1f * i);
        }

        repo.shutdown();

        File file = new File(dir, "edges/primitives/SIMILAR.json");

        String json = FileUtils.readFileToString(file);
        assertNotNull(json);

        Resource resource =
            new ClassPathResource("/fixtures/edges/primitives/expectedWeighted.json");
        String expected =
            FileUtils.readFileToString(resource.getFile()).trim();

        assertEquals(expected, json);
    }

    @Test
    public void testShutdownUnweightedWithDataDir() throws IOException {
        EdgeType edgeType = TestEdgeType.BOUGHT;
        TypedEdgePrimitivesRepository repo =
            new TypedEdgePrimitivesRepositoryImpl(edgeType,
                                                  new ShardedEdgePrimitivesBuffer(edgeType, 2, 10));
        File dir = out.newFolder();
        repo.setRootDataDirectory(dir.getAbsolutePath());
        for (int i = 1; i <= 20; i += 3) {
            repo.addWeightedEdge(new EdgeId(edgeType, i), i * 2, i * 3, 1f * i);
        }

        repo.shutdown();

        File file = new File(dir, "edges/primitives/BOUGHT.json");

        String json = FileUtils.readFileToString(file);
        assertNotNull(json);

        Resource resource =
            new ClassPathResource("/fixtures/edges/primitives/expectedUnweighted.json");
        String expected =
            FileUtils.readFileToString(resource.getFile()).trim();

        assertEquals(expected, json);

    }

}
