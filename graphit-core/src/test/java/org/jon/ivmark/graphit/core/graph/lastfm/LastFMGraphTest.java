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

package org.jon.ivmark.graphit.core.graph.lastfm;

import org.jon.ivmark.graphit.core.graph.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.edge.EdgeDirection;
import org.jon.ivmark.graphit.core.graph.edge.EdgeTypeFilter;
import org.jon.ivmark.graphit.core.graph.node.NodeId;
import org.jon.ivmark.graphit.core.graph.node.NodeTypeFilter;
import org.jon.ivmark.graphit.test.categories.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Category(IntegrationTest.class)
public class LastFMGraphTest {

    @Test
    public void testImport() throws IOException {
        PropertyGraph lastFMGraph = LastFMGraph.importGraph();
        validate(lastFMGraph);
    }

    @Test
    public void testLoadJson() throws IOException {
        PropertyGraph lastFMGraph = LastFMGraph.load();
        validate(lastFMGraph);
    }

    private void validate(PropertyGraph lastFMGraph) {
        assertNotNull(lastFMGraph);

        int numEdges = lastFMGraph.getEdges().size();
        assertEquals(303209, numEdges);

        int numUserTags =
            lastFMGraph.getEdges().filter(new EdgeTypeFilter(LastFMGraph.TAGGED)).size();
        // Slightly fewer than what the home page states. This is because some
        // tagged artists does not exist in the artists data set, and these have
        // been ignored.
        assertEquals(184941, numUserTags);

        int numListenedTo =
            lastFMGraph.getEdges().filter(new EdgeTypeFilter(LastFMGraph.LISTENED_TO)).size();
        assertEquals(92834, numListenedTo);

        int numFriendsWith =
            lastFMGraph.getEdges().filter(new EdgeTypeFilter(LastFMGraph.FRIENDS_WITH)).size();
        assertEquals(25434, numFriendsWith);

        int numNodes = lastFMGraph.getNodes().size();
        assertEquals(19524, numNodes);

        int numUsers = lastFMGraph.getNodes().filter(new NodeTypeFilter(LastFMGraph.USER)).size();
        assertEquals(1892, numUsers);

        int numArtists =
            lastFMGraph.getNodes().filter(new NodeTypeFilter(LastFMGraph.ARTIST)).size();
        assertEquals(17632, numArtists);

        NodeId coldPlay = new NodeId(LastFMGraph.ARTIST, "65");
        int numTagsForColdPlay =
            lastFMGraph.getEdges(coldPlay, LastFMGraph.TAGGED, EdgeDirection.INCOMING).size();
        assertEquals(454, numTagsForColdPlay);
    }
}
