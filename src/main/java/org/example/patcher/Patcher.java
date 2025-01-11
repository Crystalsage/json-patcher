package org.example.patcher;

import org.example.patcher.operations.*;
import org.example.tree.JsonNode;

import java.util.List;

public class Patcher {
    public static void applyPatches(JsonNode rootNode, List<Patch> patches) {
        for (Patch patch : patches) {
            var patchOperation = switch (patch.op) {
                case "ADD" -> new AddOperation();
                case "REMOVE" -> new RemoveOperation();
                case "REPLACE" -> new ReplaceOperation();
                case "MOVE" -> new MoveOperation();
                case "COPY" -> new CopyOperation();
                case "TEST" -> new TestOperation();
                default -> throw new RuntimeException("Invalid operation");
            };

            patchOperation.apply(rootNode, patch);
        }
    }
}
