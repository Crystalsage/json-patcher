package patcher;

import org.example.Util;
import org.example.patcher.operations.CopyOperation;
import org.example.patcher.Patch;
import org.example.patcher.operations.PatchOperation;
import org.example.tree.JsonNode;
import org.junit.jupiter.api.Test;

public class CopyOperationTest {
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
    PatchOperation copyOperation = new CopyOperation();

    @Test
    public void testCopyObject() {
        copyOperation.apply(root, new Patch("COPY", "/A", "B"));
        assert root.getChild("B").isPresent();
    }

    @Test
    public void testCopyFromAndToSameLocation() {
        assert root.getChild("A").isPresent();
        assert root.getChild("A").get().getValue().equals("B");
        copyOperation.apply(root, new Patch("COPY", "/A", "/A"));
        assert root.getChild("A").isPresent();
        assert root.getChild("A").get().getValue().equals("B");
    }

    @Test
    public void testCopyFromArrayToObject() {
        copyOperation.apply(root, new Patch("COPY", "/C/0", "/D/E"));
        var node = root.getChild("D").get().getChild("E").get();
        assert node.getValue() instanceof Double;
        assert (Double) node.getValue() == 1.0;
    }
}
