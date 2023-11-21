package com.springboot.blog.exception;

public class BlogNotFoundException extends BlogAPIException2{
    private static final long serialVersionUID = 1L;
    public static final String ENTITY_NAME = "Blog";
    private Object entityId = null;

    public BlogNotFoundException(Object entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getMessage() {
        return String.format("%s with an id %s cannot be found or does not exist in record.", ENTITY_NAME, entityId.toString());
    }
}
