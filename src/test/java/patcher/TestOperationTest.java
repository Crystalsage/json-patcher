package patcher;

import org.example.Util;
import org.example.patcher.Patch;
import org.example.patcher.operations.CopyOperation;
import org.example.patcher.operations.PatchOperation;
import org.example.patcher.operations.TestOperation;
import org.example.tree.JsonNode;
import org.junit.jupiter.api.Test;

public class TestOperationTest {
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
    PatchOperation testOperation = new TestOperation();

    @Test
    public void testTestObject() {
        testOperation.apply(root, new Patch("TEST", "/A", (Object) "B"));
    }
}
