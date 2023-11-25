package com.springboot.blog.service;

import com.springboot.blog.dto.ChangePasswordDto;
import com.springboot.blog.dto.ProfileDto;
import com.springboot.blog.dto.UpdateAvatarDto;
import com.springboot.blog.dto.UpdateProfileDto;

public interface ProfileService {
    ProfileDto getProfile(Long id);
    boolean editProfile(UpdateProfileDto updateProfileDto, Long id);
    boolean editAvatar(UpdateAvatarDto updateAvatarDto, Long id);
    void deactivateAccount(Long id);
    boolean changePassword(ChangePasswordDto changePasswordDto);
}
