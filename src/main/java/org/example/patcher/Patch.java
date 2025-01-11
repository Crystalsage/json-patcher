package org.example.patcher;

public class Patch {
    public String op;
    public String path;
    public String from;
    public Object value;

    public Patch(String op, String path, Object value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public Patch(String op, String path) {
        this.op = op;
        this.path = path;
    }

    public Patch(String op, String from, String path) {
        this.op = op;
        this.from = from;
        this.path = path;
    }
}
