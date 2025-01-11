package org.example.patcher.operations;

import org.example.patcher.Patch;
import org.example.tree.*;

import java.util.List;

import static org.example.tree.JsonTreeTraverser.getArrayIndex;
import static org.example.tree.JsonTreeTraverser.isArrayIndex;

public class AddOperation implements PatchOperation {

    /**
     *
     * @param patch: The patch we want to apply
     */

     // The 'add' patch operation does one of the three following things:
     //   1. If the target location specifies array index, then insert new value into the array.
     //   2. If the target location specifies an object member that does not exist, then add the object.
     //   3. If the target location specifies an object member that does exist, then replace the object.
    // e.g.
    //  Patch: { "op": "add", "path": "/a/b/c", "value": [ "foo", "bar" ] }
    @Override
    public void apply(JsonNode rootNode, Patch patch) {
        JsonNode targetNode = JsonPointer.derefer(rootNode, patch.path, false);

        String targetKey = JsonPointer.getTargetNodeKey(patch.path);
        JsonNode targetValue = JsonNode.parseJson(targetKey, patch.value);


        if (targetNode.getChild(targetKey).isPresent()) {
            targetNode.putChild(new JsonNode(targetKey, targetValue));
            return;
        }

        if (isArrayIndex(targetKey)) {
            if (targetNode.getValue() instanceof List jsonList) {
                var index = getArrayIndex(patch.path, jsonList);
                if (index == jsonList.size()) {
                    jsonList.addLast(targetValue);
                } else {
                    jsonList.add(index.intValue(), targetValue);
                }
            }
        } else {
            targetNode.putChild(targetValue);
        }
    }
}
