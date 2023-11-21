package com.springboot.blog.utils;

import com.springboot.blog.model.enums.BlogState;

public class BlogUtil {

    public static BlogState getState(String state) {

        switch (state) {
            case "Draft":
                return BlogState.Draft;

            case "Published":
                return BlogState.Published;

            default:
                return null;
        }
    }
}
