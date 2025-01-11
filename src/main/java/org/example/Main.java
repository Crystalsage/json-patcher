package org.example;

import com.google.gson.Gson;
import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;

import java.util.HashMap;

public class Main {
    private static final String JSON = """
            {
              "A": "B",
              "C": [1, 2],
              "D": {
                "E": 1
              }
            }
            """;

    public static void main(String[] args) {
        var root = JsonNode.parseJson(null, Util.deserializeJson(JSON));
        var node = JsonPointer.derefer(root, "/A", true);
        System.out.println(node.getKey());
    }
}