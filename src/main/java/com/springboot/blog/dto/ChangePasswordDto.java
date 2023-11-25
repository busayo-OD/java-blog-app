package com.springboot.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDto {
    private String email;
    private String existingPassword;
    private String newPassword;
    private String confirmPassword;
}
