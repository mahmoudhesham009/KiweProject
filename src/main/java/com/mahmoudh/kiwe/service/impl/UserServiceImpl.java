package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.dto.ChangePasswordRequest;
import com.mahmoudh.kiwe.dto.Message;
import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.repository.UserRepository;
import com.mahmoudh.kiwe.service.UserService;
import com.mahmoudh.kiwe.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UtilService utilService;

    @Override
    public User saveNewUser(User user) {
        return userRepo.save(user);
    }


    @Override
    public User getUserByEmailOrUserName(String credential) {
        if (credential == null) {
            return null;
        }
        User user;
        if (utilService.isEmail(credential)) {
            user = userRepo.findUserByEmail(credential);
        } else {
            user = userRepo.findUserByUsername(credential);
        }
        return user;
    }

    @Override
    public Message validateUserSignUp(User user) {
        User u1 = getUserByEmailOrUserName(user.getUsername());
        User u2 = getUserByEmailOrUserName(user.getEmail());
        if (user.getFirstname() == null || user.getLastname() == null || user.getUsername() == null ||
                user.getAge() == null || user.getEmail() == null || user.getPassword() == null) {
            return new Message("complete all requirement");
        }
        if (!utilService.isEmail(user.getEmail())) {
            return new Message("not valid email");
        }
        if (u1 != null) {
            return new Message("Username is already used");
        }
        if (u2 != null) {
            return new Message("Email is already used");
        }
        if (user.getAge() < 18) {
            return new Message("You have to be 18 at least to sign up");
        }
        return null;
    }

    @Override
    public Message validateChangePassword(ChangePasswordRequest changeBody, User user) {
        if (changeBody.getNewPassword() == null || changeBody.getOldPassword() == null) {
            new Message("complete all requirement");
        }
        if (!passwordEncoder.matches(changeBody.getOldPassword(), user.getPassword()))
            return new Message("old password is wrong");
        if (passwordEncoder.matches(changeBody.getNewPassword(), user.getPassword()))
            return new Message("New password mustn't be similar to old password");

        return null;
    }

}
