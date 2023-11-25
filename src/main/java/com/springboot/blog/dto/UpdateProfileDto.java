package com.springboot.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
}
