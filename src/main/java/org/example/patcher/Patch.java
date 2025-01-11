package org.example.patcher;

public class Patch {
    String op;
    String path;
    Object value;

    public Patch(String op, String path, Object value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public Patch(String op, String path) {
        this.op = op;
        this.path = path;
    }
}
