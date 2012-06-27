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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.TestEdgeType;
import org.graphit.graph.exception.GraphException;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class ByteBufferTypedEdgePrimitivesRepositoryTest extends
    AbstractTypedEdgePrimitivesRepositoryTest {

    @Override
    protected TypedEdgePrimitivesRepository createRepo(EdgeType edgeType, int initialCapacity) {
        return new ByteBufferTypedEdgePrimitivesRepository(edgeType, initialCapacity);
    }

    @Test
    public void testDumpUnweighted() throws IOException {
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 3);
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
        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.SIMILAR, 3);
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

        TypedEdgePrimitivesRepository repo = createRepo(TestEdgeType.BOUGHT, 3);
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
        TypedEdgePrimitivesRepository repo =
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
        TypedEdgePrimitivesRepository repo =
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
