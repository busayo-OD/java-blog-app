package com.springboot.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
