package org.example.patcher;

import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;

import java.util.List;

@SuppressWarnings("unchecked")
public class Patcher {
    // returns root of the JSON tree after it has been patched
    public static void applyPatches(JsonNode jsonRoot, List<Patch> patches) throws Exception {
        for (Patch patch : patches) {
            switch (patch.op) {
                case ADD -> handleAdd(jsonRoot, patch.path, patch.value);
//                case REMOVE -> handleRemove(jsonRoot, patch.path);
//                case REPLACE -> handleReplace(jsonRoot, patch.path, patch.value);
//                case MOVE -> handleMove(jsonRoot, patch.path, patch.value);
                default -> throw new Exception("Unsupported operation");
            }
        }
    }

    /*
      Add operation does one of the following:
        - If the path points to an index in the array, it adds the value to the array.
        - If the path points to an object that doesn't exist, it creates the object using value.
        - If the path points to an object that does exist, it replaces the object's value is replaced.
     */
    // TODO + FIXME
    private static void handleAdd(JsonNode jsonRoot, String path, Object value) {
        JsonNode jsonNode = JsonPointer.derefer(jsonRoot, path, false);

        // node does not exist: add a new node
        if (jsonNode == null) {
            return;
        }

        // FIXME: otherwise if it exists, check if we are pointing to an array
        if (jsonNode.getValue() != null && jsonNode.getValue() instanceof List jsonList) {
            var arrayIndex = JsonPointer.getArrayIndex(path, jsonList);
            jsonList.add(arrayIndex, value);
        }

        // FIXME: otherwise add object
        return;
    }
}