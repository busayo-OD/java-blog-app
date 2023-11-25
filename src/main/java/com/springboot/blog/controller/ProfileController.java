package com.springboot.blog.controller;

import com.springboot.blog.dto.ChangePasswordDto;
import com.springboot.blog.dto.ProfileDto;
import com.springboot.blog.dto.UpdateAvatarDto;
import com.springboot.blog.dto.UpdateProfileDto;
import com.springboot.blog.service.ProfileService;
import com.springboot.blog.utils.CurrentUserUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping()
    public ProfileDto viewProfile(){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return profileService.getProfile(userId);
    }

    @PutMapping("/edit")
    public boolean editProfile(@RequestBody UpdateProfileDto updateProfileDto){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return profileService.editProfile(updateProfileDto, userId);
    }

    @PutMapping("/edit-avatar")
    public boolean editAvatar(@RequestBody UpdateAvatarDto updateAvatarDto){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        return profileService.editAvatar(updateAvatarDto, userId);
    }

    @PostMapping("/deactivate-account")
    public void deactivateAccount(){
        Long userId = Objects.requireNonNull(CurrentUserUtil.getCurrentUser()).getId();
        profileService.deactivateAccount(userId);
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public boolean changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        return profileService.changePassword(changePasswordDto);
    }
}
