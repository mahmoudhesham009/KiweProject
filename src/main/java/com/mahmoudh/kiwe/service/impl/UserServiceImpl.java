package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.UserRepository;
import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;
    @Override
    public User saveNewUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User getUserByEmailOrUserName(String credential) {
        User user;
        if(isEmail(credential)){
            user=userRepo.findUserByEmail(credential);
        }else{
            user=userRepo.findUserByUsername(credential);
        }
        return user;
    }

    boolean isEmail(String emailAddress) {
        return Pattern.compile("^(.+)@(\\\\S+)$")
                .matcher(emailAddress)
                .matches();
    }

}
