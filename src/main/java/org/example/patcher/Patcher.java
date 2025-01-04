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
                case ADD -> handleAdd(jsonRoot, patch.path, patch.value);
                case REMOVE -> handleRemove(jsonRoot, patch.path, patch.value);
                default -> throw new Exception("Unsupported operation");
            }
        }
    }

    private static void handleRemove(JsonNode jsonRoot, String path, Object value) throws Exception {
        var pointers = path.split("/");
        String key = null;

        var parentNode = jsonRoot;

        for (int i = 1; i < pointers.length; i++) {
            key = pointers[i];

            if (Character.isDigit(pointers[i].charAt(0)) || "-".equals(pointers[i])) {
                if ("-".equals(pointers[i])) {
                    key = "-";
                } else {
                    key = pointers[i];
                }
            } else {
                if (parentNode.getChild(pointers[i]).isEmpty()) {
                    throw new Exception("Malformed path: Path not found");
                }
            }

            if (i != pointers.length - 1) {
                parentNode = parentNode.getChild(pointers[i]).get();
            }
        }

        assert key != null;
        if (Character.isDigit(key.charAt(0)) || "-".equals(key)) {
            if ("-".equals(key)) {
                ((List) parentNode.getValue()).removeLast();
            } else {
                ((List) parentNode.getValue()).remove(Integer.valueOf(key).intValue());
            }
        } else {
            parentNode.removeChild(key);
        }
    }

    private static void handleAdd(JsonNode jsonRoot, String path, Object value) throws Exception {
        var pointers = path.split("/");
        String key = null;
        JsonNode currentNode = jsonRoot;

        for (String pointer : pointers) {
            key = pointer;

            // if the pointer path exists as a child, head down into the child node
            var nextChild = currentNode.getChild(key);
            if (nextChild.isPresent()) {
                currentNode = nextChild.get();
            }
        }

        // index into array
        if (Character.isDigit(key.charAt(0)) || key == "-") {
            if (key.equals("-")) {
                ((List) currentNode.getValue()).addLast(JsonNode.parseJson(null, value));
            }
            ((List) currentNode.getValue()).add(Integer.parseInt(key), JsonNode.parseJson(null, value));
        }

        // just add
        var newNode = JsonNode.parseJson(key, value);
        currentNode.putChild(newNode);
    }
}