package com.springboot.blog.service.impl;

import com.springboot.blog.dto.ChangePasswordDto;
import com.springboot.blog.dto.ProfileDto;
import com.springboot.blog.dto.UpdateAvatarDto;
import com.springboot.blog.dto.UpdateProfileDto;
import com.springboot.blog.exception.PasswordMismatchException;
import com.springboot.blog.exception.UserNotFoundException;
import com.springboot.blog.model.User;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.ProfileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ProfileDto getProfile(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToProfileDto(user);

    }

    private ProfileDto mapToProfileDto (User user){
        ProfileDto profileDto = new ProfileDto();
        profileDto.setFirstName(user.getFirstName());
        profileDto.setLastName(user.getLastName());
        profileDto.setEmail(user.getEmail());
        profileDto.setUsername(user.getUsername());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setCity(user.getCity());
        profileDto.setState(user.getState());
        profileDto.setCountry(user.getCountry());
        return profileDto;
    }

    @Override
    public boolean editProfile(UpdateProfileDto updateProfileDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!updateProfileDto.getFirstName().isEmpty()) {
            user.setFirstName(updateProfileDto.getFirstName());
        }
        if (!updateProfileDto.getLastName().isEmpty()) {
            user.setLastName(updateProfileDto.getLastName());
        }

        if (!updateProfileDto.getCity().isEmpty()) {
            user.setState(updateProfileDto.getCity());
        }

        if (!updateProfileDto.getState().isEmpty()) {
            user.setState(updateProfileDto.getState());
        }
        if (!updateProfileDto.getCountry().isEmpty()) {
            user.setCountry(updateProfileDto.getCountry());
        }
        if (!updateProfileDto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(updateProfileDto.getPhoneNumber());
        }

        userRepository.save(user);
        return true;
    }

    @Override
    public boolean editAvatar(UpdateAvatarDto updateAvatarDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setAvatar(updateAvatarDto.getAvatar());
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deactivateAccount(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setStatus("Deactivated");
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean changePassword(ChangePasswordDto changePasswordDto) {
        User user = userRepository.findByEmail(changePasswordDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(changePasswordDto));
        boolean matches = passwordEncoder.matches(changePasswordDto.getExistingPassword(), user.getPassword());

        if (!matches) {

            throw new PasswordMismatchException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(user);

        return true;
    }

}
