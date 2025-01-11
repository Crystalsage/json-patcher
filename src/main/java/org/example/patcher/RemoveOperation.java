package org.example.patcher;

import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;

import java.util.List;

import static org.example.tree.JsonTreeTraverser.getArrayIndex;
import static org.example.tree.JsonTreeTraverser.isArrayIndex;

public class RemoveOperation implements PatchOperation {
    @Override
    public void apply(JsonNode rootNode, Patch patch) {
        JsonNode targetNode = JsonPointer.derefer(rootNode, patch.path, false);
        String targetKey = JsonPointer.getTargetNodeKey(patch.path);

        if (isArrayIndex(targetKey)) {
            if (targetNode.getValue() instanceof List jsonList) {
                var index = getArrayIndex(patch.path, jsonList);
                if (index == jsonList.size()) {
                    jsonList.removeLast();
                } else {
                    jsonList.remove(index.intValue());
                }
            }
        } else {
            targetNode.removeChild(targetKey);
        }
    }
}
