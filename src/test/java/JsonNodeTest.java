import org.example.Util;
import org.example.tree.JsonNode;
import org.junit.jupiter.api.Test;

public class JsonNodeTest {
    final String JSON = """
                {
                  "A": "B",
                  "C": [1, 2],
                  "D": {
                    "E": 1
                  }
                }
                """;

    @Test
    public void testParseJson() {
        JsonNode root = JsonNode.parseJson(null, Util.deserializeJson(JSON));
        var something = Util.deserializeJson(JSON);
        assert root != null;
    }
}
