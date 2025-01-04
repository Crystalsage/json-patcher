package org.example;

import com.google.gson.Gson;
import org.example.patcher.Operation;
import org.example.patcher.Patch;
import org.example.patcher.Patcher;
import org.example.tree.JsonNode;

import java.util.HashMap;
import java.util.List;

public class Main {
    private static final String JSON = """
            {
              "A": "B",
              "C": [1, 2]
            }
            """;

    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        var json = gson.fromJson(JSON, HashMap.class);

        var root = JsonNode.parseJson(null, json);

        var patch = new Patch(Operation.REMOVE, "/C/0");
        Patcher.applyPatches(root, List.of(patch));
    }
}