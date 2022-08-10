package com.mahmoudh.kiwe.service;

import com.mahmoudh.kiwe.entity.User;

public interface UserService {
    User saveNewUser(User user);
    User updateUser(User user);
    User getUserByEmailOrUserName(String credential);

    boolean isEmail(String emailAddress);
}
