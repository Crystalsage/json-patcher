package org.example.tree;

import java.util.*;

@SuppressWarnings("rawtypes")
public class JsonNode {
    String key;
    Object value;
    Class clazz;
    HashMap<String, JsonNode> children;

    JsonNode(String key, Object value) {
        this.key = key;
        this.value = value;
        this.clazz = value.getClass();
        this.children = new HashMap<>();
    }

    public static JsonNode parseJson(String identifier, Object json) {
        if (json instanceof HashMap<?, ?> jsonMap) {
            JsonNode parent = new JsonNode(identifier, json);

            for (Object key : jsonMap.keySet()) {
                if (jsonMap.get(key) instanceof HashMap<?, ?> || jsonMap.get(key) instanceof List<?>) {
                    parent.putChild(parseJson(key.toString(), jsonMap.get(key)));
                } else {
                    // primitive
                    parent.putChild(new JsonNode(key.toString(), jsonMap.get(key)));
                }
            }
            return parent;
        } else if (json instanceof List<?> jsonList) {
            // list elements have no identifiers
            var jsonElements = jsonList.stream()
                    .map(e -> parseJson(null, e))
                    .toList();
            return new JsonNode(identifier, new ArrayList<>(jsonElements));
        } else {
            // handle primitive
            return new JsonNode(identifier, json);
        }
    }

    public String getKey() {
        return key;
    }

    public void putChild(JsonNode child) {
        this.children.put(child.getKey(), child);
    }

    public Optional<JsonNode> getChild(String key) {
        return Optional.ofNullable(this.children.get(key));
    }

    public Object getValue() {
        return value;
    }

    protected HashMap getChildren() {
        return children;
    }
}
