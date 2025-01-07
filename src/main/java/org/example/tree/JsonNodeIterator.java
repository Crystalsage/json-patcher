package org.example.tree;

import java.util.*;

// Iterate over the JSON tree depth-first.
public class JsonNodeIterator implements Iterator<JsonNode> {
    private JsonNode parentNode;
    private JsonNode currentNode;

    // A set of child nodes represented by their keys
    private final Stack<JsonNode> nodesToVisit = new Stack<>();

    public JsonNodeIterator(JsonNode rootNode) {
        this.currentNode = rootNode;
        this.parentNode = rootNode;
        this.nodesToVisit.push(rootNode);
    }

    @Override
    public boolean hasNext() {
        return !nodesToVisit.isEmpty();
    }

    @Override
    public JsonNode next() {
        if (hasNext()) {
            // add children nodes as pending
            for (Object node: currentNode.getChildren().values()) {
                nodesToVisit.push((JsonNode) node);
            }
        }

        // Move to the next unvisited node:
        // If the current node has children, then the next node is the first child.
        // Otherwise it is a sibling node.
        currentNode = nodesToVisit.pop();
        return currentNode;
    }
}
