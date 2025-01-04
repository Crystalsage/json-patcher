package org.example.patcher;

public class Patch {
    Operation op;
    String path;
    Object value;

    public Patch(Operation op, String path, Object value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public Patch(Operation op, String path) {
        this.op = op;
        this.path = path;
    }
}
