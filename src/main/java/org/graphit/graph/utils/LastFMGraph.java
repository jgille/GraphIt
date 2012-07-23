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

package org.graphit.graph.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.graphit.common.procedures.Procedure;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypeImpl;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.graphit.graph.schema.GraphMetadata;
import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.service.PropertyGraphImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

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

    private static final String PATH = "hetrec2011-lastfm-2k/%s";

    public static final NodeType ARTIST = new NodeTypeImpl("Artist");
    public static final NodeType USER = new NodeTypeImpl("User");

    public static final EdgeType FRIENDS_WITH = new EdgeTypeImpl("FriendsWith");
    public static final EdgeType LISTENED_TO =
        new EdgeTypeImpl("ListenedTo", EdgeSortOrder.DESCENDING_WEIGHT);
    public static final EdgeType TAGGED = new EdgeTypeImpl("Tagged");

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
        Resource resource = new ClassPathResource(String.format(PATH, "LastFMGraph.json"));
        graph.importJson(resource.getFile());
        return graph;
    }

    private static void importArtists(final PropertyGraph graph) throws IOException {
        System.out.println("Importing artists...");
        Resource resource = new ClassPathResource(String.format(PATH, "artists.dat"));
        String file = resource.getFile().getAbsolutePath();
        CSVIterator.forEachRow(file, '\t', '\\', new Procedure<CSVRow>() {

            @Override
            public boolean apply(CSVRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importArtist(graph, row);
                return true;
            }
        });
    }

    private static void importArtist(PropertyGraph graph, CSVRow row) {
        int i = 0;
        String id = row.getColumnData(i++);
        String name = row.getColumnData(i++);
        String url = row.getColumnData(i++);
        String pictureUrl = row.getColumnData(i++);

        Node artist = graph.addNode(new NodeId(ARTIST, id));
        artist.setProperty("Name", name);
        artist.setProperty("Url", url);
        artist.setProperty("PictureUrl", pictureUrl);
        graph.setNodeProperties(artist.getNodeId(), artist);

    }

    private static void importListenedTo(final PropertyGraph graph) throws IOException {
        System.out.println("Importing 'ListenedTo' edges...");
        Resource resource = new ClassPathResource(String.format(PATH, "user_artists.dat"));
        String file = resource.getFile().getAbsolutePath();
        CSVIterator.forEachRow(file, '\t', '\\', new Procedure<CSVRow>() {

            @Override
            public boolean apply(CSVRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importListenedTo(graph, row);
                return true;
            }
        });
    }

    private static void importListenedTo(PropertyGraph graph, CSVRow row) {
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
        Assert.notNull(artist, "Missing artist: " + artistId);

        graph.addEdge(user.getNodeId(), artist.getNodeId(), LISTENED_TO,
                      Float.valueOf(weight));
    }

    private static void importTagged(final PropertyGraph graph) throws IOException {
        final Map<String, Tag> tags = importTags();

        System.out.println("Importing 'Tagged' edges...");
        Resource resource =
            new ClassPathResource(String.format(PATH, "user_taggedartists-timestamps.dat"));
        String file = resource.getFile().getAbsolutePath();

        CSVIterator.forEachRow(file, '\t', '\\', new Procedure<CSVRow>() {

            @Override
            public boolean apply(CSVRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importTagged(graph, tags, row);
                return true;
            }
        });
    }

    private static void importTagged(PropertyGraph graph, Map<String, Tag> tags, CSVRow row) {
        int i = 0;
        String userId = row.getColumnData(i++);
        String artistId = row.getColumnData(i++);
        String tagId = row.getColumnData(i++);
        long timestamp = Long.parseLong(row.getColumnData(i++));
        Tag tag = tags.get(tagId);
        Assert.notNull(tags);

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
        tagged.setProperty("TimeStamp", timestamp);
        tagged.setProperty("TagValue", tag.getTagValue());
        tagged.setProperty("TagId", tag.getTagId());
        graph.setEdgeProperties(tagged.getEdgeId(), tagged);
    }

    private static Map<String, Tag> importTags() throws IOException {
        System.out.println("Importing tags...");
        Resource resource =
            new ClassPathResource(String.format(PATH, "tags.dat"));
        String file = resource.getFile().getAbsolutePath();

        final Map<String, Tag> tags = new HashMap<String, Tag>();
        CSVIterator.forEachRow(file, '\t', '\\', new Procedure<CSVRow>() {

            @Override
            public boolean apply(CSVRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                int i = 0;
                String tagId = row.getColumnData(i++);
                String tagValue = row.getColumnData(i++);

                tags.put(tagId, new Tag(tagId, tagValue));
                return true;
            }
        });

        return tags;
    }

    private static void importFriendsWith(final PropertyGraph graph) throws IOException {
        System.out.println("Importing 'FriendsWith' edges...");
        Resource resource = new ClassPathResource(String.format(PATH, "user_friends.dat"));
        String file = resource.getFile().getAbsolutePath();
        CSVIterator.forEachRow(file, '\t', '\\', new Procedure<CSVRow>() {

            @Override
            public boolean apply(CSVRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                importFriendsWith(graph, row);
                return true;
            }
        });
    }

    private static void importFriendsWith(PropertyGraph graph, CSVRow row) {
        int i = 0;
        String userId = row.getColumnData(i++);
        String friendId = row.getColumnData(i++);

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

    private static class Tag {
        private final String tagId;
        private final String tagValue;

        public Tag(String tagId, String tagValue) {
            this.tagId = tagId;
            this.tagValue = tagValue;
        }

        public String getTagId() {
            return tagId;
        }

        public String getTagValue() {
            return tagValue;
        }
    }
}