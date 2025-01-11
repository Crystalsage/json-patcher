package org.example.tree;

import java.util.List;
import java.util.Optional;

public class JsonTreeTraverser {
    /**
     *
     * @param rootNode: The root of the tree we want to traverse
     * @param pointer: The JSON pointer specifying the path we want to traverse.
     * @return The result of the traversal
     */

    /*
        Iterate the tree along the JSON pointer. Ultimately iterates in a DFS-like fashion.
     */
    public static TraversalResult traverse(JsonNode rootNode, String pointer) {
        JsonNode currentNode = rootNode;

        String key;
        var keys = pointer.substring(1).split("/");

        for (int i = 0; i < keys.length; i++) {
            key = keys[i];

            if (isNumber(key)) {
               int index = Integer.parseInt(key);
               List jsonList = (List) currentNode.getValue();
               currentNode = (JsonNode) jsonList.get(index);
               continue;
            }

            Optional<JsonNode> childNode = currentNode.getChild(key);

            if (childNode.isEmpty()) {
                return new TraversalResult(null, false, key);
            }

            currentNode = childNode.get();
        }

        return new TraversalResult(currentNode, true, null);
    }

    private static boolean isNumber(String key) {
        try {
            Integer.parseInt(key);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
