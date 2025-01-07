package org.example.patcher;

import org.example.tree.JsonNode;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class Patcher {
    // returns root of the JSON tree after it has been patched
    public static void applyPatches(JsonNode jsonRoot, List<Patch> patches) throws Exception {
        for (Patch patch : patches) {
            switch (patch.op) {
//                case ADD -> handleAdd(jsonRoot, patch.path, patch.value);
//                case REMOVE -> handleRemove(jsonRoot, patch.path);
//                case REPLACE -> handleReplace(jsonRoot, patch.path, patch.value);
//                case MOVE -> handleMove(jsonRoot, patch.path, patch.value);
                default -> throw new Exception("Unsupported operation");
            }
        }
    }
}