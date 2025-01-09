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
        JsonNodeIterator iterator = new JsonNodeIterator(rootNode);

        var tokens = pointer.split("/");
        for (String token: tokens) {
            if (!iterator.hasNext() && required) {
                return null;
            }

            JsonNode currentNode = iterator.next();
            if (!currentNode.getKey().equals(token) && required) {
                return null;
            }

            return currentNode;
        }
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
