package org.example.patcher;

import org.example.tree.JsonNode;

public interface PatchOperation {
    /**
     * Applies a patch to a JSON tree.
     *
     * @param rootNode: Root node of the JSON tree.
     * @param patch: Patch we want to applyf.
     */
    void apply(JsonNode rootNode, Patch patch);
}
