package org.example;

import com.google.gson.Gson;
import org.example.patcher.Operation;
import org.example.patcher.Patch;
import org.example.patcher.Patcher;
import org.example.tree.JsonNode;
import org.example.tree.JsonNodeIterator;

import java.util.HashMap;
import java.util.List;

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

    public static void main(String[] args) throws Exception {
        var root = JsonNode.parseJson(null, deserializeJson(JSON));
        JsonNodeIterator iterator = new JsonNodeIterator(root);
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getValue());
        }
//        var patch = new Patch(Operation.ADD, "/C/0", 3.0);
    }

    public static HashMap deserializeJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(JSON, HashMap.class);
    }
}