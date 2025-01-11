package org.example.tree;

import java.util.List;

public class JsonPointer {
    private static final String ARRAY_INDEX_LAST = "-";

    /*
        params:
        - rootNode : The node we want to traverse.
        - pointer  : The pointer we want to derefer.
        - required : Is it necessary for the node to exist?
     */
    public static JsonNode derefer(JsonNode rootNode, String pointer, boolean required) {
        TraversalResult result = JsonTreeTraverser.traverse(rootNode, pointer);
        if (!result.getFound()) {
            if (required) {
                throw new RuntimeException("Malformed path: Node not found");
            }
            // If the node was not required, then a new node can be constructed.
            return new JsonNode(result.getNewKey(), null);
        }
        return result.getTraversedNode();
    }

    public static int getArrayIndex(String pointer, List jsonList) {
        var pointers = pointer.split("/");
        if (pointers[pointers.length - 1].equals(ARRAY_INDEX_LAST)) {
            return jsonList.size() - 1;
        }
        var arrayIndex = Integer.parseInt(pointers[pointers.length - 1]);
        if (arrayIndex >= jsonList.size()) {
            throw new RuntimeException("Array index greater than array size");
        }
        return arrayIndex;
    }
}
