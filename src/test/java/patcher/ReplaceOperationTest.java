package patcher;

import org.example.Util;
import org.example.patcher.Patch;
import org.example.patcher.operations.PatchOperation;
import org.example.patcher.operations.ReplaceOperation;
import org.example.tree.JsonNode;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReplaceOperationTest {
    final String JSON = """
                {
                  "A": "B",
                  "C": [1, 2],
                  "D": {
                    "E": 1,
                    "F": "X"
                  }
                }
                """;

    final JsonNode root = JsonNode.parseJson(null, Util.deserializeJson(JSON));
    PatchOperation replaceOperation = new ReplaceOperation();

    @Test
    public void testReplaceObject() {
        replaceOperation.apply(root, new Patch("REPLACE", "/A", 1));

        var node = root.getChild("A").get();
        assert node.getValue() instanceof Integer;
        assert (Integer) node.getValue() == 1;
    }

    @Test
    public void testReplaceNestedObject() {
        replaceOperation.apply(root, new Patch("REPLACE", "/D/E", List.of(1, 2)));

        var node =  root.getChild("D").get().getChild("E").get();
        assert node.getValue() instanceof JsonNode;
        var jsonList = (List) ((JsonNode) node.getValue()).getValue();
        assert jsonList.size() == 2;
        assert (Integer) ((JsonNode) jsonList.get(0)).getValue() == 1;
    }

    @Test
    public void testReplaceInList() {
        replaceOperation.apply(root, new Patch("REPLACE", "/C/0", 3));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 2;
        assert (Integer) ((JsonNode) jsonList.get(0)).getValue() == 3;
    }

    @Test
    public void testReplaceInListLast() {
        replaceOperation.apply(root, new Patch("REPLACE", "/C/-", 3));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 2;
        assert (Integer) ((JsonNode) jsonList.getLast()).getValue() == 3;
    }
}
