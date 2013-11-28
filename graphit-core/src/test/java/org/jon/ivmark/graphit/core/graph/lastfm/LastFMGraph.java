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

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.io.util.CsvIterator;
import org.jon.ivmark.graphit.core.io.util.CsvRow;
import org.jon.ivmark.graphit.core.procedures.Procedure;
import org.jon.ivmark.graphit.core.graph.edge.domain.Edge;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeSortOrder;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeType;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeTypeImpl;
import org.jon.ivmark.graphit.core.graph.node.domain.Node;
import org.jon.ivmark.graphit.core.graph.node.domain.NodeId;
import org.jon.ivmark.graphit.core.graph.node.schema.NodeType;
import org.jon.ivmark.graphit.core.graph.node.schema.NodeTypeImpl;
import org.jon.ivmark.graphit.core.graph.schema.GraphMetadata;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraphImpl;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.core.properties.domain.EnumMapPropertiesFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class imports a graph representing the HetRec 2011 Last.FM dataset:
 * http://www.grouplens.org/node/462#attachments
 *
 * The graph consists of 2 node types (users and artists), 3 edge types (tagged,
 * listened to, friends with) and has around 20K nodes and 300K edges.
 *
 * @author jon
 *
 */
public final class LastFMGraph {

    public static enum ArtistProperty {
        NAME, URL, PICTURE_URL;
    }

    public static enum TaggedProperty {
        TIMESTAMP, TAG_ID, TAG_VALUE;
    }

    private static final String PATH = "hetrec2011-lastfm-2k/%s";

    public static final NodeType ARTIST =
        new NodeTypeImpl("Artist",
                         new EnumMapPropertiesFactory<ArtistProperty>(ArtistProperty.class));
    public static final NodeType USER = new NodeTypeImpl("User");

    public static final EdgeType FRIENDS_WITH = new EdgeTypeImpl("FriendsWith");
    public static final EdgeType LISTENED_TO =
        new EdgeTypeImpl("ListenedTo", EdgeSortOrder.DESCENDING_WEIGHT);
    public static final EdgeType TAGGED =
        new EdgeTypeImpl("Tagged", EdgeSortOrder.UNDEFINED,
                         new EnumMapPropertiesFactory<TaggedProperty>(TaggedProperty.class));

    public static final GraphMetadata METADATA =
        new GraphMetadata("LastFMGraph")
            .addNodeType(ARTIST).addNodeType(USER)
            .addEdgeType(FRIENDS_WITH).addEdgeType(LISTENED_TO)
            .addEdgeType(TAGGED);

    private LastFMGraph() {

    }

    /**
     * Imports the graph from .dat files in the resource dir.
     */
    static PropertyGraph importGraph() throws IOException {
        PropertyGraph graph = new PropertyGraphImpl(METADATA);
        importArtists(graph);
        importListenedTo(graph);
        importTagged(graph);
        importFriendsWith(graph);
        return graph;
    }

    /**
     * Loads the graph from a json file.
     */
    public static PropertyGraph load() throws IOException {
        PropertyGraph graph = new PropertyGraphImpl();
        File file = ResourceUtils.resourceFile(String.format(PATH, "LastFMGraph.json"));
        long t0 = System.currentTimeMillis();
        graph.importJson(file);
        long t1 = System.currentTimeMillis();
        System.out.println("Loaded Last.FM graph in " + (t1 - t0) + " ms.");
        return graph;
    }

