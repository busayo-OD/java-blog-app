package com.springboot.blog.exception;

public class CategoryNotFoundException extends BlogAPIException2{
    private static final long serialVersionUID = 1L;
    public static final String ENTITY_NAME = "Category";
    private Object entityId = null;

    public CategoryNotFoundException(Object entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getMessage() {
        return String.format("%s with an id %s cannot be found or does not exist in record.", ENTITY_NAME, entityId.toString());
    }
}
