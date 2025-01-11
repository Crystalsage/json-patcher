package org.example.tree;

import com.sun.nio.sctp.NotificationHandler;

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

    /**
     * Construct a JSON tree from a JSON represented in Java native types.
     *
     * @param identifier: Identifier of the node we are trying to construct.
     * @param json: JSON represented in native types.
     * @return The root of the tree constructed.
     */
    public static JsonNode parseJson(String identifier, Object json) {
        if (json instanceof Map<?, ?> jsonMap) {
            JsonNode parent = new JsonNode(identifier, json);

            for (Object key : jsonMap.keySet()) {
                if (jsonMap.get(key) instanceof Map<?, ?> map) {
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

    public static boolean equals(JsonNode node, JsonNode otherNode) {
        boolean isEqual = false;

        isEqual |= node.getValue().getClass().equals(otherNode.getValue());

        if (node.getValue() instanceof String || node.getValue() instanceof Boolean || node.getValue() instanceof Number) {
            isEqual |= node.getValue().equals(otherNode.getValue());
        }

        if (node.getValue() instanceof List) {
            isEqual = ((List<?>) node.getValue()).size() == ((List) otherNode.getValue()).size();
            if (isEqual) {
                for (int i = 0; i < ((List) node.getValue()).size(); i++) {
                    var firstValue = (JsonNode) ((List<?>) node.getValue()).get(i);
                    var otherValue = (JsonNode) ((List<?>) otherNode.getValue()).get(i);

                    isEqual |= JsonNode.equals(firstValue, otherValue);
                }
            }
        }

        if (node.getValue() instanceof Map) {
            isEqual |= node.getChildrenCount() == otherNode.getChildrenCount();

            Set<String> childKeysSet = node.getChildKeys();

            for (String childKey: childKeysSet) {
                var firstNodeChild = node.getChild(childKey).get();
                var otherNodeChild = otherNode.getChild(childKey);
                isEqual |= otherNodeChild.isPresent();
                isEqual |= JsonNode.equals(firstNodeChild, otherNodeChild.get());
            }
        }

        return isEqual;
    }

    private Set<String> getChildKeys() {
        return this.children.keySet();
    }

    private int getChildrenCount() {
        return this.children.size();
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

    public Object removeChild(String targetKey) {
        return this.children.remove(targetKey);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
