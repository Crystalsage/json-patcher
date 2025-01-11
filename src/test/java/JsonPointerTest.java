import org.example.Util;
import org.example.tree.JsonNode;
import org.example.tree.JsonPointer;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assert node.getKey() == null;
        assert node.getChild("A").isPresent();
        assert node.getChild("A").get().getValue() instanceof String;
        assert node.getChild("A").get().getValue().equals("B");
    }

    @Test
    public void testArrayDereference() {
        var node = JsonPointer.derefer(root, "/C/0", true);
        assert node != null;
        assert node.getKey().equals("C");
        assert node.getValue() instanceof List;
        var listElement = ((List<?>) node.getValue()).get(0);
        assert listElement instanceof JsonNode;
        assert ((JsonNode) listElement).getValue() instanceof Number;
        assert ((JsonNode) listElement).getValue().equals(1.0);
    }

    @Test
    public void testInvalidArrayDereference() {
        var node = JsonPointer.derefer(root, "/C/2", false);
        assert node != null;
        assert node.getKey().equals("C");
        assert node.getValue() instanceof List;
        assert ((List<?>) node.getValue()).size() < 3;
    }

    @Test
    public void testNestedObject() {
        var node = JsonPointer.derefer(root, "/D/E", true);
        assert node != null;
        assert node.getKey().equals("D");
        assert node.getChild("E").isPresent();
        assert node.getChild("E").get().getValue().equals(1.0);
    }

    @Test
    public void testFetchObject() {
        var node = JsonPointer.derefer(root, "/D", true);
        assert node != null;
        assert node.getKey() == null;
        assert (node.getChild("D").isPresent());

        var childNode = node.getChild("D").get();
        assert childNode.getChild("E").isPresent();
        assert childNode.getChild("F").isPresent();
    }
}
