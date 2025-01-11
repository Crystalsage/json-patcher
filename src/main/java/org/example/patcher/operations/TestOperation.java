package org.example.patcher.operations;

import org.example.patcher.Patch;
import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;

import java.util.List;

import static org.example.tree.JsonTreeTraverser.getArrayIndex;
import static org.example.tree.JsonTreeTraverser.isArrayIndex;

public class TestOperation implements PatchOperation {
    @Override
    public void apply(JsonNode rootNode, Patch patch) {
        JsonNode targetNode = JsonPointer.derefer(rootNode, patch.path, false);

        String targetKey = JsonPointer.getTargetNodeKey(patch.path);
        JsonNode targetValue = JsonNode.parseJson(targetKey, patch.value);

        targetNode = targetNode.getChild(targetKey).get();

        if (isArrayIndex(targetKey)) {
            if (targetNode.getValue() instanceof List jsonList) {
                var index = getArrayIndex(patch.path, jsonList);
                if (index == jsonList.size()) {
                    targetNode = (JsonNode) jsonList.getLast();
                } else {
                    targetNode = (JsonNode) jsonList.get(index);
                }
            }
        }

        if (!JsonNode.equals(targetNode, targetValue)) {
            throw new RuntimeException("Test failed.");
        }
    }
}
