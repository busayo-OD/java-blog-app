package com.springboot.blog.exception;

public class PasswordMismatchException extends BlogAPIException2{
    @Override
    public String getMessage() {
        return "New password and existing or confirmation password should match.";
    }
}
