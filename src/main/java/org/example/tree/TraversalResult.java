package org.example.tree;

public class TraversalResult {
    private final JsonNode traversedNode;
    private final boolean found;
    private final String newKey;

    TraversalResult(JsonNode traversedNode, boolean found, String newKey) {
        this.traversedNode = traversedNode;
        this.found = found;
        this.newKey = newKey;
    }

    public boolean getFound() {
        return this.found;
    }

    public JsonNode getTraversedNode() {
        return this.traversedNode;
    }

    public String getNewKey() {
        return this.newKey;
    }
}