    private static void importArtists(final PropertyGraph graph) throws IOException {
        System.out.println("Importing artists...");
        String file = ResourceUtils.resourceFile(String.format(PATH, "artists.dat")).getAbsolutePath();
        CsvIterator.forEachRow(file, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importArtist(graph, row);
                return true;
            }
        });
    }

    private static void importArtist(PropertyGraph graph, CsvRow row) {
        int i = 0;
        String id = row.getColumnData(i++);
        String name = row.getColumnData(i++);
        String url = row.getColumnData(i++);
        String pictureUrl = row.getColumnData(i++);

        Node artist = graph.addNode(new NodeId(ARTIST, id));
        artist.setProperty(ArtistProperty.NAME.name(), name);
        artist.setProperty(ArtistProperty.URL.name(), url);
        artist.setProperty(ArtistProperty.PICTURE_URL.name(), pictureUrl);
        graph.setNodeProperties(artist.getNodeId(), artist);

    }

    private static void importListenedTo(final PropertyGraph graph) throws IOException {
        System.out.println("Importing 'ListenedTo' edges...");
        String file = ResourceUtils.resourceFile(String.format(PATH, "user_artists.dat")).getAbsolutePath();
        CsvIterator.forEachRow(file, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importListenedTo(graph, row);
                return true;
            }
        });
    }

    private static void importListenedTo(PropertyGraph graph, CsvRow row) {
        int i = 0;
        String userId = row.getColumnData(i++);
        String artistId = row.getColumnData(i++);
        String weight = row.getColumnData(i++);

        NodeId uid = new NodeId(USER, userId);
        Node user = graph.getNode(uid);
        if (user == null) {
            user = graph.addNode(uid);
        }
        NodeId aid = new NodeId(ARTIST, artistId);
        Node artist = graph.getNode(aid);
        Preconditions.checkNotNull(artist, "Missing artist: " + artistId);

        graph.addEdge(user.getNodeId(), artist.getNodeId(), LISTENED_TO,
                      Float.valueOf(weight));
    }

    private static void importTagged(final PropertyGraph graph) throws IOException {
        final Map<String, Tag> tags = importTags();

        System.out.println("Importing 'Tagged' edges...");
        String file =
                ResourceUtils.resourceFile(String.format(PATH, "user_taggedartists-timestamps.dat")).getAbsolutePath();

        CsvIterator.forEachRow(file, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importTagged(graph, tags, row);
                return true;
            }
        });
    }

    private static void importTagged(PropertyGraph graph, Map<String, Tag> tags, CsvRow row) {
        int i = 0;
        String userId = row.getColumnData(i++);
        String artistId = row.getColumnData(i++);
        String tagId = row.getColumnData(i++);
        long timestamp = Long.parseLong(row.getColumnData(i++));
        Tag tag = tags.get(tagId);
        Preconditions.checkNotNull(tags);

        NodeId uid = new NodeId(USER, userId);
        Node user = graph.getNode(uid);
        if (user == null) {
            user = graph.addNode(uid);
        }
        Node artist = graph.getNode(new NodeId(ARTIST, artistId));
        if (artist == null) {
            return;
        }

        Edge tagged = graph.addEdge(user.getNodeId(), artist.getNodeId(), TAGGED);
        tagged.setProperty(TaggedProperty.TIMESTAMP.name(), timestamp);
        tagged.setProperty(TaggedProperty.TAG_VALUE.name(), tag.getTagValue());
        tagged.setProperty(TaggedProperty.TAG_ID.name(), tag.getTagId());
        graph.setEdgeProperties(tagged.getEdgeId(), tagged);
    }

    private static Map<String, Tag> importTags() throws IOException {
        System.out.println("Importing tags...");
        String file =
                ResourceUtils.resourceFile(String.format(PATH, "tags.dat")).getAbsolutePath();

        final Map<String, Tag> tags = new HashMap<String, Tag>();
        CsvIterator.forEachRow(file, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                int i = 0;
                String tagId = row.getColumnData(i++);
                String tagValue = row.getColumnData(i);

                tags.put(tagId, new Tag(tagId, tagValue));
                return true;
            }
        });

        return tags;
    }

    private static void importFriendsWith(final PropertyGraph graph) throws IOException {
        System.out.println("Importing 'FriendsWith' edges...");
        String file =
                ResourceUtils.resourceFile(String.format(PATH, "user_friends.dat")).getAbsolutePath();
        CsvIterator.forEachRow(file, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importFriendsWith(graph, row);
                return true;
            }
        });
    }

    private static void importFriendsWith(PropertyGraph graph, CsvRow row) {
        int i = 0;
        String userId = row.getColumnData(i++);
        String friendId = row.getColumnData(i);

        NodeId uid = new NodeId(USER, userId);
        Node user = graph.getNode(uid);
        if (user == null) {
            user = graph.addNode(uid);
        }

        NodeId fid = new NodeId(USER, friendId);
        Node friend = graph.getNode(fid);
        if (friend == null) {
            friend = graph.addNode(fid);
        }

        graph.addEdge(user.getNodeId(), friend.getNodeId(), FRIENDS_WITH);
    }

    private static final class Tag {
        private final String tagId;
        private final String tagValue;

        private Tag(String tagId, String tagValue) {
            this.tagId = tagId;
            this.tagValue = tagValue;
        }

        private String getTagId() {
            return tagId;
        }

        private String getTagValue() {
            return tagValue;
        }
    }
}