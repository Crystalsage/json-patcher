import org.example.Util;
import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JsonPointerTest {
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

    @Test
    public void testStringValueDereference() {
        var node = JsonPointer.derefer(root, "/A", true);
        assert node != null;
        assert node.getKey() != null;
        assert node.getKey().equals("A");
        assert node.getValue() instanceof String;
        assert node.getValue().equals("B");
    }

    @Test
    public void testArrayDereference() {
        var node = JsonPointer.derefer(root, "/C/0", true);
        assert node != null;
        assert node.getKey() == null;
        assert node.getValue() instanceof Number;
        assert node.getValue().equals(1.0);
    }

    @Test
    public void testNestedObject() {
        var node = JsonPointer.derefer(root, "/D/E", true);
        assert node != null;
        assert node.getKey().equals("E");
        assert node.getValue() instanceof Number;
        assert node.getValue().equals(1.0);
    }

    @Test
    public void testFetchObject() {
        var node = JsonPointer.derefer(root, "/D", true);
        assert node != null;
        assert node.getKey().equals("D");
        assert node.getValue() instanceof Map;
        assert (node.getChild("E").isPresent());
        assert (node.getChild("F").isPresent());
        assert (node.getChild("F").get().getValue().equals("X"));
    }
}
