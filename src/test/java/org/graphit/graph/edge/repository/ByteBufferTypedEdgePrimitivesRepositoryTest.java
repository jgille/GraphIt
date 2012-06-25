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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.repository.ByteBufferTypedEdgePrimitivesRepository;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.TestEdgeType;
import org.graphit.graph.exception.GraphException;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ByteBufferTypedEdgePrimitivesRepositoryTest {

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    @Test
    public void testAddSingleWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddSingleUnweightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.BOUGHT));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testAddSingleUnweightedEdgeForWeightedEdgeType() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addEdge(1, 2);
        assertThat(edgeId, Matchers.notNullValue());
        assertThat(edgeId.getEdgeType(), Matchers.is((EdgeType) TestEdgeType.SIMILAR));
        assertThat(edgeId.getIndex(), Matchers.is(0));
    }

    @Test
    public void testGetWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(100f));
    }

    @Test
    public void testGetUnweightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(0f));
    }

    @Test
    public void testGetNonExistingWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.getEdge(new EdgeId(TestEdgeType.SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(TestEdgeType.SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetNonExistingUnweightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        repo.addEdge(1, 2);

        assertThat(repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 7)), Matchers.nullValue());
        assertThat(repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetUnweightedEdgeForWeightedEdgeType() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());
        assertThat(edge.getEdgeId(), Matchers.is(edgeId));
        assertThat(edge.getStartNodeIndex(), Matchers.is(1));
        assertThat(edge.getEndNodeIndex(), Matchers.is(2));
        assertThat(edge.getWeight(), Matchers.is(0f));
    }

    @Test
    public void testRemoveWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId = repo.addWeightedEdge(1, 2, 100);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveUnWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId = repo.addEdge(1, 2);

        EdgePrimitive edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.notNullValue());

        repo.removeEdge(edgeId);
        edge = repo.getEdge(edgeId);
        assertThat(edge, Matchers.nullValue());
    }

    @Test
    public void testRemoveNonExistingWeightedEdge() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        repo.addWeightedEdge(1, 2, 100);

        assertThat(repo.removeEdge(new EdgeId(TestEdgeType.SIMILAR, 7)), Matchers.nullValue());
        assertThat(repo.removeEdge(new EdgeId(TestEdgeType.SIMILAR, 17)), Matchers.nullValue());
    }

    @Test
    public void testGetOutgoingWeightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId1 = repo.addWeightedEdge(1, 2, 100);
        repo.addWeightedEdge(2, 4, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(1, 3, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(1, 4, 10);

        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId3.getIndex(), edgeId1.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetOutgoingUnweightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId1 = repo.addEdge(1, 2);
        repo.addEdge(2, 4);
        EdgeId edgeId3 = repo.addEdge(1, 3);
        EdgeId edgeId4 = repo.addEdge(1, 4);

        EdgeVector edges = repo.getOutgoingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetIncomingWeightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        EdgeId edgeId1 = repo.addWeightedEdge(2, 1, 100);
        repo.addWeightedEdge(4, 2, 10);
        EdgeId edgeId3 = repo.addWeightedEdge(3, 1, 101);
        EdgeId edgeId4 = repo.addWeightedEdge(4, 1, 10);

        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId3.getIndex(), edgeId1.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testGetIncomingUnweightedEdgesForNode() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        EdgeId edgeId1 = repo.addEdge(2, 1);
        repo.addEdge(4, 2);
        EdgeId edgeId3 = repo.addEdge(3, 1);
        EdgeId edgeId4 = repo.addEdge(4, 1);

        EdgeVector edges = repo.getIncomingEdges(1);
        assertThat(edges, Matchers.notNullValue());
        assertThat(edges.asList(),
                   Matchers.is(Arrays.asList(edgeId1.getIndex(), edgeId3.getIndex(), edgeId4.getIndex())));
    }

    @Test
    public void testExpandBufferListWeightedEdges() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 10);
        int edgeCount = 20;
        Random random = new Random(1);
        for (int i = 0; i < edgeCount; i++) {
            int startNodeId = Math.abs(random.nextInt());
            int endNodeId = Math.abs(random.nextInt());
            float weight = random.nextFloat();
            EdgeId edgeId =
                repo.addWeightedEdge(startNodeId, endNodeId, weight);
            assertThat(edgeId, Matchers.notNullValue());
            EdgePrimitive edge = repo.getEdge(edgeId);
            assertThat(edge, Matchers.notNullValue());
            assertThat(edge.getEdgeId(), Matchers.is(edgeId));
            assertThat(edge.getStartNodeIndex(), Matchers.is(startNodeId));
            assertThat(edge.getEndNodeIndex(), Matchers.is(endNodeId));
            assertThat(edge.getWeight(), Matchers.is(weight));
        }
    }

    @Test
    public void testExpandBufferListUnweightedEdges() {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10);
        int edgeCount = 20;
        Random random = new Random(1);
        for (int i = 0; i < edgeCount; i++) {
            int startNodeId = Math.abs(random.nextInt());
            int endNodeId = Math.abs(random.nextInt());
            EdgeId edgeId =
                repo.addEdge(startNodeId, endNodeId);
            assertThat(edgeId, Matchers.notNullValue());
            EdgePrimitive edge = repo.getEdge(edgeId);
            assertThat(edge, Matchers.notNullValue());
            assertThat(edge.getEdgeId(), Matchers.is(edgeId));
            assertThat(edge.getStartNodeIndex(), Matchers.is(startNodeId));
            assertThat(edge.getEndNodeIndex(), Matchers.is(endNodeId));
        }
    }

    @Test
    public void testDumpUnweighted() throws IOException {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 3);
        repo.addEdge(2, 1);
        repo.addEdge(4, 2);
        repo.addEdge(3, 1);
        EdgeId edgeId = repo.addEdge(4, 1);
        repo.addEdge(4, 3);

        repo.removeEdge(edgeId);

        File file = out.newFile("testDump");
        repo.dump(file);

        int[] expected = new int[] { 2, 1, 4, 2, 3, 1, -1, -1, 4, 3, -1, -1 };
        byte[] bytes = FileUtils.readFileToByteArray(file);
        IntBuffer buffer = ByteBuffer.wrap(bytes).asIntBuffer();
        assertEquals(12, buffer.remaining());
        for (int expextedInt : expected) {
            assertEquals(expextedInt, buffer.get());
        }
    }

    @Test
    public void testDumpWeighted() throws IOException {
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.SIMILAR, 3);
        repo.addWeightedEdge(2, 1, 1f);
        repo.addWeightedEdge(4, 2, 2f);
        repo.addWeightedEdge(3, 1, 3f);
        EdgeId edgeId = repo.addWeightedEdge(4, 1, 4f);
        repo.addWeightedEdge(4, 3, 1.5f);

        repo.removeEdge(edgeId);

        File file = out.newFile("testDump");
        repo.dump(file);

        byte[] bytes = FileUtils.readFileToByteArray(file);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        assertEquals(72, buffer.remaining()); // 2 buffers x 3 edges per buffer
                                              // x 12 bytes per edge
        int[] expectedStartNodes = new int[] { 2, 4, 3, -1, 4, -1 };
        int[] expectedEndNodes = new int[] { 1, 2, 1, -1, 3, -1 };
        float[] expectedWeights = new float[] { 1f, 2f, 3f, 4f, 1.5f, -1f };
        for (int i = 0; i < 6; i++) {
            assertEquals("Wrong start node for edge with index: " + i, expectedStartNodes[i],
                         buffer.getInt());
            assertEquals("Wrong end node for edge with index: " + i, expectedEndNodes[i],
                         buffer.getInt());
            assertEquals("Wrong weight for edge with index: " + i, expectedWeights[i],
                         buffer.getFloat(),
                         0.00001f);
        }
    }

    @Test
    public void testRestoreUnweighted() throws IOException {
        File file = out.newFile("testRestore");

        int[] data = new int[] { 2, 1, 4, 2, 3, 1, -1, -1, 4, 3, -1, -1 };
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.asIntBuffer().put(data);
        byte[] bytes = buffer.array();

        FileUtils.writeByteArrayToFile(file, bytes);

        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 3);
        repo.restore(file);

        EdgePrimitive ep0 = repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 0));
        assertNotNull(ep0);
        assertEquals(2, ep0.getStartNodeIndex());
        assertEquals(1, ep0.getEndNodeIndex());

        EdgePrimitive ep1 = repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 1));
        assertNotNull(ep1);
        assertEquals(4, ep1.getStartNodeIndex());
        assertEquals(2, ep1.getEndNodeIndex());

        EdgePrimitive ep2 = repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 2));
        assertNotNull(ep2);
        assertEquals(3, ep2.getStartNodeIndex());
        assertEquals(1, ep2.getEndNodeIndex());

        EdgePrimitive ep3 = repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 3));
        assertNull(ep3);

        EdgePrimitive ep4 = repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 4));
        assertNotNull(ep4);
        assertEquals(4, ep4.getStartNodeIndex());
        assertEquals(3, ep4.getEndNodeIndex());

        EdgePrimitive ep5 = repo.getEdge(new EdgeId(TestEdgeType.BOUGHT, 5));
        assertNull(ep5);

        assertEquals(Arrays.asList(0), repo.getOutgoingEdges(2).asList());
        assertEquals(Arrays.asList(2), repo.getOutgoingEdges(3).asList());
        assertEquals(Arrays.asList(1, 4), repo.getOutgoingEdges(4).asList());

        assertEquals(Arrays.asList(0, 2), repo.getIncomingEdges(1).asList());
        assertEquals(Arrays.asList(1), repo.getIncomingEdges(2).asList());
        assertEquals(Arrays.asList(4), repo.getIncomingEdges(3).asList());
    }

    @Test
    public void testInitWithoutDataDir() {
        final AtomicReference<String> restoredFrom = new AtomicReference<String>();
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10) {
                @Override
                public void restore(File in) {
                    restoredFrom.set(in.getAbsolutePath());
            }
            };
        repo.init();
        assertNull(restoredFrom.get());
    }

    @Test
    public void testInitWithDataDir() throws IOException {
        final AtomicReference<String> restoredFrom = new AtomicReference<String>();
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10) {
                @Override
                public void restore(File in) {
                    restoredFrom.set(in.getAbsolutePath());
                }
            };
        String dir = out.newFolder().getAbsolutePath();
        repo.setRootDataDirectory(dir);
        repo.init();
        assertEquals(FilenameUtils.concat(repo.getDataDirectory(), repo.getFileName()),
                     restoredFrom.get());
    }

    @Test
    public void testShutdownWithoutDataDir() {
        final AtomicReference<String> dumpedTo = new AtomicReference<String>();
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10) {
                @Override
                public void dump(File out) {
                    dumpedTo.set(out.getAbsolutePath());
                }
            };
        repo.shutdown();
        assertNull(dumpedTo.get());
    }

    @Test
    public void testShutdownWithDataDir() throws IOException {
        final AtomicReference<String> dumpedTo = new AtomicReference<String>();
        ByteBufferTypedEdgePrimitivesRepository repo =
            new ByteBufferTypedEdgePrimitivesRepository(TestEdgeType.BOUGHT, 10) {
                @Override
                public void dump(File out) {
                    try {
                        FileUtils.touch(out);
                    } catch (IOException e) {
                        throw new GraphException("IOException caught.", e);
                    }
                    dumpedTo.set(out.getAbsolutePath());
                }
            };
        String dir = out.newFolder().getAbsolutePath();
        repo.setRootDataDirectory(dir);
        repo.shutdown();
        assertNotNull(dumpedTo.get());
        Pattern fileNamePattern =
            Pattern.compile(FilenameUtils.concat(FilenameUtils.concat(repo.getDataDirectory(),
                                                                      "stage"),
                                                 repo.getFileName()) + "\\.\\d*");
        assertTrue(fileNamePattern.matcher(dumpedTo.get()).matches());

    }
}
