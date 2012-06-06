package org.opengraph.graph.node.domain;

public class NodePrimitive {

    private int index;
    private String type;
    private String id;

    public NodePrimitive(int index, NodeId nodeId) {
        this.index = index;
        this.type = nodeId.getNodeType().name();
        this.id = nodeId.getId();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
