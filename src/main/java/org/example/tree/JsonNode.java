package org.example.tree;

import java.util.*;

@SuppressWarnings("rawtypes")
public class JsonNode {
    String key;
    Object value;
    Class clazz;
    HashMap<String, JsonNode> children;

    public JsonNode(String key, Object value) {
        this.key = key;
        this.value = value;
        this.clazz = value == null ? null : value.getClass();
        this.children = new HashMap<>();
    }

    public static JsonNode parseJson(String identifier, Object json) {
        if (json instanceof Map<?, ?> jsonMap) {
            JsonNode parent = new JsonNode(identifier, json);

            for (Object key : jsonMap.keySet()) {
                if (jsonMap.get(key) instanceof Map<?,?> map) {
                    parent.putChild(parseJson(key.toString(), map));
                } else if (jsonMap.get(key) instanceof List<?> list) {
                    parent.putChild(parseJson(key.toString(), list));
                } else {
                    // primitive
                    parent.putChild(new JsonNode(key.toString(), jsonMap.get(key)));
                }
            }
            return parent;
        } else if (json instanceof List<?> jsonList) {
            var jsonElements = jsonList.stream()
                    .map(e -> parseJson(String.valueOf(jsonList.indexOf(e)), e))
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

    public void removeChild(String targetKey) {
        this.children.remove(targetKey);
    }
}
