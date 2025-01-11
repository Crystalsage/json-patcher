package patcher;

import org.example.Util;
import org.example.patcher.Patch;
import org.example.patcher.PatchOperation;
import org.example.patcher.RemoveOperation;
import org.example.tree.JsonNode;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RemoveOperationTest {
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
    PatchOperation removeOperation = new RemoveOperation();

    @Test
    public void testRemoveObject() {
        removeOperation.apply(root, new Patch("REMOVE", "/A"));
        assert root.getChild("A").isEmpty();
    }

    @Test
    public void testRemoveNestedObject() {
        removeOperation.apply(root, new Patch("REMOVE", "/D/E"));
        assert root.getChild("D").get().getChild("E").isEmpty();
    }

    @Test
    public void testRemoveFromList() {
        removeOperation.apply(root, new Patch("REMOVE", "/C/0"));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 1;
        assert (Double) ((JsonNode) jsonList.get(0)).getValue() == 2;
    }

    @Test
    public void testRemoveFromListLastElement() {
        removeOperation.apply(root, new Patch("REMOVE", "/C/-"));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 1;
        assert (Double) ((JsonNode) jsonList.get(0)).getValue() == 1;
    }
}
