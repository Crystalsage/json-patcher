package patcher;

import org.example.Util;
import org.example.patcher.operations.AddOperation;
import org.example.patcher.Patch;
import org.example.patcher.operations.PatchOperation;
import org.example.tree.JsonNode;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AddOperationTest {
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
    PatchOperation addOperation = new AddOperation();

    @Test
    public void testAddObjectWhenDoesNotExist() {
        addOperation.apply(root, new Patch("ADD", "/B", List.of(1, 2)));

        assert root.getChild("B").isPresent();
        var jsonList = (List) root.getChild("B").get().getValue();
        assert jsonList.get(0) instanceof JsonNode;
        assert ((JsonNode) jsonList.get(0)).getValue() instanceof Integer;
        assert (Integer) ((JsonNode) jsonList.get(0)).getValue() == 1;
    }

    @Test
    public void testAddObjectWhenExists() {
        addOperation.apply(root, new Patch("ADD", "/B", 3.0));
        assert root.getChild("B").isPresent();
        assert root.getChild("B").get().getValue() instanceof Number;
        assert root.getChild("B").get().getValue().equals(3.0);
    }

    @Test
    public void testAddToArrayAtLastIndex() {
        addOperation.apply(root, new Patch("ADD", "/C/-", 3));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 3;
        assert jsonList.getLast() instanceof JsonNode;
        assert (Integer) ((JsonNode) jsonList.getLast()).getValue() == 3;
    }

    @Test
    public void testAddToArrayIndexDoesNotExist() {
        addOperation.apply(root, new Patch("ADD", "/C/1", 3));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 3;
        assert jsonList.getLast() instanceof JsonNode;
        assert (Double) ((JsonNode) jsonList.getLast()).getValue() == 2;
    }

    @Test
    public void testAddToArrayIndexWhenExists() {
        addOperation.apply(root, new Patch("ADD", "/C/1", 3));
        var jsonList = (List) root.getChild("C").get().getValue();
        assert jsonList.size() == 3;
        assert jsonList.get(1) instanceof JsonNode;
        assert (Integer) ((JsonNode) jsonList.get(1)).getValue() == 3;
        assert (Double) ((JsonNode) jsonList.get(2)).getValue() == 2;
    }
}
