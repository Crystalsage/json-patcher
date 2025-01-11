package org.example.patcher.operations;

import org.example.patcher.Patch;
import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;

import java.util.List;

import static org.example.tree.JsonTreeTraverser.getArrayIndex;
import static org.example.tree.JsonTreeTraverser.isArrayIndex;

public class ReplaceOperation implements PatchOperation {
    @Override
    public void apply(JsonNode rootNode, Patch patch) {
        JsonNode targetNode = JsonPointer.derefer(rootNode, patch.path, true);

        String targetKey = JsonPointer.getTargetNodeKey(patch.path);
        JsonNode targetValue = JsonNode.parseJson(targetKey, patch.value);

        if (isArrayIndex(targetKey)) {
            if (targetNode.getValue() instanceof List jsonList) {
                var index = getArrayIndex(patch.path, jsonList);
                if (index == jsonList.size()) {
                    jsonList.set(index - 1, targetValue);
                } else {
                    jsonList.set(index, targetValue);
                }
            }
        } else {
            targetNode.getChild(targetKey).get().setValue(targetValue);
        }
    }
}
