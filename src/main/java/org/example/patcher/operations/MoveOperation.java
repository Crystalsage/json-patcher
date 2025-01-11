package org.example.patcher.operations;

import org.example.patcher.Patch;
import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;

import java.util.List;

import static org.example.tree.JsonTreeTraverser.getArrayIndex;
import static org.example.tree.JsonTreeTraverser.isArrayIndex;

public class MoveOperation implements PatchOperation {

    @Override
    public void apply(JsonNode rootNode, Patch patch) {
        String fromKey = JsonPointer.getTargetNodeKey(patch.from);
        JsonNode fromNode = JsonPointer.derefer(rootNode, patch.from, true);

        JsonNode childToBeMoved = null;
        if (isArrayIndex(fromKey)) {
            if (fromNode.getValue() instanceof List jsonList) {
                var index = getArrayIndex(patch.from, jsonList);
                if (index == jsonList.size()) {
                    childToBeMoved = (JsonNode) jsonList.get(index - 1);
                } else {
                    childToBeMoved = (JsonNode) jsonList.get(index);
                }
            }
        } else {
            // FIXME: Can we get the getChild method to cast the value by itself?
            childToBeMoved = (JsonNode) fromNode.removeChild(fromKey);
        }

        String toKey = JsonPointer.getTargetNodeKey(patch.path);
        JsonNode toNode = JsonPointer.derefer(rootNode, patch.path, true);

        childToBeMoved.setKey(toKey);
        if (isArrayIndex(toKey)) {
            if (toNode.getValue() instanceof List jsonList) {
                var index = getArrayIndex(patch.path, jsonList);
                if (index == jsonList.size()) {
                    jsonList.set(index - 1, childToBeMoved);
                } else {
                    jsonList.set(index, childToBeMoved);
                }
            }
        } else {
            toNode.putChild(childToBeMoved);
        }
    }
}
