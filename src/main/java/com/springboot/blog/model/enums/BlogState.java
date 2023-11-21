package com.springboot.blog.model.enums;

public enum BlogState {
    Draft("Draft"),
    Published("Published");

    private final String value;

    BlogState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
