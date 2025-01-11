package org.example.tree;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class JsonTreeTraverser {
    private static final String ARRAY_INDEX_LAST = "-";

    /**
     * Iterate the tree along the JSON pointer.
     *
     * @param rootNode: The root of the tree we want to traverse
     * @param pointer:  The JSON pointer specifying the path we want to traverse.
     * @return The result of the traversal
     */

    // Note: This returns the parent node of the targeted node, always. But also verifies if targeted node exists as a
    // child. This is to make manipulation easier in the operations.

    // e.g. if path is "/A/B/C" then the traversal will return the node "B".
    // Each operation can then decide if and how to use the child node "C".
    public static TraversalResult traverse(JsonNode rootNode, String pointer) {
        JsonNode currentNode = rootNode;

        String key;
        var keys = pointer.substring(1).split("/");

        for (int i = 0; i < keys.length - 1; i++) {
            key = keys[i];

            if (isArrayIndex(key)) {
                List jsonList = (List) currentNode.getValue();
                Integer index = getArrayIndex(key, jsonList);
                if (index == jsonList.size()) {
                    // we are appending to the array
                    return new TraversalResult(currentNode, false, keys[i+1]);
                } else {
                    currentNode = (JsonNode) jsonList.get(index);
                    continue;
                }
            }

            Optional<JsonNode> childNode = currentNode.getChild(key);

            if (childNode.isEmpty()) {
                return new TraversalResult(currentNode, false, keys[i+1]);
            }

            currentNode = childNode.get();
        }

        return new TraversalResult(currentNode, true, null);
    }

    public static boolean isArrayIndex(String key) {
        return key.equals(ARRAY_INDEX_LAST) || isNumber(key);
    }

    private static boolean isNumber(String key) {
        try {
            Integer.parseInt(key);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Integer getArrayIndex(String pointer, List jsonList) {
        var pointers = pointer.split("/");
        if (pointers[pointers.length - 1].equals(ARRAY_INDEX_LAST)) {
            return jsonList.size();
        }
        var arrayIndex = Integer.parseInt(pointers[pointers.length - 1]);
        if (arrayIndex > jsonList.size()) {
            throw new RuntimeException("Array index larger than array size");
        }
        return arrayIndex;
    }
}
