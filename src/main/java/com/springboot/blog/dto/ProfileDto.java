package com.springboot.blog.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    private String avatar;

}
