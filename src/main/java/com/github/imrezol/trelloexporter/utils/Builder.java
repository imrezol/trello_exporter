package com.github.imrezol.trelloexporter.utils;

public class Builder {

    private final StringBuilder builder = new StringBuilder();

    public Builder append(String str) {
        if (str != null) {
            builder.append(str);
        }
        return this;
    }

    public Builder appendLine(String str) {
        if (str != null) {
            builder.append(str);
        }
        builder.append(System.lineSeparator());
        return this;
    }

    public String toString() {
        return builder.toString();
    }

    public Builder appendNewLine() {
        builder.append(System.lineSeparator());
        return this;
    }

    public Builder appendNewLine(int count) {
        builder.append(System.lineSeparator().repeat(count));
        return this;
    }
}
