package org.example.tree;

import java.util.List;

public class JsonPointer {

    /*
        params:
        - rootNode : The node we want to traverse.
        - pointer  : The pointer we want to derefer.
        - required : Is it necessary for the node to exist?
     */
    public static JsonNode derefer(JsonNode rootNode, String pointer, boolean required) {
        TraversalResult result = JsonTreeTraverser.traverse(rootNode, pointer);
        if (!result.getFound() && required) {
            throw new RuntimeException("Malformed path: Node not found");
        }
        return result.getTraversedNode();
    }

    public static String getTargetNodeKey(String pointer) {
        var pointers = pointer.split("/");
        return pointers[pointers.length - 1];
    }
}
